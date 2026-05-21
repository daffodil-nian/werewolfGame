package org.arrinna.nobugdemo.dto.vo;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@AllArgsConstructor
@Data
public class SpeakResult {
    @NonNull
    @SerializedName("reasoning_process")
    private String reasoningProcess;

    @NonNull
    @SerializedName("my_speech")
    private String mySpeech;
}
