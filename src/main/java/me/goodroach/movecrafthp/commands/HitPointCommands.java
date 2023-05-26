package me.goodroach.movecrafthp.commands;

import me.goodroach.movecrafthp.MovecraftHitPoints;
import me.goodroach.movecrafthp.hitpoints.CraftHitPoints;
import net.countercraft.movecraft.craft.Craft;
import net.countercraft.movecraft.craft.CraftManager;
import net.countercraft.movecraft.util.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HitPointCommands implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!command.getName().equalsIgnoreCase("hitpoint")) {
            return false;
        }

        if (args.length == 0) {
            sender.sendMessage(ChatUtils.ERROR_PREFIX + " Please supply an argument. Valid arguments include: 'check' 'reload' 'remove'");
            return true;
        }

        if (args[0].equalsIgnoreCase("check")) {
            if (!sender.hasPermission("movecrafthp.command.check")) {
                sender.sendMessage(ChatUtils.ERROR_PREFIX + " You don't have permission to use this command.");
                return true;
            }
            Player target = args.length > 1 ? Bukkit.getPlayer(args[1]) : (sender instanceof Player ? (Player) sender : null);
            return checkCommand(sender, target);
        }

        if (args[0].equalsIgnoreCase("remove")) {
            if (!sender.hasPermission("movecrafthp.command.remove")) {
                sender.sendMessage(ChatUtils.ERROR_PREFIX + " You don't have permission to use this command.");
                return true;
            }
            if (args.length < 3) {
                sender.sendMessage(ChatUtils.ERROR_PREFIX + " Insufficient arguments. Try /hitpoint remove <playername> <amount>");
                return true;
            }
            double amount;
            try {
                amount = Double.parseDouble(args[2]);
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatUtils.ERROR_PREFIX + " Invalid amount value.");
                return true;
            }
            Player target = Bukkit.getPlayer(args[1]);
            return removeCommand(sender, target, amount);
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("movecrafthp.command.reload")) {
                sender.sendMessage(ChatUtils.ERROR_PREFIX + " You don't have permission to use this command.");
                return true;
            }
            return reloadCommand(sender);
        }

        sender.sendMessage(ChatUtils.ERROR_PREFIX + " Incorrect argument. Valid arguments include: 'check' 'reload' 'remove'");
        return false;
    }

    private boolean checkCommand(CommandSender sender, Player target) {
        if (target == null) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatUtils.ERROR_PREFIX + " You must be a player to execute this command this way. Try /hitpoint check <playername>");
                return true;
            }

            Craft craft = CraftManager.getInstance().getCraftByPlayer((Player) sender);
            if (craft == null) {
                sender.sendMessage(ChatUtils.MOVECRAFT_COMMAND_PREFIX + " You must be piloting a craft.");
                return true;
            }

            CraftHitPoints hitPoints = MovecraftHitPoints.getInstance().getHitPointManager().getCraftHitPoints(craft);
            if (hitPoints == null) {
                sender.sendMessage(ChatUtils.MOVECRAFT_COMMAND_PREFIX + " This craft does not use the hit points feature.");
                return true;
            }

            sender.sendMessage(ChatUtils.MOVECRAFT_COMMAND_PREFIX +
                    " The current craft hit points is: " + hitPoints.getCurrentHitPoints() + " / " + hitPoints.getHitPoints());
            return true;
        }

        Craft craft = CraftManager.getInstance().getCraftByPlayer(target);
        if (craft == null) {
            sender.sendMessage(ChatUtils.MOVECRAFT_COMMAND_PREFIX + " Player '" + target.getDisplayName() + "' is not piloting a craft.");
            return true;
        }

        CraftHitPoints hitPoints = MovecraftHitPoints.getInstance().getHitPointManager().getCraftHitPoints(craft);
        if (hitPoints == null) {
            sender.sendMessage(ChatUtils.MOVECRAFT_COMMAND_PREFIX + " This craft does not use the hit points feature.");
            return true;
        }

        sender.sendMessage(ChatUtils.MOVECRAFT_COMMAND_PREFIX +
                " The current craft hit points is: " + hitPoints.getCurrentHitPoints() + " / " + hitPoints.getHitPoints());
        return true;
    }

    private boolean removeCommand(CommandSender sender, Player target, double amount) {
        if (target == null) {
            sender.sendMessage(ChatUtils.MOVECRAFT_COMMAND_PREFIX + " Player not found.");
            return true;
        }

        Craft craft = CraftManager.getInstance().getCraftByPlayer(target);
        if (craft == null) {
            sender.sendMessage(ChatUtils.MOVECRAFT_COMMAND_PREFIX + " Player '" + target.getDisplayName() + "' is not piloting a craft.");
            return true;
        }

        CraftHitPoints hitPoints = MovecraftHitPoints.getInstance().getHitPointManager().getCraftHitPoints(craft);
        if (hitPoints == null) {
            sender.sendMessage(ChatUtils.MOVECRAFT_COMMAND_PREFIX + " This craft does not use the hit points feature.");
            return true;
        }

        hitPoints.removeHitPoints(amount);
        sender.sendMessage(ChatUtils.MOVECRAFT_COMMAND_PREFIX +
                " The current craft hit points is: " + hitPoints.getCurrentHitPoints() + " / " + hitPoints.getHitPoints());
        return true;
    }

    private boolean reloadCommand(CommandSender sender) {
        MovecraftHitPoints.getInstance().getConfigManager().reloadConfig();
        MovecraftHitPoints.getInstance().saveDefaultConfig();
        sender.sendMessage(ChatUtils.MOVECRAFT_COMMAND_PREFIX + " Successfully reloaded the configuration.");
        return true;
    }
}
