package org.arrinna.nobugdemo.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.arrinna.nobugdemo.dto.ApiResult;
import org.arrinna.nobugdemo.dto.SpeakRequestDto;
import org.arrinna.nobugdemo.dto.StartGameDto;
import org.arrinna.nobugdemo.dto.VoteRequestDto;
import org.arrinna.nobugdemo.dto.WitchNightDto;
import org.arrinna.nobugdemo.dto.vo.*;
import org.arrinna.nobugdemo.game.GameData;
import org.arrinna.nobugdemo.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/game")
@Slf4j
@CrossOrigin(origins = "*")
@Api(tags = "狼人杀游戏接口")
public class GameController {

    @Autowired
    private GameService gameService;

    @PostMapping("/start")
    @ApiOperation(value = "开始游戏", notes = "10人局，身份随机洗牌分配；players 可不传")
    public ApiResult<StartGameResultVo> startGame(@RequestBody(required = false) StartGameDto dto) {
        //如果没有玩家就new 10个
        if (dto == null) {
            dto = new StartGameDto();
        }
        return gameService.startGame(dto);
    }

    @DeleteMapping("/end")
    @ApiOperation(value = "结束游戏")
    public ApiResult<String> endGame() {
        GameData.clear();
        return ApiResult.success("游戏已结束");
    }

    @GetMapping("/players")
    @ApiOperation(value = "全部玩家列表（含身份，观战用）")
    public ApiResult<List<PlayerVo>> listPlayers() {
        return ApiResult.success(gameService.listPlayers());
    }

    @PostMapping("/night/wolf")
    @ApiOperation(value = "夜晚·狼人刀人", notes = "调用 WerewolfPlayer.skill() → LLM 决策刀口")
    public ApiResult<NightPhaseResultVo> nightWolf() {
        return gameService.nightWolf();
    }

    @PostMapping("/night/seer")
    @ApiOperation(value = "夜晚·预言家查验", notes = "调用 ProphetPlayer.skill()")
    public ApiResult<NightPhaseResultVo> nightSeer() {
        return gameService.nightSeer();
    }

    @PostMapping("/night/witch")
    @ApiOperation(value = "夜晚·女巫行动", notes = "save/usePoison 控制是否救人、毒人")
    public ApiResult<NightPhaseResultVo> nightWitch(@RequestBody(required = false) WitchNightDto dto) {
        return gameService.nightWitch(dto);
    }

    @PostMapping("/night/resolve")
    @ApiOperation(value = "夜晚·结算死亡")
    public ApiResult<NightPhaseResultVo> nightResolve() {
        return gameService.nightResolve();
    }

    @PostMapping("/day/speeches")
    @ApiOperation(value = "白天·全员发言", notes = "依次调用各存活玩家的 speak()")
    public ApiResult<DayPhaseResultVo> daySpeeches() {
        return gameService.daySpeeches();
    }

    @PostMapping("/day/votes")
    @ApiOperation(value = "白天·全员投票放逐", notes = "依次 vote() 并统计最高票出局")
    public ApiResult<DayPhaseResultVo> dayVotes() {
        return gameService.dayVotes();
    }

    @PostMapping("/speak")
    @ApiOperation(value = "单个玩家发言")
    public ApiResult<String> speak(@RequestBody SpeakRequestDto request) {
        return gameService.speak(request);
    }

    @PostMapping("/vote")
    @ApiOperation(value = "单个玩家投票")
    public ApiResult<Integer> vote(@RequestBody VoteRequestDto request) {
        return gameService.vote(request);
    }

    @GetMapping("/information")
    @ApiOperation(value = "获取游戏对局信息")
    public ApiResult<InformationVo> getGameInformation() {
        return gameService.getInformation();
    }

    @GetMapping("/alive-players")
    @ApiOperation(value = "获取存活玩家 ID")
    public ApiResult<List<Integer>> getAlivePlayerIds() {
        return ApiResult.success(GameData.getAlivePlayerId());
    }

    @GetMapping("/player/{playerId}")
    @ApiOperation(value = "获取玩家详情")
    public ApiResult<PlayerVo> getPlayer(@PathVariable @ApiParam(value = "玩家ID", example = "1") Integer playerId) {
        return gameService.getPlayerVo(playerId);
    }

    @GetMapping("/werewolf-count")
    @ApiOperation(value = "狼人存活数量")
    public ApiResult<Long> getAliveWerewolfCount() {
        return ApiResult.success(GameData.aliveWerewolfCount());
    }

    @GetMapping("/status")
    @ApiOperation(value = "游戏状态")
    public ApiResult<GameStatusVo> getGameStatus() {
        return gameService.getGameStatus();
    }
}
