package org.arrinna.nobugdemo.Role;


import dev.langchain4j.service.AiServices;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.arrinna.nobugdemo.Role.assistant.VillagerAssistant;
import org.arrinna.nobugdemo.dto.vo.SpeakResult;
import org.arrinna.nobugdemo.dto.vo.TestamentResult;
import org.arrinna.nobugdemo.dto.vo.VoteResult;
import org.arrinna.nobugdemo.utils.ChatLanguageModelUtil;

import java.util.List;

/**
 * -- 村民
 * 除了assistant之外，我们还得调用AI，投票结果，发言结果，遗言是什么
 */

@Slf4j
public class VillagerRole extends AbstractRole implements GoodRole{

    @NonNull
    private final VillagerAssistant assistant;

    /**
     * 创建村民的服务类型哈，然后我们就可以调用这个assistant了，我们调用一下api看看返回的是什么东西
     * @param service
     * @param apiKey
     * @param modelName
     * @param temperature
     */
    public VillagerRole(@NonNull String service,@NonNull String apiKey,String modelName,Double temperature){
        super(service,apiKey,modelName,temperature);
        this.assistant= AiServices.create(VillagerAssistant.class,chatLanguageModel);
    }

    public int vote(int id, String gameInformation, List<Integer> voteIds){
        String answer = assistant.vote(id,gameInformation,voteIds);
        log.info(answer);
    //然后给出投票结果
        VoteResult result = ChatLanguageModelUtil.jsonAnswer2Object(answer, VoteResult.class);
        log.info("投票结果：{}", result);

        return result.getVoteId()!=null?result.getVoteId():-1;
    }
    public String testament(int id, String gameInformation) {
        String answer = assistant.testament(id, gameInformation);
        log.info(answer);
        TestamentResult result = ChatLanguageModelUtil.jsonAnswer2Object(answer, TestamentResult.class);
        log.info("遗言结果：{}", result);
        return result.getLastWords();
    }
    public String speak(int id, int index, String gameInformation) {
        String answer = assistant.speak(id, index, gameInformation);
        log.info(answer);
        SpeakResult result = ChatLanguageModelUtil.jsonAnswer2Object(answer, SpeakResult.class);
        log.info("发言结果：{}", result);
        return result.getMySpeech();
    }

}
