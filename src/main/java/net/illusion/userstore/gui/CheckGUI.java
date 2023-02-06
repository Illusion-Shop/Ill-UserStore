package net.illusion.userstore.gui;

import net.illusion.core.util.item.PDCData;
import net.illusion.userstore.UserStorePlugin;
import net.illusion.userstore.data.MessageData;
import net.illusion.userstore.utils.StoreUtil;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static net.illusion.userstore.UserStorePlugin.store;

public class CheckGUI implements InventoryHolder {

    private Inventory inv;

    private byte amount = 1;

    private int CURRENT_PAGE;

    private ItemStack itemStack;

    public CheckGUI() {
        inv = Bukkit.createInventory(this, 54, "구매 설정");
    }

    public void openInventory(Player player, ItemStack itemStack) {
        this.itemStack = itemStack;

        ItemStack clone = itemStack.clone();
        ItemMeta itemMeta = clone.getItemMeta();

        List<String> result = MessageData.getCheckLore("item.check.lore", amount, clone);

        itemMeta.setLore(result);

        clone.setItemMeta(itemMeta);

        inv.setItem(22, clone);

        if (amount + 1 > itemStack.getAmount()) {
            player.openInventory(inv);
            return;
        }
        player.openInventory(inv);
    }

    /**
     * @param player 구매한 플레이어
     * @return 플레이어가 아이템 가격보다 돈이 적거나 이벤토리가 꽉 차있으면 false를 반환합니다.
     */
    public boolean purchase(Player player) {
        Economy eco = UserStorePlugin.getEcon();

        List<ItemStack> itemStacks = StoreUtil.getItemStacks();

        if (itemStacks.contains(itemStack)) {
            PDCData storage = new PDCData(UserStorePlugin.getPlugin());

            if (!eco.has(player, storage.getLong(itemStack, "price"))) {
                player.sendMessage(MessageData.notEnoughMoney("message.notEnoughMoney"));
                return false;
            }

            if (player.getInventory().firstEmpty() == -1) {
                player.sendMessage(MessageData.notEnoughMoney("message.notEnoughInventory"));
                return false;
            }

            player.sendMessage(MessageData.itemMessage("message.purchase", itemStack));

            storage.removeNBT(itemStack, "price");
            storage.removeNBT(itemStack, "uuid");
            player.getInventory().addItem(itemStack);

            itemStacks.remove(itemStack);

            store.getConfig().set("items", itemStacks);
            store.saveConfig();

            player.closeInventory();
            return true;
        } else {
            player.sendMessage("해당 아이템은 존재하지 않습니다.");
            return false;
        }
    }

    public void setCURRENT_PAGE(int CURRENT_PAGE) {
        this.CURRENT_PAGE = CURRENT_PAGE;
    }

    public int getCURRENT_PAGE() {
        return CURRENT_PAGE;
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inv;
    }
}
