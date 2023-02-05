package net.illusion.userstore.gui;

import net.illusion.core.util.item.PDCData;
import net.illusion.userstore.UserStorePlugin;
import net.illusion.userstore.packet.InventoryUpdate;
import net.illusion.userstore.utils.StoreUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static net.illusion.userstore.UserStorePlugin.store;

public class StoreGUI implements InventoryHolder {


    private Inventory inv;

    private int CURRENT_PAGE = 1;

    private int MAX_PAGE;

    private List<ItemStack> CURRENT_ITEMS = new ArrayList<>();

    public StoreGUI() {
        MAX_PAGE = (StoreUtil.getItemStacks().size() / 45);
        inv = Bukkit.createInventory(this, 54, "유저 상점 페이지 " + CURRENT_PAGE + "/" + MAX_PAGE);
    }

    public StoreGUI(Player player) {
        MAX_PAGE = (StoreUtil.getItemStacks().size() / 45);
        inv = Bukkit.createInventory(this, 54, "유저 상점 페이지 " + CURRENT_PAGE + "/" + MAX_PAGE);

    }

    public void updateInventory(Player player) {
        try {
            inv.clear();
            int slot = 0;
            this.CURRENT_ITEMS.clear();

            for (int i = (CURRENT_PAGE - 1) * 49; i <= CURRENT_PAGE * 49; i++) {
                if (slot < 45) {
                    ItemStack itemStack = StoreUtil.getItemStacks().get(i);
                    ItemStack clone = itemStack.clone();
                    ItemMeta itemMeta = clone.getItemMeta();

                    PDCData storage = new PDCData(UserStorePlugin.getPlugin());

                    String uuid = storage.getString(clone, "uuid");

                    if (uuid == null) continue;

                    long price = storage.getLong(clone, "price");

                    OfflinePlayer seller = Bukkit.getOfflinePlayer(UUID.fromString(uuid));

                    itemMeta.setLore(
                            Arrays.asList(ChatColor.GRAY + "판매자 : " + seller.getName(),
                                    ChatColor.GRAY + "가격 : " + ChatColor.GOLD + StoreUtil.decal(price) + ""));

                    clone.setItemMeta(itemMeta);
                    inv.setItem(slot++, clone);
                    this.CURRENT_ITEMS.add(itemStack);
                }
            }
        } catch (IndexOutOfBoundsException e) {

        }
        player.openInventory(inv);

        if (CURRENT_PAGE >= MAX_PAGE) return;
        StoreUtil.newItemStack("다음 페이지", Material.ARROW, 1, Arrays.asList(""), 50, inv);
    }

    public void nextPage(Player player) {
        if (CURRENT_PAGE == MAX_PAGE) return;
        CURRENT_PAGE++;

        inv.clear();

        updateInventory(player);

        StoreUtil.newItemStack("이전 페이지", Material.ARROW, 1, Arrays.asList(""), 48, inv);
        InventoryUpdate.updateInventory(UserStorePlugin.getPlugin(), player, "유저 상점 페이지 " + CURRENT_PAGE + "/" + MAX_PAGE);
    }


    public void previousPage(Player player) {
        if (CURRENT_PAGE == 1) {
            return;
        }

        CURRENT_PAGE--;

        inv.clear();

        updateInventory(player);

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

        List<ItemStack> list = StoreUtil.getItemStacks();
        clone.setAmount(amount);
        list.add(clone);

        PDCData pdcData = new PDCData(UserStorePlugin.getPlugin());

        pdcData.setString(itemStack, "uuid", player.getUniqueId().toString());
        pdcData.setLong(itemStack, "price", price);

        store.getConfig().set("items", list);
        store.saveConfig();


        player.sendMessage(UserStorePlugin.prefix + ChatColor.GOLD + " " + StoreUtil.decal(price) + ChatColor.WHITE + "원에 성공적으로 등록 하였습니다! §7+" + ChatColor.GOLD + amount);

        itemStack.setAmount(itemStack.getAmount() - amount);
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
