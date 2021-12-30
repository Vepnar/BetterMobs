package vepnar.bettermobs.listeners;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Creeper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import vepnar.bettermobs.Main;
import vepnar.bettermobs.genericMobs.GenericMob;
import vepnar.bettermobs.utils.PotionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MagicalCreepers extends GenericMob {
    private final List<CreeperClass> creeperClasses = new ArrayList<>();
    private boolean onlyNatural;
    private boolean effectStacking;

    public MagicalCreepers(Main javaPlugin) {
        super(javaPlugin, "MagicalCreepers", 1);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onCreeperSpawn(CreatureSpawnEvent event) {
        // Filter out all other entities
        if (!(event.getEntity() instanceof Creeper)) return;


        // Only allow natural spawn cases when enabled.
        if (event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.NATURAL) && !onlyNatural) return;
        Creeper creeper = (Creeper) event.getEntity();

        // Try each class
        for (CreeperClass creeperClass : creeperClasses) {
            if (!shouldOccur(creeperClass.spawnProbability)) continue;


            // Prepare potion effect.
            int strength = (int) (Math.random() * (creeperClass.effectStrength + 1));
            int effectIndex = (int) (Math.random() * creeperClass.effects.size());
            PotionEffect effect = new PotionEffect(creeperClass.effects.get(effectIndex), 9999, strength);
            creeper.addPotionEffect(effect);

            // Allow a creeper to be more than one class
            if (!effectStacking) break;
        }
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();

        onlyNatural = config.getBoolean("onlyNatural", true);
        effectStacking = config.getBoolean("effectStacking", false);

        // Parse creeper class configuration
        creeperClasses.clear();
        Set<String> keys = config.getConfigurationSection("classes").getKeys(false);
        if (keys.isEmpty()) {
            CORE.getLogger().info("There are no classes added to MagicalCreepers");
            return;
        }

        for (String key : keys) {
            ConfigurationSection subConfig = config.getConfigurationSection("classes").getConfigurationSection(key);
            if (!subConfig.getBoolean("enabled", false)) continue;

            // Create new class
            CreeperClass creeperClass = new CreeperClass();
            creeperClass.effects = PotionUtil.parsePotionEffects(subConfig.getStringList("effects"));
            creeperClass.effectStrength = subConfig.getInt("effectStrength", 0);
            creeperClass.spawnProbability = subConfig.getDouble("spawnPercentageChance", 0) / 100;
            creeperClasses.add(creeperClass);
            if (creeperClass.effects.isEmpty())
                CORE.debug(key + " in MagicalCreepers contains no valid potion effects.");
        }
        if (creeperClasses.isEmpty()) {
            CORE.debug("No valid creeperClasses have been added.");
            this.disable();
        }

    }
}

class CreeperClass {
    public double spawnProbability;
    public List<PotionEffectType> effects;
    public int effectStrength;
}
