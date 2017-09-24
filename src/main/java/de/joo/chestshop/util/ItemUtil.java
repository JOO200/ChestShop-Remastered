package de.joo.chestshop.util;


import de.joo.chestshop.util.encoding.Base62;
import de.joo.chestshop.util.encoding.Base64;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.file.YamlConstructor;
import org.bukkit.configuration.file.YamlRepresenter;
import org.bukkit.inventory.ItemStack;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;

import java.io.IOException;

/**
 * Created by Johannes on 25.04.2017.
 */
public class ItemUtil {


    /**
     * Gets the item code for this item
     *
     * @param item Item
     * @return Item code for this item
     */
    public static String getItemCode(ItemStack item) {
        item.setAmount(1);
        YamlConfiguration config = new YamlConfiguration();
        config.set("item", item);
        return config.saveToString();
    }

    /**
     * Gets an ItemStack from a item code
     *
     * @param code Item code
     * @return ItemStack represented by this code
     */
    public static ItemStack getFromCode(String code)
    {
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.loadFromString(code);
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
        return config.getItemStack("item");
    }
}
