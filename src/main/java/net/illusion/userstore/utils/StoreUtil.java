package net.illusion.userstore.utils;

import net.illusion.userstore.data.StoreMapData;
import net.illusion.userstore.gui.CheckGUI;
import net.illusion.userstore.gui.StoreGUI;
import org.bukkit.Material;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;
import java.util.*;

import static net.illusion.userstore.UserStorePlugin.store;

public class StoreUtil {


    public static boolean removeItemStacks(StoreGUI storeGUI, Player player, ItemStack itemStack) {

        List<ItemStack> itemStacks = getItemStacks();
        itemStacks.remove(itemStack);
        store.getConfig().set("items", itemStacks);
        store.saveConfig();
        storeGUI.updateInventory(player);
        return true;
    }

    public static List<ItemStack> getItemStacks() {
        List<ItemStack> itemStacks = (List<ItemStack>) store.getConfig().getList("items");
        if (itemStacks != null) return itemStacks;
        return new ArrayList<>();
    }

    public static void updateInventory(Player player) {
        Set<UUID> uuidSet = StoreMapData.checkMap.keySet();

        if (uuidSet.isEmpty()) return;

        uuidSet.forEach(uuid -> {
            StoreGUI storeGUI = StoreMapData.storeMap.get(uuid);

            if (storeGUI != null)
                storeGUI.updateInventory(player);
        });

    }

    public static void newItemStack(String name, Material type, int stack, List<String> lore, int loc, Inventory inv) {
        ItemStack item = new ItemStack(type, stack);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        item.setItemMeta(meta);
        inv.setItem(loc, item);
    }

    public static String decal(long to) {
        DecimalFormat df = new DecimalFormat("###,###");
        return df.format(to);
    }

    public static boolean isCheckGuiInventory(Inventory inv) {
        if (inv != null)
            return inv.getHolder() instanceof CheckGUI;
        return false;
    }

    public static boolean isStoreGuiInventory(Inventory inv) {
        if (inv != null)
            return inv.getHolder() instanceof StoreGUI;
        return false;
    }
}
