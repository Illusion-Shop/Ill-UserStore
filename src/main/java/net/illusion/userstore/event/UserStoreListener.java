package net.illusion.userstore.event;

import net.illusion.userstore.UserStorePlugin;
import net.illusion.userstore.data.MessageData;
import net.illusion.userstore.data.StoreMapData;
import net.illusion.userstore.gui.CheckGUI;
import net.illusion.userstore.gui.StoreGUI;
import net.illusion.userstore.utils.StoreUtil;

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

        int slot = event.getSlot();


        if (StoreUtil.isStoreGuiInventory(inv)) {
            if (!StoreMapData.storeMap.containsKey(player.getUniqueId())) {
                System.out.println("test");
                event.setCancelled(true);
                return;
            }
            StoreGUI storeGUI = StoreMapData.storeMap.get(player.getUniqueId());

            if (slot == 50) {
                storeGUI.nextPage(player);
                event.setCancelled(true);
            }

            if (slot == 48) {
                storeGUI.previousPage(player);
                event.setCancelled(true);
            }

            ItemStack select;

            try {
                select = storeGUI.getCURRENT_ITEMS().get(event.getSlot());

                if (slot < 48) {
                    if (player.isOp() && event.isShiftClick()) {
                        StoreUtil.removeItemStacks(storeGUI, player, select);
                        event.setCancelled(true);
                        return;
                    }

                    if (select != null) {
                        CheckGUI checkGUI = new CheckGUI();

                        checkGUI.openInventory(player, select);
                        checkGUI.setStoreGUI(storeGUI);
                        StoreMapData.checkMap.put(player.getUniqueId(), checkGUI);
                        return;
                    }
                }
            } catch (Exception e) {
                event.setCancelled(true);
                return;
            }

            return;
        }

        //TODO Check GUI 체크,
        CheckGUI checkGUI = StoreMapData.checkMap.get(player.getUniqueId());

        if (StoreUtil.isCheckGuiInventory(inv)) {
            if (event.getSlot() == 22) {
                if (checkGUI.purchase(player)) {
                    StoreUtil.updateInventory(player);
                    player.closeInventory();
                } else {
                    player.sendMessage(UserStorePlugin.prefix + " &c해당 아이템은 존재하지 않습니다.");
                    player.closeInventory();
                }
            }
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player)) return;

        Player player = (Player) event.getPlayer();

        Inventory inv = event.getInventory();

        if (StoreUtil.isCheckGuiInventory(inv)) {
            CheckGUI checkGUI = StoreMapData.checkMap.get(player.getUniqueId());

            StoreGUI storeGUI = new StoreGUI();
            new BukkitRunnable() {
                @Override
                public void run() {
                    storeGUI.updateInventory(checkGUI.getStoreGUI().getCURRENT_PAGE(), player);
                    StoreMapData.storeMap.put(player.getUniqueId(), storeGUI);
                }
            }.runTaskLater(UserStorePlugin.getPlugin(), 1);
            return;
        }

        if (StoreUtil.isStoreGuiInventory(inv)) {
            if (StoreMapData.storeMap.containsKey(player.getUniqueId()) && StoreMapData.checkMap.containsKey(player.getUniqueId())) {
                StoreMapData.storeMap.remove(player.getUniqueId());
            }
        }
    }
}
