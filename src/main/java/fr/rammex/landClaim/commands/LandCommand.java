package fr.rammex.landClaim.commands;

import fr.rammex.landClaim.LandClaim;
import fr.rammex.landClaim.data.DataManager;
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
                    if(!DataManager.isNameUsed(args[1]) && !DataManager.asLand(player.getUniqueId().toString())) {
                        DataManager.createLand(player.getUniqueId().toString(), args[1]);
                        DataManager.addPlayer(player.getUniqueId().toString(), args[1], "owner");
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
                    if(DataManager.isNameUsed(args[1]) && DataManager.isOwner(player.getUniqueId().toString(), args[1])) {
                        DataManager.deleteLand(player.getUniqueId().toString(), args[1]);
                        sender.sendMessage(Messages.getMessage("land.delete.success"));
                    } else {
                        sender.sendMessage(Messages.getMessage("land.delete.error.not_owner"));
                    }
                    break;

                case "claim":
                    if (args.length != 2) {
                        sender.sendMessage(Messages.getMessage("usage.land.claim"));
                        return false;
                    }
                    if(!DataManager.isPlayerInLand(player.getUniqueId().toString(), args[1]) && DataManager.playerAsPermission(player.getUniqueId().toString(),
                            DataManager.getChunkLandName(player.getLocation().getChunk().toString()),
                            99)) {
                        sender.sendMessage(Messages.getMessage("land.claim.error.dont_have_permission"));
                    } else
                    if(DataManager.isChunkClaimed(player.getLocation().getChunk().toString())) {
                        sender.sendMessage(Messages.getMessage("land.claim.error.already_claimed"));
                    } else {
                        DataManager.addLand(player.getLocation().getChunk().toString(), player.getUniqueId().toString(), args[1]);
                        sender.sendMessage(Messages.getMessage("land.claim.success"));
                    }
                    break;
                case "trust":
                    if (args.length != 3) {
                        sender.sendMessage(Messages.getMessage("usage.land.trust"));
                        return false;
                    }
                    if(DataManager.isPlayerAlreadyTrusted(player.getUniqueId().toString(), args[1])) {
                        if(DataManager.isPlayerInLand(DataManager.getPlayerUUID(args[2]), args[1])) {
                            sender.sendMessage(Messages.getMessage("land.trust.error.already_trusted"));
                        } else {
                            DataManager.addPlayerTrust(DataManager.getPlayerUUID(args[2]), args[1]);
                            DataManager.addPlayer(DataManager.getPlayerUUID(args[2]), args[1], "trusted");
                            sender.sendMessage(Messages.getMessage("land.trust.success"));
                        }
                    } else {
                        sender.sendMessage(Messages.getMessage("land.trust.error.not_owner"));
                    }
                    break;
                case "untrust":
                    if (args.length != 3) {
                        sender.sendMessage(Messages.getMessage("usage.land.untrust"));
                        return false;
                    }
                    if(DataManager.isPlayerAlreadyTrusted(player.getUniqueId().toString(), args[1])) {
                        if(DataManager.isPlayerInLand(DataManager.getPlayerUUID(args[2]), args[1])) {
                            DataManager.unTrustPlayer(DataManager.getPlayerUUID(args[2]), args[1]);
                            DataManager.removePlayer(DataManager.getPlayerUUID(args[2]), args[1]);
                            sender.sendMessage(Messages.getMessage("land.untrust.success"));
                        } else {
                            sender.sendMessage(Messages.getMessage("land.untrust.error.not_trusted"));
                        }
                    } else {
                        sender.sendMessage(Messages.getMessage("land.untrust.error.not_owner"));
                    }
                    break;
                case "rename":
                    if (args.length != 3) {
                        sender.sendMessage(Messages.getMessage("usage.land.rename"));
                        return false;
                    }
                    if(DataManager.isOwner(player.getUniqueId().toString(), args[1])) {
                        DataManager.renameLand(player.getUniqueId().toString(), args[1], args[2]);
                        sender.sendMessage(Messages.getMessage("land.rename.success"));
                    } else {
                        sender.sendMessage(Messages.getMessage("land.rename.error.not_owner"));
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