package org.arrinna.nobugdemo.Player;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.arrinna.nobugdemo.Role.WerewolfRole;
import org.arrinna.nobugdemo.dto.vo.KillResult;
import org.arrinna.nobugdemo.exception.GameOverException;
import org.arrinna.nobugdemo.game.GameData;
import org.arrinna.nobugdemo.game.RoleKind;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class WerewolfPlayer extends AbstractPlayer{

    private final WerewolfRole role;

    public WerewolfPlayer(int id, String roleName,@NonNull  String service
    , @NonNull String apiKey,String modelName,Double temperature){
        super(id, roleName, RoleKind.WEREWOLF);
        this.role=new WerewolfRole(service, apiKey, modelName, temperature);

    }

    @Override
    public boolean isGoodGuys() {
        return false;
    }

    @Override
    public String speak(int index) {
       List<String> werewolfTeams = GameData.getWerewolfPlayers()
               .stream()
               .map(p -> p.getId() + "号玩家")
               .collect(Collectors.toList());
       return role.speak(id,index,GameData.getGameInformation(),werewolfTeams,GameData.werewolfKillId);
    }

    @Override
    public int vote(List<Integer> votingIds) {
        return role.vote(id,GameData.getGameInformation(),votingIds);
    }

    @Override
    public String testament() throws GameOverException {
        return role.testament(id, GameData.getGameInformation());
    }

    public KillResult skill(String teamStrategies){
        List<String> werewolfTeammates=GameData.getWerewolfPlayers()
                .stream()
                .filter(p -> p.getId() != id)
                .map(p -> p.getId() + "号玩家")
                .collect(Collectors.toList());
        return role.skill(id, GameData.getGameInformation(), werewolfTeammates, teamStrategies);

    }
}
