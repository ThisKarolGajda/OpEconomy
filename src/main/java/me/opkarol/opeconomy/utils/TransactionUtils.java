package me.opkarol.opeconomy.utils;

import me.opkarol.opeconomy.economy.Database;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static me.opkarol.opeconomy.utils.ObjectUtils.*;

public class TransactionUtils extends Database {
    private static String receiverDoesntExists;
    private static String notEnoughMoney;
    private static String getMoneyMessage;

    public static void setGetMoney(String getMoney) {
        TransactionUtils.getMoneyMessage = getMoney;
    }

    public static void setNotEnoughMoney(String notEnoughMoney) {
        TransactionUtils.notEnoughMoney = notEnoughMoney;
    }

    public static void setReceiverDoesntExists(String receiverDoesntExists) {
        TransactionUtils.receiverDoesntExists = receiverDoesntExists;
    }

    public static boolean payPlayerMoney(Object giver, Object receiver, int amount){
        Player giverPlayer = null;
        Player receiverPlayer = null;
        if (giver instanceof Player) giverPlayer = (Player) giver;
        if (receiver instanceof Player) receiverPlayer = (Player) receiver;
        UUID giverUUID = getUUIDFromObject(giver);
        UUID receiverUUID = getUUIDFromObject(receiver);

        if (!isPlayerAccountExists(receiverUUID)){
            if (isntPlayerNull(giverPlayer)) giverPlayer.sendMessage(receiverDoesntExists);
            return false;
        }

        if (getMoneyFromPlayer(giverUUID) >= amount){
            removePlayerMoney(giverUUID, amount);
            addPlayerMoney(receiverUUID, amount);
            Player player = getPlayerFromUUID(receiverUUID);
            assert receiverPlayer != null;
            if (isPlayerOnline(receiverPlayer)){
                player.sendMessage(getSendMessage(giverPlayer.getName(), amount));
            }
            return false;
        } else if (isntPlayerNull(giverPlayer)) giverPlayer.sendMessage(notEnoughMoney);
        return false;

    }

    public static @NotNull String getSendMessage(String playerGiverName, int amount){
        return getMoneyMessage.replace("%money_giver_name%", playerGiverName).replace("%money_amount%", String.valueOf(amount));
    }

    public static boolean isntPlayerNull(Player player){
        return player != null;
    }

    public static boolean isPlayerOnline(@NotNull Player player){
        return player.isOnline();
    }

    public static Player getPlayerFromUUID(UUID uuid){
        return Bukkit.getPlayer(uuid);
    }
}
