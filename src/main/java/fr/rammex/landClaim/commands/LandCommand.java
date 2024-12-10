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
                    if(!LandClaim.instance.getDatabase("lands").isNameUsed(args[1]) && !LandClaim.instance.getDatabase("lands").asLand(player.getUniqueId().toString())) {
                        LandClaim.instance.getDatabase("lands").createLand(player.getUniqueId().toString(), args[1]);
                        LandClaim.instance.getDatabase("lands").addPlayer(player.getUniqueId().toString(), args[1], "owner");
                        sender.sendMessage("Land created");
                    } else {
                        sender.sendMessage("Land name already used");
                    }
                    break;
                case "delete":
                    if (args.length != 2) {
                        sender.sendMessage("Usage: /land delete <name>");
                        return false;
                    }
                    if(LandClaim.instance.getDatabase("lands").isNameUsed(args[1]) && LandClaim.instance.getDatabase("lands").isOwner(player.getUniqueId().toString(), args[1])) {
                        LandClaim.instance.getDatabase("lands").deleteLand(player.getUniqueId().toString(), args[1]);
                        sender.sendMessage("Land deleted");
                    } else {
                        sender.sendMessage("Land not found or you are not the owner");
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