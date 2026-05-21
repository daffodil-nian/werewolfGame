package org.arrinna.nobugdemo.dto.vo;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class CheckResult {

    @NonNull
    @SerializedName("check_id")
    private Integer checkId;

    @NonNull
    @SerializedName("check_cause")
    private String checkCause;
}
