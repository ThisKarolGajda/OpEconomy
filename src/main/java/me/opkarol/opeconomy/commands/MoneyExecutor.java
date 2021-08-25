package me.opkarol.opeconomy.commands;

import me.opkarol.opeconomy.utils.TransactionUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static me.opkarol.opeconomy.utils.Utils.*;

public class MoneyExecutor extends TransactionUtils implements CommandExecutor {
    private static String dontHavePermission;
    private static String moneyMessage;
    private static String yourselfMoneyMessage;
    private static String lastArgumentNotNumber;
    private static String successfulAction;
    private static String badUsage;
    private static String targetDontExists;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("opeconomy.command.use") && !sender.isOp()) return returnMessageToSender(sender, dontHavePermission);

        if (args.length == 0 && sender instanceof Player){
            if (!sender.hasPermission("opeconomy.command.check") && !sender.isOp()) return returnMessageToSender(sender, dontHavePermission);
            sender.sendMessage(getYourselfMoneyMessage(sender));
            return true;
        } else if (args.length == 0) return false;


        Player target = Bukkit.getPlayer(args[0]);
        UUID uuid;
        if (target == null) {
            uuid = Bukkit.getOfflinePlayer(args[0]).getUniqueId();
        } else uuid = target.getUniqueId();

        if (args.length == 1){
            if (!sender.hasPermission("opeconomy.command.check.others") && !sender.isOp()) return returnMessageToSender(sender, dontHavePermission);

            if (target == sender) sender.sendMessage(getYourselfMoneyMessage(sender));
            else if (isPlayerAccountExists(uuid)) sender.sendMessage(getMoneyMessage(args[0], getMoneyFromUUID(uuid)));
            else sender.sendMessage(targetDontExists);

            return true;
        }

        if (args.length == 3){
            if (!sender.hasPermission("opeconomy.command.manage") && !sender.isOp()) return returnMessageToSender(sender, dontHavePermission);
            // /command <player> (set/remove/add) (amount)
            String amountString = replaceDigits(args[2]);
            if(!isValidInteger(amountString)) return returnMessageToSender(sender, lastArgumentNotNumber);

            int amount;
            try {amount = Integer.parseInt(amountString);} catch (NumberFormatException e) { return returnMessageToSender(sender, lastArgumentNotNumber); }

            if (amount < 0) return returnMessageToSender(sender, lastArgumentNotNumber);

            switch (args[1]){
                case "set" -> changeMoneyOfPlayer(uuid, amount);
                case "add" -> addPlayerMoney(uuid, amount);
                case "remove" -> removePlayerMoney(uuid, amount);
            }
            return returnMessageToSender(sender, successfulAction);
        }
        return returnMessageToSender(sender, badUsage);
    }

    private @NotNull String getMoneyMessage(String name, int amount){
        return moneyMessage.replace("%money_target_name%", name).replace("%money_target_amount%", String.valueOf(amount)).replace("%currency%", getCurrency());
    }

    private @NotNull String getYourselfMoneyMessage(CommandSender sender){
        return yourselfMoneyMessage.replace("%money_target_amount%", String.valueOf(getMoneyFromPlayer(sender))).replace("%currency%", getCurrency());
    }

    public static void setLastArgumentNotNumber(String lastArgumentNotNumber) {
        MoneyExecutor.lastArgumentNotNumber = lastArgumentNotNumber;
    }

    public static void setMoneyMessage(String moneyMessage) {
        MoneyExecutor.moneyMessage = moneyMessage;
    }

    public static void setDontHavePermission(String dontHavePermission) {
        MoneyExecutor.dontHavePermission = dontHavePermission;
    }

    public static void setSuccessfulAction(String successfulAction) {
        MoneyExecutor.successfulAction = successfulAction;
    }

    public static void setYourselfMoneyMessage(String yourselfMoneyMessage) {
        MoneyExecutor.yourselfMoneyMessage = yourselfMoneyMessage;
    }

    public static void setBadUsage(String badUsage) {
        MoneyExecutor.badUsage = badUsage;
    }

    public static void setTargetDontExists(String targetDontExists) {
        MoneyExecutor.targetDontExists = targetDontExists;
    }
}
