package net.illusion.userstore.utils;

import net.illusion.core.util.item.PDCData;
import net.illusion.userstore.UserStorePlugin;
import net.illusion.userstore.data.StoreMapData;
import net.illusion.userstore.gui.CheckGUI;
import net.illusion.userstore.gui.StoreGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

import static net.illusion.userstore.UserStorePlugin.store;

public class StoreUtil {
    public static void removeItemStacks(ItemStack itemStack) {
        List<ItemStack> itemStacks = getItemStacks();
        itemStacks.remove(itemStack);
        store.getConfig().set("items", itemStacks);
        store.saveConfig();
    }


    public static void addItemStacks(Player player, byte amount, long price) {


        ItemStack itemStack = player.getInventory().getItemInMainHand();
        ItemStack clone = itemStack.clone();
        if (itemStack.getType() == Material.AIR) {
            player.sendMessage("응 못해~");
            return;
        }

        if (!(itemStack.getAmount() >= amount)) {
            player.sendMessage("응 꺼져~");
            return;
        }


        List<ItemStack> list = getItemStacks();
        clone.setAmount(amount);
        list.add(clone);

        PDCData pdcData = new PDCData(UserStorePlugin.getPlugin());

        pdcData.setString(itemStack, "uuid", player.getUniqueId().toString());
        pdcData.setLong(itemStack, "price", price);

        store.getConfig().set("items", list);
        store.saveConfig();

        player.sendMessage("§a해당 아이템을 " + price + " 가격에 성공적으로 등록 하였습니다! §7+" + amount);

        itemStack.setAmount(itemStack.getAmount() - amount);
        updateInventory();
    }


    public static List<ItemStack> getItemStacks() {
        List<ItemStack> itemStacks = (List<ItemStack>) store.getConfig().getList("items");
        if (itemStacks != null) return itemStacks;
        return new ArrayList<>();
    }

    public static void updateInventory() {
        System.out.println("업데이트 더ㅚㅁ");
        Set<UUID> uuids = StoreMapData.checkMap.keySet();
        Collection<StoreGUI> storeGUIS = StoreMapData.storeMap.values();

        uuids.forEach(uuid -> {
            Player player = Bukkit.getPlayer(uuid);
            storeGUIS.forEach(storeGUI -> {
                storeGUI.openInventory(player);
            });
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

    public static boolean isCheckGuiInventory(Inventory inv) {

        return inv.getHolder() instanceof CheckGUI;
    }

    public static boolean isStoreGuiInventory(Inventory inv) {

        return inv.getHolder() instanceof StoreGUI;
    }
}
