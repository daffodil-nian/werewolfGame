package org.arrinna.nobugdemo.Role.assistant;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

import java.util.List;

public interface WitchAssistant {

    /**
     *  投票
     * @param id  玩家id
     * @param index  投票的turn次
     * @param gameInformation  游戏信息
     * @param skillInformation  技能信息
     * @return
     */
    @SystemMessage(fromResource = "/prompt_template/player-system-prompt.txt")
    @UserMessage(fromResource = "/prompt_template/witch-speak-prompt.txt")
    String speak(@V("id") int id,@V("index") int index,@V("gameInformation") String gameInformation
            ,@V("skillInformation") String skillInformation);


    /**
     *
     * @param id
     * @param gameInformation
     * @param killId 使用技能毒杀谁
     * @return
     */
    @SystemMessage(fromResource = "/prompt_template/player-system-prompt.txt")
    @UserMessage(fromResource = "/prompt_template/witch-skill-prompt.txt")
    String skill(@V("id") int id,@V("gameInformation") String gameInformation,@V("killId") int killId);


    /**
     * 只需要跟据自己知道的信息和已知的身份和gameInformation就行了
     * @param id
     * @param gameInformation
     * @return
     */
    @SystemMessage(fromResource = "/prompt_template/player-system-prompt.txt")
    @UserMessage(fromResource = "/prompt_template/witch-testament-prompt.txt")
    String testament(@V("id") int id,@V("gameInformation") String gameInformation);


    /**
     * 给几号投票，id是哪位玩家，跟据已有的知识在剩余存活玩家中投票
     * @param id
     * @param gameInformation
     * @param voteIds
     * @return
     */
    @SystemMessage(fromResource = "/prompt_template/player-system-prompt.txt")
    @UserMessage(fromResource = "/prompt_template/witch-vote-prompt.txt")
    String vote(@V("id") int id, @V("gameInformation") String gameInformation, @V("voteIds")List<Integer> voteIds);
}
