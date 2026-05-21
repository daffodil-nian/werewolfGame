package org.arrinna.nobugdemo.game;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 写的是游戏的运行的一个状态
 * 1.要有游戏的当前的天数
 * 2.要活着的用户的ID
 * 3.昨天晚上被狼人杀的ID
 * 4.上一轮大家说了什么
 * 5.上一轮投票结果如何,几号ID有几个人投票了
 * 6.今天谁被出局了
 */
@Data
public class GameState {

    private int day = 1;

    private List<Integer> alivePlayers;

    private Integer lastNightDead;

    private List<String> lastSpeeches;

    private Map<Integer, Integer> voteResult;

    private Integer eliminatedPlayer;

}
