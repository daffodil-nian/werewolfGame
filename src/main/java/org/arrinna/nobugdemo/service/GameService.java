package org.arrinna.nobugdemo.service;

import lombok.extern.slf4j.Slf4j;
import org.arrinna.nobugdemo.Player.*;
import org.arrinna.nobugdemo.config.AiModelProperties;
import org.arrinna.nobugdemo.dto.*;
import org.arrinna.nobugdemo.dto.vo.*;
import org.arrinna.nobugdemo.exception.GameOverException;
import org.arrinna.nobugdemo.game.GameData;
import org.arrinna.nobugdemo.game.RoleKind;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
public class GameService {

    //标准牌组，一个列表有哪些角色
    private static final List<RoleKind> STANDARD_DECK = List.of(
            RoleKind.WEREWOLF, RoleKind.WEREWOLF, RoleKind.WEREWOLF,
            RoleKind.PROPHET, RoleKind.WITCH, RoleKind.HUNTER,
            RoleKind.VILLAGER, RoleKind.VILLAGER, RoleKind.VILLAGER, RoleKind.VILLAGER
    );

    @Autowired
    private AiModelProperties aiModelProperties;

    //加上synchronized可以保证多线程安全
    public synchronized ApiResult<StartGameResultVo> startGame(StartGameDto dto) {
        if (!dto.isValid()) {
            return ApiResult.error(400, "需要恰好 10 名玩家，或不传 players 由服务端自动生成");
        }

        GameData.clear();

        //首先解析配置
        List<GamePlayerConfigDao> configs = resolvePlayerConfigs(dto.getPlayers());
        //2.然后是身份
        List<RoleKind> shuffledRoles = new ArrayList<>(STANDARD_DECK);


        //3.然后给角色洗牌
        Collections.shuffle(shuffledRoles);

        //4.接着给每个角色配置一个身份
        for (int i = 0; i < configs.size(); i++) {
            AbstractPlayer player = createPlayer(shuffledRoles.get(i), configs.get(i));
            GameData.players.add(player);
            GameData.gameInformations.put(player.getId(), "");
        }

        GameData.round = 1;
        GameData.phase = "night_wolf";
        log.info("游戏开始，随机身份：{}", getRoleDistribution());

        StartGameResultVo result = new StartGameResultVo(
                "游戏已启动，身份已随机分配",
                GameData.players.stream().map(this::toPlayerVo).collect(Collectors.toList())
        );
        return ApiResult.success(result);
    }

    //晚上狼人
    public synchronized ApiResult<NightPhaseResultVo> nightWolf() {
        if (GameData.players.isEmpty()) {
            return ApiResult.error(400, "请先开始游戏");
        }
        GameData.phase = "night_wolf";
        List<AbstractPlayer> wolves = GameData.getAliveWerewolfPlayers();
        if (wolves.isEmpty()) {
            return ApiResult.error(400, "没有存活狼人");
        }

        //获取狼王
        WerewolfPlayer leader = (WerewolfPlayer) wolves.get(0);
        //狼人刀人目标一致
        KillResult killResult = leader.skill("团队统一刀口");
        //获取到杀谁
        int killId = killResult != null && killResult.getKillId() != null ? killResult.getKillId() : -1;

        //设置杀的是谁
        GameData.werewolfKillId = killId;

        String msg = killId > 0
                ? "狼人选择刀杀 " + killId + " 号"
                : "狼人本夜未选出有效刀口";

        //添加系统信息
        GameData.addPlayerInformation(0, "[系统] " + msg);
        log.info(msg);

        return ApiResult.success(new NightPhaseResultVo(
                "night_wolf", msg, killId > 0 ? killId : null, null, null, null, null, null
        ));
    }

    public synchronized ApiResult<NightPhaseResultVo> nightSeer() {
        if (GameData.players.isEmpty()) {
            return ApiResult.error(400, "请先开始游戏");
        }
        GameData.phase = "night_seer";
        Optional<AbstractPlayer> seerOpt = GameData.getAliveProphetPlayers().stream().findFirst();
        if (seerOpt.isEmpty()) {
            return ApiResult.success(new NightPhaseResultVo(
                    "night_seer", "预言家已出局，跳过查验", null, null, null, null, null, null
            ));
        }

        ProphetPlayer seer = (ProphetPlayer) seerOpt.get();
        seer.skill();
        CheckResult check = seer.getCheckResult();
        Integer checkId = check != null ? check.getCheckId() : null;
        AbstractPlayer checked = checkId != null ? GameData.getPlayer(checkId) : null;
        Boolean isGood = checked != null ? checked.isGoodGuys() : null;
        String msg = checkId != null
                ? "预言家查验 " + checkId + " 号，结果：" + (Boolean.TRUE.equals(isGood) ? "好人" : "狼人")
                : "预言家查验失败";
        GameData.addPlayerInformation(seer.getId(), "[技能] " + msg);

        return ApiResult.success(new NightPhaseResultVo(
                "night_seer", msg, null, null, null, checkId, isGood, null
        ));
    }

