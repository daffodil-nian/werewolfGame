package org.arrinna.nobugdemo.Player;

import lombok.NonNull;
import org.arrinna.nobugdemo.Role.HunterRole;
import org.arrinna.nobugdemo.dto.vo.ShotResult;
import org.arrinna.nobugdemo.exception.GameOverException;
import org.arrinna.nobugdemo.game.GameData;
import org.arrinna.nobugdemo.game.RoleKind;

import java.util.List;

public class HunterPlayer extends AbstractPlayer {

    private final HunterRole role;

    public HunterPlayer(int id,String roleName,@NonNull String service
            , @NonNull String apiKey,String modelName,Double temperature){
        super(id, roleName, RoleKind.HUNTER);
        this.role=new HunterRole(service,apiKey,modelName,temperature);
    }
    @Override
    public boolean isGoodGuys() {
        return true;
    }

    @Override
    public String speak(int index) {
        return role.speak(id, index, GameData.getGameInformation());
    }

    @Override
    public int vote(List<Integer> votingIds) {
        return role.vote(id, GameData.getGameInformation(),votingIds);
    }

    @Override
    public String testament() throws GameOverException {
        ShotResult result =role.skill(id,GameData.getGameInformation());
        //然后获取是否开枪
        if(result.isShot()){
            int shotId = result.getShotId();
            System.out.println(">>>>>>" + shotId + "号被开枪带走<<<<<<");
            GameData.playerDead(shotId);
        }
        return result.getShotCause();
    }
}
