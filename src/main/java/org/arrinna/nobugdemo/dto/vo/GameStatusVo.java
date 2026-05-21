package org.arrinna.nobugdemo.dto.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameStatusVo {
    private String status;
    private int round;
    private String phase;
    private String winner;
    private boolean gameOver;
    private boolean goodGuysWin;
    private int aliveCount;
    private List<Integer> alivePlayerIds;
}
