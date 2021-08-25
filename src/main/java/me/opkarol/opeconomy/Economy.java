package me.opkarol.opeconomy;

import me.opkarol.opeconomy.economy.DatabaseLoader;
import org.bukkit.plugin.java.JavaPlugin;

public final class Economy extends JavaPlugin {
    private static Economy economy;
    private static PluginController pluginController;

    public static Economy getEconomy() {
        return economy;
    }

    public static PluginController getPluginController() {
        return pluginController;
    }

    @Override
    public void onEnable() {
        economy = this;
        pluginController = new PluginController(getEconomy());
        DatabaseLoader.onStart();
        me.opkarol.opeconomy.redeem.DatabaseLoader.onStart();
    }

    @Override
    public void onDisable() {
        DatabaseLoader.onDisable();
        me.opkarol.opeconomy.redeem.DatabaseLoader.onDisable();
        economy = null;
    }
}
