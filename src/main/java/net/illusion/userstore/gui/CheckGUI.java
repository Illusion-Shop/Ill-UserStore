package net.illusion.userstore.gui;

import net.illusion.core.util.item.PDCData;
import net.illusion.userstore.UserStorePlugin;
import net.illusion.userstore.utils.StoreUtil;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

import static net.illusion.userstore.UserStorePlugin.store;

public class CheckGUI implements InventoryHolder {

    private Inventory inv;

    private int amount = 1;

    private ItemStack itemStack;

    public CheckGUI() {
        inv = Bukkit.createInventory(this, 54, "구매 설정");
    }

    public void openInventory(Player player, ItemStack itemStack) {
        this.itemStack = itemStack;
        inv.setItem(22, itemStack);

        if (amount + 1 > itemStack.getAmount()) {
            player.openInventory(inv);
            return;
        }

        StoreUtil.newItemStack(ChatColor.WHITE + "수량 설정", Material.COMMAND_BLOCK, 1, Arrays.asList(""), 31, inv);
        player.openInventory(inv);
    }

    /**
     * @param player 구매한 플레이어
     * @return 플레이어가 아이템 가격보다 돈이 적으면 false 를 리턴합니다.
     */
    public boolean purchase(Player player) {
        Economy eco = UserStorePlugin.getEcon();

        List<ItemStack> itemStacks = StoreUtil.getItemStacks();
        PDCData storage = new PDCData(UserStorePlugin.getPlugin());

        if (!eco.has(player, storage.getLong(itemStack, "price"))) {
            player.sendMessage("§c충분한 돈을 가지고 있지 않습니다!");
            return false;
        }

        if (player.getInventory().firstEmpty() == -1) {
            player.sendMessage("응 인벤토리 꽉 참 ㅆㄱ ");
            return false;
        }

        player.sendMessage(UserStorePlugin.prefix + ChatColor.WHITE + " 성공적으로 아이템을 구매했습니다!");

        storage.removeNBT(itemStack, "price");
        storage.removeNBT(itemStack, "uuid");
        player.getInventory().addItem(itemStack);

        itemStacks.remove(itemStack);


        store.getConfig().set("items", itemStacks);
        store.saveConfig();

        player.closeInventory();
        return true;
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inv;
    }
}
