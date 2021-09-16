package me.opkarol.opeconomy;

import com.tchristofferson.configupdater.ConfigUpdater;
import me.opkarol.opeconomy.commands.*;
import me.opkarol.opeconomy.economy.Database;
import me.opkarol.opeconomy.events.ClickEvent;
import me.opkarol.opeconomy.events.DeathEvent;
import me.opkarol.opeconomy.events.JoinEvent;
import me.opkarol.opeconomy.misc.Metrics;
import me.opkarol.opeconomy.misc.OpEconomyExpansion;
import me.opkarol.opeconomy.misc.TimeEqualsMoney;
import me.opkarol.opeconomy.misc.UpdateChecker;
import me.opkarol.opeconomy.notes.NoteItem;
import me.opkarol.opeconomy.utils.ColorUtils;
import me.opkarol.opeconomy.utils.TransactionUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.Callable;

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
        updateConfig();
        loadConfigurationFile();
        me.opkarol.opeconomy.balanceTop.Database.loadMap();
        registerEvents();
        registerCommands();
        registerTabCompleters();
        TimeEqualsMoney.onServerStart();
        enableMetrics();
        checkUpdates();
        new BukkitRunnable() {
            @Override
            public void run() {
                economy.getLogger().warning(getMessage());
            }
        }.runTaskLater(economy, 100);

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
        DeathEvent.setKillerGetsMoney((Boolean) getFromConfig("DeathSettings.killerGetsMoney"));
        DeathEvent.setLostMoneyOnDeath((Double) getFromConfig("DeathSettings.lostMoneyOnDeath"));
        DeathEvent.setLostPercentageOfMoneyOnDeath((Double) getFromConfig("DeathSettings.lostPercentageOfMoneyOnDeath"));
        NoteItem.setDepositedMoney(getMessageFromConfig("Notes.messages.depositedMoney"));
        NoteItem.setWithdrawnMoney(getMessageFromConfig("Notes.messages.withdrawnMoney"));
        NoteItem.setBadUsage(getMessageFromConfig("Notes.messages.badUsage"));
        NoteItem.setNotAvailableToConsole(getMessageFromConfig("Notes.messages.notAvailableToConsole"));
        NoteItem.setLastArgumentNotNumber(getMessageFromConfig("Notes.messages.lastArgumentNotNumber"));
        NoteItem.setDontHavePermission(getMessageFromConfig("Notes.messages.dontHavePermission"));
        NoteItem.setMinimumNote((Double) getFromConfig("Notes.notesSettings.minimumNote"));
        NoteItem.setMaximumNote((Double) getFromConfig("Notes.notesSettings.maximumNote"));
        NoteItem.setDepositSound(getMessageFromConfig("Notes.sounds.depositSound"));
        NoteItem.setWithdrawSound(getMessageFromConfig("Notes.sounds.withdrawSound"));
        NoteItem.setName(getMessageFromConfig("Notes.notesItem.name"));
        NoteItem.setLore((List<String>) getFromConfig("Notes.notesItem.lore"));
        NoteItem.setMaterial(getMessageFromConfig("Notes.notesItem.material"));
        NoteItem.setEnchanted((Boolean) getFromConfig("Notes.notesItem.enchanted"));
        NoteItem.setHidden((Boolean) getFromConfig("Notes.notesItem.hidden"));
        NoteItem.setTooBigOrSmallAmount(getMessageFromConfig("Notes.messages.tooBigOrSmallAmount"));
        TimeEqualsMoney.setAntiAfkEnabled((Boolean) getFromConfig("TimeEqualsMoney.enabled"));
        TimeEqualsMoney.setRewardTime((Integer) getFromConfig("TimeEqualsMoney.reward.every"));
        TimeEqualsMoney.setEnabled((Boolean) getFromConfig("TimeEqualsMoney.enabled"));
        TimeEqualsMoney.setRewardPrice((Integer) getFromConfig("TimeEqualsMoney.reward.price"));
        TimeEqualsMoney.setRewardMessage((String) getFromConfig("TimeEqualsMoney.message.rewardMessage"));
        TimeEqualsMoney.setUseActionBar((Boolean) getFromConfig("TimeEqualsMoney.useActionBar"));
        TimeEqualsMoney.setLength((Integer) getFromConfig("TimeEqualsMoney.reward.length"));
    }

    public void registerEvents() {
        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(new JoinEvent(), economy);
        manager.registerEvents(new DeathEvent(), economy);
        manager.registerEvents(new ClickEvent(), economy);
    }

    public void registerCommands() {
        Objects.requireNonNull(economy.getCommand("money")).setExecutor(new MoneyExecutor());
        Objects.requireNonNull(economy.getCommand("pay")).setExecutor(new PayExecutor());
        Objects.requireNonNull(economy.getCommand("opreload")).setExecutor(new ReloadExecutor());
        Objects.requireNonNull(economy.getCommand("redeem")).setExecutor(new RedeemExecutor());
        Objects.requireNonNull(economy.getCommand("balancetop")).setExecutor(new BalTopExecutor());
        Objects.requireNonNull(economy.getCommand("moneynote")).setExecutor(new NoteExecutor());
    }

    public void registerTabCompleters(){
        Objects.requireNonNull(economy.getCommand("money")).setTabCompleter(new MoneyExecutor());
        Objects.requireNonNull(economy.getCommand("pay")).setTabCompleter(new PayExecutor());
        Objects.requireNonNull(economy.getCommand("opreload")).setTabCompleter(new ReloadExecutor());
        Objects.requireNonNull(economy.getCommand("redeem")).setTabCompleter(new RedeemExecutor());
        Objects.requireNonNull(economy.getCommand("balancetop")).setTabCompleter(new BalTopExecutor());
        Objects.requireNonNull(economy.getCommand("moneynote")).setTabCompleter(new NoteExecutor());
    }

    public String getMessageFromConfig(String path) {
        return ColorUtils.formatText(economy.getConfig().getString(path));
    }

    public int getValueFromConfig(String path) {
        return economy.getConfig().getInt(path);
    }

    public Object getFromConfig(String path) {
        return economy.getConfig().get(path);
    }

    public void enableMetrics() {
        int pluginId = 12735;
        Metrics metrics = new Metrics(Economy.getEconomy(), pluginId);
        metrics.addCustomChart(new Metrics.SingleLineChart("money", new Callable<>() {
            @Override
            public Integer call() throws Exception {
                int money = 0;
                for (UUID object : Database.getMoneyMap().keySet()) {
                    money = money + Database.getMoneyFromUUID(object);
                }
                return money;
            }
        }
        ));
    }
    private String message = "Cannot look for updates.";

    public void checkUpdates() {
        String versionString = economy.getDescription().getVersion();

        new UpdateChecker(economy, 95674).getVersion(version -> {
            if (versionString.equalsIgnoreCase(version)) {
                setMessage("There is not a new update available. Current version: " + versionString);
            } else {
                setMessage("There is a new update available. Current version: " + versionString + ", New version: " + version + ". Download it now: https://www.spigotmc.org/resources/95674/");
            }
        });
    }

    public double checkConfigVersion() {
        return (double) getFromConfig("version");
    }

    public void instantConfigUpdate(){
        File configFile = new File(economy.getDataFolder(), "config.yml");

        try {
            economy.saveConfig();
            ConfigUpdater.update(economy, "config.yml", configFile, null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        economy.reloadConfig();
    }

    public void updateConfig() {
        economy.saveDefaultConfig();
        double version = 0.51;
        if (version != checkConfigVersion() || checkConfigVersion() == 0) {
            economy.getConfig().set("version", version);
            instantConfigUpdate();
            economy.getLogger().warning("Configuration file was recreated with new objects and old values! Please check it out! Previous version was " + checkConfigVersion());
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
