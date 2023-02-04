package net.illusion.userstore.data;

import net.illusion.userstore.gui.CheckGUI;
import net.illusion.userstore.gui.StoreGUI;

import java.util.HashMap;
import java.util.UUID;

public class StoreMapData {
    public static HashMap<UUID, StoreGUI> storeMap = new HashMap<>();

    public static HashMap<UUID, CheckGUI> checkMap = new HashMap<>();

}
