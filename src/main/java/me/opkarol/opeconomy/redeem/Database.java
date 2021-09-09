package me.opkarol.opeconomy.redeem;

import me.opkarol.opeconomy.utils.ObjectUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Database extends me.opkarol.opeconomy.economy.Database {

    private static HashMap<String, Redeem> map = new HashMap<>();

    public static HashMap<String, Redeem> getMap() {
        return map;
    }

    public static void addCodeToMap(String code, Redeem redeem){
        map.put(code, redeem);
    }

    public static Redeem getRedeemFromCode(String code){
        return map.get(code);
    }

    public static void removeCodeFromMap(String code){
        map.remove(code);
    }

    public static void setMap(HashMap<String, Redeem> map2) {
        map = map2;
    }

    public static boolean isntCodeExists(String code){
        return !map.containsKey(code);
    }

    public static boolean playerUsedValidCode(String code, Player player){
        if (isntCodeExists(code)) return false;
        Redeem redeem = map.get(code);
        int uses = redeem.getUses();
        if (uses >= redeem.getMaxUses()) return false;
        else {
            redeem.setUses(uses + 1);

            List<Player> playerList = redeem.getUsed();
            if (playerList == null) playerList = new ArrayList<>();
            playerList.add(player);
            redeem.setUsed(playerList);

            addPlayerMoney(ObjectUtils.getUUIDFromObject(player), redeem.getReward());
            return true;
        }
    }

    public static boolean hasPlayerUsedCode(String code, @NotNull CommandSender player){
        Redeem redeem = map.get(code);
        if (redeem == null) return false;
        AtomicBoolean has = new AtomicBoolean(false);
        redeem.getUsed().forEach(player1 -> {if (player1==player) has.set(true);});
        return has.get();
    }
}
