package de.joo.chestshop.util;


import de.joo.chestshop.util.encoding.Base62;
import de.joo.chestshop.util.encoding.Base64;
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

    private final Yaml yaml;

    public ItemUtil() {
        yaml = new Yaml(new YamlBukkitConstructor(), new YamlRepresenter(), new DumperOptions());
    }

    /**
     * Gets the item code for this item
     *
     * @param item Item
     * @return Item code for this item
     */
    public String getItemCode(ItemStack item) {
        try {
            return Base64.encodeObject(yaml.dump(new ItemStack(item)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets an ItemStack from a item code
     *
     * @param code Item code
     * @return ItemStack represented by this code
     */
    public ItemStack getFromCode(String code)
    {
        // TODO java.lang.StackOverflowError - http://pastebin.com/eRD8wUFM - Corrupt item DB?
        try {
            return yaml.loadAs((String)Base64.decodeToObject(code), ItemStack.class);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class YamlBukkitConstructor extends YamlConstructor {
        public YamlBukkitConstructor() {
            this.yamlConstructors.put(new Tag(Tag.PREFIX + "org.bukkit.inventory.ItemStack"), yamlConstructors.get(Tag.MAP));
        }

    }
}
