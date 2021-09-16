package me.opkarol.opeconomy.commands;

import me.clip.placeholderapi.PlaceholderAPI;
import me.opkarol.opeconomy.economy.Database;
import me.opkarol.opeconomy.notes.NoteItem;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static me.opkarol.opeconomy.utils.Utils.isntValidInteger;
import static me.opkarol.opeconomy.utils.Utils.returnMessageToSender;

public class NoteExecutor extends NoteItem implements CommandExecutor, TabCompleter {

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String alias, String @NotNull [] args) {
        List<String> results = new ArrayList<>();

        switch (args.length){
            case 1 -> results.add("<amount>");
        }

        return results;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("opeconomy.command.moneynote") && !sender.isOp()) return returnMessageToSender(sender, getDontHavePermission());

        if (!(sender instanceof Player)) return returnMessageToSender(sender, getNotAvailableToConsole());

        if (args.length == 0) return returnMessageToSender(sender, getBadUsage());

        String moneyToWithdrawString = args[0];
        if(isntValidInteger(moneyToWithdrawString)) return returnMessageToSender(sender, getLastArgumentNotNumber());

        int amount;
        try {amount = Integer.parseInt(moneyToWithdrawString);} catch (NumberFormatException e) { return returnMessageToSender(sender, getLastArgumentNotNumber()); }

        if (amount < 0) return returnMessageToSender(sender, getLastArgumentNotNumber());
        Player player = (Player) sender;
        if (amount > getMaximumNote() || amount < getMinimumNote()) return returnMessageToSender(sender, getTooBigOrSmallAmount().replace("%tried_withdraw_amount%", String.valueOf(amount)));

        if (Database.getMoneyFromUUID(player.getUniqueId()) >= amount){
            player.getInventory().addItem(NoteItem.getNote(amount, player));
            Database.removePlayerMoney(player.getUniqueId(), amount);
            playSound(player, Sound.valueOf(getWithdrawSound()));
            return returnMessageToSender(sender, PlaceholderAPI.setPlaceholders(player, getWithdrawnMoney().replace("%money_withdrawn%", String.valueOf(amount))));
        }

        return returnMessageToSender(sender, getBadUsage());
    }


}
