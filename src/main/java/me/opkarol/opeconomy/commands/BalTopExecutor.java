package me.opkarol.opeconomy.commands;

import me.opkarol.opeconomy.balanceTop.Database;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static me.opkarol.opeconomy.utils.Utils.*;

public class BalTopExecutor implements CommandExecutor {
    private static String dontHavePermission;
    private static String lastArgumentNotNumber;
    private static String badUsage;

    public static void setDontHavePermission(String dontHavePermission) {
        BalTopExecutor.dontHavePermission = dontHavePermission;
    }

    public static void setLastArgumentNotNumber(String lastArgumentNotNumber) {
        BalTopExecutor.lastArgumentNotNumber = lastArgumentNotNumber;
    }

    public static void setBadUsage(String badUsage) {
        BalTopExecutor.badUsage = badUsage;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("opeconomy.baltop.command.use") && !sender.isOp()) return returnMessageToSender(sender, dontHavePermission);
        if (args.length == 1){
            String amountString = replaceDigits(args[0]);
            if(isntValidInteger(amountString)) return returnMessageToSender(sender, lastArgumentNotNumber);

            int amount;
            try {amount = Integer.parseInt(amountString);} catch (NumberFormatException e) { return returnMessageToSender(sender, lastArgumentNotNumber); }

            if (amount < 0) return returnMessageToSender(sender, lastArgumentNotNumber);
            sender.spigot().sendMessage(Database.getComponentFromPage(amount));
            return true;
        }
        sender.sendMessage(badUsage);
        return false;
    }
}
