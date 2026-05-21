package org.arrinna.nobugdemo.game;

import lombok.extern.slf4j.Slf4j;
import org.arrinna.nobugdemo.Player.*;
import org.apache.commons.lang3.StringUtils;
import org.arrinna.nobugdemo.exception.GameOverException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 在这个文件中我们要写游戏数据的代码部分
 * 游戏一共有几个部分
 *
 * 一、狼人：
 * 1.查看当前存活狼人的数量，如果为0就游戏结束了
 * 2.狼人昨天晚上猎杀的用户的ID
 * 3.
 *
 * 二、预言家
 * 1.查看预言家查的人以及身份
 *
 * 三、女巫
 * 1.查看女巫是否使用了毒药/解药
 * 2.作用对象是谁
 *
 * 四、猎人
 * 1.查看猎人是否使用了技能（1次）
 *
 * 五、好人阵营
 * 1.查看当前好人阵营的人数
 *
 * 六、其他
 * 1.查看当前存活的用户的ID
 *
 * 这个存储的是静态的数据哈
 */
@Slf4j
public class GameData {
    private GameData(){

    }
    //所有玩家，也就是玩家列表
    public static final List<AbstractPlayer> players = new ArrayList<>();

    //游戏对局情况，用链式哈希Map就可以了
    public static final LinkedHashMap<Integer, String> gameInformations = new LinkedHashMap<>(); // 游戏对局信息

    public static final Set<Integer> killIds = new HashSet<>();

    public static int werewolfKillId = -1; // 狼人昨晚猎杀的玩家ID

    public static boolean goodGuysWin = true; // 是否好人阵营获胜

    public static int round = 0;

    public static String phase = "idle";

    /** 本局是否已用过解药（整局一瓶，用过后不能再救） */
    public static boolean witchSaveUsed = false;

    public static boolean witchPoisonUsed = false;

    public static int pendingPoisonId = -1;

    /** 本夜女巫救活的玩家 ID（仅当晚结算有效，与 witchSaveUsed 不同） */
    public static int nightSavedId = -1;

    /**
     * 这个是跟据用户的ID获取玩家
     * 这是跟据用户的id获取它的实际的一些方法哦
     * @param id
     * @return
     */
    public static AbstractPlayer getPlayer(int id){
        for (AbstractPlayer player : players) {
            if (player.getId() == id) {
                return player;
            }
        }
        return null;
    }

    /** 校验并解析为有效玩家 ID（必须在场且存活） */
    public static Integer resolveAlivePlayerId(Integer targetId, int excludeId) {
        if (targetId != null && targetId > 0) {
            AbstractPlayer p = getPlayer(targetId);
            if (p != null && p.isAlive() && p.getId() != excludeId) {
                return targetId;
            }
        }
        return getAlivePlayerId().stream()
                .filter(pid -> pid != excludeId)
                .findFirst()
                .orElse(null);
    }


    /**
     * 获取还活着的人的ID号，就这样获得到活着的人的列表信息
     * @return
     */
    public static List<AbstractPlayer> getAlivePlayers(){
        return players.stream().filter(AbstractPlayer::isAlive).collect(Collectors.toList());
    }

    public static List<Integer> getAlivePlayerId(){
        return players.stream().filter(AbstractPlayer::isAlive).map(AbstractPlayer::getId).collect(Collectors.toList());
    }

    /**
     * 狼人身份信息
     * @return
     */
    public static List<AbstractPlayer> getWerewolfPlayers(){
        return players.stream().filter(p->p instanceof WerewolfPlayer)
                .collect(Collectors.toList());
    }

    /**
     * 查看还哪些狼人是活着的
     * @return
     */
    public static List<AbstractPlayer> getAliveWerewolfPlayers(){
        return players.stream()
                .filter(p->p instanceof WerewolfPlayer)
                .filter(AbstractPlayer::isAlive)
                .collect(Collectors.toList());
    }

    /**
     * 获取活着的狼人
     * @return
     */
    public static long aliveWerewolfCount(){
        return getAliveWerewolfPlayers().size();
    }