    public synchronized ApiResult<NightPhaseResultVo> nightWitch(WitchNightDto dto) {
        if (GameData.players.isEmpty()) {
            return ApiResult.error(400, "请先开始游戏");
        }
        GameData.phase = "night_witch";
        Optional<AbstractPlayer> witchOpt = GameData.getAliveWitchPlayers().stream().findFirst();
        if (witchOpt.isEmpty()) {
            return ApiResult.success(new NightPhaseResultVo(
                    "night_witch", "女巫已出局，跳过", null, null, null, null, null, null
            ));
        }

        WitchPlayer witch = (WitchPlayer) witchOpt.get();
        boolean trySave = dto == null || dto.getSave() == null || Boolean.TRUE.equals(dto.getSave());
        boolean tryPoison = dto != null && Boolean.TRUE.equals(dto.getUsePoison());

        Integer savedId = null;
        if (trySave && !GameData.witchSaveUsed && GameData.werewolfKillId > 0) {
            AbstractPlayer victim = GameData.getPlayer(GameData.werewolfKillId);
            if (victim != null && victim.isAlive()) {
                witch.save(GameData.werewolfKillId);
                GameData.witchSaveUsed = true;
                GameData.nightSavedId = GameData.werewolfKillId;
                savedId = GameData.werewolfKillId;
                GameData.addPlayerInformation(witch.getId(), "[技能] 使用解药救 " + savedId + " 号");
            }
        }

        Integer poisonedId = null;
        if (tryPoison && !GameData.witchPoisonUsed) {
            int poisonTarget = witch.skill(GameData.werewolfKillId > 0 ? GameData.werewolfKillId : -1);
            if (poisonTarget > 0) {
                GameData.witchPoisonUsed = true;
                GameData.pendingPoisonId = poisonTarget;
                poisonedId = poisonTarget;
                GameData.addPlayerInformation(witch.getId(), "[技能] 毒杀 " + poisonedId + " 号");
            }
        }

        String msg = "女巫行动完成";
        return ApiResult.success(new NightPhaseResultVo(
                "night_witch", msg, GameData.werewolfKillId > 0 ? GameData.werewolfKillId : null,
                savedId, poisonedId, null, null, null
        ));
    }

    public synchronized ApiResult<NightPhaseResultVo> nightResolve() {
        if (GameData.players.isEmpty()) {
            return ApiResult.error(400, "请先开始游戏");
        }
        GameData.phase = "night_resolve";
        List<Integer> deaths = new ArrayList<>();
        try {
            int wolfTarget = GameData.werewolfKillId;
            if (wolfTarget > 0 && !GameData.killIds.contains(wolfTarget)) {
                // 仅当本夜该刀口被女巫救下才免死（不能用 witchSaveUsed，那是整局是否用过解药）
                boolean savedTonight = GameData.nightSavedId == wolfTarget;
                if (!savedTonight) {
                    GameData.playerDead(wolfTarget);
                    deaths.add(wolfTarget);
                }
            }
            int poisonTarget = GameData.pendingPoisonId;
            if (poisonTarget > 0 && !GameData.killIds.contains(poisonTarget)) {
                GameData.playerDead(poisonTarget);
                if (!deaths.contains(poisonTarget)) {
                    deaths.add(poisonTarget);
                }
            }
        } catch (GameOverException e) {
            log.info("夜晚结算后游戏结束");
        }

        GameData.werewolfKillId = -1;
        GameData.pendingPoisonId = -1;
        GameData.nightSavedId = -1;

        String msg;
        if (deaths.isEmpty()) {
            msg = "昨夜平安夜";
        } else if (deaths.size() == 1) {
            msg = deaths.get(0) + " 号昨夜死亡";
        } else {
            msg = deaths.stream().map(id -> id + " 号").collect(Collectors.joining("、")) + "昨夜死亡";
        }
        GameData.addPlayerInformation(0, "[系统] " + msg);

        Integer deathId = deaths.isEmpty() ? null : deaths.get(deaths.size() - 1);
        return ApiResult.success(new NightPhaseResultVo(
                "night_resolve", msg, null, null, null, null, null, deathId
        ));
    }

