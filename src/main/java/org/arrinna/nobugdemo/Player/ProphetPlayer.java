package org.arrinna.nobugdemo.Player;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.arrinna.nobugdemo.Role.ProphetRole;
import org.arrinna.nobugdemo.dto.vo.CheckResult;
import org.arrinna.nobugdemo.exception.GameOverException;
import org.arrinna.nobugdemo.game.GameData;
import org.arrinna.nobugdemo.game.RoleKind;

import java.util.List;

@Slf4j
public class ProphetPlayer extends AbstractPlayer{

    //1.把预言家的角色也弄进来

    private final ProphetRole role;

    @lombok.Getter
    private CheckResult checkResult;

    private boolean checkIsOk;


    public ProphetPlayer(int id, String roleName, @NonNull String service, @NonNull String apiKey, String modelName, Double temperature) {
        super(id, roleName, RoleKind.PROPHET);
        this.role = new ProphetRole(service, apiKey, modelName, temperature);
    }

    @Override
    public boolean isGoodGuys() {
        return true;
    }

    @Override
    public String speak(int index) {

        if (checkResult == null) {
            return role.speak(id, index, GameData.getGameInformation(), -1, "未知", "尚未查验");
        }
        return role.speak(id, index, GameData.getGameInformation(), checkResult.getCheckId()
                , checkIsOk ? "好人" : "狼人", checkResult.getCheckCause());
    }

    @Override
    public int vote(List<Integer> votingIds) {
        return role.vote(id,GameData.getGameInformation(),votingIds);
    }

    @Override
    public String testament() throws GameOverException {
        return role.testament(id,GameData.getGameInformation());
    }

    public void skill(){
        checkResult = role.skill(id, GameData.getGameInformation());
        if (checkResult == null || checkResult.getCheckId() == null) {
            log.warn("预言家查验结果无效，跳过本夜查验");
            return;
        }
        Integer resolvedId = GameData.resolveAlivePlayerId(checkResult.getCheckId(), id);
        if (resolvedId == null) {
            log.warn("预言家查验目标无效 checkId={}，当前无可用存活玩家", checkResult.getCheckId());
            return;
        }
        if (!resolvedId.equals(checkResult.getCheckId())) {
            log.warn("预言家查验目标 {} 无效，已改为 {}", checkResult.getCheckId(), resolvedId);
            checkResult.setCheckId(resolvedId);
        }
        int checkId = resolvedId;
        AbstractPlayer target = GameData.getPlayer(checkId);
        if (target == null) {
            log.warn("预言家查验目标 {} 号玩家不存在", checkId);
            return;
        }
        if (target.isGoodGuys()) {
            checkIsOk = true;
            log.info("查验的{}号是好人", checkId);
        } else {
            checkIsOk = false;
            log.info("查验的{}号是狼人", checkId);
        }
    }

}
