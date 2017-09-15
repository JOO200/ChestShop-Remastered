package de.joo.chestshop.listener.player;


import org.bukkit.event.Listener;

/**
 * @author Acrobot
 */
public class PlayerInventory implements Listener {
    /*@EventHandler
    public static void onInventoryOpen(InventoryOpenEvent event) {
        if (event.getInventory().getType() != InventoryType.CHEST) {
            return;
        }

        if (!Properties.TURN_OFF_DEFAULT_PROTECTION_WHEN_PROTECTED_EXTERNALLY) {
            return;
        }

        HumanEntity entity = event.getPlayer();

        if (!(entity instanceof Player) || (!(event.getInventory().getHolder() instanceof Chest) && !(event.getInventory().getHolder() instanceof DoubleChest))) {
            return;
        }

        Player player = (Player) entity;
        Block chest;

        if (event.getInventory().getHolder() instanceof Chest) {
            chest = ((BlockState) event.getInventory().getHolder()).getBlock();
        } else {
            chest = ((DoubleChest) event.getInventory().getHolder()).getLocation().getBlock();
        }

        if (!PlayerInteract.canOpenOtherShops(player) && !ChestShop.canAccess(player, chest)) {
            player.sendMessage(Messages.prefix(Messages.ACCESS_DENIED));
            event.setCancelled(true);
        }
    }*/
}