    /**
     * 发言
     * @return
     */
    public synchronized ApiResult<DayPhaseResultVo> daySpeeches() {
        if (GameData.players.isEmpty()) {
            return ApiResult.error(400, "请先开始游戏");
        }
        GameData.phase = "day_speech";
        List<SpeechRecordVo> speeches = new ArrayList<>();
        //看现在第几轮了
        int speechRound = GameData.round;

        for (AbstractPlayer player : new ArrayList<>(GameData.getAlivePlayers())) {
            if (!player.isAlive()) continue;
            try {
                //说话的内容
                String content = player.speak(speechRound);
                GameData.addPlayerInformation(player.getId(), "发言：" + content);
                speeches.add(new SpeechRecordVo(
                        player.getId(),
                        formatLabel(player.getId()),
                        content,
                        System.currentTimeMillis(),
                        "speech"
                ));
                log.info("玩家 {} 发言：{}", player.getId(), content);
            } catch (Exception e) {
                log.warn("玩家 {} 发言失败: {}", player.getId(), e.getMessage());
            }
        }

        return ApiResult.success(new DayPhaseResultVo(
                "day_speech",
                "白天发言完成，共 " + speeches.size() + " 人发言",
                speeches,
                null,
                null,
                GameData.isGameOver(),
                resolveWinner()
        ));
    }

    public synchronized ApiResult<DayPhaseResultVo> dayVotes() {
        if (GameData.players.isEmpty()) {
            return ApiResult.error(400, "请先开始游戏");
        }
        GameData.phase = "day_vote";

        //这个是用来记录谁投给谁的
        Map<Integer, Integer> voteMap = new HashMap<>();
        List<Integer> aliveIds = GameData.getAlivePlayerId();

        for (AbstractPlayer player : new ArrayList<>(GameData.getAlivePlayers())) {
            if (!player.isAlive()) continue;
            try {
                int targetId = player.vote(new ArrayList<>(aliveIds));
                if (targetId > 0 && aliveIds.contains(targetId)) {
                    voteMap.merge(targetId, 1, Integer::sum);
                    GameData.addPlayerInformation(player.getId(), "投票给" + targetId + " 号");
                }
            } catch (Exception e) {
                log.warn("玩家 {} 投票失败: {}", player.getId(), e.getMessage());
            }
        }

        Integer exiledId = pickExileTarget(voteMap);
        if (exiledId != null) {
            try {
                AbstractPlayer exiled = GameData.getPlayer(exiledId);
                if (exiled != null && exiled.isAlive()) {
                    GameData.addPlayerInformation(exiledId, "[出局] 被投票放逐");
                    GameData.playerDead(exiledId);
                }
            } catch (GameOverException e) {
                log.info("放逐后游戏结束");
            }
        }

        GameData.round++;
        GameData.phase = GameData.isGameOver() ? "ended" : "night_wolf";

        return ApiResult.success(new DayPhaseResultVo(
                "day_vote",
                exiledId != null ? exiledId + " 号被投票出局" : "今日无人出局",
                null,
                voteMap,
                exiledId,
                GameData.isGameOver(),
                resolveWinner()
        ));
    }

    public synchronized ApiResult<String> speak(SpeakRequestDto request) {
        AbstractPlayer player = GameData.getPlayer(request.getPlayerId());
        if (player == null) {
            return ApiResult.error(404, "玩家不存在");
        }
        if (!player.isAlive()) {
            return ApiResult.error(400, "玩家已死亡");
        }

        String speech = player.speak(Math.max(1, GameData.round));
        GameData.addPlayerInformation(request.getPlayerId(), "发言：" + speech);
        log.info("玩家 {} 发言：{}", request.getPlayerId(), speech);
        return ApiResult.success(speech);
    }

    public synchronized ApiResult<Integer> vote(VoteRequestDto request) {
        AbstractPlayer player = GameData.getPlayer(request.getPlayerId());
        if (player == null) {
            return ApiResult.error(404, "玩家不存在");
        }
        if (!player.isAlive()) {
            return ApiResult.error(400, "玩家已死亡");
        }

        List<Integer> aliveIds = GameData.getAlivePlayerId();
        if (aliveIds.size() <= 1) {
            return ApiResult.error(400, "只剩一名玩家，游戏即将结束");
        }

        int targetId;
        if (request.getTargetPlayerId() != null
                && aliveIds.contains(request.getTargetPlayerId())
                && !request.getTargetPlayerId().equals(request.getPlayerId())) {
            targetId = request.getTargetPlayerId();
        } else {
            targetId = player.vote(aliveIds);
        }

        GameData.addPlayerInformation(request.getPlayerId(), "投票给" + targetId + " 号");
        log.info("玩家 {} 投票给 {}", request.getPlayerId(), targetId);
        return ApiResult.success(targetId);
    }

