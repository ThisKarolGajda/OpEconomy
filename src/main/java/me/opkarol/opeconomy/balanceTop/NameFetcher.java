package me.opkarol.opeconomy.balanceTop;

import org.bukkit.Bukkit;

import java.util.Objects;
import java.util.UUID;

public class NameFetcher {

    public static String getName(UUID uuid) {
        if (Bukkit.getPlayer(uuid) != null) Objects.requireNonNull(Bukkit.getPlayer(uuid)).getName();
        return Bukkit.getOfflinePlayer(uuid).getName();
    }

}
