package net.illusion.userstore.gui;

import net.illusion.core.util.item.PDCData;
import net.illusion.userstore.UserStorePlugin;
import net.illusion.userstore.data.MessageData;
import net.illusion.userstore.packet.InventoryUpdate;
import net.illusion.userstore.utils.StoreUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.illusion.userstore.UserStorePlugin.store;

public class StoreGUI implements InventoryHolder {


    private Inventory inv;

    private int CURRENT_PAGE = 1;

    private int MAX_PAGE;

    private List<ItemStack> CURRENT_ITEMS = new ArrayList<>();

    public StoreGUI() {
        MAX_PAGE = (StoreUtil.getItemStacks().size() / 45) + 1;
        inv = Bukkit.createInventory(this, 54, "유저 상점 페이지 " + CURRENT_PAGE + "/" + MAX_PAGE);
    }

    public void updateInventory(Player player) {
        this.CURRENT_ITEMS.clear();
        StoreUtil.replaceItem(CURRENT_ITEMS, inv, CURRENT_PAGE, MAX_PAGE);

        player.openInventory(inv);
        InventoryUpdate.updateInventory(UserStorePlugin.getPlugin(), player, "유저 상점 페이지 " + CURRENT_PAGE + "/" + MAX_PAGE);
    }

    public void updateInventory(int CURRENT_PAGE, Player player) {

        this.CURRENT_ITEMS.clear();
        this.CURRENT_PAGE = CURRENT_PAGE;

        StoreUtil.replaceItem(CURRENT_ITEMS, inv, this.CURRENT_PAGE, MAX_PAGE);

        player.openInventory(inv);

        InventoryUpdate.updateInventory(UserStorePlugin.getPlugin(), player, "유저 상점 페이지 " + this.CURRENT_PAGE + "/" + MAX_PAGE);

        if (CURRENT_PAGE != 1)
            StoreUtil.newItemStack("이전 페이지", Material.ARROW, 1, Arrays.asList(""), 48, inv);
    }

    public void nextPage(Player player) {
        if (CURRENT_PAGE == MAX_PAGE) return;
        CURRENT_PAGE++;
        InventoryUpdate.updateInventory(UserStorePlugin.getPlugin(), player, "유저 상점 페이지 " + CURRENT_PAGE + "/" + MAX_PAGE);
        inv.clear();
        this.CURRENT_ITEMS.clear();
        StoreUtil.replaceItem(CURRENT_ITEMS, inv, CURRENT_PAGE, MAX_PAGE);
        StoreUtil.newItemStack("이전 페이지", Material.ARROW, 1, Arrays.asList(""), 48, inv);
    }

    public void previousPage(Player player) {
        if (CURRENT_PAGE == 1) {
            return;
        }
        CURRENT_PAGE--;

        inv.clear();
        this.CURRENT_ITEMS.clear();
        StoreUtil.replaceItem(CURRENT_ITEMS, inv, CURRENT_PAGE, MAX_PAGE);
        if (CURRENT_PAGE != 1)
            StoreUtil.newItemStack("이전 페이지", Material.ARROW, 1, Arrays.asList(""), 48, inv);

        InventoryUpdate.updateInventory(UserStorePlugin.getPlugin(), player, "유저 상점 페이지 " + CURRENT_PAGE + "/" + MAX_PAGE);
    }


    public void addItemStacks(Player player, byte amount, long price) {
        ItemStack itemStack = player.getInventory().getItemInMainHand();

        ItemStack clone = itemStack.clone();

        if (itemStack.getType() == Material.AIR) {
            player.sendMessage(UserStorePlugin.prefix + ChatColor.RED + " 공기는 판매할 수 없습니다.");
            return;
        }

        if (!(itemStack.getAmount() >= amount)) {
            player.sendMessage(UserStorePlugin.prefix + ChatColor.RED + " 아이템 개수가 부족합니다.");
            return;
        }
        PDCData pdcData = new PDCData(UserStorePlugin.getPlugin());

        pdcData.setString(clone, "uuid", player.getUniqueId().toString());
        pdcData.setLong(clone, "price", price);

        List<ItemStack> list = StoreUtil.getItemStacks();
        clone.setAmount(amount);
        list.add(clone);

        store.getConfig().set("items", list);
        store.saveConfig();

        player.sendMessage(MessageData.itemMessage("message.sell", clone));
        itemStack.setAmount(itemStack.getAmount() - amount);
    }


    public void setMAX_PAGE(int MAX_PAGE) {
        this.MAX_PAGE = MAX_PAGE;
    }


    public void setCURRENT_PAGE(int CURRENT_PAGE) {
        this.CURRENT_PAGE = CURRENT_PAGE;
    }

    public void subCURRENT_PAGE() {
        this.CURRENT_PAGE--;
    }

    public int getCURRENT_PAGE() {
        return CURRENT_PAGE;
    }

    public int getMAX_PAGE() {
        return MAX_PAGE;
    }

    public List<ItemStack> getCURRENT_ITEMS() {
        return CURRENT_ITEMS;
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inv;
    }
}
