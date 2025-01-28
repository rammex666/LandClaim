package fr.rammex.landClaim.listener.lands;

import fr.rammex.landClaim.LandClaim;
import fr.rammex.landClaim.data.DataManager;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class RideEntityListener implements Listener {
    static LandClaim plugin = LandClaim.getInstance();

    @EventHandler
    public void onRideEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();
        String uuid = player.getUniqueId().toString();
        String locString = entity.getLocation().toString();
        Location loc = entity.getLocation();

        if (entity instanceof org.bukkit.entity.Horse ||
                entity instanceof org.bukkit.entity.Pig ||
                entity instanceof org.bukkit.entity.Llama ||
                entity instanceof org.bukkit.entity.Strider) {

            if(!DataManager.isPlayerInClaim(uuid, loc)) {
                return;
            }

            if(!DataManager.playerAsPermission(uuid, locString, 2)){
                event.setCancelled(true);
                player.sendMessage(plugin.getMessagesConf().getString("land.dont_have_permission"));
            }
        }
    }
}