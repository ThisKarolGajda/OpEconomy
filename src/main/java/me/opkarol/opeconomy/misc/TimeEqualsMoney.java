package me.opkarol.opeconomy.misc;

import me.opkarol.opeconomy.Economy;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.TestOnly;

import java.util.HashMap;
import java.util.UUID;

import static me.opkarol.opeconomy.utils.Utils.returnMessageToSender;

public class TimeEqualsMoney {
    private static boolean enabled;
    private static int rewardTime;
    private static int rewardPrice;
    private static int length;
    private static boolean useActionBar;
    private static String rewardMessage;
    @TestOnly @Deprecated
    private static boolean antiAfkEnabled;

    private static HashMap<UUID, Integer> timeLeftToPrice = new HashMap<>(); // in minutes

    public static void onServerStart(){
        if (!enabled) return;
        new BukkitRunnable() {
            @Override
            public void run() {
                timeLeftToPrice.forEach((uuid, integer) -> logicOfUUIDMap(uuid));

            }
        }.runTaskTimer(Economy.getEconomy(), rewardTime * 20L, rewardTime * 20L);
    }

    public static void safeAddUUIDToMap(UUID uuid){
        if (!enabled) return;
        if (timeLeftToPrice.containsKey(uuid)) return;
        timeLeftToPrice.put(uuid, length);

    }

    public static void logicOfUUIDMap(UUID uuid){
        int timeLeft = timeLeftToPrice.get(uuid);

        if (timeLeft == 0 || timeLeft == 1) rewardPlayer(uuid);
        else timeLeftToPrice.replace(uuid, timeLeft - 1);
    }

    /**
     *
     * @param uuid - Unique user's id.
     * @return - Was operation done successful
     */
    public static boolean rewardPlayer(UUID uuid){
        timeLeftToPrice.replace(uuid, length);
        Player player = Bukkit.getPlayer(uuid);

        Economy.getAPI().addMoney(uuid, rewardPrice);

        if (player == null) return false;

        if (!useActionBar) return returnMessageToSender(player, getRewardMessage());
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(getRewardMessage()));
        return true;
    }

    private static String getRewardMessage(){
        return rewardMessage.replace("%item_price%", String.valueOf(rewardPrice));
    }

    public static void setAntiAfkEnabled(boolean antiAfkEnabled) {
        TimeEqualsMoney.antiAfkEnabled = antiAfkEnabled;
    }

    public static void setRewardMessage(String rewardMessage) {
        TimeEqualsMoney.rewardMessage = rewardMessage;
    }

    public static void setUseActionBar(boolean useActionBar) {
        TimeEqualsMoney.useActionBar = useActionBar;
    }

    public static void setRewardPrice(int rewardPrice) {
        TimeEqualsMoney.rewardPrice = rewardPrice;
    }

    public static void setRewardTime(int rewardTime) {
        TimeEqualsMoney.rewardTime = rewardTime;
    }

    public static void setEnabled(boolean enabled) {
        TimeEqualsMoney.enabled = enabled;
    }

    static HashMap<UUID, Integer> getTimeLeftToPrice() {
        return timeLeftToPrice;
    }

    static void setTimeLeftToPrice(HashMap<UUID, Integer> timeLeftToPrice) {
        TimeEqualsMoney.timeLeftToPrice = timeLeftToPrice;
    }

    public static void setLength(int length) {
        TimeEqualsMoney.length = length;
    }
}
