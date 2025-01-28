package fr.rammex.landClaim.listener.player;

import fr.rammex.landClaim.data.DataManager;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class LandListener implements Listener {


    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Location from = event.getFrom();
        Location to = event.getTo();
        Player player = event.getPlayer();

        if (to != null && (from.getChunk().getX() != to.getChunk().getX() || from.getChunk().getZ() != to.getChunk().getZ())) {
            Chunk oldChunk = from.getChunk();
            Chunk newChunk = to.getChunk();


            if(!DataManager.isChunkClaimed(oldChunk.toString()) && DataManager.isChunkClaimed(newChunk.toString())){
                String newChunkMessage = ChatColor.translateAlternateColorCodes('&', "&6Vous entrez dans "+DataManager.getChunkLandName(newChunk.toString()));
                player.sendTitle("", newChunkMessage, 10, 70, 20);
            } else if (DataManager.isChunkClaimed(oldChunk.toString()) && !DataManager.isChunkClaimed(newChunk.toString())) {
                String oldChunkMessage = ChatColor.translateAlternateColorCodes('&', "&6Vous quittez "+DataManager.getChunkLandName(oldChunk.toString()));
                player.sendTitle("", oldChunkMessage, 10, 70, 20);
            } else if (DataManager.isChunkClaimed(oldChunk.toString()) && DataManager.isChunkClaimed(newChunk.toString()) && !DataManager.getChunkLandName(oldChunk.toString()).equals(DataManager.getChunkLandName(newChunk.toString()))){
                String oldChunkMessage = ChatColor.translateAlternateColorCodes('&', "&6Vous quittez "+DataManager.getChunkLandName(oldChunk.toString()));
                String newChunkMessage = ChatColor.translateAlternateColorCodes('&', "&6Vous entrez dans "+DataManager.getChunkLandName(newChunk.toString()));
                player.sendTitle(oldChunkMessage, newChunkMessage, 10, 70, 20);
            }

        }
    }

}
