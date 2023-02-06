package net.illusion.userstore.utils;

import net.illusion.core.util.item.PDCData;
import net.illusion.userstore.UserStorePlugin;
import net.illusion.userstore.data.MessageData;
import net.illusion.userstore.data.StoreMapData;
import net.illusion.userstore.gui.CheckGUI;
import net.illusion.userstore.gui.StoreGUI;
import net.illusion.userstore.packet.InventoryUpdate;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;
import java.util.*;

import static net.illusion.userstore.UserStorePlugin.store;

public class StoreUtil {


    public static void replaceItem(List<ItemStack> CURRENT_ITEMS, Inventory inv, int CURRENT_PAGE, int MAX_PAGE) {
        try {
            int slot = 0;

            inv.clear();
            for (int i = (CURRENT_PAGE - 1) * 45; i <= CURRENT_PAGE * 45; i++) {
                if (slot <= 44) {
                    ItemStack itemStack = StoreUtil.getItemStacks().get(StoreUtil.getItemStacks().indexOf(StoreUtil.getItemStacks().get(i)));

                    if (itemStack == null) continue;
                    ItemStack clone = itemStack.clone();

                    ItemMeta itemMeta = clone.getItemMeta();

                    PDCData storage = new PDCData(UserStorePlugin.getPlugin());

                    String uuid = storage.getString(clone, "uuid");
                    if (uuid == null) continue;

                    List<String> result = MessageData.getSellLore("item.sell.lore", clone);
                    List<String> lore = itemMeta.getLore();
                    if (lore != null)
                        result.addAll(lore);


                    itemMeta.setLore(result);
                    clone.setItemMeta(itemMeta);

                    inv.setItem(slot++, clone);
                    CURRENT_ITEMS.add(itemStack);
                }

            }
            if (MAX_PAGE > 1 && CURRENT_PAGE != MAX_PAGE)
                StoreUtil.newItemStack("다음 페이지", Material.ARROW, 1, Arrays.asList(""), 50, inv);

        } catch (IndexOutOfBoundsException e) {

        }
    }

    //TODO 아이템을 삭제 할 시 페이지보다 작아지면 자동으로 이전 페이지로 넘어가야뎀ㅇㅇ
    public static void removeItemStacks(StoreGUI storeGUI, Player player, ItemStack itemStack) {
        List<ItemStack> itemStacks = getItemStacks();

        itemStacks.remove(itemStack);

        store.getConfig().set("items", itemStacks);
        store.saveConfig();

        InventoryUpdate.updateInventory(UserStorePlugin.getPlugin(), player, "유저 상점 페이지 " + storeGUI.getCURRENT_PAGE() + "/" + storeGUI.getMAX_PAGE());

        updateInventory(player);

        if (storeGUI.getCURRENT_PAGE() != 1)
            StoreUtil.newItemStack("이전 페이지", Material.ARROW, 1, Arrays.asList(""), 48, storeGUI.getInventory());
    }

    public static List<ItemStack> getItemStacks() {
        List<ItemStack> itemStacks = (List<ItemStack>) store.getConfig().getList("items");
        if (itemStacks != null) return itemStacks;
        return new ArrayList<>();
    }

    public static void updateInventory(Player player) {
        Collection<StoreGUI> storeGUIS = StoreMapData.storeMap.values();

        storeGUIS.forEach(storeGUI -> {
            replaceItem(storeGUI.getCURRENT_ITEMS(), storeGUI.getInventory(), storeGUI.getCURRENT_PAGE(), storeGUI.getMAX_PAGE());

            storeGUI.setMAX_PAGE((StoreUtil.getItemStacks().size() / 45) + 1);
            InventoryUpdate.updateInventory(UserStorePlugin.getPlugin(), player, "유저 상점 페이지 " + storeGUI.getCURRENT_PAGE() + "/" + storeGUI.getMAX_PAGE());

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
