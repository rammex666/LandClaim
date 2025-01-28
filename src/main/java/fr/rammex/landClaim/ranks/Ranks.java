package fr.rammex.landClaim.ranks;

import lombok.Getter;
import org.bukkit.ChatColor;

public enum Ranks {
    TRUST(0, ChatColor.translateAlternateColorCodes('&',
            "&2Trust"),
            false,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjE3YjkyODI4YTU1MmNkZTUxOGE1ZjQyMTQ2NmM1MGM2Mjg5YmU4OWU5ZjdjZTc4ZGVhMzNmMGY3Mjc4NSJ9fX0="),
    MEMBRE(1,
            ChatColor.translateAlternateColorCodes('&',"&2Membre"),
            true,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTZjNWQ2N2QyMjFkYTNlOTJhNmZkYTA0ZDhhNTEyMmZmZGNmYTNlZTM4MThkMzc5MGE0YTY2NjY4MGNkIn19fQ=="),
    AINE(2,
            ChatColor.translateAlternateColorCodes('&',"&2Aîné"),
            false,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzMzY2UzN2JiZGYyZWI1MTkwY2Q2Mjg3ODdmZGI3Y2VkMzYxOGNlZjVlM2RiMWYyZThhNWY1NTgwYWY0YTM5In19fQ=="),
    GRADE(3,
            ChatColor.translateAlternateColorCodes('&',"&2Gradé"),
            false,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTY4NzJmNzczYjhkY2UzMjY2NWI3MjY0NGVjNTViYjA1NDVjNzQ4NDU4OTQwZThmNTQxNTAyNzZjMjBlOCJ9fX0="),
    OFFICIER(4,
            ChatColor.translateAlternateColorCodes('&',"&2Officier"),
            false,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWQzMTJlZGJjNDlhNTcxNzI3MTVjZTU2MTQxNjkzMWUwODRhZmFmYzE0ZTdlNGExOGYxYzk2ZjJjNjU4YTdjIn19fQ==");

    @Getter
    private final int permissionLevel;

    @Getter
    private final String displayName;

    @Getter
    private final String skullURL;

    private final boolean isDefault;

    Ranks(int permissionLevel, String displayName, boolean isDefault, String skullURL) {
        this.permissionLevel = permissionLevel;
        this.displayName = displayName;
        this.isDefault = isDefault;
        this.skullURL = skullURL;
    }

    public boolean isDefault() {
        return isDefault;
    }
}