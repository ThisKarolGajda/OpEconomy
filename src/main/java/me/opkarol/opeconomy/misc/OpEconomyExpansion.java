package me.opkarol.opeconomy.misc;

import me.opkarol.opeconomy.Economy;
import me.opkarol.opeconomy.redeem.Database;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class OpEconomyExpansion extends PlaceholderExpansion{

    @Override
    public @NotNull String getAuthor() {
        return "OpKarol";
    }

    @Override
    public @NotNull String getIdentifier() {
        return "opeconomy";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.2.0";
    }

    @Override
    public boolean persist() {
        return true; // This is required or else PlaceholderAPI will unregister the Expansion on reload
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (params.startsWith("money_")){
            final String name = params.substring(6);
            final Player target = Bukkit.getPlayer(name);
            if (target == null){
                return String.valueOf(Economy.getAPI().getBalance(Bukkit.getOfflinePlayer(name).getUniqueId()));
            } else return String.valueOf(Economy.getAPI().getBalance(target));
        }

        if (params.startsWith("baltop_number_")){
            int number;
            try {
                number = Integer.parseInt(params.substring(14));
            } catch (NumberFormatException ignore) {
                return null;
            }
            return me.opkarol.opeconomy.balanceTop.Database.getPlayerNameByPosition(number);
        }

        if (params.startsWith("baltop_player_position_")){
            final String playerName = params.substring(23);
            return String.valueOf(me.opkarol.opeconomy.balanceTop.Database.getPlayerPosition(playerName));
        }

        if (params.startsWith("money")){
            return String.valueOf(Economy.getAPI().getBalance(player.getUniqueId()));
        }

        if (params.startsWith("currency")){
            return Economy.getAPI().getActiveCurrency();
        }

        if (params.startsWith("is_active_code_")){
            final String code = params.substring(15);
            return String.valueOf(!Database.isntCodeExists(code));
        }

        if (params.startsWith("max_uses_code_")){
            final String code = params.substring(14);
            return String.valueOf(Database.getRedeemFromCode(code).getMaxUses());
        }

        if (params.startsWith("uses_code_")){
            final String code = params.substring(10);
            return String.valueOf(Database.getRedeemFromCode(code).getUses());

        }

        if (params.startsWith("reward_code_")){
            final String code = params.substring(12);
            return String.valueOf(Database.getRedeemFromCode(code).getReward());
        }

        return null; // Placeholder is unknown by the Expansion
    }
}
