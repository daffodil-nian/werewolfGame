package org.arrinna.nobugdemo.Role;

import org.arrinna.nobugdemo.utils.ChatLanguageModelUtil;
import dev.langchain4j.model.chat.ChatLanguageModel;
import lombok.NonNull;

public abstract class AbstractRole {

    @NonNull
    protected final ChatLanguageModel chatLanguageModel;

    /**
     * 工具类
     * @param service
     * @param apiKey
     * @param modelName
     * @param temperature
     */
    public AbstractRole(@NonNull String service,@NonNull String apiKey,
                        @NonNull String modelName,Double temperature){
        this.chatLanguageModel= ChatLanguageModelUtil.build(service,apiKey,modelName,temperature);
    }

}
