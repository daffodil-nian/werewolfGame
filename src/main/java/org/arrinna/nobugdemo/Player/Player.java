package org.arrinna.nobugdemo.Player;

import org.arrinna.nobugdemo.exception.GameOverException;

import java.util.List;

public interface Player {

    /**
     * 判断玩家是否是好人阵营
     * @return
     */
    boolean isGoodGuys();

    /**
     * 是否还活着
     * @return
     */
    boolean isAlive();

    /**
     * 发言
     * @param index
     * @return
     */
    String speak(int index);

    /**
     * 投票情况
     * @param votingIds
     * @return
     */
    int vote(List<Integer> votingIds);

    /**
     * 玩家遗言
     * @return
     * @throws GameOverException
     */
    String testament() throws GameOverException;
}
