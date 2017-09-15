package de.joo.chestshop.ids;

import de.exlll.asynclib.task.AsyncTask;
import de.joo.chestshop.plugin.ChestShop;
import de.joo.chestshop.util.ItemUtil;
import de.joo.chestshop.util.encoding.Base62;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Logger;

/**
 * Created by Johannes on 07.06.2017.
 */
public class ItemManager {
    private static Logger log = ChestShop.log;
    private static ItemUtil util = new ItemUtil();

    private static Map<String, Integer> base64ToID = new HashMap<>();
    private static Map<Integer, String> idToBase64 = new HashMap<>();

    private static Map<ItemStack, String> isToSignLine = new HashMap<>();
    private static Map<String, ItemStack> signLineToIS = new HashMap<>();

    /**
     * Gibt das Base64-codierte Itemstack als String zurück. (Umwandlung Itemstack - String)
     *
     * @param item ItemStack zum konvertieren
     * @return Base64-codiertes Item
     */
    public static String getItemCode(ItemStack item) {
        return util.getItemCode(item);
    }

    /**
     * Gibt vom Base64-codierten Itemstack das ItemStack zurück. (Umwandlung String - Itemstack)
     *
     * @param code String zum konvertieren
     * @return ItemStack
     */
    public static ItemStack getFromCode(String code) {
        return util.getFromCode(code);

    }

    public static int getIDfromItemStack(ItemStack item) {
        String string = getItemCode(item);
        return getIDfromBase64(string);
    }

    public static int getIDfromBase64(String string) {
        if(string == null) {
            return -1;
        }
        //if(base64ToID.containsKey(string)) {
        //    return base64ToID.get(string);
        //}

        int integer = -1;
        try {
            integer = ChestShop.db.getItemIDfromCodeSync(string);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        //base64ToID.put(string, integer);
        //idToBase64.put(integer, string);
        return integer;
    }

    public static void getBase64FromID(int id, Consumer<String> callback) {
        if(id < 1) {
            callback.accept(null);
            return;
        }
        if(idToBase64.containsKey(id)) {
            callback.accept(idToBase64.get(id));
            return;
        }
        ChestShop.db.getCodeFromItemID(id, (string, e) -> {
            if(e != null) {
                log.warning("Fehler bei Verbindung zu Datenbank!");
                e.printStackTrace();
                callback.accept(null);
                return;
            }
            callback.accept(string);
            base64ToID.put(string, id);
            idToBase64.put(id, string);
        });
    }

    public static String getSignLineFromItemSync(ItemStack is) {
        //if(isToSignLine.containsKey(is)) {
        //    return isToSignLine.get(is);
        //}
        String itemName = getSignLineFromMaterial(is.getType());
        Material mat = Material.getMaterial(toUpperCase(itemName));
        if(mat != null && itemName.length() <= 15) {
            ItemStack temp = new ItemStack(mat);
            if (is.equals(temp)) {
                //isToSignLine.put(is, itemName);
                ChestShop.db.getItemIDfromCode(getItemCode(is), (integer, e) -> {
                    if(e != null) {
                        e.printStackTrace();
                    }
                });
                return itemName;
            }
        }
        int integer = getIDfromItemStack(is);
        if(integer == -1) {
            return null;
        }
        String line;
        if(itemName == null) {
            line = Base62.encode(integer);
        }  else {
            line = Base62.encode(integer) + ":" + itemName;
        }
        //isToSignLine.put(is, line);
        return line;
    }

    public static void getSignLineFromItem(ItemStack is, Consumer<String> callback) {
        AsyncTask task = new AsyncTask() {
            @Override
            public void execute() {
                callback.accept(getSignLineFromItemSync(is));
                return;
            }

            @Override
            public void finish() {

            }
        };
        task.execute();
    }

    public static String getSignLineFromMaterial(Material material) {
        return getSignLineFromMaterial(material.toString());
    }

    public static String getSignLineFromMaterial(String string) {
        if(string == null) {
            return null;
        }
        if(toUpperCase(toLowerCase(string)).equals(string)) {
            return toLowerCase(string);
        }
        return null;
    }

    public static void getFromSignLine(String signLine, Consumer<ItemStack> callback) {
        if(signLine == null) {
            callback.accept(null);
            return;
        }
        if(signLineToIS.containsKey(signLine)) {
            callback.accept(signLineToIS.get(signLine));
            return;
        }
        if(signLine.contains(" ")) {
            String base62 = signLine.split(":")[0];
            int id = Base62.decode(base62);
            if(id == -1) {
                callback.accept(null);
                return;
            }
            getBase64FromID(id, s -> {
                if(s == null) {
                    callback.accept(null);
                    return;
                }
                ItemStack is = getFromCode(s);
                callback.accept(is);
            });
        } else {
            ItemStack is = new ItemStack(Material.getMaterial(toUpperCase(signLine)));
            callback.accept(is);
        }
    }

    private static String toLowerCase(String string) {
        string = string.replaceAll("_", " ").toLowerCase();
        return WordUtils.capitalize(string);
    }

    private static String toUpperCase(String lowerCase) {
        return lowerCase == null ? null:lowerCase.toUpperCase().replaceAll(" ", "_");
    }
}
