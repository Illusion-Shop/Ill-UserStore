package net.illusion.userstore.gui;

import net.illusion.userstore.utils.StoreUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CheckGUI implements InventoryHolder {


    private Inventory inv;

    private ItemStack itemStack;

    public CheckGUI() {
        inv = Bukkit.createInventory(this, 54, "진짜 살꺼야?");
    }

    public void openInventory(ItemStack itemStack, Player player) {
        inv.setItem(22, itemStack);
        player.openInventory(inv);
        this.itemStack = itemStack;
    }

    public void purchase() {
        StoreUtil.removeItemStacks(itemStack);
    }

    /**
     * Get the object's inventory.
     *
     * @return The inventory.
     */
    @NotNull
    @Override
    public Inventory getInventory() {
        return inv;
    }
}
