package me.opkarol.opeconomy.commands;

import me.clip.placeholderapi.PlaceholderAPI;
import me.opkarol.opeconomy.utils.ObjectUtils;
import me.opkarol.opeconomy.utils.TransactionUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static me.opkarol.opeconomy.utils.Utils.*;

public class PayExecutor extends TransactionUtils implements CommandExecutor, TabCompleter {
    private static String dontHavePermission;
    private static String badUsage;
    private static String cantPayYourself;
    private static String lastArgumentNotNumber;
    private static String dontHaveEnoughMoney;
    private static String notAvailableToConsole;
    private static String targetDoesntExists;
    private static String receivedMoney;
    private static String gaveMoney;

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String alias, String @NotNull [] args) {
        List<String> results = new ArrayList<>();

        switch (args.length){
            case 1 -> Bukkit.getOnlinePlayers().forEach(player -> results.add(player.getName()));
            case 2 -> results.add("<amount>");
        }

        return results;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("opeconomy.command.pay") && !sender.isOp()) return returnMessageToSender(sender, dontHavePermission);

        if (!(sender instanceof Player)) return returnMessageToSender(sender, notAvailableToConsole);

        if (args.length == 2){
            UUID uuid = ObjectUtils.getUUIDFromString(args[0]);

            OfflinePlayer player = getPlayerFromUUID(uuid);

            if (player == sender) return returnMessageToSender(sender, cantPayYourself);

            if (!isPlayerAccountExists(uuid)) return returnMessageToSender(sender, targetDoesntExists);

            String amountString = replaceDigits(args[1]);

            if(isntValidInteger(amountString)) return returnMessageToSender(sender, lastArgumentNotNumber);

            int amount;
            try {amount = Integer.parseInt(amountString);} catch (NumberFormatException e) { return returnMessageToSender(sender, lastArgumentNotNumber); }

            int senderMoney = getMoneyFromUUID(((Player) sender).getUniqueId());
            if (senderMoney < amount) return returnMessageToSender(sender, dontHaveEnoughMoney);

            if (senderMoney - amount < 0) return returnMessageToSender(sender, dontHaveEnoughMoney);

            if (amount < 0) return returnMessageToSender(sender, lastArgumentNotNumber);

            removePlayerMoney(((Player) sender).getUniqueId(), amount);
            addPlayerMoney(uuid, amount);

            try {
                if (player.isOnline())
                    return returnMessageToSender((CommandSender) player, receivedMoney.replace("%money_sent_amount%", amountString).replace("%money_sender_name%", sender.getName()).replace("%currency%", getCurrency()));
            } catch (Exception ignore) {}
            return returnMessageToSender(sender, PlaceholderAPI.setPlaceholders((Player) sender, gaveMoney.replace("%money_receiver_name%", args[0]).replace("%money_sent_amount%", amountString).replace("%currency%", getCurrency())));
        }

        return returnMessageToSender(sender, badUsage);
    }

    public static void setGaveMoney(String gaveMoney) {
        PayExecutor.gaveMoney = gaveMoney;
    }

    public static void setReceivedMoney(String receivedMoney) {
        PayExecutor.receivedMoney = receivedMoney;
    }

    public static void setBadUsage(String badUsage) {
        PayExecutor.badUsage = badUsage;
    }

    public static void setDontHavePermission(String dontHavePermission) {
        PayExecutor.dontHavePermission = dontHavePermission;
    }

    public static void setLastArgumentNotNumber(String lastArgumentNotNumber) {
        PayExecutor.lastArgumentNotNumber = lastArgumentNotNumber;
    }

    public static void setTargetDoesntExists(String targetDoesntExists) {
        PayExecutor.targetDoesntExists = targetDoesntExists;
    }

    public static void setCantPayYourself(String cantPayYourself) {
        PayExecutor.cantPayYourself = cantPayYourself;
    }

    public static void setDontHaveEnoughMoney(String dontHaveEnoughMoney) {
        PayExecutor.dontHaveEnoughMoney = dontHaveEnoughMoney;
    }

    public static void setNotAvailableToConsole(String notAvailableToConsole) {
        PayExecutor.notAvailableToConsole = notAvailableToConsole;
    }
}
