package net.illusion.userstore.command;

import net.illusion.userstore.data.StoreMapData;
import net.illusion.userstore.gui.StoreGUI;
import net.illusion.userstore.utils.StoreUtil;
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
            storeGUI.openInventory(player);
            StoreMapData.storeMap.put(player.getUniqueId(), storeGUI);
            return true;
        }

        switch (args[0]) {
            case "등록":

                if (args.length == 1) {
                    player.sendMessage("돈 입력해~ ");
                    return true;
                }

                if (args.length == 2) {
                    player.sendMessage("양 입력해~ ");
                    return true;
                }

                if (args.length != 3) {
                    player.sendMessage("잘못된 명령어 입니다!");
                    return true;
                }

                byte amount;
                long price;

                try {
                    amount = Byte.parseByte(args[1]);
                    price = Long.parseLong(args[2]);

                    StoreUtil.addItemStacks(player, amount, price);
                } catch (NumberFormatException e) {
                    player.sendMessage("응 정수^^");
                }
                break;
        }
        return false;
    }
}
