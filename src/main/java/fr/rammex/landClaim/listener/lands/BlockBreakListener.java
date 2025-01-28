package fr.rammex.landClaim.listener.lands;

import fr.rammex.landClaim.LandClaim;
import fr.rammex.landClaim.data.DataManager;
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
        String loc = event.getBlock().getLocation().toString();

        if (!DataManager.playerAsPermission(uuid, loc, 2)) {

            event.setCancelled(true);
            player.sendMessage(plugin.getMessagesConf().getString("land.dont_have_permission"));
        }
    }
}