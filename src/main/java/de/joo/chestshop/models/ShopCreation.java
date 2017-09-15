package de.joo.chestshop.models;

import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

/**
 * Created by Johannes on 06.06.2017.
 */
public class ShopCreation {
    private static final HandlerList handlers = new HandlerList();
    private CreationOutcome outcome = CreationOutcome.SUCCESS;

    private Player creator;
    private Sign sign;
    private String[] signLines;
    private Chest chest;

    private boolean nameChecked = false, itemChecked = false;

    public ShopCreation(Player creator, Chest chest, Sign sign, String[] lines) {
        this.creator = creator;
        this.sign = sign;
        this.chest = chest;
        this.signLines = lines;
    }

    public void setNameChecked() {
        nameChecked = true;
    }

    public void setItemChecked() {
        itemChecked = true;
    }

    public boolean isItemChecked() {
        return itemChecked;
    }

    public boolean isNameChecked() {
        return nameChecked;
    }

    public boolean isCancelled() {
        return outcome != CreationOutcome.SUCCESS;
    }

    // please use "setOutcome(State state) instead
    @Deprecated
    public void setCancelled(boolean b) {
        outcome = CreationOutcome.OTHER;
    }

    public void setOutcome(CreationOutcome outcome) {
        this.outcome = outcome;
    }

    public CreationOutcome getOutcome() { return this.outcome; }

    public Player getPlayer() {
        return creator;
    }

    public void setChest(Chest chest) {
        this.chest = chest;
    }

    public Chest getChest() {
        return chest;
    }

    public String[] getSignLines() {
        return signLines;
    }

    public void setSignLines(String[] signLines) {
        this.signLines = signLines;
    }

    public void setSignLine(String text, byte line) {
        this.signLines[line] = text;
    }

    public Sign getSign() {
        return sign;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public enum CreationOutcome {
        SUCCESS,
        NO_CHEST,
        SIGN_ERROR,
        INVALID_PLAYER,
        NO_PERMISSION,
        WORLD_GUARD,
        OTHER, ID_ERROR;
    }
}
