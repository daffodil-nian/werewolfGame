package org.arrinna.nobugdemo.dto.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DayPhaseResultVo {
    private String phase;
    private String message;
    private List<SpeechRecordVo> speeches;
    private Map<Integer, Integer> votes;
    private Integer exiledId;
    private boolean gameOver;
    private String winner;
}
