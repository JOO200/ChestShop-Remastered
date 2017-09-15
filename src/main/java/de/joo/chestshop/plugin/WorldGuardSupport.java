package de.joo.chestshop.plugin;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.flags.*;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import org.bukkit.Bukkit;

import javax.annotation.Nullable;

/**
 * Created by Johannes on 21.08.2017.
 * *
 * *
 * This file is part of JooChestShop.
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
public class WorldGuardSupport {
    //TODO: Config abfrage ob shop-creation und shop-use standardmäßig auf true sein soll.
    public static final RegionGroupFlag SHOP_USE_FLAG = new RegionGroupFlag("shop-use", RegionGroup.ALL);
    public static final RegionGroupFlag SHOP_CREATION_FLAG = new RegionGroupFlag("shop-creation", RegionGroup.MEMBERS);


    public WorldGuardSupport() {
        FlagRegistry registry = WGBukkit.getPlugin().getFlagRegistry();
        ChestShop.log.info("Add WorldGuard-Flag " + SHOP_CREATION_FLAG.getName());
        try {
            registry.register(SHOP_CREATION_FLAG);
        } catch (FlagConflictException e1) {
            ChestShop.log.warning("Die Flag " + SHOP_CREATION_FLAG.getName() + " ist bereits definiert!");
        }
        ChestShop.log.info("Add WorldGuard-Flag " + SHOP_USE_FLAG.getName());
        try {
            registry.register(SHOP_USE_FLAG);
        } catch (FlagConflictException e2) {
            ChestShop.log.warning("Die Flag " + SHOP_USE_FLAG.getName() + " ist bereits definiert!");
        }
    }
}
