package org.arrinna.nobugdemo.Role;

import dev.langchain4j.service.AiServices;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.arrinna.nobugdemo.Role.assistant.ProphetAssistant;
import org.arrinna.nobugdemo.dto.vo.CheckResult;
import org.arrinna.nobugdemo.dto.vo.SpeakResult;
import org.arrinna.nobugdemo.dto.vo.TestamentResult;
import org.arrinna.nobugdemo.dto.vo.VoteResult;
import org.arrinna.nobugdemo.utils.ChatLanguageModelUtil;

import java.util.List;

@Slf4j
public class ProphetRole extends AbstractRole implements DeityRole{

    @NonNull
    private final ProphetAssistant assistant;

    public ProphetRole(@NonNull String service,@NonNull String apiKey,
                       @NonNull String modelName,Double temperature){
        super(service,apiKey,modelName,temperature);
        this.assistant= AiServices.create(ProphetAssistant.class,chatLanguageModel);
    }

    public String speak(int id, int index, String gameInformation, int checkId, String camp, String checkCause){
        String answer = assistant.speak(id, index, gameInformation, checkId, camp, checkCause);
        log.info(answer);
        SpeakResult result = ChatLanguageModelUtil.jsonAnswer2Object(answer, SpeakResult.class);
        log.info("发言结果：{}", result);
        return result.getMySpeech();

    }
    public int vote(int id, String gameInformation, List<Integer> voteIds){
        String answer = assistant.vote(id, gameInformation, voteIds);
        log.info(answer);
        VoteResult result = ChatLanguageModelUtil.jsonAnswer2Object(answer, VoteResult.class);
        log.info("投票结果：{}", result);
        return result.getVoteId() != null ? result.getVoteId() : -1;
    }
    public CheckResult skill(int id, String gameInformation){
        String answer = assistant.skill(id, gameInformation);
        log.info(answer);
        CheckResult result = ChatLanguageModelUtil.jsonAnswer2Object(answer, CheckResult.class);
        log.info("查验输出：{}", result);
        return result;
    }
    public String testament(int id, String gameInformation){
        String answer = assistant.testament(id, gameInformation);
        log.info(answer);
        TestamentResult result = ChatLanguageModelUtil.jsonAnswer2Object(answer, TestamentResult.class);
        log.info("遗言结果：{}", result);
        return result.getLastWords();
    }
}
