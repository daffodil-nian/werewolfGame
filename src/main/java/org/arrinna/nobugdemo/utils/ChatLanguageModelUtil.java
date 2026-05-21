package org.arrinna.nobugdemo.utils;

import com.google.gson.Gson;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.dashscope.QwenChatModel;
import dev.langchain4j.model.dashscope.QwenModelName;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class ChatLanguageModelUtil {
    private ChatLanguageModelUtil(){

    }
    private static final Pattern JSON_PATTERN = Pattern
            .compile("^\\s*```(.*?)\\n([\\s\\S]*?)\\n\\s*```", Pattern.MULTILINE);

    /**
     *
     * @param service
     * @param apiKey
     * @return
     */
    public static ChatLanguageModel build(@NonNull String service,@NonNull String apiKey){
        //1.首先我们得确定模型，我们看看能不能行得通哈
        return build(service,apiKey,null,null);
    }

    /**
     *
     * @param service
     * @param apiKey
     * @param modelName
     * @param temperature
     * @return
     */
    public static ChatLanguageModel build(@NonNull String service,@NonNull String apiKey,@NonNull String modelName,Double temperature){
       if("dashscope".equalsIgnoreCase(service)){
           QwenChatModel.QwenChatModelBuilder builder=QwenChatModel.builder()
                   .modelName(Optional.ofNullable(modelName).orElse(QwenModelName.QWEN_TURBO))
                   .apiKey(apiKey);
           Optional.ofNullable(temperature).ifPresent(t->builder.temperature(temperature.floatValue()));
           return builder.build();
       }
       else{
           QwenChatModel.QwenChatModelBuilder builder=QwenChatModel.builder()
                   .modelName(Optional.ofNullable(modelName).orElse(QwenModelName.QWEN_TURBO))
                   .apiKey(apiKey);
           Optional.ofNullable(temperature).ifPresent(t->builder.temperature(temperature.floatValue()));
           return builder.build();
       }
    }

    /**
     * JSON 解析：将 LLM 返回的字符串解析为 Java 对象
     */
    public static <T> T jsonAnswer2Object(String answer, Class<T> classOfT) {
        // 提取 markdown 代码块中的 JSON
        Matcher matcher = JSON_PATTERN.matcher(answer);
        if (matcher.find()) {
            answer = matcher.group(2);
        }

        // 清理 JSON 字符串
        Gson gson = new Gson();
        answer = answer.replaceAll(",\\s*//.*?\\n", ", \n")  // 删除注释
                .replaceAll(",(?=\\s*?[}\\]])", "");           // 删除尾随逗号

        return gson.fromJson(answer, classOfT);
    }
}
