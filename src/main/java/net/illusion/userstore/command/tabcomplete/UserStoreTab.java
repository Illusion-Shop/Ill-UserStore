package net.illusion.userstore.command.tabcomplete;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserStoreTab implements TabCompleter {

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            List<String> newReturn = new ArrayList<>();
            switch (args.length) {
                case 1:
                    newReturn.add("등록");
                    break;

                case 2:
                    if ("등록".equalsIgnoreCase(args[0]))
                        newReturn.add("양");
                    break;
                case 3:
                    if ("등록".equalsIgnoreCase(args[0]))
                        newReturn.add("돈");
                    break;
            }

            return newReturn;
        }
        return Collections.emptyList();
    }
}
