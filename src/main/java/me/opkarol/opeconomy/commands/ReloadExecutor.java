package me.opkarol.opeconomy.commands;

import me.opkarol.opeconomy.Economy;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static me.opkarol.opeconomy.utils.Utils.returnMessageToSender;

public class ReloadExecutor implements CommandExecutor, TabCompleter {
    private static String dontHavePermission;
    private static String successAction;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("opeconomy.command.reload") && !sender.isOp()) return returnMessageToSender(sender, dontHavePermission);
        Economy.getPluginController().instantConfigUpdate();
        Economy.getPluginController().loadConfigurationFile();
        return returnMessageToSender(sender, successAction);
    }

    public static void setSuccessAction(String successAction) {
        ReloadExecutor.successAction = successAction;
    }

    public static void setDontHavePermission(String dontHavePermission) {
        ReloadExecutor.dontHavePermission = dontHavePermission;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return new ArrayList<>();
    }
}
