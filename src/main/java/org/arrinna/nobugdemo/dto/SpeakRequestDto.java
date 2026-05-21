package org.arrinna.nobugdemo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "玩家发言请求")
public class SpeakRequestDto {

    @ApiModelProperty(value = "玩家ID", required = true, example = "1")
    private Integer playerId;
}