package de.joo.chestshop.executor;

import de.exlll.asynclib.task.AsyncTask;
import de.joo.chestshop.ids.ItemManager;
import de.joo.chestshop.ids.NameManager;
import de.joo.chestshop.models.Transaction;
import de.joo.chestshop.plugin.ChestShop;
import de.joo.chestshop.transaction.TransactionOutcome;
import de.joo.chestshop.transaction.TransactionType;
import de.joo.chestshop.util.PriceUtil;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.function.Consumer;

/**
 * Created by Johannes on 08.06.2017.
 */
public class PreTransaction {

    public PreTransaction(Transaction transaction, Consumer<Boolean> callback) {
        Sign sign = transaction.getSign();
        String nameLine = sign.getLine(ChestShop.NAME_LINE);
        String priceLine = sign.getLine(ChestShop.PRICE_LINE);
        String itemLine = sign.getLine(ChestShop.ITEM_LINE);

        transaction.setPrice((transaction.getType() == TransactionType.BUY ? PriceUtil.getBuyPrice(priceLine) : PriceUtil.getSellPrice(priceLine)));

        int amount = Integer.parseInt(sign.getLine(ChestShop.AMOUNT_LINE));

        //TODO: Adminshop?!
        System.out.println("Transaction gestartet.");

        NameManager.getUUIDFromSign(nameLine, uuid -> {
            if(uuid == null) {
                transaction.setOutcome(TransactionOutcome.ID_ERROR);
                callback.accept(false);
                return;
            }
            transaction.setOwner(uuid);
            if(transaction.getStock() != null) {
                callback.accept(true);
            }
        });

        ItemManager.getFromSignLine(itemLine, itemStack -> {
            if(itemStack == null) {
                transaction.setOutcome(TransactionOutcome.ID_ERROR);
                callback.accept(false);
                return;
            }
            itemStack.setAmount(amount);
            transaction.setStock(new ItemStack[]{itemStack});
            if(transaction.getOwner() != null) {
                callback.accept(true);
            }
        });
    }
}
