package de.joo.chestshop.models;

import de.joo.chestshop.transaction.TransactionOutcome;
import de.joo.chestshop.transaction.TransactionType;
import de.joo.chestshop.util.ChestFinder;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * Created by Johannes on 08.06.2017.
 */
public class Transaction {
    private TransactionOutcome outcome = TransactionOutcome.TRANSACTION_SUCCESFUL;

    private TransactionType type;

    private Inventory chest;
    private Inventory clientInv;

    private Player client;
    private UUID owner;

    private ItemStack[] stock;
    private double price;

    private Sign sign;

    public Transaction(Sign sign, Player player, TransactionType type, UUID owner, ItemStack[] stock, double price) {
        this.sign = sign;
        this.client = player;
        this.clientInv = player.getInventory();
        this.chest = ChestFinder.findConnectedChest(sign).getBlockInventory();
        this.type = type;
        this.stock = stock;
        this.owner = owner;
        this.price = price;
    }

    public Transaction(Sign sign, Player player, Action action) {
        this.type = action==Action.LEFT_CLICK_BLOCK || action ==Action.LEFT_CLICK_AIR ? TransactionType.SELL : TransactionType.BUY;
        this.sign = sign;
        this.client = player;
        this.clientInv = player.getInventory();
        this.chest = ChestFinder.findConnectedChest(sign).getBlockInventory();
    }

    public TransactionOutcome getOutcome() {
        return outcome;
    }

    public Sign getSign() {
        return sign;
    }

    public double getPrice() {
        return price;
    }

    public Inventory getChest() {
        return chest;
    }

    public Inventory getClientInv() {
        return clientInv;
    }

    public ItemStack[] getStock() {
        return stock;
    }

    public Player getClient() {
        return client;
    }

    public TransactionType getType() {
        return type;
    }

    public UUID getOwner() {
        return owner;
    }

    public void setOutcome(TransactionOutcome outcome) {
        this.outcome = outcome;
    }

    public void setChest(Inventory chest) {
        this.chest = chest;
    }

    public void setClient(Player client) {
        this.client = client;
    }

    public void setClientInv(Inventory clientInv) {
        this.clientInv = clientInv;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setSign(Sign sign) {
        this.sign = sign;
    }

    public void setStock(ItemStack[] stock) {
        this.stock = stock;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }
}
