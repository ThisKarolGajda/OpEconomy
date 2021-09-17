package me.opkarol.opeconomy.events;

import me.clip.placeholderapi.PlaceholderAPI;
import me.opkarol.opeconomy.economy.Database;
import me.opkarol.opeconomy.notes.NoteItem;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.tags.CustomItemTagContainer;
import org.bukkit.inventory.meta.tags.ItemTagType;
import org.jetbrains.annotations.NotNull;

public class ClickEvent extends NoteItem implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void clickEvent(@NotNull PlayerInteractEvent event){
        ItemStack item = event.getPlayer().getInventory().getItemInHand();
        if (!item.getType().equals(itemStack.getType())) return;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        CustomItemTagContainer tagContainer = meta.getCustomTagContainer();
        int foundValue;
        if(tagContainer.hasCustomTag(getNamespacedKey() , ItemTagType.INTEGER)) {
            foundValue = tagContainer.getCustomTag(getNamespacedKey(), ItemTagType.INTEGER);
        } else return;

        Player player = event.getPlayer();

        int slot = player.getInventory().getHeldItemSlot();
        ItemStack amount = player.getInventory().getItem(slot);

        assert amount != null;
        if (amount.getAmount() == 1) player.getInventory().remove(item);
        else amount.setAmount(amount.getAmount() - 1);

        Database.addPlayerMoney(player.getUniqueId(), foundValue);
        playSound(player, Sound.valueOf(getDepositSound()));
        player.sendMessage(PlaceholderAPI.setPlaceholders(player, getDepositedMoney().replace("%money_deposited%", String.valueOf(foundValue))));
    }
}
