package fr.rammex.landClaim.listener.lands;

import fr.rammex.landClaim.LandClaim;
import fr.rammex.landClaim.data.DataManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {
    static LandClaim plugin = LandClaim.getInstance();

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        String uuid = player.getUniqueId().toString();
        Location loc = event.getBlock().getLocation();
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