package org.arrinna.nobugdemo.Role.assistant;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

import java.util.List;

public interface WerewolfAssistant {

    /***
     *
     * @param id
     * @param index
     * @param gameInformation
     * @param werewolfTeams
     * @param killId 是你们昨天晚上杀的人的Id
     * @return
     */
    @SystemMessage(fromResource = "/prompt_template/player-system-prompt.txt")
    @UserMessage(fromResource = "/prompt_template/werewolf-speak-prompt.txt")
    String speak(@V("id") int id, @V("index") int index,
                 @V("gameInformation") String gameInformation, @V("werewolfTeams")List<String> werewolfTeams,
                 @V("killId") int killId);


    /**
     * 给所有人投票
     * @param id
     * @param gameInformation
     * @param voteIds
     * @return
     */
    @SystemMessage(fromResource = "/prompt_template/player-system-prompt.txt")
    @UserMessage(fromResource = "/prompt_template/werewolf-vote-prompt.txt")
    String vote(@V("id") int id, @V("gameInformation") String gameInformation,@V("voteIds") List<Integer> voteIds);


    /**
     * 遗言
     * @param id
     * @param gameInformation
     * @return
     */
    @SystemMessage(fromResource = "/prompt_template/player-system-prompt.txt")
    @UserMessage(fromResource = "/prompt_template/werewolf-testament-prompt.txt")
    String testament(@V("id") int id, @V("gameInformation") String gameInformation);

    /**
     * 技能，狼人是杀人的
     * @param id
     * @param werewolfTeammates
     * @param gameInformation
     * @param teamStrategies
     * @return
     */
    @SystemMessage(fromResource = "/prompt_template/player-system-prompt.txt")
    @UserMessage(fromResource = "/prompt_template/werewolf-skill-prompt.txt")
    String skill(@V("id") int id, @V("werewolfTeammates") List<String> werewolfTeammates,
                 @V("gameInformation") String gameInformation, @V("teamStrategies") String teamStrategies);
}
