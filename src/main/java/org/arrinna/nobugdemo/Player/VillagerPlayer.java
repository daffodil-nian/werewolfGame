package org.arrinna.nobugdemo.Player;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.arrinna.nobugdemo.Role.VillagerRole;
import org.arrinna.nobugdemo.exception.GameOverException;
import org.arrinna.nobugdemo.game.GameData;
import org.arrinna.nobugdemo.game.RoleKind;

import java.util.List;

@Slf4j
public class VillagerPlayer extends AbstractPlayer{

    private final VillagerRole role;


    public VillagerPlayer(int id, String roleName, @NonNull String service,@NonNull String apiKey,
                          String modelName,Double temperature){
        super(id, roleName, RoleKind.VILLAGER);
        this.role=new VillagerRole(service,apiKey,modelName,temperature);
    }

    @Override
    public boolean isGoodGuys() {
        return true;
    }

    @Override
    public String speak(int index) {
        return role.speak(id,index, GameData.getGameInformation());
    }

    @Override
    public int vote(List<Integer> votingIds) {
        return role.vote(id,GameData.getGameInformation(),votingIds);
    }

    @Override
    public String testament() throws GameOverException {
        return role.testament(id,GameData.getGameInformation());
    }
}
