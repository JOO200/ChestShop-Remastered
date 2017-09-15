package de.joo.chestshop.transaction;

/**
 * Created by Johannes on 06.06.2017.
 */
public enum TransactionOutcome {
    SHOP_DOES_NOT_BUY_THIS_ITEM,
    SHOP_DOES_NOT_SELL_THIS_ITEM,

    CLIENT_DOES_NOT_HAVE_PERMISSION,

    CLIENT_DOES_NOT_HAVE_ENOUGH_MONEY,
    SHOP_DOES_NOT_HAVE_ENOUGH_MONEY,

    CLIENT_DEPOSIT_FAILED,
    SHOP_DEPOSIT_FAILED,

    NOT_ENOUGH_SPACE_IN_CHEST,
    NOT_ENOUGH_SPACE_IN_INVENTORY,

    NOT_ENOUGH_STOCK_IN_CHEST,
    NOT_ENOUGH_STOCK_IN_INVENTORY,

    INVALID_SHOP,

    SPAM_CLICKING_PROTECTION,
    CREATIVE_MODE_PROTECTION,
    SHOP_IS_RESTRICTED,

    ID_ERROR,

    OTHER, //For plugin use!

    TRANSACTION_SUCCESFUL
}