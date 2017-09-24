package de.joo.chestshop.listener.player;

import de.joo.chestshop.events.TransactionEvent;
import de.joo.chestshop.executor.PreTransaction;
import de.joo.chestshop.migration.MigrationManager;
import de.joo.chestshop.models.Transaction;
import de.joo.chestshop.plugin.ChestShop;
import de.joo.chestshop.signs.ChestShopSign;
import de.joo.chestshop.transaction.TransactionOutcome;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;
import java.util.function.Consumer;

import static de.joo.chestshop.util.BlockUtil.isSign;
import static org.bukkit.event.block.Action.LEFT_CLICK_BLOCK;
import static org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK;

/**
 * @author Acrobot
 */
public class PlayerInteract implements Listener {

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public static void onInteract(PlayerInteractEvent event)
    {
        Block block = event.getClickedBlock();
        if (block == null)
            return;

        Action action = event.getAction();
        Player player = event.getPlayer();

        if (!isSign(block) || player.getInventory().getItemInMainHand().getType() == Material.SIGN) // Blocking accidental sign edition
            return;

        Sign sign = (Sign) block.getState();
        if(ChestShop.migration != null && MigrationManager.isOldSign(sign)) {
            ChestShop.migration.handleOldSign(event);
            return;
        }
        if (!ChestShopSign.isValid(sign)) {
            System.out.println("Ungültig");
            return;
        }

        if (action == RIGHT_CLICK_BLOCK) {
            event.setCancelled(true);
        }

        //Bukkit.getLogger().info("ChestShop - DEBUG - "+block.getWorld().getName()+": "+block.getLocation().getBlockX()+", "+block.getLocation().getBlockY()+", "+block.getLocation().getBlockZ());
        Transaction transaction = new Transaction(sign, player, action);
        new PreTransaction(transaction, b -> {
            if(!b || transaction.getOutcome() != TransactionOutcome.TRANSACTION_SUCCESFUL) {
                System.out.println("Fehler: " + transaction.getOutcome().name());
                return;
            }
            player.getInventory().addItem(transaction.getStock());
        });

/*
        Bukkit.getPluginManager().callEvent(pEvent);
        if (pEvent.isCancelled())
            return;

        TransactionEvent tEvent = new TransactionEvent(pEvent, sign);
        Bukkit.getPluginManager().callEvent(tEvent);*/
    }

    /*
        Action buy = Properties.REVERSE_BUTTONS ? LEFT_CLICK_BLOCK : RIGHT_CLICK_BLOCK;
        double price = (action == buy ? PriceUtil.getBuyPrice(prices) : PriceUtil.getSellPrice(prices));

        Chest chest = uBlock.findConnectedChest(sign);
        Inventory ownerInventory = (ChestShopSign.isAdminShop(sign) ? new AdminInventory() : chest != null ? chest.getInventory() : null);

        ItemStack item = MaterialUtil.getItem(material);
        if (item == null || !NumberUtil.isInteger(quantity)) {
            player.sendMessage(Messages.prefix(Messages.INVALID_SHOP_DETECTED));
            return null;
        */

    /*

        if (Properties.SHIFT_SELLS_IN_STACKS && player.isSneaking() && price != PriceUtil.NO_PRICE && isAllowedForShift(action == buy)) {
            int newAmount = getStackAmount(item, ownerInventory, player, action);
            if (newAmount > 0) {
                price = (price / amount) * newAmount;
                amount = newAmount;
            }
        }

        item.setAmount(amount);

        ItemStack[] items = {item};

        TransactionType transactionType = (action == buy ? BUY : SELL);
        return new PreTransactionEvent(ownerInventory, player.getInventory(), items, price, player, owner, sign, transactionType);
    }

    private static boolean isAllowedForShift(boolean buyTransaction) {
        String allowed = Properties.SHIFT_ALLOWS;

        if (allowed.equalsIgnoreCase("ALL")) {
            return true;
        }

        return allowed.equalsIgnoreCase(buyTransaction ? "BUY" : "SELL");
    }

    private static int getStackAmount(ItemStack item, Inventory inventory, Player player, Action action) {
        Action buy = Properties.REVERSE_BUTTONS ? LEFT_CLICK_BLOCK : RIGHT_CLICK_BLOCK;
        Inventory checkedInventory = (action == buy ? inventory : player.getInventory());

        if (checkedInventory.containsAtLeast(item, item.getMaxStackSize())) {
            return item.getMaxStackSize();
        } else {
            return InventoryUtil.getAmount(item, checkedInventory);
        }
    }

    public static boolean canOpenOtherShops(Player player) {
        return Permission.has(player, Permission.ADMIN) || Permission.has(player, Permission.MOD);
    }

    private static void showChestGUI(Player player, Block signBlock) {
        Chest chest = uBlock.findConnectedChest(signBlock);

        if (chest == null) {
            player.sendMessage(Messages.prefix(Messages.NO_CHEST_DETECTED));
            return;
        }

        if (!canOpenOtherShops(player) && !Security.canAccess(player, signBlock)) {
            return;
        }

        BlockUtil.openBlockGUI(chest, player);
    }*/
}
