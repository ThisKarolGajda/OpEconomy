package me.opkarol.opeconomy.balanceTop;

import me.opkarol.opeconomy.Economy;
import me.opkarol.opeconomy.utils.ObjectUtils;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Database {
    private static int pageSize;
    private static String pageFront;
    private static String pageMiddle;
    private static boolean pageEndEnabled;
    private static String pageEndNextPage;
    private static String pageEndPreviousPage;
    private static String pageEndHoverMessage;

    private static final HashMap<String, Integer> map = new HashMap<>();

    @Contract(pure = true)
    public static void setupMap(UUID @NotNull [] uuids){
        String name;
        int money;
        for (UUID uuid : uuids){
            name = NameFetcher.getName(uuid);
            money = me.opkarol.opeconomy.economy.Database.getMoneyFromUUID(uuid);
            map.put(name, money);
        }
    }

    private static HashMap<String, Integer> integerHashMap;
    public static void refreshMap(){
        new BukkitRunnable() {
            @Override
            public void run() {
                setupMap(me.opkarol.opeconomy.economy.Database.getUUIDs());
                integerHashMap = sortByValue();
            }
        }.runTaskLaterAsynchronously(Economy.getEconomy(), 0);
    }

    public static @Nullable String getPlayerNameByPosition(int position){
        int size = integerHashMap.size();
        if (position >= size) return null;
        int number = size - position;
        return String.valueOf(integerHashMap.keySet().toArray()[number]);
    }

    public static int getPlayerPosition(String name){
        return 0;
    }

    public static void loadMap(){
        refreshMap();
        new BukkitRunnable() {
            @Override
            public void run() { refreshMap();}
        }.runTaskTimerAsynchronously(Economy.getEconomy(), 20*60, 20*60);
    }

    private static final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public static TextComponent getComponentFromPage(int page){
        if (page == 0) page = 1;
        return getStringBuilder(page);
    }

    public static @Nullable TextComponent getStringBuilder(int page){
        TextComponent main = new TextComponent();

        int index = (page * pageSize) - pageSize;
        int length = map.size();
        int i2 = 0;
        if (page == 0) page = 1;

        if (length == 0) return null;

        main.addExtra(pageFront.replace("%all_players%", String.valueOf(length)).replace("%current_date%", dateFormat.format(new Date())).replace("%current_page%", String.valueOf(page)));
        while (i2 < pageSize && length > i2) {
            String playerName;
            int number = length - 1 - i2 - index;
            try { playerName = (String) integerHashMap.keySet().toArray()[number];} catch (Exception e) { break;}
            main.addExtra(pageMiddle.replace("%player_number%", String.valueOf(length - number)).replace("%player_nick%", playerName).replace("%player_money%", String.valueOf(integerHashMap.get(playerName))).replace("%currency%", me.opkarol.opeconomy.economy.Database.getCurrency()));
            i2++;
        }
        if (pageEndEnabled) {
            TextComponent previous = new TextComponent(pageEndPreviousPage);
            TextComponent next = new TextComponent(pageEndNextPage);
            ObjectUtils.setFullRunCommandEvent(previous, "/balancetop " + (page - 1), pageEndHoverMessage);
            ObjectUtils.setFullRunCommandEvent(next, "/balancetop " + (page + 1), pageEndHoverMessage);

            main.addExtra(previous);
            main.addExtra(next);
        }

        return main;
    }

    private static <K, V> @NotNull HashMap<K, V> sortByValue() {

        LinkedList<Map.Entry<K, V>> list = new LinkedList<>(((HashMap<K, V>) Database.map).entrySet());
        list.sort((Comparator<Object>) (o1, o2) -> ((Comparable<V>) ((Map.Entry<K, V>) (o1)).getValue()).compareTo(((Map.Entry<K, V>) (o2)).getValue()));

        HashMap<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) result.put(entry.getKey(), entry.getValue());
        return result;
    }

    public static void setPageSize(int pageSize) {
        Database.pageSize = pageSize;
    }

    public static void setPageFront(String pageFront) {
        Database.pageFront = pageFront;
    }

    public static void setPageMiddle(String pageMiddle) {
        Database.pageMiddle = pageMiddle;
    }

    public static void setPageEndEnabled(Boolean pageEndEnabled) {
        Database.pageEndEnabled = pageEndEnabled;
    }

    public static void setPageEndNextPage(String pageEndNextPage) {
        Database.pageEndNextPage = pageEndNextPage;
    }

    public static void setPageEndPreviousPage(String pageEndPreviousPage) {
        Database.pageEndPreviousPage = pageEndPreviousPage;
    }

    public static void setPageEndHoverMessage(String pageEndHoverMessage) {
        Database.pageEndHoverMessage = pageEndHoverMessage;
    }

}
