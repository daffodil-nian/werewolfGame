package org.arrinna.nobugdemo.Role;

import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.V;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.arrinna.nobugdemo.Role.assistant.WerewolfAssistant;
import org.arrinna.nobugdemo.dto.vo.KillResult;
import org.arrinna.nobugdemo.dto.vo.SpeakResult;
import org.arrinna.nobugdemo.dto.vo.TestamentResult;
import org.arrinna.nobugdemo.dto.vo.VoteResult;
import org.arrinna.nobugdemo.utils.ChatLanguageModelUtil;

import java.util.List;

@Slf4j
public class WerewolfRole extends AbstractRole implements BadRole{

    @NonNull
    private final WerewolfAssistant assistant;

    public WerewolfRole(@NonNull String service,@NonNull String apiKey,
                        @NonNull String modelName,Double temperature){
        super(service,apiKey,modelName,temperature);
        this.assistant= AiServices.create(WerewolfAssistant.class,chatLanguageModel);
    }

    //然后接下来写四种方法

    /**
     * 狼人发言
     * @param id
     * @param index
     * @param gameInformation
     * @param werewolfTeams
     * @param killId
     * @return
     */
    public String speak(int id, int index, String gameInformation,
                        List<String> werewolfTeams, int killId){
        //1.获取答案
        String answer= assistant.speak(id, index, gameInformation, werewolfTeams, killId);
        log.info("狼人发言：{}",answer);
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
    public int vote( int id, String gameInformation, List<Integer> voteIds){
        //1.获取用户的答案
        String answer =assistant.vote(id, gameInformation, voteIds);
        VoteResult result = ChatLanguageModelUtil.jsonAnswer2Object(answer, VoteResult.class);

        return result.getVoteId()!=null?result.getVoteId():-1;
    }
    public String testament(int id, String gameInformation){
        String answer = assistant.testament(id, gameInformation);
        log.info(answer);
        TestamentResult result = ChatLanguageModelUtil.jsonAnswer2Object(answer, TestamentResult.class);
        log.info("遗言结果：{}", result);
        return result.getLastWords();
    }
    public KillResult skill(int id, String gameInformation, List<String> werewolfTeammates, String teamStrategies){
        String answer = assistant.skill(id, werewolfTeammates, gameInformation, teamStrategies);
        log.info(answer);
        KillResult result=ChatLanguageModelUtil.jsonAnswer2Object(answer, KillResult.class);
        log.info("杀人结果：{}", result);
        return result;
    }

}
