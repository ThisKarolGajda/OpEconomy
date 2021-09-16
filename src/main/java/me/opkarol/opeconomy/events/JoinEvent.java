package me.opkarol.opeconomy.events;

import me.opkarol.opeconomy.Economy;
import me.opkarol.opeconomy.misc.TimeEqualsMoney;
import me.opkarol.opeconomy.utils.TransactionUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

public class JoinEvent extends TransactionUtils implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    void joinEvent(@NotNull PlayerJoinEvent event) {
        if (!isPlayerAccountExists(event.getPlayer().getUniqueId())) createNewBankAccount(event.getPlayer().getUniqueId());
        TimeEqualsMoney.safeAddUUIDToMap(event.getPlayer().getUniqueId());
        Economy.getPluginController().checkUpdates();
        if (event.getPlayer().isOp()) event.getPlayer().sendMessage("[OpEconomy] " + Economy.getPluginController().getMessage());
    }
}
