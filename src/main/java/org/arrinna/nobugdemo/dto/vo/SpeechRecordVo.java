package org.arrinna.nobugdemo.dto.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpeechRecordVo {
    private Integer playerId;
    private String playerLabel;
    private String content;
    private long timestamp;
    private String kind;
}
