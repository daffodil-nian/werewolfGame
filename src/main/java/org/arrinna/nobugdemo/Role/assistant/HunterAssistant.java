package org.arrinna.nobugdemo.Role.assistant;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

import java.util.List;

public interface HunterAssistant {

    @SystemMessage(fromResource = "/prompt_template/player-system-prompt.txt")
    @UserMessage(fromResource = "/prompt_template/hunter-vote-prompt.txt")
    String vote(@V("id") int id, @V("gameInformation") String gameInformation, @V("voteIds") List<Integer> voteIds);

    @SystemMessage(fromResource = "/prompt_template/player-system-prompt.txt")
    @UserMessage(fromResource = "/prompt_template/hunter-speak-prompt.txt")
    String speak(@V("id") int id, @V("index") int index,@V("gameInformation") String gameInformation);

    @SystemMessage(fromResource = "/prompt_template/player-system-prompt.txt")
    @UserMessage(fromResource = "/prompt_template/hunter-skill-prompt.txt")
    String skill(@V("id") int id, @V("gameInformation") String gameInformation);

}
