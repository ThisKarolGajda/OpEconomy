package me.opkarol.opeconomy.events;

import me.opkarol.opeconomy.Economy;
import me.opkarol.opeconomy.economy.Database;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.jetbrains.annotations.NotNull;

public class DeathEvent implements Listener {
    static double lostMoneyOnDeath;
    static double lostPercentageOfMoneyOnDeath;
    static boolean killerGetsMoney;

    @EventHandler(priority = EventPriority.HIGH)
    public void deathEvent(@NotNull PlayerDeathEvent event){
        Player died = event.getEntity();
        Player killer = event.getEntity().getKiller();

        if (died == killer){
            return;
        }

        if (killer == null) return;

        if (lostPercentageOfMoneyOnDeath != 0 && lostMoneyOnDeath != 0) return; //Both can't be turned on in the same time
        else if (lostPercentageOfMoneyOnDeath == 0 && lostMoneyOnDeath == 0) return; //Nothing happens, both turned off

        if (!killerGetsMoney) return;

        double money;
        if (lostMoneyOnDeath != 0) money = lostMoneyOnDeath;
        else money = Database.getMoneyFromPlayer(died) * lostPercentageOfMoneyOnDeath;

        Economy.getAPI().removeMoney(died.getUniqueId(), (int) money);
        Economy.getAPI().addMoney(killer.getUniqueId(), (int) money);
    }

    public static void setKillerGetsMoney(boolean killerGetsMoney) {
        DeathEvent.killerGetsMoney = killerGetsMoney;
    }

    public static void setLostMoneyOnDeath(double lostMoneyOnDeath) {
        DeathEvent.lostMoneyOnDeath = lostMoneyOnDeath;
    }

    public static void setLostPercentageOfMoneyOnDeath(double lostPercentageOfMoneyOnDeath) {
        DeathEvent.lostPercentageOfMoneyOnDeath = lostPercentageOfMoneyOnDeath;
    }
}
