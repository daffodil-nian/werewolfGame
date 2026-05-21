package org.arrinna.nobugdemo.Role;

import dev.langchain4j.service.AiServices;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.arrinna.nobugdemo.Role.assistant.HunterAssistant;
import org.arrinna.nobugdemo.dto.vo.ShotResult;
import org.arrinna.nobugdemo.dto.vo.SpeakResult;
import org.arrinna.nobugdemo.dto.vo.VoteResult;
import org.arrinna.nobugdemo.utils.ChatLanguageModelUtil;

import java.util.List;

@Slf4j
public class HunterRole extends AbstractRole implements GoodRole{

    @NonNull
    private final HunterAssistant assistant;

    public HunterRole(@NonNull String service,@NonNull String apiKey,
                      @NonNull String modelName,Double temperature){
        super(service,apiKey,modelName,temperature);
        this.assistant= AiServices.create(HunterAssistant.class,chatLanguageModel);
    }

    /**
     * 发言
     * @param id
     * @param index
     * @param gameInformation
     * @return
     */
    public String speak(int id,int index,String gameInformation){
        String answer = assistant.speak(id,index,gameInformation);
        log.info("speak:{}",answer);
        SpeakResult result= ChatLanguageModelUtil.jsonAnswer2Object(answer,SpeakResult.class);
        return result.getMySpeech();
    }

    /**
     * 投票
     * @param id
     * @param gameInformation
     * @param voteIds
     * @return
     */
    public int vote(int id, String gameInformation, List<Integer> voteIds) {
        //获取回答
        String answer = assistant.vote(id, gameInformation, voteIds);
        log.info(answer);
        //解析投票结果
        VoteResult result = ChatLanguageModelUtil.jsonAnswer2Object(answer, VoteResult.class);
        log.info("投票结果：{}", result);
        return result.getVoteId()!=null?result.getVoteId():-1;
    }

    public ShotResult skill(int id, String gameInformation){

        String answer = assistant.skill(id,gameInformation);
        log.info("skill:{}",answer);
        ShotResult result= ChatLanguageModelUtil.jsonAnswer2Object(answer,ShotResult.class);
        return result;
    }
}
