package fr.rammex.landClaim.utils;

import fr.rammex.landClaim.LandClaim;

public class Messages {
    static LandClaim plugin = LandClaim.getInstance();

    public static String getMessage (String path){
        if (plugin.getMessagesConf().getString(path) == null){
            return "Message not found";
        }

        return plugin.getMessagesConf().getString(path);
    }
}
