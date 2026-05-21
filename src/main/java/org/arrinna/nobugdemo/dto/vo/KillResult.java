package org.arrinna.nobugdemo.dto.vo;


import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class KillResult {
    @SerializedName("kill_id")
    //杀几号
    private Integer killId;

    @SerializedName("my_strategy")
    private String myStrategy;
}
