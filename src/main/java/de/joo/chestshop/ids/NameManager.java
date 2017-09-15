package de.joo.chestshop.ids;

import de.joo.chestshop.plugin.ChestShop;
import de.joo.chestshop.util.StringUtil;
import de.joo.chestshop.util.encoding.Base62;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.logging.Logger;

/**
 * Created by Johannes on 06.06.2017.
 */
public class NameManager {
    private static Map<UUID, Integer> uuidToID = new HashMap<>();
    private static Map<Integer, UUID> iDToUUID = new HashMap<>();
    private static Logger log = ChestShop.log;

    public static void getID(UUID uuid, Consumer<Integer> callback) {
        System.out.println("UUID: " + uuid);
        if(uuidToID.containsKey(uuid)) {
            callback.accept(uuidToID.get(uuid));
            return;
        }

        ChestShop.db.getID(uuid, (integer, exception) -> {
            if(exception != null) {
                log.warning("Fehler bei Verbindung zu Datenbank!");
                exception.printStackTrace();
                callback.accept(0);
                return;
            }
            log.info("ID ist: " + integer);
            callback.accept(integer);
            uuidToID.put(uuid, integer);
            iDToUUID.put(integer, uuid);
        });
    }

    public static int getIDSync(UUID uuid) {
       // if(uuidToID.containsKey(uuid)) {
       //     return uuidToID.get(uuid);
       // }

        try {
            return ChestShop.db.getIDSync(uuid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;

    }

    public static void getBase62ForSign(UUID uuid, Consumer<String> callback) {
        getID(uuid, integer -> {
            callback.accept(Base62.encode(integer));
        });
    }

    public static void getUUIDFromSign(String sign, Consumer<UUID> callback) {
        int id = -1;
        try {
            id = Base62.decode(sign.split(" ")[0]);
        } catch (NumberFormatException e) {
            ChestShop.log.info("Ungültiges ChestShop-Schild gefunden.");
        }
        if(id == -1) {
            callback.accept(null);
            return;
        }
        getUUID(id, callback);
    }

    public static void getUUID(int id, Consumer<UUID> callback) {
        if(iDToUUID.containsKey(id)) {
            callback.accept(iDToUUID.get(id));
            return;
        }

        ChestShop.db.getUUID(id, (uuidString, exception) -> {
            if(exception != null) {
                log.warning("Fehler bei Verbindung zu Datenbank!");
                exception.printStackTrace();
                callback.accept(null);
                return;
            }
            UUID uuid = UUID.fromString(uuidString);
            callback.accept(uuid);
            uuidToID.put(uuid, id);
            iDToUUID.put(id, uuid);
        });
    }

    public static String getBase62ForSignSync(UUID uuid) {
        return String.valueOf(getIDSync(uuid));
    }
}
