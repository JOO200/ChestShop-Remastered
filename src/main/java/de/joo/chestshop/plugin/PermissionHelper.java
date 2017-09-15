package de.joo.chestshop.plugin;

import org.bukkit.entity.Player;

/**
 * Created by Johannes on 06.06.2017.
 */
public class PermissionHelper {
    private static Permission perm;

    public static boolean has(Player player, Permission permission) {
        return true; //player.hasPermission(perm.getPermission(permission));
    }

    public static enum Permission {
        ADMINSHOP_CREATION ("chestshop.adminshop.create"),
        ADMIN_ACCESS ("chestshop.admin.access"),
        PLAYER_CREATE_SHOP ("chestshop.player.shop.create");

        private final String permission;

        Permission (String perm) {
            permission = perm;
        }

        public String getPermission(Permission p) {
            return p.permission;
        }
    }
}
