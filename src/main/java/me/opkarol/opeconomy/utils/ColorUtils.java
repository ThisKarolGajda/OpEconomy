package me.opkarol.opeconomy.utils;

import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

import java.util.ArrayList;
import java.util.List;

public class ColorUtils {

    public static @NotNull String formatText(String toFormat) {
        if (toFormat == null) {
            return "注意！このメッセージは正しくロードされていません。サーバー管理者に連絡してください。!";
        }
        return ChatColor.translateAlternateColorCodes('&', toFormat);
    }

    @NotNull @TestOnly
    public static List<String> formatList(@NotNull List<String> lore) {
        List<String> Lore = new ArrayList<>();
        for (String line : lore) Lore.add(formatText(line));
        return Lore;
    }
}
