package me.opkarol.opeconomy;

import me.opkarol.opeconomy.commands.*;
import me.opkarol.opeconomy.economy.Database;
import me.opkarol.opeconomy.events.JoinEvent;
import me.opkarol.opeconomy.misc.Metrics;
import me.opkarol.opeconomy.misc.OpEconomyExpansion;
import me.opkarol.opeconomy.misc.UpdateChecker;
import me.opkarol.opeconomy.utils.ColorUtils;
import me.opkarol.opeconomy.utils.TransactionUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import java.util.Objects;
import java.util.UUID;
import java.util.logging.Logger;

public class PluginController {
    Economy economy;

    public PluginController(Economy economy1) {
        economy = economy1;
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            economy.getLogger().warning("PlaceholderAPI not found! Disabling plugin, please install it to use OpEconomy.");
            Bukkit.getPluginManager().disablePlugin(economy);
        } else {
            new OpEconomyExpansion().register();
            economy.getLogger().info("Player Expansion enabled.");
        }
        onPluginStart();
    }

    public void onPluginStart() {
        loadConfigurationFile();
        me.opkarol.opeconomy.balanceTop.Database.loadMap();
        registerCommands();
        registerEvents();
    }

    public void loadConfigurationFile() {
        economy.saveDefaultConfig();
        economy.reloadConfig();
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
        me.opkarol.opeconomy.balanceTop.Database.setPageEndEnabled((Boolean) getFromConfig("BalanceTop.pageEnd.enabled"));
        me.opkarol.opeconomy.balanceTop.Database.setPageEndHoverMessage(getMessageFromConfig("BalanceTop.pageEnd.hoverMessage"));
        me.opkarol.opeconomy.balanceTop.Database.setPageEndNextPage(getMessageFromConfig("BalanceTop.pageEnd.nextPage"));
        me.opkarol.opeconomy.balanceTop.Database.setPageEndPreviousPage(getMessageFromConfig("BalanceTop.pageEnd.previousPage"));
        me.opkarol.opeconomy.balanceTop.Database.setPageMiddle(getMessageFromConfig("BalanceTop.pageMiddle"));
        me.opkarol.opeconomy.balanceTop.Database.setPageFront(getMessageFromConfig("BalanceTop.pageFront"));
        me.opkarol.opeconomy.balanceTop.Database.setPageSize(getValueFromConfig("BalanceTop.pageSize"));
        BalTopExecutor.setDontHavePermission(getMessageFromConfig("BalanceTop.Executor.dontHavePermission"));
        BalTopExecutor.setBadUsage(getMessageFromConfig("BalanceTop.Executor.badUsage"));
        BalTopExecutor.setLastArgumentNotNumber(getMessageFromConfig("BalanceTop.Executor.lastArgumentNotNumber"));
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
        Objects.requireNonNull(economy.getCommand("balancetop")).setExecutor(new BalTopExecutor());
    }

    public String getMessageFromConfig(String path) {
        return ColorUtils.formatText(economy.getConfig().getString(path));
    }

    public int getValueFromConfig(String path) {
        return economy.getConfig().getInt(path);
    }

    public Object getFromConfig(String path) {
        return economy.getConfig().get(path); }

    public void enableMetrics(){
        int pluginId = 12735;
        Metrics metrics = new Metrics(Economy.getEconomy(), pluginId);
        metrics.addCustomChart(new Metrics.SingleLineChart("money", () -> {
            int money = 0;
            for (UUID object : Database.getMoneyMap().keySet()){
                money = money + Database.getMoneyFromUUID(object);
            }
            return money;
        }));
    }

    public void checkUpdates(){
        Logger logger = Economy.getEconomy().getLogger();

        new UpdateChecker(Economy.getEconomy(), 95674).getVersion(version -> {
            String versionString = Economy.getEconomy().getDescription().getVersion();
            if (versionString.equalsIgnoreCase(version)) {
                logger.info("There is not a new update available. Current version: " + versionString);
            } else {
                logger.info("There is a new update available. Current version: " + versionString + ", New version: " + version);
            }
        });
    }
}
