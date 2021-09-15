package me.opkarol.opeconomy.notes;

import me.clip.placeholderapi.PlaceholderAPI;
import me.opkarol.opeconomy.Economy;
import me.opkarol.opeconomy.utils.ColorUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.bukkit.inventory.ItemFlag.*;

public class NoteItem {
    private static String dontHavePermission;
    private static String lastArgumentNotNumber;
    private static String notAvailableToConsole;
    private static String badUsage;
    private static String withdrawnMoney;
    private static String depositedMoney;
    private static String name;
    private static List<String> lore;
    private static String material;
    private static boolean enchanted;
    private static boolean hidden;
    private static String withdrawSound;
    private static String depositSound;
    private static double minimumNote;
    private static double maximumNote;

    public static ItemStack itemStack = new ItemStack(Material.getMaterial(material) != null ? Objects.requireNonNull(Material.getMaterial(material)) : Material.PAPER);
    static ItemMeta meta = itemStack.getItemMeta();

    public static @NotNull ItemStack getNote(int value, Player player){
        assert meta != null;
        meta.setDisplayName(replaceHolders(name, value, player));
        List<String> loreIG = new ArrayList<>();
        lore.forEach(s -> loreIG.add(replaceHolders(s, value, player)));
        meta.setLore(ColorUtils.formatList(loreIG));
        if (enchanted) meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
        if (hidden) { meta.addItemFlags(HIDE_ATTRIBUTES); meta.addItemFlags(HIDE_ENCHANTS);}

        meta.getPersistentDataContainer().set(Objects.requireNonNull(NamespacedKey.fromString("opeconomy-money-note-key-dont-leak-pls")), PersistentDataType.INTEGER, value);

        itemStack.setItemMeta(meta);

        return itemStack;
    }

    private static @NotNull String replaceHolders(@NotNull String string, int value, Player player){
        return PlaceholderAPI.setPlaceholders(player, string.replace("%note_value%", String.valueOf(value)));
    }

    public void playSound(@NotNull Player player, Sound sound){
        try {
            player.playSound(player.getLocation(), sound, 10, 0);
        } catch (IllegalArgumentException ignore) {
            Economy.getEconomy().getLogger().warning("Looks like withdraw/deposit sound is invalid. Please fix it.");
        }
    }

    public static void setDepositSound(String depositSound) {
        NoteItem.depositSound = depositSound;
    }

    public static void setWithdrawSound(String withdrawSound) {
        NoteItem.withdrawSound = withdrawSound;
    }

    public static void setMaximumNote(double maximumNote) {
        NoteItem.maximumNote = maximumNote;
    }

    public static void setMinimumNote(double minimumNote) {
        NoteItem.minimumNote = minimumNote;
    }

    public static void setHidden(boolean hidden) {
        NoteItem.hidden = hidden;
    }

    public static void setEnchanted(boolean enchanted) {
        NoteItem.enchanted = enchanted;
    }

    public static void setMaterial(String material) {
        NoteItem.material = material;
    }

    public static void setLore(List<String> lore) {
        NoteItem.lore = lore;
    }

    public static void setName(String name) {
        NoteItem.name = name;
    }

    public static void setDepositedMoney(String depositedMoney) {
        NoteItem.depositedMoney = depositedMoney;
    }

    public static void setWithdrawnMoney(String withdrawnMoney) {
        NoteItem.withdrawnMoney = withdrawnMoney;
    }

    public static void setNotAvailableToConsole(String notAvailableToConsole) {
        NoteItem.notAvailableToConsole = notAvailableToConsole;
    }

    public static void setLastArgumentNotNumber(String lastArgumentNotNumber) {
        NoteItem.lastArgumentNotNumber = lastArgumentNotNumber;
    }

    public static void setDontHavePermission(String dontHavePermission) {
        NoteItem.dontHavePermission = dontHavePermission;
    }

    public static String getDepositSound() {
        return depositSound;
    }

    public static String getWithdrawSound() {
        return withdrawSound;
    }

    public static String getDepositedMoney() {
        return depositedMoney;
    }

    public static String getWithdrawnMoney() {
        return withdrawnMoney;
    }

    public static String getNotAvailableToConsole() {
        return notAvailableToConsole;
    }

    public static String getLastArgumentNotNumber() {
        return lastArgumentNotNumber;
    }

    public static String getDontHavePermission() {
        return dontHavePermission;
    }

    public static double getMinimumNote() {
        return minimumNote;
    }

    public static double getMaximumNote() {
        return maximumNote;
    }

    public static String getBadUsage() {
        return badUsage;
    }

    public static void setBadUsage(String badUsage) {
        NoteItem.badUsage = badUsage;
    }
}
