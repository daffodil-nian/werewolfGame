package org.arrinna.nobugdemo.dto.vo;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class PoisonResult {

    @SerializedName("is_kill")
    private boolean isKill;

    @SerializedName("kill_id")
    private Integer killId;

    @NonNull
    @SerializedName("kill_cause")
    private String killCause;
}
