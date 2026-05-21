package org.arrinna.nobugdemo.dto.vo;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@AllArgsConstructor
@Data
public class TestamentResult {
    @NonNull
    @SerializedName("reasoning_process")
    private String reasoningProcess;

    @NonNull
    @SerializedName("last_words")
    private String lastWords;
}