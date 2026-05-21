package org.arrinna.nobugdemo.dto.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InformationVo {
    private String rawText;
    private List<SpeechRecordVo> records;
}
