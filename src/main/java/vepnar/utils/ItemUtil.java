package vepnar.utils;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import vepnar.bettermobs.Main;

import java.util.ArrayList;
import java.util.List;

public class ItemUtil {
    public static final List<Material> MELEE_WEAPONS = new ArrayList<>();
    public static final List<Material> RANGED_WEAPONS = new ArrayList<>();
    private static final ItemUtil instance = null;

    public static void reloadAll(Main core) {
        loadMeleeWeapons(core);
        loadRangedWeapons(core);
    }

    public static boolean isEmpty(ItemStack is) {
        if (is == null) return true;
        return is.getType() == Material.AIR;
    }

    public static ItemType getItemType(Material m) {
        if (MELEE_WEAPONS.contains(m)) return ItemType.MELEE;
        if (RANGED_WEAPONS.contains(m)) return ItemType.RANGED;
        return ItemType.NONE;
    }

    public static int randomDurability(Material m) {
        double max = Math.pow(m.getMaxDurability(), 2) / 2;
        double random = Math.random() * max + 1;
        return (int) Math.ceil(random) + 10;
    }

    private static List<String> getCleanList(FileConfiguration config, String path) {
        List<String> result = new ArrayList<>();

        // Check if there is a list in the configuration file.
        if (!config.isList(path)) return result;

        for (Object rawString : config.getList(path)) {
            if (rawString instanceof String) {
                String string = (String) rawString;
                if (!string.isEmpty()) {
                    result.add(string);
                }
            }
        }

        return result;
    }

    private static List<Material> getMaterialList(FileConfiguration config, String path) {
        List<Material> result = new ArrayList<>();
        for (String materialName : getCleanList(config, path)) {
            Material material = Material.matchMaterial(materialName);
            if (material != null) result.add(material);
        }
        return result;
    }

    private static void loadMeleeWeapons(Main core) {
        MELEE_WEAPONS.clear();
        MELEE_WEAPONS.addAll(getMaterialList(core.getConfig(), "meleeWeapons"));
        if (MELEE_WEAPONS.isEmpty()) core.getLogger().warning("meleeWeapons is empty, this may break some modules.");

    }

    private static void loadRangedWeapons(Main core) {
        RANGED_WEAPONS.clear();
        RANGED_WEAPONS.addAll(getMaterialList(core.getConfig(), "rangedWeapons"));
        if (RANGED_WEAPONS.isEmpty()) core.getLogger().warning("rangedWeapons is empty, this may break some modules.");
    }

    public enum ItemType {
        RANGED,
        MELEE,
        NONE,
    }

}