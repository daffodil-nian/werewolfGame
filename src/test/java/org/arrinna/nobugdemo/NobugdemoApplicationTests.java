package org.arrinna.nobugdemo;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.dashscope.QwenChatModel;
import org.arrinna.nobugdemo.config.AiModelProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class NobugdemoApplicationTests {

    @Autowired
    private AiModelProperties aiModelProperties;

    @Test
    public void testSimple() {
        System.out.println(aiModelProperties.getApiKey());
    }
    @Test
    public void testConfig(){

        QwenChatModel model = QwenChatModel.builder()
                .apiKey(aiModelProperties.getApiKey())
                .modelName(aiModelProperties.getModelName())
                .temperature(0.7f)
                .build();
        UserMessage userMessage = UserMessage.from("你好AI，你玩狼人杀吗");
        AiMessage response = model.generate(userMessage).content();
        System.out.println(response.toString());
    }

}
