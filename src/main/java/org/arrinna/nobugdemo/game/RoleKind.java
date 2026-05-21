package org.arrinna.nobugdemo.game;

/**
 * 标准 10 人局身份池：3 狼、1 预言家、1 女巫、1 猎人、4 村民
 */
public enum RoleKind {
    WEREWOLF("狼人", "werewolf"),
    PROPHET("预言家", "seer"),
    WITCH("女巫", "witch"),
    HUNTER("猎人", "hunter"),
    VILLAGER("村民", "villager");

    private final String displayName;
    private final String roleKey;

    RoleKind(String displayName, String roleKey) {
        this.displayName = displayName;
        this.roleKey = roleKey;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getRoleKey() {
        return roleKey;
    }
}
