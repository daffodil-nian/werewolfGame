package org.arrinna.nobugdemo.dto.vo;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
@AllArgsConstructor
@Data
public class ShotResult {
    @SerializedName("is_kill")
    private boolean isShot;

    @SerializedName("shot_id")
    private Integer shotId;

    @NonNull
    @SerializedName("shot_cause")
    private String shotCause;
}
