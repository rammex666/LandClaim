package fr.rammex.landClaim.listener.lands;

import fr.rammex.landClaim.LandClaim;
import fr.rammex.landClaim.data.DataManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class HitEntityListener implements Listener {

    static LandClaim plugin = LandClaim.getInstance();

    @EventHandler
    public void onHitEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            String uuid = player.getUniqueId().toString();
            Location loc = player.getLocation();
            String locString = loc.toString();

            if(!DataManager.isPlayerInClaim(uuid, loc)) {
                return;
            }
            if (!DataManager.playerAsPermission(uuid, locString, 2)) {
                event.setCancelled(true);
                player.sendMessage(plugin.getMessagesConf().getString("land.dont_have_permission"));
            }
        }
    }
}
