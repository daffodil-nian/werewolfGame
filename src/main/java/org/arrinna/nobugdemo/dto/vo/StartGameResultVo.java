package org.arrinna.nobugdemo.dto.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StartGameResultVo {
    private String message;
    private List<PlayerVo> players;
}
