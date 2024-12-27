package fr.rammex.landClaim.commands;

import fr.rammex.landClaim.LandClaim;
import fr.rammex.landClaim.utils.Messages;
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
                        sender.sendMessage(Messages.getMessage("usage.land.create"));
                        return false;
                    }
                    if(!LandClaim.instance.getDatabase("lands").isNameUsed(args[1]) && !LandClaim.instance.getDatabase("lands").asLand(player.getUniqueId().toString())) {
                        LandClaim.instance.getDatabase("lands").createLand(player.getUniqueId().toString(), args[1]);
                        LandClaim.instance.getDatabase("lands").addPlayer(player.getUniqueId().toString(), args[1], "owner");
                        sender.sendMessage(Messages.getMessage("land.created"));
                    } else {
                        sender.sendMessage(Messages.getMessage("land.create.error.name_already_exists"));
                    }
                    break;
                case "delete":
                    if (args.length != 2) {
                        sender.sendMessage(Messages.getMessage("usage.land.delete"));
                        return false;
                    }
                    if(LandClaim.instance.getDatabase("lands").isNameUsed(args[1]) && LandClaim.instance.getDatabase("lands").isOwner(player.getUniqueId().toString(), args[1])) {
                        LandClaim.instance.getDatabase("lands").deleteLand(player.getUniqueId().toString(), args[1]);
                        sender.sendMessage(Messages.getMessage("land.delete.succes"));
                    } else {
                        sender.sendMessage(Messages.getMessage("land.delete.error.not_owner"));
                    }
                    break;
                default:
                    sender.sendMessage(Messages.getMessage("usage.error.command_not_found"));
                    break;
            }
        }

        return true;
    }
}