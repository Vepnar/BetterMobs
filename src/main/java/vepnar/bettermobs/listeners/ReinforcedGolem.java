package vepnar.bettermobs.listeners;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import vepnar.bettermobs.Main;
import vepnar.bettermobs.genericMobs.GenericMob;

public class ReinforcedGolem extends GenericMob {

    private double maxHealthIron;
    private double maxHealthSnow;

    public ReinforcedGolem(Main javaPlugin) {
        super(javaPlugin, "ReinforcedGolem", 1, 13);
    }

    @SuppressWarnings("unused")
    @EventHandler(priority = EventPriority.NORMAL)
    public void onGolemSpawn(CreatureSpawnEvent event) {
        if(!(event.getEntity() instanceof Golem)) return;
        Golem golem = (Golem) event.getEntity();

        double maxHealth = golem instanceof IronGolem ? maxHealthIron : maxHealthSnow;

        // Set the health to the new max health.
        golem.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxHealth);
        golem.setHealth(maxHealth);


    }



    @Override
    public void reloadConfig() {
        super.reloadConfig();
        maxHealthIron = config.getDouble("maxHealthIron", 100);
        maxHealthSnow = config.getDouble("maxHealthSnow", 4);
    }


}
