package me.opkarol.opeconomy;

import me.opkarol.opeconomy.api.OpEconomyApi;
import me.opkarol.opeconomy.economy.DatabaseLoader;
import org.bukkit.plugin.java.JavaPlugin;

public final class Economy extends JavaPlugin {
    private static Economy economy;
    private static PluginController pluginController;
    private static OpEconomyApi opEconomyApi;

    public static Economy getEconomy() {
        return economy;
    }

    public static PluginController getPluginController() {
        return pluginController;
    }

    public static OpEconomyApi getAPI(){
        return opEconomyApi;
    }

    @Override
    public void onLoad(){
        economy = this;
        opEconomyApi = new OpEconomyApi();

    }

    @Override
    public void onEnable() {
        DatabaseLoader.onStart();
        pluginController = new PluginController(getEconomy());
        me.opkarol.opeconomy.redeem.DatabaseLoader.onStart();

    }

    @Override
    public void onDisable() {
        DatabaseLoader.onDisable();
        me.opkarol.opeconomy.redeem.DatabaseLoader.onDisable();
        economy = null;
    }
}
