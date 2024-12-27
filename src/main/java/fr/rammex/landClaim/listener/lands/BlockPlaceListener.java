package fr.rammex.landClaim.listener.lands;

import fr.rammex.landClaim.data.DataManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listener {
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        String uuid = player.getUniqueId().toString();
        String loc = event.getBlock().getLocation().toString();

        if (!DataManager.playerAsPermission(uuid, loc, 2)) {
            event.setCancelled(true);
            player.sendMessage("You do not have permission to place blocks in this area.");
        }
    }
}
