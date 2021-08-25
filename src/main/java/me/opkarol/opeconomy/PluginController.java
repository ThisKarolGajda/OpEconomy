package me.opkarol.opeconomy;

import me.opkarol.opeconomy.commands.MoneyExecutor;
import me.opkarol.opeconomy.commands.PayExecutor;
import me.opkarol.opeconomy.commands.RedeemExecutor;
import me.opkarol.opeconomy.commands.ReloadExecutor;
import me.opkarol.opeconomy.economy.Database;
import me.opkarol.opeconomy.events.JoinEvent;
import me.opkarol.opeconomy.utils.ColorUtils;
import me.opkarol.opeconomy.utils.TransactionUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import java.util.Objects;

public class PluginController {
    Economy economy;

    public PluginController(Economy economy1) {
        economy = economy1;
        onPluginStart();
    }

    public void onPluginStart() {
        loadConfigurationFile();
        registerCommands();
        registerEvents();
    }

    public void loadConfigurationFile() {
        economy.saveDefaultConfig();
        economy.reloadConfig();
        Database.setDefaultMoney(getValueFromConfig("Economy.Database.defaultMoney"));
        TransactionUtils.setNotEnoughMoney(getMessageFromConfig("Economy.TransactionUtils.notEnoughMoney"));
        TransactionUtils.setGetMoney(getMessageFromConfig("Economy.TransactionUtils.getMoneyMessage"));
        TransactionUtils.setReceiverDoesntExists(getMessageFromConfig("Economy.TransactionUtils.receiverDoesntExists"));
        MoneyExecutor.setMoneyMessage(getMessageFromConfig("Economy.MoneyExecutor.moneyMessage"));
        MoneyExecutor.setDontHavePermission(getMessageFromConfig("Economy.MoneyExecutor.dontHavePermission"));
        MoneyExecutor.setBadUsage(getMessageFromConfig("Economy.MoneyExecutor.badUsage"));
        MoneyExecutor.setLastArgumentNotNumber(getMessageFromConfig("Economy.MoneyExecutor.lastArgumentNotNumber"));
        MoneyExecutor.setSuccessfulAction(getMessageFromConfig("Economy.MoneyExecutor.successfulAction"));
        MoneyExecutor.setYourselfMoneyMessage(getMessageFromConfig("Economy.MoneyExecutor.yourselfMoneyMessage"));
        MoneyExecutor.setTargetDontExists(getMessageFromConfig("Economy.MoneyExecutor.targetDontExists"));
        Database.setCurrency(getMessageFromConfig("Economy.Database.currency"));
        PayExecutor.setDontHavePermission(getMessageFromConfig("Economy.PayExecutor.dontHavePermission"));
        PayExecutor.setBadUsage(getMessageFromConfig("Economy.PayExecutor.badUsage"));
        PayExecutor.setCantPayYourself(getMessageFromConfig("Economy.PayExecutor.cantPayYourself"));
        PayExecutor.setLastArgumentNotNumber(getMessageFromConfig("Economy.PayExecutor.lastArgumentNotNumber"));
        PayExecutor.setDontHaveEnoughMoney(getMessageFromConfig("Economy.PayExecutor.dontHaveEnoughMoney"));
        PayExecutor.setNotAvailableToConsole(getMessageFromConfig("Economy.PayExecutor.notAvailableToConsole"));
        PayExecutor.setTargetDoesntExists(getMessageFromConfig("Economy.PayExecutor.targetDoesntExists"));
        PayExecutor.setReceivedMoney(getMessageFromConfig("Economy.PayExecutor.receivedMoney"));
        PayExecutor.setGaveMoney(getMessageFromConfig("Economy.PayExecutor.gaveMoney"));
        ReloadExecutor.setDontHavePermission(getMessageFromConfig("Reload.dontHavePermission"));
        ReloadExecutor.setSuccessAction(getMessageFromConfig("Reload.successAction"));
        RedeemExecutor.setDontHavePermission(getMessageFromConfig("Redeem.dontHavePermission"));
        RedeemExecutor.setNotAvailableToConsole(getMessageFromConfig("Redeem.notAvailableToConsole"));
        RedeemExecutor.setValidCodeEntered(getMessageFromConfig("Redeem.validCodeEntered"));
        RedeemExecutor.setNotValidCodeEntered(getMessageFromConfig("Redeem.notValidCodeEntered"));
        RedeemExecutor.setLastArgumentNotNumber(getMessageFromConfig("Redeem.lastArgumentNotNumber"));
        RedeemExecutor.setCreatedCode(getMessageFromConfig("Redeem.createdCode"));
        RedeemExecutor.setBadUsage(getMessageFromConfig("Redeem.badUsage"));
        RedeemExecutor.setRemovedCode(getMessageFromConfig("Redeem.removedCode"));
        RedeemExecutor.setInfoMessage(getMessageFromConfig("Redeem.infoMessage"));
        RedeemExecutor.setCodeLength(getValueFromConfig("Redeem.codeLength"));
        RedeemExecutor.setRemoveAdditional((Boolean) getFromConfig("Redeem.removeAdditional"));
    }

    public void registerEvents() {
        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(new JoinEvent(), economy);
    }

    public void registerCommands() {
        Objects.requireNonNull(economy.getCommand("money")).setExecutor(new MoneyExecutor());
        Objects.requireNonNull(economy.getCommand("pay")).setExecutor(new PayExecutor());
        Objects.requireNonNull(economy.getCommand("opreload")).setExecutor(new ReloadExecutor());
        Objects.requireNonNull(economy.getCommand("redeem")).setExecutor(new RedeemExecutor());
    }

    public String getMessageFromConfig(String path) {
        return ColorUtils.formatText(economy.getConfig().getString(path));
    }

    public int getValueFromConfig(String path) {
        return economy.getConfig().getInt(path);
    }

    public Object getFromConfig(String path) {return economy.getConfig().get(path); }

}