    public ApiResult<GameStatusVo> getGameStatus() {
        GameStatusVo vo = new GameStatusVo();
        boolean over = GameData.isGameOver();
        vo.setGameOver(over);
        vo.setGoodGuysWin(GameData.goodGuysWin);
        vo.setAliveCount(GameData.getAlivePlayers().size());
        vo.setAlivePlayerIds(GameData.getAlivePlayerId());
        vo.setRound(GameData.round);
        vo.setPhase(GameData.phase);
        vo.setStatus(over ? "ended" : (GameData.players.isEmpty() ? "idle" : "running"));
        vo.setWinner(resolveWinner());
        return ApiResult.success(vo);
    }

    public ApiResult<InformationVo> getInformation() {
        String raw = GameData.getGameInformation();
        List<SpeechRecordVo> records = GameData.gameInformations.entrySet().stream()
                .map(e -> {
                    String content = e.getValue() != null ? e.getValue().trim() : "";
                    if (content.isEmpty()) {
                        return null;
                    }
                    int key = e.getKey();
                    if (key == 0) {
                        return new SpeechRecordVo(
                                null,
                                "[系统]",
                                content,
                                System.currentTimeMillis(),
                                "system"
                        );
                    }
                    return new SpeechRecordVo(
                            key,
                            formatLabel(key),
                            content,
                            System.currentTimeMillis(),
                            detectSpeechKind(content)
                    );
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return ApiResult.success(new InformationVo(raw, records));
    }

    private static String detectSpeechKind(String content) {
        if (content.contains("[系统]")) {
            return "system";
        }
        if (content.contains("[技能]") || content.contains("[出局]")) {
            return "skill";
        }
        if (content.contains("发言：") || content.contains("投票给")) {
            return "speech";
        }
        return "speech";
    }

    public ApiResult<PlayerVo> getPlayerVo(int playerId) {
        AbstractPlayer player = GameData.getPlayer(playerId);
        if (player == null) {
            return ApiResult.error(404, "玩家不存在");
        }
        return ApiResult.success(toPlayerVo(player));
    }

    public List<PlayerVo> listPlayers() {
        return GameData.players.stream().map(this::toPlayerVo).collect(Collectors.toList());
    }

    private List<GamePlayerConfigDao> resolvePlayerConfigs(List<GamePlayerConfigDao> fromDto) {
        if (fromDto != null && !fromDto.isEmpty()) {
            return fromDto;
        }
        return IntStream.rangeClosed(1, 10)
                .mapToObj(i -> new GamePlayerConfigDao(i, "AI-" + String.format("%02d", i), "default"))
                .collect(Collectors.toList());
    }

    private AbstractPlayer createPlayer(RoleKind kind, GamePlayerConfigDao config) {
        String service = aiModelProperties.getService();
        String apiKey = aiModelProperties.getApiKey();
        String modelName = aiModelProperties.getModelName();
        Double temperature = aiModelProperties.getTemperature();
        int playerId = config.getPlayerId() != null ? config.getPlayerId() : 0;
        String name = kind.getDisplayName();

        return switch (kind) {
            case WEREWOLF -> new WerewolfPlayer(playerId, name, service, apiKey, modelName, temperature);
            case PROPHET -> new ProphetPlayer(playerId, name, service, apiKey, modelName, temperature);
            case WITCH -> new WitchPlayer(playerId, name, service, apiKey, modelName, temperature);
            case HUNTER -> new HunterPlayer(playerId, name, service, apiKey, modelName, temperature);
            case VILLAGER -> new VillagerPlayer(playerId, name, service, apiKey, modelName, temperature);
        };
    }

    private PlayerVo toPlayerVo(AbstractPlayer player) {
        String label = formatLabel(player.getId());
        return new PlayerVo(
                player.getId(),
                label,
                player.getRoleName(),
                player.getRoleKind().getRoleKey(),
                player.isAlive(),
                player.isGoodGuys()
        );
    }

    private String formatLabel(int id) {
        return "AI-" + String.format("%02d", id);
    }

    private String getRoleDistribution() {
        return GameData.players.stream()
                .map(p -> p.getId() + ":" + p.getRoleName())
                .collect(Collectors.joining(", "));
    }

    private Integer pickExileTarget(Map<Integer, Integer> voteMap) {
        if (voteMap.isEmpty()) return null;
        return voteMap.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    private String resolveWinner() {
        if (!GameData.isGameOver()) return null;
        return GameData.goodGuysWin ? "好人阵营胜利" : "狼人阵营胜利";
    }
}
