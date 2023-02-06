package net.illusion.userstore.data;

import net.illusion.core.util.item.PDCData;
import net.illusion.userstore.UserStorePlugin;
import net.illusion.userstore.utils.StoreUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MessageData {
    public static List<String> getSellLore(String path, ItemStack itemStack) {
        PDCData storage = new PDCData(UserStorePlugin.getPlugin());

        long price = storage.getLong(itemStack, "price");
        String uuid = storage.getString(itemStack, "uuid");

        List<String> result = UserStorePlugin.config.getConfig().getStringList(path);

        List<String> list = new ArrayList<>();

        for (String s : result) {
            s = ChatColor.translateAlternateColorCodes('&', s);
            s = s.replaceAll("\\{seller\\}", Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName());
            s = s.replaceAll("\\{price\\}", StoreUtil.decal(price));

            list.add(s);
        }
        return list;
    }

    public static List<String> getCheckLore(String path, byte amount, ItemStack itemStack) {
        PDCData storage = new PDCData(UserStorePlugin.getPlugin());

        long price = storage.getLong(itemStack, "price");
        String uuid = storage.getString(itemStack, "uuid");

        List<String> result = UserStorePlugin.config.getConfig().getStringList(path);

        List<String> list = new ArrayList<>();

        for (String s : result) {
            s = ChatColor.translateAlternateColorCodes('&', s);
            s = s.replaceAll("\\{seller\\}", Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName());
            s = s.replaceAll("\\{price\\}", StoreUtil.decal(price));
            s = s.replaceAll("\\{amount\\}", String.valueOf(amount));

            list.add(s);
        }
        return list;
    }

    public static String itemMessage(String path, ItemStack itemStack) {
        PDCData storage = new PDCData(UserStorePlugin.getPlugin());

        long price = storage.getLong(itemStack, "price");

        ItemMeta itemMeta = itemStack.getItemMeta();

        String result = UserStorePlugin.config.getConfig().getString(path);

        result = ChatColor.translateAlternateColorCodes('&', result);

        result = result.replaceAll("\\{prefix\\}", UserStorePlugin.prefix);
        result = result.replaceAll("\\{price\\}", StoreUtil.decal(price));
        result = result.replaceAll("\\{name\\}", itemStack.getType().name());
        result = result.replaceAll("\\{amount\\}", String.valueOf(itemStack.getAmount()));

        if (itemStack.getItemMeta().hasDisplayName())
            result = result.replaceAll("\\{name\\}", itemMeta.getDisplayName());

        return result;
    }



    public static String notEnoughMoney(String path) {
        String result = UserStorePlugin.config.getConfig().getString(path);

        result = ChatColor.translateAlternateColorCodes('&', result);

        result = result.replaceAll("\\{prefix\\}", UserStorePlugin.prefix);

        return result;
    }
}
