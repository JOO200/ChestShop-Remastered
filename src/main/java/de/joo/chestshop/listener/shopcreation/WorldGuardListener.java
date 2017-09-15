package de.joo.chestshop.listener.shopcreation;

import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.RegionGroup;
import com.sk89q.worldguard.protection.flags.RegionGroupFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.joo.chestshop.events.ShopPreCreationEvent;
import de.joo.chestshop.models.ShopCreation;
import de.joo.chestshop.plugin.ChestShop;
import de.joo.chestshop.plugin.WorldGuardSupport;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * Created by Johannes on 13.06.2017.
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
 *
 */
public class WorldGuardListener implements Listener {
    private WorldGuardPlugin worldGuard = WGBukkit.getPlugin();


    @EventHandler
    public void onShopPreCreation(ShopPreCreationEvent event) {
        if(event.isCancelled()) return;
        if(!ChestShop.generalConfig.isWorldGuard_Use_Flag()) return;
        Sign sign = event.getSign();
        RegionContainer container = worldGuard.getRegionContainer();
        if(container == null) return;
        RegionManager regionManager = container.get(sign.getBlock().getWorld());
        if(regionManager == null) return;
        LocalPlayer wgPlayer = worldGuard.wrapPlayer(event.getCreator());
        ApplicableRegionSet set = regionManager.getApplicableRegions(sign.getLocation());
        if(set.size() > 0) {
            RegionGroup regionGroup = set.queryValue(wgPlayer, WorldGuardSupport.SHOP_CREATION_FLAG);
            if(!RegionGroupFlag.isMember(set, regionGroup, wgPlayer)) {
                event.setOutcome(ShopCreation.CreationOutcome.WORLD_GUARD);
                event.getCreator().sendMessage("Fehler: WorldGuard fehler!");
            };
        }
    }
}
