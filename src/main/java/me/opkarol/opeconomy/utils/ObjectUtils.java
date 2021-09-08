package me.opkarol.opeconomy.utils;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ObjectUtils {
    public enum HoverType{
        Copy, RunCommand
    }

    public static @NotNull UUID getUUIDFromObject(Object object){
        if (object instanceof CommandSender) {
            return getUUIDFromString(((CommandSender) object).getName());
        } else {
            UUID uuid;
            try {
                uuid = UUID.fromString(object.toString());
            } catch (Exception e) {
                uuid = getUUIDFromString((String) object);
            }
            return uuid;
        }
    }

    public static @NotNull UUID getUUIDFromString(String string){
        Player target = Bukkit.getPlayer(string);
        UUID uuid;
        if (target == null) {
            uuid = Bukkit.getOfflinePlayer(string).getUniqueId();
        } else uuid = target.getUniqueId();
        return uuid;
    }

    static String getStringFromEnumType(HoverType type){
        String toReturn;
        switch (type){
            case Copy -> toReturn = "Click to copy message!";
            case RunCommand -> toReturn = "Click to run command";
            default -> throw new IllegalStateException("Unexpected value: " + type);
        }
        return toReturn;
    }

    static void setHoverEventEnum(@NotNull TextComponent textComponent, HoverType type){
        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(getStringFromEnumType(type)).create()));
    }

    static void setCopyClickEvent(TextComponent textComponent, String toCopy){
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, toCopy));
    }

    public static void setFullTextComponentCopy(TextComponent textComponent, String toCopy, HoverType type){
        setCopyClickEvent(textComponent, toCopy);
        setHoverEventEnum(textComponent, type);
    }

    private static void setHoverEvent(TextComponent textComponent, String hoverText){
        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hoverText).create()));
    }

    private static void setRunCommandEvent(TextComponent textComponent, String command){
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
    }

    public static void setFullRunCommandEvent(TextComponent textComponent, String command, String hover){
        setHoverEvent(textComponent, hover);
        setRunCommandEvent(textComponent, command);
    }
}
