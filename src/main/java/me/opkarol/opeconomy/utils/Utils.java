package me.opkarol.opeconomy.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.text.ParsePosition;

public class Utils {

    public static boolean isntValidInteger(String string){
        return !isNumeric(string);
    }

    @Contract(pure = true)
    public static @NotNull String replaceDigits(@NotNull String string){
        return string.replaceAll("/\\D+/g", "");
    }

    public static boolean isNumeric(String str) {
        ParsePosition pos = new ParsePosition(0);
        NumberFormat.getInstance().parse(str, pos);
        return str.length() == pos.getIndex();
    }

    public static boolean returnMessageToSender(@NotNull CommandSender sender, String message){
        if (sender instanceof Player) sender.sendMessage(PlaceholderAPI.setPlaceholders((Player) sender, message));
        else sender.sendMessage(message);
        return true;
    }

    public static boolean returnMessageToSender(@NotNull CommandSender sender, TextComponent message){
        sender.spigot().sendMessage(message);
        return true;
    }
}
