package net.illusion.userstore.command;

import net.illusion.userstore.UserStorePlugin;
import net.illusion.userstore.data.StoreMapData;
import net.illusion.userstore.gui.StoreGUI;
import net.illusion.userstore.utils.StoreUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UserStoreCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;

        Player player = (Player) sender;

        if (args.length == 0) {
            StoreGUI storeGUI = new StoreGUI();
            storeGUI.updateInventory(player);

            StoreMapData.storeMap.put(player.getUniqueId(), storeGUI);
            return false;
        }
        StoreGUI storeGUI = new StoreGUI();

        switch (args[0]) {
            case "등록":
                if (args.length == 1) {
                    player.sendMessage(UserStorePlugin.prefix + ChatColor.RED + " 돈을 입력해주세요.");
                    return false;
                }

                if (args.length == 2) {
                    player.sendMessage(UserStorePlugin.prefix + ChatColor.RED + " 양을 입력해주세요.");
                    return false;
                }

                if (args.length != 3) {
                    player.sendMessage(UserStorePlugin.prefix + ChatColor.RED + " 잘못된 명령어입니다.");
                    return false;
                }

                byte amount;
                long price;

                try {
                    amount = Byte.parseByte(args[1]);
                    price = Long.parseLong(args[2]);
                } catch (NumberFormatException e) {
                    player.sendMessage(UserStorePlugin.prefix + ChatColor.RED + " 정수를 입력하세요.");
                    return false;
                }
                //TODO 유저가 업로드를 할 시, 이미 오픈되어있는 유저들을 불러와
                storeGUI.addItemStacks(player, amount, price);
                StoreUtil.updateInventory(player);
                return true;
            case "관리":
                storeGUI.updateInventory(player);

                if (args.length != 1) {
                    player.sendMessage("잘못된 명령어 입니다.");
                    return false;
                }
                return true;
        }
        return false;
    }
}
