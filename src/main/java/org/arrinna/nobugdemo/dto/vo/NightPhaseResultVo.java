package org.arrinna.nobugdemo.dto.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NightPhaseResultVo {
    private String phase;
    private String message;
    private Integer killTargetId;
    private Integer savedId;
    private Integer poisonedId;
    private Integer checkTargetId;
    private Boolean checkIsGood;
    private Integer resolvedDeathId;
}
