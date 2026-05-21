package org.arrinna.nobugdemo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("女巫夜晚行动")
public class WitchNightDto {

    @ApiModelProperty("是否使用解药救昨晚刀口（默认 true 且有刀口时尝试救）")
    private Boolean save;

    @ApiModelProperty("是否使用毒药（由 AI 决策目标）")
    private Boolean usePoison;
}
