package net.illusion.userstore.gui;

import lombok.Getter;
import lombok.Setter;
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

    private ItemStack itemStack;
    @Getter
    @Setter
    private StoreGUI storeGUI;


    public void openInventory(Player player, ItemStack itemStack) {
        this.itemStack = itemStack;

        inv = Bukkit.createInventory(this, 54, "구매 설정");

        ItemStack clone = itemStack.clone();
        ItemMeta itemMeta = clone.getItemMeta();

        List<String> result = MessageData.getCheckLore("item.check.lore", (byte) itemStack.getAmount(), clone);

        itemMeta.setLore(result);

        clone.setItemMeta(itemMeta);

        inv.setItem(22, clone);

        player.openInventory(inv);
    }

    /**
     * @param player 구매한 플레이어
     * @return 플레이어가 아이템 가격보다 돈이 적거나 이벤토리가 꽉 차있으면 false를 반환합니다.
     */
    public boolean purchase(Player player) {
        Economy eco = UserStorePlugin.getEcon();

        List<ItemStack> itemStacks = StoreUtil.getItemStacks();

        for (ItemStack items : itemStacks) {
            if (items != itemStack) continue;

            PDCData storage = new PDCData(UserStorePlugin.getPlugin());
            long amount = storage.getLong(itemStack, "price");

            if (!eco.has(player, amount)) {
                eco.withdrawPlayer(player, amount);
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
        }
        return false;
    }


    @NotNull
    @Override
    public Inventory getInventory() {
        return inv;
    }
}
