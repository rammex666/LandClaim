package fr.rammex.landClaim.listener.lands;

import fr.rammex.landClaim.LandClaim;
import fr.rammex.landClaim.data.DataManager;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.Map;

public class InteractRedStoneListener implements Listener {
    static LandClaim plugin = LandClaim.getInstance();
    private static final Map<Location, Player> redstoneInteractions = new HashMap<>();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Location loc = event.getClickedBlock() != null ? event.getClickedBlock().getLocation() : null;

        if (loc != null && event.getClickedBlock().getType().isInteractable()) {
            redstoneInteractions.put(loc, player);
        }
    }

    @EventHandler
    public void onRedstoneInteract(BlockRedstoneEvent event) {
        Block block = event.getBlock();
        Location loc = block.getLocation();
        String locString = loc.toString();

        Player player = getPlayerInteractingWithRedstone(loc);
        if (player == null) {
            return;
        }

        String uuid = player.getUniqueId().toString();

        if (!DataManager.isPlayerInClaim(uuid, loc)) {
            return;
        }
        if (!DataManager.playerAsPermission(uuid, locString, 6)) {
            event.setNewCurrent(event.getOldCurrent());
            player.sendMessage(plugin.getMessagesConf().getString("land.dont_have_permission"));
        }
    }

    private Player getPlayerInteractingWithRedstone(Location loc) {
        return redstoneInteractions.get(loc);
    }
}