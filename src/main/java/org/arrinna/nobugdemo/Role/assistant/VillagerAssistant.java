package org.arrinna.nobugdemo.Role.assistant;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

import java.util.List;

/**
 * 村民
 * 1.发言
 * 2.投票
 * 3.遗言
 */
public interface VillagerAssistant {

    /**
     * 发言
     * @param id
     * @param index
     * @param gameInformation 也就是当前游戏的局面信息
     * @return
     */
    @SystemMessage(fromResource = "/prompt_template/player-system-prompt.txt")
    @UserMessage(fromResource = "/prompt_template/villager-speak-prompt.txt")
    String speak(@V("id") int id,@V("index") int index,@V("gameInformation") String gameInformation);

    /**
     *
     * @param id
     * @param gameInformation
     * @param voteIds 可投票的玩家的ID
     * @return
     */
    @SystemMessage(fromResource = "/prompt_template/player-system-prompt.txt")
    @UserMessage(fromResource = "/prompt_template/villager-vote-prompt.txt")
    String vote(@V("id") int id, @V("gameInformation") String gameInformation, @V("voteIds")List<Integer> voteIds);


    @SystemMessage(fromResource = "/prompt_template/player-system-prompt.txt")
    @UserMessage(fromResource = "/prompt_template/villager-testament-prompt.txt")
    String testament(@V("id") int id,@V("gameInformation") String gameInformation);
}
