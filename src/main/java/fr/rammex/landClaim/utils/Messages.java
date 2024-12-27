package fr.rammex.landClaim.utils;

import fr.rammex.landClaim.LandClaim;

public class Messages {
    LandClaim plugin = LandClaim.getInstance();

    public static String getMessage (String path){
        if (LandClaim.getInstance().getMessagesConf().getString(path) == null){
            return "Message not found";
        }

        return LandClaim.getInstance().getMessagesConf().getString(path);
    }
}
