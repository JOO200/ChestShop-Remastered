package de.joo.chestshop.listener.block;


import de.exlll.asynclib.task.AsyncTask;
import de.exlll.asynclib.task.AsyncTasks;
import de.joo.chestshop.events.ShopCreationEvent;
import de.joo.chestshop.events.ShopPreCreationEvent;
import de.joo.chestshop.executor.PreShopCreation;
import de.joo.chestshop.ids.ItemManager;
import de.joo.chestshop.ids.NameManager;
import de.joo.chestshop.models.ShopCreation;
import de.joo.chestshop.plugin.ChestShop;
import de.joo.chestshop.signs.ChestShopSign;
import de.joo.chestshop.util.BlockUtil;
import de.joo.chestshop.util.ChestFinder;
import de.joo.chestshop.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;

/**
 * @author Acrobot
 */
public class BlockPlace implements Listener {

    private ChestShop plugin;

    public BlockPlace(ChestShop plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlaceAgainstSign(BlockPlaceEvent event) {
        Block against = event.getBlockAgainst();

        if (!ChestShopSign.isValid(against)) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        Block signBlock = event.getBlock();
        String[] line = StringUtil.stripColourCodes(event.getLines());

        if (!BlockUtil.isSign(signBlock)) {
            return;
        }

        if (!ChestShopSign.isValidPreparedSign(line)) {
            return;
        }
        Sign sign = (Sign)event.getBlock().getState();
        Chest chest = ChestFinder.findConnectedChest(sign);

        ShopPreCreationEvent preEvent = new ShopPreCreationEvent(sign, event.getPlayer(), chest);
        Bukkit.getPluginManager().callEvent(preEvent);
        if(preEvent.isCancelled()) {
            preEvent.getSign().getBlock().breakNaturally();
            return;
        }

        ShopCreation sC = new ShopCreation(event.getPlayer(), chest, sign, event.getLines());

        // ShopCreaton has to be async but Spigot dont support asynch events fired from synch events.
        PreShopCreation preShopCreation = new PreShopCreation(sC, plugin);

        AsyncTask task = AsyncTasks.newAsyncTask(preShopCreation, () -> {
            sC.updateSign();
            ShopCreationEvent postEvent = new ShopCreationEvent(
                    sC.getPlayer(), sC.getSign(),
                    sC.getChest(), sC.getSignLines());
            Bukkit.getPluginManager().callEvent(postEvent);
        });
        ChestShop.asynService.execute(task);
    }
}
