package de.joo.chestshop.migration;

import com.Acrobot.Breeze.Utils.Encoding.Base64;
import com.Acrobot.ChestShop.Database.Account;
import com.Acrobot.ChestShop.Database.DaoCreator;
import com.Acrobot.ChestShop.Database.Item;
import com.Acrobot.ChestShop.Listeners.PostTransaction.ItemManager;
import com.Acrobot.ChestShop.Metadata.ItemDatabase;
import com.Acrobot.ChestShop.ORMlite.dao.Dao;
import com.Acrobot.ChestShop.Signs.ChestShopSign;
import com.Acrobot.ChestShop.UUIDs.NameManager;
import com.sk89q.worldedit.event.platform.BlockInteractEvent;
import de.joo.chestshop.plugin.ChestShop;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.YamlConstructor;
import org.bukkit.configuration.file.YamlRepresenter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Johannes on 16.09.2017.
 * *
 * *
 * This file is part of ChestShop-4.
 * Copyright (C) 2017
 * *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */
public class MigrationManager {
    private ChestShop plugin;
    public MigrationManager(ChestShop plugin) {
        this.plugin = plugin;
    }

    public void start() {
        disableOldListener();
        migrateDatabase();
    }

    private final Yaml yaml = new Yaml(new YamlBukkitConstructor(), new YamlRepresenter(), new DumperOptions());

    public static boolean isOldSign(Sign sign) {
        boolean valid = sign.getLine(0).matches("^[\\w_]{3,16}$");
        return valid && ChestShopSign.isValid(sign);
    }

    private class YamlBukkitConstructor extends YamlConstructor {
        public YamlBukkitConstructor() {
            this.yamlConstructors.put(new Tag("tag:yaml.org,2002:org.bukkit.inventory.ItemStack"), this.yamlConstructors.get(Tag.MAP));
        }
    }
    private void migrateDatabase() {
        if(!ChestShop.db.itemListIsEmpty()) return;
        Dao<Item, Integer> dao = null;
        List<Item> itemList = null;
        try {
            dao = DaoCreator.getDao(Item.class);
            if(!dao.isTableExists()) return;
            itemList = dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(itemList == null || itemList.isEmpty()) return;
        Map<Integer, String> itemStackMap = new HashMap<>();
        for(Item item : itemList) {
            if(item == null) continue;
            int itemID = item.getId();
            ItemStack is = null;
            try {
                is = this.yaml.loadAs((String)Base64.decodeToObject(item.getBase64ItemCode()), ItemStack.class);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            if(is == null) continue;
            itemStackMap.put(itemID, de.joo.chestshop.ids.ItemManager.getItemCode(is));
        }
        ChestShop.db.addAllItems(itemStackMap, e -> {
            if(e!=null) {
                ChestShop.log.severe("Fehler bei Migration!");
                e.printStackTrace();
            }
        });
    }

    private void disableOldListener() {
        HandlerList.unregisterAll(com.Acrobot.ChestShop.ChestShop.getPlugin());
    }

    public void handleOldSign(PlayerInteractEvent event) {
        Sign sign = (Sign)event.getClickedBlock();
        if(!ChestShopSign.isValid(sign)) return;
        Player player = event.getPlayer();
        player.sendMessage("Altes ChestShop Schild gefunden. Migration wird durchgeführt.");
        String name = sign.getLine(0);
        String quantity = sign.getLine(1);
        String prices = sign.getLine(2);
        String material = sign.getLine(3);
        Account account = NameManager.getAccountFromShortName(name);
        UUID uuid = account.getUuid();
        de.joo.chestshop.ids.NameManager.getBase62ForSign(uuid, s -> {
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                sign.setLine(0, s + " " + account.getName());
                sign.setLine(1, quantity);
                sign.setLine(2, prices);
                sign.setLine(3, material);
                sign.update();
            });

        });
    }
}
