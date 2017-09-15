package de.joo.chestshop.config;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;

import java.nio.file.Path;

/**
 * Created by Johannes on 18.08.2017.
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
public class GeneralConfig extends Configuration {

    @Comment({"General Configuration for ChestShop from JOO200.", "",
            "How often can a player use the shop sign? (in ms)"
    })
    private int Shop_Interaction_Interval = 250;
    @Comment("Do you want to allow using shops to people in creative mode?")
    private boolean Ignore_Creative_Mode = true;
    @Comment("Reverse the left and right mouse button: left-click = buy and right-click = sell")
    private boolean Reverse_Buttons = false;
    @Comment("If true, people will be able to buy/sell stacks of the item while holding the crouch button")
    private boolean Shift_Sells_In_Stacks = false;
    @Comment("What can you do by clicking shift with Shift_Sells_In_Stacks enabled? (ALL/BUY/SELL)")
    private String Shift_Allows = "ALL";
    @Comment("Can shop's chest be opened by owner with right-clicking a shop's sign?")
    private boolean Allow_Sign_Chest_Open = true;
    @Comment("If true, when you left-click your own shop sign you won't open chest's inventory but instead you will start destroying the sign.")
    private boolean Allow_Left_Click_Destroying = true;

    @Comment("If true, if the shop is empty, the sign is destroyed and put into the chest, so the shop isn't usable anymore.")
    private boolean Remove_Empty_Shops = false;
    @Comment("If true, if the Remove_Empty_Shops option is turned on, the chest is also destroyed.")
    private boolean Remove_Empty_Chests = false;

    @Comment("First line of your Admin Shop's sign should look like this:")
    private String Admin_Shop_Name = "Admin Shop";
    @Comment("The economy account which Admin Shops should use and to which all taxes will go.")
    private String Server_Economy_Account = "";
    @Comment("Percent of the price that hould go to the server's account. (100 = 100 percent)")
    private int Tax_Amount = 0;
    @Comment("Percent of the price that hould go to the server's account when buying from an Admin Shop.")
    private int Server_Tax_Amount = 0;
    @Comment("Amound of money payer must pay to create a shop")
    private double Shop_Creation_Price = 0.0;
    @Comment("How much money do you get back when destroying a shop sign?")
    private double Shop_Refund_Price = 0.0;

    @Comment("Should we block shops that sell things for more than they buy? (This prevents newbies from creating shops that would be exploited)")
    private boolean Block_Shops_Higher_Sell_Than_Buy = true;

    @Comment("Can shops be used even when the seller doesn't have enough items? (The price will be scaled adequatly to the item amount)")
    private boolean Allow_Partial_Transactions = true;
    @Comment("Can '?' be put in place of item name in order for the sign to be auto-filled?")
    private boolean Allow_Auto_Item_Fill = true;

    @Comment("If true, plugin will log transactions in its own file.")
    private boolean Log_To_File = false;
    @Comment("If true, plugin will log transactions to database.")
    private boolean Log_Transaction_To_Database = false;
    @Comment("If true, plugin will log shop creations to database.")
    private boolean Log_Shops_To_Database = false;
    @Comment("If true, plugin will show transactions in console.")
    private boolean Log_To_Console = true;

    @Comment("Do you want to stack all items up to 64 item stacks?")
    private boolean Stack_To_64 = false;
    @Comment("Enable LWC?")
    private boolean Enable_LWC = true;
    @Comment("Do you want to only let people build inside regions?")
    private boolean WorldGuard_Integration = false;
    @Comment("Do you want to only let people build inside flagged by doing /region <Name> flag chestshop allow?")
    private boolean WorldGuard_Use_Flag = true;


    public GeneralConfig(Path configPath) {
        super(configPath);
    }

    public double getShop_Creation_Price() {
        return Shop_Creation_Price;
    }

    public double getShop_Refund_Price() {
        return Shop_Refund_Price;
    }

    public int getServer_Tax_Amount() {
        return Server_Tax_Amount;
    }

    public int getShop_Interaction_Interval() {
        return Shop_Interaction_Interval;
    }

    public int getTax_Amount() {
        return Tax_Amount;
    }

    public String getAdmin_Shop_Name() {
        return Admin_Shop_Name;
    }

    public String getServer_Economy_Account() {
        return Server_Economy_Account;
    }

    public String getShift_Allows() {
        return Shift_Allows;
    }

    public boolean isAllow_Left_Click_Destroying() {
        return Allow_Left_Click_Destroying;
    }

    public boolean isAllow_Sign_Chest_Open() {
        return Allow_Sign_Chest_Open;
    }

    public boolean isBlock_Shops_Higher_Sell_Than_Buy() {
        return Block_Shops_Higher_Sell_Than_Buy;
    }

    public boolean isIgnore_Creative_Mode() {
        return Ignore_Creative_Mode;
    }

    public boolean isLog_Shops_To_Database() {
        return Log_Shops_To_Database;
    }

    public boolean isLog_To_Console() {
        return Log_To_Console;
    }

    public boolean isLog_To_File() {
        return Log_To_File;
    }

    public boolean isLog_Transaction_To_Database() {
        return Log_Transaction_To_Database;
    }

    public boolean isWorldGuard_Integration() {
        return WorldGuard_Integration;
    }

    public boolean isWorldGuard_Use_Flag() {
        return WorldGuard_Use_Flag;
    }

    public boolean isAllow_Auto_Item_Fill() {
        return Allow_Auto_Item_Fill;
    }

    public boolean isAllow_Partial_Transactions() {
        return Allow_Partial_Transactions;
    }

    public boolean isEnable_LWC() {
        return Enable_LWC;
    }

    public boolean isRemove_Empty_Chests() {
        return Remove_Empty_Chests;
    }

    public boolean isRemove_Empty_Shops() {
        return Remove_Empty_Shops;
    }

    public boolean isReverse_Buttons() {
        return Reverse_Buttons;
    }

    public boolean isShift_Sells_In_Stacks() {
        return Shift_Sells_In_Stacks;
    }

    public boolean isStack_To_64() {
        return Stack_To_64;
    }
}
