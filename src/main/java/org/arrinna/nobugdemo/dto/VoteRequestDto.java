package org.arrinna.nobugdemo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "玩家投票请求")
public class VoteRequestDto {

    @ApiModelProperty(value = "投票者玩家ID", required = true, example = "1")
    private Integer playerId;

    @ApiModelProperty(value = "被投票玩家ID（可选，如不填则由AI决策）", example = "3")
    private Integer targetPlayerId;
}