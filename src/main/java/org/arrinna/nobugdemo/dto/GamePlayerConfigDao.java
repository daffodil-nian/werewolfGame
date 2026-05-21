package org.arrinna.nobugdemo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GamePlayerConfigDao {
    private Integer playerId;
    private String playerName;
    private String modelId;
}
