package me.opkarol.opeconomy.events;

import me.clip.placeholderapi.PlaceholderAPI;
import me.opkarol.opeconomy.economy.Database;
import me.opkarol.opeconomy.notes.NoteItem;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ClickEvent extends NoteItem implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void clickEvent(@NotNull PlayerInteractEvent event){
        ItemStack item = event.getPlayer().getInventory().getItemInHand();
        if (!item.isSimilar(itemStack)) return;

        int value;
        try {
            value = Objects.requireNonNull(item.getItemMeta()).getPersistentDataContainer().get(Objects.requireNonNull(NamespacedKey.fromString("opeconomy-money-note-key-dont-leak-pls")), PersistentDataType.INTEGER);
        } catch (Exception e) {
            return;
        }

        Player player = event.getPlayer();

        int slot = player.getInventory().getHeldItemSlot();
        ItemStack amount = player.getInventory().getItem(slot);

        assert amount != null;
        if (amount.getAmount() == 1) player.getInventory().remove(item);
        else amount.setAmount(amount.getAmount() - 1);

        Database.addPlayerMoney(player.getUniqueId(), value);
        playSound(player, Sound.valueOf(getDepositSound()));
        player.sendMessage(PlaceholderAPI.setPlaceholders(player, getDepositedMoney().replace("%money_deposited%", String.valueOf(value))));
    }
}
