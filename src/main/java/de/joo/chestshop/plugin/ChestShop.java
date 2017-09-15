package de.joo.chestshop.plugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import de.joo.chestshop.config.GeneralConfig;
import de.joo.chestshop.db.DatabaseController;
import de.joo.chestshop.listener.block.BlockPlace;
import de.joo.chestshop.listener.player.PlayerInteract;
import de.joo.chestshop.listener.shopcreation.WorldGuardListener;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Johannes on 25.04.2017.
 *
 *
 * This file is part of JooChestShop.
 * Copyright (C) 2017
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */
public class ChestShop extends JavaPlugin {
    public final static byte NAME_LINE = 0;
    public final static byte AMOUNT_LINE = 1;
    public final static byte PRICE_LINE = 2;
    public final static byte ITEM_LINE = 3;

    public static Economy economy;

    public static GeneralConfig generalConfig;

    public static DatabaseController db;
    public static Logger log;

    private boolean getWorldGuard() {
        Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");
        return !(plugin == null || !(plugin instanceof WorldGuardPlugin));
    }

    public void onEnable() {
        log = getLogger();
        generalConfig = new GeneralConfig((Paths.get(getDataFolder()+"/config.yml")));
        try {
            generalConfig.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(!setupEconomy()) {
            log.severe("Vault wurde nicht gefunden. Deaktiviere Plugin");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        db = new DatabaseController(this);
        if(!getWorldGuard()) {
            log.warning("WorldGuard wurde nicht gefunden. Worldguard-Funktionen werden nicht genutzt.");
        } else {
            log.info("WorldGuard gefunden und geladen.");
            getServer().getPluginManager().registerEvents(new WorldGuardListener(), this);
        }
        //if(generalConfig.isWorldGuard_Integration()) {
            new WorldGuardSupport();
        //}
        getServer().getPluginManager().registerEvents(new BlockPlace(this), this);
        getServer().getPluginManager().registerEvents(new PlayerInteract(), this);
    }

    public static List<String> getAdminShops() {
        return Collections.singletonList("AdminShop");
    }

    private boolean setupEconomy()
    {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }
}
