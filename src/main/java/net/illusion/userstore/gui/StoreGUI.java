package net.illusion.userstore.gui;

import net.illusion.core.util.item.PDCData;
import net.illusion.userstore.UserStorePlugin;
import net.illusion.userstore.packet.InventoryUpdate;
import net.illusion.userstore.utils.StoreUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class StoreGUI implements InventoryHolder {


    private Inventory inv;

    private int CURRENT_PAGE = 1;

    private int MAX_PAGE;

    public StoreGUI() {
        MAX_PAGE = (StoreUtil.getItemStacks().size() / 45) + 1;
        inv = Bukkit.createInventory(this, 54, "유저 상점 페이지 " + CURRENT_PAGE + "/" + MAX_PAGE);
    }

    public StoreGUI openInventory(Player player) {
        inv.clear();
        try {
            int i2 = 0;
            for (int i = (CURRENT_PAGE - 1) * 45; i <= CURRENT_PAGE * 45; i++) {
                if (i2 <= 45) {
                    ItemStack itemStack = StoreUtil.getItemStacks().get(i);
                    ItemStack clone = itemStack.clone();
                    ItemMeta itemMeta = clone.getItemMeta();

                    PDCData storage = new PDCData(UserStorePlugin.getPlugin());

                    String uuid = storage.getString(clone, "uuid");

                    if (uuid == null) continue;

                    long price = storage.getLong(clone, "price");

                    Player seller = Bukkit.getPlayer(UUID.fromString(uuid));

                    itemMeta.setLore(Arrays.asList("판매자 : " + seller.getDisplayName(),
                            "가격 : " + price + ""));

                    clone.setItemMeta(itemMeta);
                    inv.setItem(i2++, clone);
                }
            }
        } catch (IndexOutOfBoundsException e) {

        }


        player.openInventory(getInventory());
        
        if (MAX_PAGE <= 1) return this;
        StoreUtil.newItemStack("다음 페이지", Material.ARROW, 1, Arrays.asList(""), 50, inv);

        return this;
    }

    public void nextPage(Player player) {

        if (CURRENT_PAGE == MAX_PAGE) return;

        inv.clear();
        StoreUtil.newItemStack("이전 페이지", Material.ARROW, 1, Arrays.asList(""), 48, inv);
        CURRENT_PAGE++;
        openInventory(player);

        InventoryUpdate.updateInventory(UserStorePlugin.getPlugin(), player, "유저 상점 페이지 " + CURRENT_PAGE + "/" + MAX_PAGE);

    }

    public void previousPage(Player player) {
        if (CURRENT_PAGE == 1) return;

        inv.clear();
        StoreUtil.newItemStack("다음 페이지", Material.ARROW, 1, Arrays.asList(""), 50, inv);

        CURRENT_PAGE--;
        openInventory(player);

        InventoryUpdate.updateInventory(UserStorePlugin.getPlugin(), player, "유저 상점 페이지 " + CURRENT_PAGE + "/" + MAX_PAGE);
    }

    @NotNull
    @Override
    public Inventory getInventory() {

        return inv;
    }
}
