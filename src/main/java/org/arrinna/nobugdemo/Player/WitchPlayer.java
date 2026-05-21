package org.arrinna.nobugdemo.Player;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.arrinna.nobugdemo.Role.WitchRole;
import org.arrinna.nobugdemo.dto.vo.PoisonResult;
import org.arrinna.nobugdemo.exception.GameOverException;
import org.arrinna.nobugdemo.game.GameData;
import org.arrinna.nobugdemo.game.RoleKind;

import java.util.List;

@Slf4j
public class WitchPlayer extends AbstractPlayer{

    private final WitchRole role;
    private boolean elixirs = true; // 灵药是否还有
    private boolean poisons = true; // 毒药是否还有

    private int saveId = -1; // 昨晚救活的ID
    private int killId = -1; // 昨晚毒死的ID

    public WitchPlayer(int id, String roleName, @NonNull String service
    ,@NonNull String apiKey,String modelName,Double temperature){
        super(id, roleName, RoleKind.WITCH);
        this.role=new WitchRole(service,apiKey,modelName,temperature);
    }

    @Override
    public boolean isGoodGuys() {
        return true;
    }

    /**
     * 女巫发言看看有没有人毒死有没有人被救，这是我的技能信息的部分的内容哦
     * @param index
     * @return
     */
    @Override
    public String speak(int index) {
        String skillInformation = "";
        if (saveId != -1) {
            skillInformation += "### 用药结果 ###\n昨晚救了" + saveId + "号。\n";
            this.saveId = -1;
        } else if (killId != -1) {
            skillInformation += "### 用药结果 ###\n昨晚毒死了" + killId + "号。\n";
            this.killId = -1;
        }
        return role.speak(id, index, GameData.getGameInformation(), skillInformation);
    }

    @Override
    public int vote(List<Integer> votingIds) {
        return role.vote(id,GameData.getGameInformation(),votingIds);
    }

    @Override
    public String testament() throws GameOverException {
        return role.testament(id,GameData.getGameInformation());
    }

    /**
     * 女巫开始使用自己的技能了~~~
     * 毒人要用到LLM去决策，救人就不用
     * @param killId
     * @return
     */
    public int skill(int killId){
        if(!this.poisons){
            return -1;
        }
        PoisonResult result=role.skill(id,GameData.getGameInformation(),killId);
        if (result.isKill() && result.getKillId() != null && result.getKillId() > 0) {
            this.killId = result.getKillId();
            this.poisons = false;
            return result.getKillId();
        }
        return -1;
    }

    public void save(int killId){
        if (!this.elixirs){
            return;
        }
        this.elixirs=false;
        this.saveId=killId;
    }

}
