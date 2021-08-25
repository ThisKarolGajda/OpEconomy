package me.opkarol.opeconomy.commands;

import me.opkarol.opeconomy.Economy;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static me.opkarol.opeconomy.utils.Utils.returnMessageToSender;

public class ReloadExecutor implements CommandExecutor {
    private static String dontHavePermission;
    private static String successAction;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("opeconomy.command.reload") && !sender.isOp()) return returnMessageToSender(sender, dontHavePermission);
        Economy.getPluginController().loadConfigurationFile();
        return returnMessageToSender(sender, successAction);
    }

    public static void setSuccessAction(String successAction) {
        ReloadExecutor.successAction = successAction;
    }

    public static void setDontHavePermission(String dontHavePermission) {
        ReloadExecutor.dontHavePermission = dontHavePermission;
    }
}
