package me.opkarol.opeconomy.api;

import me.opkarol.opeconomy.Economy;
import me.opkarol.opeconomy.economy.Database;
import me.opkarol.opeconomy.misc.TimeWatch;
import me.opkarol.opeconomy.redeem.Redeem;
import org.bukkit.entity.Player;

import java.util.UUID;

public class OpEconomyApi {

    public OpEconomyApi(){
        Economy economy = Economy.getEconomy();
        if (this.getClass()==null){
            economy.getLogger().warning("\n---\n---\nPL: API pluginu nie wystartowało pomyślnie.\nUS: The plug-in API did not start successfully.\nJP: プラグインAPIが正常に起動しませんでした\nDE: Die Plugin-API wurde nicht normal gestartet.\n---\n---");
        } else economy.getLogger().info("OpEconomy's API started with success.");
    }

    /**
     *
     * @param maxUses - Integer object that is maximum amount of uses code.
     * @param reward - Integer object with value price that receiver gets.
     * @param string - Code name.
     */
    public void createRedeemCode(String string, int maxUses, int reward){
        me.opkarol.opeconomy.redeem.Database.addCodeToMap(string, new Redeem(maxUses, 0, reward));
    }

    /**
     *
     * @param code - Code name.
     * @param player - Player object that is saved.
     */
    public void isValidCodeEntered(String code, Player player){
        me.opkarol.opeconomy.redeem.Database.playerUsedValidCode(code, player);
    }

    /**
     *
     * @param code - Code name.
     * @param player - Player object that is saved.
     * @return - Boolean if object of player used code already.
     */
    public boolean hasPlayerRedeemedCode(String code, Player player){
        return me.opkarol.opeconomy.redeem.Database.hasPlayerUsedCode(code, player);
    }

    /**
     *
     * @return - Class object used for timing.
     */
    public TimeWatch getTimeWatch(){
        return new TimeWatch();
    }

    /**
     *
     * @param uuid - Unique ID.
     * @param amount - Amount to add.
     */
    public void addMoney(UUID uuid, int amount){
        Database.addPlayerMoney(uuid, amount);
    }

    /**
     *
     * @param uuid - Unique ID.
     * @param amount - Amount to remove.
     */
    public void removeMoney(UUID uuid, int amount){
        Database.removePlayerMoney(uuid, amount);
    }

    /**
     *
     * @param player - Player's object.
     * @return - Balance of specific player.
     */
    public int getBalance(Player player){
        return Database.getMoneyFromPlayer(player);
    }

    /**
     *
     * @param uuid - Unique ID.
     * @return - Balance of specific player that has this UUID.
     */
    public int getBalance(UUID uuid){
        return Database.getMoneyFromUUID(uuid);
    }

    /**
     *
     * @return - Current Currency name.
     */
    public String getActiveCurrency(){
        return Database.getCurrency();
    }

    /**
     *
     * @param newCurrency - New Currency name.
     */
    public void setActiveCurrency(String newCurrency){
        Database.setCurrency(newCurrency);
    }
}
