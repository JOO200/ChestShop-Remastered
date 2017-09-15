package de.joo.chestshop.events;

import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Represents a state after shop creation
 *
 * @author Acrobot
 */
public class ShopCreationEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final Player creator;

    private final Sign sign;
    private final String[] signLines;
    private final Chest chest;

    private boolean sendMessage = true;

    public ShopCreationEvent(Player creator, Sign sign, Chest chest, String[] signLines) {
        this.creator = creator;
        this.sign = sign;
        this.chest = chest;
        this.signLines = signLines.clone();
    }

    /**
     * Returns the text on the sign
     *
     * @param line Line number (0-3)
     * @return Text on the sign
     */
    public String getSignLine(short line) {
        return signLines[line];
    }

    /**
     * Returns the text on the sign
     *
     * @return Text on the sign
     */
    public String[] getSignLines() {
        return signLines;
    }

    /**
     * Returns the shop's creator
     *
     * @return Shop's creator
     */
    public Player getPlayer() {
        return creator;
    }

    /**
     * Returns the shop's sign
     *
     * @return Shop's sign
     */
    public Sign getSign() {
        return sign;
    }

    /**
     * Returns the shop's chest (if applicable)
     *
     * @return Shop's chest
     */
    public Chest getChest() {
        return chest;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public boolean sendMessage() {
        return sendMessage;
    }

    public void setSendMessage(boolean b) {
        sendMessage = b;
    }
}
