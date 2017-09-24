package de.joo.chestshop.executor;

import de.exlll.asynclib.task.AsyncTask;
import de.joo.chestshop.events.ShopCreationEvent;
import de.joo.chestshop.ids.ItemManager;
import de.joo.chestshop.ids.NameManager;
import de.joo.chestshop.models.ShopCreation;
import de.joo.chestshop.plugin.ChestShop;
import de.joo.chestshop.plugin.PermissionHelper;
import de.joo.chestshop.util.ChestFinder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.function.Consumer;

/**
 * Created by Johannes on 06.06.2017.
 */
public class PreShopCreation implements Runnable {
    ShopCreation creation;
    ChestShop plugin;

    public PreShopCreation(ShopCreation creation, ChestShop plugin){
        this.creation = creation;
        this.plugin = plugin;
    }

    @Override
    public void run() {
        if(checkCloseChest(creation) != ShopCreation.CreationOutcome.SUCCESS) {
            creation.setOutcome(ShopCreation.CreationOutcome.NO_CHEST);
            return;
        }

        Callable<String> itemReplace = () -> replaceItem(creation);
        Callable<String> nameReplace = () -> replaceName(creation);
        ExecutorService service = Executors.newFixedThreadPool(2);
        Future<String> name = service.submit(nameReplace);
        Future<String> item = service.submit(itemReplace);

        try {
            creation.setSignLine(name.get(), ChestShop.NAME_LINE);
            creation.setSignLine(item.get(), ChestShop.ITEM_LINE);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        service.shutdown();
    }

    private static String replaceName(ShopCreation creation) {
        String string = NameManager.getBase62ForSignSync(creation.getPlayer().getUniqueId());
        if(string.isEmpty() || string.equals("-1")) {
            return null;
        }
        return string + " " + creation.getPlayer().getName();
    }

    private static String replaceItem(ShopCreation creation) {
        String itemLine = creation.getSignLines()[ChestShop.ITEM_LINE];
        if(itemLine.equals("?")) {
            Optional<ItemStack> item = Arrays.stream(creation.getChest().getInventory().getContents()).
                    filter( itemStack ->  itemStack != null && itemStack.getData() != null  )
                    .findFirst();
            if(item.isPresent()) {
                return ItemManager.getSignLineFromItemSync(item.get());
            }
        }

        Material mat = Material.getMaterial(itemLine);
        if(mat != null) {
            return itemLine;
        }
        creation.getPlayer().sendMessage("Fehler - Item unbekannt");
        return null;
    }

    private ShopCreation.CreationOutcome checkCloseChest(ShopCreation creation) {
        if(ChestShop.getAdminShops().contains(creation.getSignLines()[ChestShop.NAME_LINE]))
            return ShopCreation.CreationOutcome.SUCCESS;

        Chest connectedChest = ChestFinder.findConnectedChest(creation.getSign().getBlock());

        if (connectedChest == null) {
            return ShopCreation.CreationOutcome.NO_CHEST;
        }

        Player player = creation.getPlayer();
        if(PermissionHelper.has(player, PermissionHelper.Permission.ADMIN_ACCESS)) {
            return ShopCreation.CreationOutcome.SUCCESS;
        }

        if(PermissionHelper.has(player, PermissionHelper.Permission.PLAYER_CREATE_SHOP)) {
            return ShopCreation.CreationOutcome.SUCCESS;
        }
        return ShopCreation.CreationOutcome.NO_PERMISSION;
    }
}
