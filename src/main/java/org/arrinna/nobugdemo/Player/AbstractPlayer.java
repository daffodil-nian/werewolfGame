package org.arrinna.nobugdemo.Player;

import lombok.Getter;
import org.arrinna.nobugdemo.game.GameData;
import org.arrinna.nobugdemo.game.RoleKind;

@Getter
public abstract class AbstractPlayer implements Player{

    protected final int id;

    protected final String roleName;

    protected final RoleKind roleKind;

    public AbstractPlayer(int id, String roleName, RoleKind roleKind){
        this.id=id;
        this.roleName=roleName;
        this.roleKind=roleKind;
    }

    @Override
    public boolean isAlive() {
        return !GameData.killIds.contains(id);
    }
}
