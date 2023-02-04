package net.illusion.userstore.event;

import net.illusion.userstore.UserStorePlugin;
import net.illusion.userstore.data.StoreMapData;
import net.illusion.userstore.gui.CheckGUI;
import net.illusion.userstore.gui.StoreGUI;
import net.illusion.userstore.utils.StoreUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class UserStoreListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent event) {

        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();

        Inventory inv = event.getClickedInventory();

        ItemStack itemStack = event.getCurrentItem();
        int slot = event.getSlot();


        if (itemStack != null && StoreMapData.storeMap.containsKey(player.getUniqueId())) {

            if (StoreUtil.isStoreGuiInventory(inv)) {
                if (slot > 45) {
                    if (slot == 50) {
                        StoreGUI storeGUI = StoreMapData.storeMap.get(player.getUniqueId());
                        storeGUI.nextPage(player);
                        return;
                    }

                    if (slot == 48) {
                        StoreGUI storeGUI = StoreMapData.storeMap.get(player.getUniqueId());
                        storeGUI.previousPage(player);
                        return;
                    }

                    event.setCancelled(true);
                    return;
                }
                ItemStack select = StoreUtil.getItemStacks().get(event.getSlot() * 1);

                CheckGUI checkGUI = new CheckGUI();
                checkGUI.openInventory(select, player);

                StoreMapData.checkMap.put(player.getUniqueId(), checkGUI);
                return;
            }


            if (StoreUtil.isCheckGuiInventory(inv)) {
                switch (event.getSlot()) {
                    case 22:
                        CheckGUI checkGUI = StoreMapData.checkMap.get(player.getUniqueId());
                        checkGUI.purchase();

                        StoreUtil.updateInventory();
                        player.closeInventory();
                        break;
                }
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {


        if (!(event.getPlayer() instanceof Player)) return;

        Player player = (Player) event.getPlayer();

        Inventory inv = event.getInventory();

        if (StoreUtil.isCheckGuiInventory(inv)) {
            player.sendMessage("응 못해~");
            new BukkitRunnable() {
                @Override
                public void run() {
                    new StoreGUI().openInventory(player);
                }
            }.runTaskLater(UserStorePlugin.getPlugin(), 1);

            return;
        }

        if (StoreUtil.isStoreGuiInventory(inv)) {
            player.sendMessage("샵 닫음");
        }
    }
}
