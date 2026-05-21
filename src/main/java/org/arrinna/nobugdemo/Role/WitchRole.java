package org.arrinna.nobugdemo.Role;

import dev.langchain4j.service.AiServices;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.arrinna.nobugdemo.Role.assistant.WitchAssistant;
import org.arrinna.nobugdemo.dto.vo.PoisonResult;
import org.arrinna.nobugdemo.dto.vo.SpeakResult;
import org.arrinna.nobugdemo.dto.vo.TestamentResult;
import org.arrinna.nobugdemo.dto.vo.VoteResult;
import org.arrinna.nobugdemo.utils.ChatLanguageModelUtil;

import java.util.List;

//abstract
@Slf4j
public class WitchRole extends AbstractRole implements DeityRole {
    @NonNull
    private final WitchAssistant assistant;

    public WitchRole(@NonNull String service, @NonNull String apiKey, String modelName, Double temperature) {
        super(service, apiKey, modelName, temperature);
        this.assistant = AiServices.create(WitchAssistant.class, chatLanguageModel);
    }

    public String speak(int id, int index, String gameInformation, String skillInformation) {
        String answer = assistant.speak(id, index, gameInformation, skillInformation);
        log.info(answer);
        SpeakResult result = ChatLanguageModelUtil.jsonAnswer2Object(answer, SpeakResult.class);
        log.info("发言结果：{}", result);
        return result.getMySpeech();
    }

    public int vote(int id, String gameInformation, List<Integer> voteIds) {
        String answer = assistant.vote(id, gameInformation, voteIds);
        log.info(answer);
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

    /**
     * 女巫用毒药
     * @param id
     * @param gameInformation
     * @param killId
     * @return
     */
    public PoisonResult skill(int id, String gameInformation, int killId) {
        String answer = assistant.skill(id, gameInformation, killId);
        log.info(answer);
        PoisonResult result = ChatLanguageModelUtil.jsonAnswer2Object(answer, PoisonResult.class);
        log.info("使用毒药结果：{}", result);
        return result;
    }
}
