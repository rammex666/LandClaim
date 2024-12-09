package fr.rammex.landClaim.commands;

import fr.rammex.landClaim.LandClaim;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LandCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to use this command");
            return false;
        }
        Player player = (Player) sender;

        if (args.length == 0) {
            sender.sendMessage("Usage: /land create");
            return false;
        } else {
            switch (args[0]) {
                case "create":
                    if (args.length != 2) {
                        sender.sendMessage("Usage: /land create <name>");
                        return false;
                    }
                    String loc = player.getLocation().getChunk().toString();
                    if (!LandClaim.instance.getDatabase("lands").isChunkClaimed(loc)) {
                        LandClaim.instance.getDatabase("lands").addLand(loc, args[1],player.getUniqueId().toString());
                        player.sendMessage("Land claimed");
                    } else {
                        player.sendMessage("Land already claimed");
                    }
                    break;
                default:
                    sender.sendMessage("Unknown command");
                    break;
            }
        }

        return true;
    }
}