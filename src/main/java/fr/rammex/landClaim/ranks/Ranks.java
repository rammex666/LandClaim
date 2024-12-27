package fr.rammex.landClaim.ranks;

import lombok.Getter;

public enum Ranks {
    TRUST(0, "Trust", false),
    MEMBRE(1, "Membre", true),
    AÎNÉ(2, "Aîné", false),
    GRADÉ(3, "Gradé", false),
    OFFICIER(4, "Officier", false);

    @Getter
    private final int permissionLevel;
    @Getter
    private final String displayName;
    private final boolean isDefault;

    Ranks(int permissionLevel, String displayName, boolean isDefault) {
        this.permissionLevel = permissionLevel;
        this.displayName = displayName;
        this.isDefault = isDefault;
    }

    public boolean isDefault() {
        return isDefault;
    }
}