    public static List<AbstractPlayer> getAliveProphetPlayers(){
        return players.stream()
                .filter(p-> p instanceof ProphetPlayer)
                .filter(AbstractPlayer::isAlive)
                .collect(Collectors.toList());
    }

    public static List<AbstractPlayer> getAliveWitchPlayers(){
        return players.stream()
                .filter(p-> p instanceof WitchPlayer)
                .filter(AbstractPlayer::isAlive)
                .collect(Collectors.toList());
    }

    public static List<AbstractPlayer> getAliveHunterPlayers(){
        return players.stream()
                .filter(p-> p instanceof HunterPlayer)
                .filter(AbstractPlayer::isAlive)
                .collect(Collectors.toList());
    }
    public static List<AbstractPlayer> getAliveVillagerPlayers(){
        return players.stream()
                .filter(p-> p instanceof VillagerPlayer)
                .filter(AbstractPlayer::isAlive)
                .collect(Collectors.toList());
    }

    /**
     * 神职有多少人
     * @return
     */
    private static long aliveDeityCount(){
        return players.stream()
                .filter(AbstractPlayer::isAlive)
                .filter(p -> p instanceof ProphetPlayer || p instanceof WitchPlayer || p instanceof HunterPlayer)
                .count();
    }

    private static long aliveVillagerCount() {
        return players.stream()
                .filter(p -> p instanceof VillagerPlayer)
                .filter(AbstractPlayer::isAlive)
                .count();
    }

    public static String getGameInformation(){
        //1.用MAP来存储
        Map<Integer,AbstractPlayer> playerMap=
                players.stream().collect(Collectors.toMap(AbstractPlayer::getId,p->p));
        //2.转换成gameInformation这种写法昂
        String gameInformation=gameInformations.entrySet().stream()
                .map(e->{
                    int key = e.getKey();
                    if (key == 0) {
                        return "[系统]" + (StringUtils.isNotBlank(e.getValue()) ? e.getValue() : "");
                    }
                    AbstractPlayer p = playerMap.get(key);
                    if (p == null) {
                        return key + "号玩家" + (StringUtils.isNotBlank(e.getValue()) ? e.getValue() : "");
                    }
                    int id = p.getId();
                    String str = id + "号玩家";

                    if (killIds.contains(id)){
                        str += "[已死亡]";
                    }
                    return str + (StringUtils.isNotBlank(e.getValue()) ? e.getValue() : "\n(第1天还未轮到发言)");
                })
                .collect(Collectors.joining("\n\n"));
        log.info("### 对局信息 ###\n" + gameInformation);
        return gameInformation;

    }
    public static void playerDead(int id) throws GameOverException {
        killIds.add(id);
        log.info("目前存活玩家：" + GameData.getAlivePlayers().stream()
                .map(p -> p.getId() + "号[" + p.getRoleName() + "]")
                .collect(Collectors.joining(", ")));
        if (aliveWerewolfCount() == 0) {
            throw new GameOverException();
        } else if (aliveDeityCount() == 0 || aliveVillagerCount() == 0) {
            goodGuysWin = false;
            throw new GameOverException();
        }
    }

    /**
     * 检查游戏是否结束
     */
    public static boolean isGameOver() {
        return aliveWerewolfCount() == 0 || aliveDeityCount() == 0 || aliveVillagerCount() == 0;
    }
    /**
     * 清空所有游戏数据
     */
    public static synchronized void clear() {
        players.clear();
        gameInformations.clear();
        killIds.clear();
        werewolfKillId = -1;
        goodGuysWin = true;
        round = 0;
        phase = "idle";
        witchSaveUsed = false;
        witchPoisonUsed = false;
        pendingPoisonId = -1;
        nightSavedId = -1;
    }
    public static void addPlayerInformation(int id, String information) {
        String existing = gameInformations.get(id);
        String newInfo = (existing != null ? existing : "") + "\n" + information;
        gameInformations.put(id, newInfo);
    }

}
