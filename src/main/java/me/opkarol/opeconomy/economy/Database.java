package me.opkarol.opeconomy.economy;

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

    private static HashMap<UUID, Integer> moneyMap = new HashMap<>();

    public static void addPlayerToMap(Object player, int money) {
        UUID uuid = getUUIDFromObject(player);
        moneyMap.put(uuid, money);
    }

    public static void createNewBankAccount(Object player){
        UUID uuid = getUUIDFromObject(player);
        moneyMap.put(uuid, defaultMoney);
    }

    public static void changeMoneyOfPlayer(Object player, int money) {
        UUID uuid = getUUIDFromObject(player);
        moneyMap.replace(uuid, money);
    }

    public static void changeMoneyOfPlayer(UUID uuid, int money){
        moneyMap.replace(uuid, money);
    }

    public static boolean isPlayerAccountExists(Object player){
        UUID uuid = getUUIDFromObject(player);
        return moneyMap.containsKey(uuid);
    }

    public static boolean isPlayerAccountExists(UUID uuid){
        return moneyMap.containsKey(uuid);
    }

    public static void removePlayerFromMap(Object player){
        UUID uuid = getUUIDFromObject(player);
        moneyMap.remove(uuid);
    }

    public static void removePlayerMoney(Object player, int amount){
        UUID uuid = getUUIDFromObject(player);
        moneyMap.replace(uuid, getMoneyFromPlayer(uuid) - amount);
    }

    public static void addPlayerMoney(Object player, int amount){
        UUID uuid = getUUIDFromObject(player);
        moneyMap.replace(uuid, getMoneyFromPlayer(uuid) + amount);
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
