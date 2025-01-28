package fr.rammex.landClaim.permissions;

import lombok.Getter;

public enum Permissions {

    BREAK_BLOCKS(0, "Casser des blocs", "true", false),
    PLACE_BLOCKS(1, "Placer des blocs", "true", false),
    INTERACT_BLOCKS(2, "Interactions", "true", true),
    INTERACT_ENTITIES(3, "Interactions avec entitées", "true", false),
    HIT_ENTITIES(4, "Frapper les entitées", "true", true),
    INTERACT_OPENINGS(5, "Interactions avec ouvertures", "true", true),
    INTERACT_REDSTONE(6, "Interactions redstone", "true", true),
    RIDE_ENTITIES(7, "Monter sur les entitées", "true", false),
    USE_ENDER_PEARLS(8, "Utiliser les enderpearl", "true", false),
    PLACE_HOME(9, "Placer des homes", "true", false),
    USE_LAND_CHEST(10, "Utiliser le coffre du land", "true", true),
    USE_LAND_BANK(11, "Utiliser la bank du land", "true", true),
    MANAGE_PERMISSIONS(12, "Gérer les permissions", "true", false),
    MANAGE_SETTINGS(13, "Gérer les settings", "true", false);

    @Getter
    public final int permId;

    @Getter
    public final String permName;

    @Getter
    public final String defaultStatus;

    public final boolean isAdvanced;

    Permissions(int permId, String permName, String defaultStatus, boolean isAdvanced) {
        this.defaultStatus = defaultStatus;
        this.permId = permId;
        this.permName = permName;
        this.isAdvanced = isAdvanced;
    }

    public boolean isAdvanced() {
        return isAdvanced;
    }
}