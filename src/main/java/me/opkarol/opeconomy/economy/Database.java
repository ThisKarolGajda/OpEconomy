package me.opkarol.opeconomy.economy;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

import static me.opkarol.opeconomy.utils.ObjectUtils.*;

public class Database {
    static int defaultMoney;
    static String currency;

    public static HashMap<UUID, Integer> getMoneyMap(){return moneyMap;}

    public static void setMoneyMap(HashMap<UUID, Integer> map1){
        moneyMap = map1;
    }

    public static UUID @NotNull [] getUUIDs(){
        return moneyMap.keySet().toArray(new UUID[0]);
    }

    private static HashMap<UUID, Integer> moneyMap = new HashMap<>();

    public static void createNewBankAccount(Object player){
        UUID uuid = getUUIDFromObject(player);
        moneyMap.put(uuid, defaultMoney);
    }

    public static void changeMoneyOfPlayer(UUID uuid, int money){
        moneyMap.replace(uuid, money);
    }

    public static boolean isPlayerAccountExists(UUID uuid){
        return moneyMap.containsKey(uuid);
    }

    public static void removePlayerMoney(UUID uuid, int amount){
        moneyMap.replace(uuid, getMoneyFromPlayer(uuid) - amount);
    }

    public static void addPlayerMoney(UUID uuid, int amount){
        moneyMap.replace(uuid, getMoneyFromPlayer(uuid) + amount);
    }

    public static int getMoneyFromPlayer(Object player){
        UUID uuid = getUUIDFromObject(player);
        return moneyMap.get(uuid);
    }

    public static int getMoneyFromUUID(UUID uuid){
        return moneyMap.get(uuid);
    }

    public static void setDefaultMoney(int money){
        defaultMoney = money;
    }

    public static void setCurrency(String currency1) {
        currency = currency1;
    }

    public static String getCurrency(){
        return currency;
    }
}
