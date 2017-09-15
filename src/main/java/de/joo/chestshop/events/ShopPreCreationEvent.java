package de.joo.chestshop.events;

import de.joo.chestshop.models.ShopCreation;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by Johannes on 13.06.2017.
 */
public class ShopPreCreationEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private Sign sign;
    private Player creator;
    private Chest chest;

    private boolean cancelled = false;

    private ShopCreation.CreationOutcome outcome = ShopCreation.CreationOutcome.SUCCESS;

    public ShopPreCreationEvent(Sign sign, Player creator, Chest chest) {
        this.sign = sign;
        this.creator = creator;
        this.chest = chest;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return outcome != ShopCreation.CreationOutcome.SUCCESS;
    }

    /*
     * Use setOutcome!
     */
    @Deprecated
    @Override
    public void setCancelled(boolean b) {
        if(b) {
            this.outcome = ShopCreation.CreationOutcome.OTHER;
        } else {
            this.outcome = ShopCreation.CreationOutcome.SUCCESS;
        }
    }

    public void setOutcome(ShopCreation.CreationOutcome outcome) {
        this.outcome = outcome;
    }

    public ShopCreation.CreationOutcome getOutcome() {
        return outcome;
    }

    public Sign getSign() {
        return sign;
    }

    public Player getCreator() {
        return creator;
    }

    public Chest getChest() {
        return chest;
    }
}
