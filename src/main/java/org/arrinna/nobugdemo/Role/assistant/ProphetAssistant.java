package org.arrinna.nobugdemo.Role.assistant;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

import java.util.List;

public interface ProphetAssistant {

    @SystemMessage(fromResource = "/prompt_template/player-system-prompt.txt")
    @UserMessage(fromResource = "/prompt_template/prophet-speak-prompt.txt")
    String speak(@V("id") int id, @V("index") int index,
                 @V("gameInformation") String gameInformation,
                 @V("checkId") int checkId, @V("camp") String camp, @V("checkCause") String checkCause);


    @SystemMessage(fromResource = "/prompt_template/player-system-prompt.txt")
    @UserMessage(fromResource = "/prompt_template/prophet-vote-prompt.txt")
    String vote(@V("id") int id,@V("gameInformation") String gameInformation,
             @V("voteIds") List<Integer> voteIds);


    /**
     * 结合要求
     * @param id
     * @param gameInformation
     * @return
     */
    @SystemMessage(fromResource = "/prompt_template/player-system-prompt.txt")
    @UserMessage(fromResource = "/prompt_template/prophet-skill-prompt.txt")
    String skill(@V("id") int id, @V("gameInformation") String gameInformation);



    @SystemMessage(fromResource = "/prompt_template/player-system-prompt.txt")
    @UserMessage(fromResource = "/prompt_template/prophet-testament-prompt.txt")
    String testament(@V("id") int id, @V("gameInformation") String gameInformation);


}
