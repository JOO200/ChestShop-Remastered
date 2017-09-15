package de.joo.chestshop.events;

import de.joo.chestshop.transaction.TransactionOutcome;
import de.joo.chestshop.transaction.TransactionType;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * Created by Johannes on 25.04.2017.
 */
public class TransactionEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private final TransactionType type;
    private TransactionOutcome outcome = TransactionOutcome.TRANSACTION_SUCCESFUL;

    private final Inventory ownerInventory, clientInventory;
    private final Player client;
    private final UUID owner;
    private final ItemStack[] stock;
    private final double price;
    private final Sign sign;

    public TransactionEvent(TransactionType type, Inventory ownerInventory, Inventory clientInventory, Player client, UUID owner,
                            ItemStack[] stock, double price, Sign sign) {
        super(true);
        this.type = type;
        this.ownerInventory = ownerInventory;
        this.clientInventory = clientInventory;
        this.client = client;
        this.owner = owner;
        this.stock = stock;
        this.price = price;
        this.sign = sign;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public double getPrice() {
        return price;
    }

    public Inventory getClientInventory() {
        return clientInventory;
    }

    public Inventory getOwnerInventory() {
        return ownerInventory;
    }

    public ItemStack[] getStock() {
        return stock;
    }

    public Player getClient() {
        return client;
    }

    public Sign getSign() {
        return sign;
    }

    public TransactionType getType() {
        return type;
    }

    public UUID getOwner() {
        return owner;
    }

    @Override
    public boolean isCancelled() {
        return outcome != TransactionOutcome.TRANSACTION_SUCCESFUL;
    }

    @Deprecated
    @Override
    public void setCancelled(boolean b) {
        if(b) {
            outcome = TransactionOutcome.OTHER;
        } else {
            outcome = TransactionOutcome.TRANSACTION_SUCCESFUL;
        }
    }

    public TransactionOutcome getOutcome() {
        return outcome;
    }

    public void setOutcome(TransactionOutcome outcome) {
        this.outcome = outcome;
    }


    public static HandlerList getHandlerList() {
        return handlers;
    }
}
