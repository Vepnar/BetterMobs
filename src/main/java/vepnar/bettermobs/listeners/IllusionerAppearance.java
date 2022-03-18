package vepnar.bettermobs.listeners;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Illusioner;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.CreatureSpawnEvent;
import vepnar.bettermobs.Main;
import vepnar.bettermobs.genericMobs.GenericMob;

@SuppressWarnings("unused")
public class IllusionerAppearance extends GenericMob {

    private double spawnProbability;
    private boolean onlyNatural;

    public IllusionerAppearance(Main javaPlugin) {
        super(javaPlugin, "IllusionerAppearance", 1, 13);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onSpawn(CreatureSpawnEvent event) {
        if (event.getEntityType() != EntityType.WITCH || event.isCancelled()) return;

        // Let the RNG decide if the Illusioner should show itself
        if (!shouldOccur(spawnProbability)) return;

        boolean naturalSpawnReason = event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.NATURAL);
        if (onlyNatural && !naturalSpawnReason) return;

        Location location = event.getLocation();
        event.getEntity().remove();

        // TODO: Research when spawnEntity produces an NullPointerException
        Illusioner target = (Illusioner) location.getWorld().spawnEntity(location, EntityType.ILLUSIONER);
        target.setCanJoinRaid(false);
        target.setPatrolLeader(false);
        event.setCancelled(true);
    }

    @Override
    public void readConfig() {
        spawnProbability = this.config.getDouble("spawnPercentage", 0) / 100;
        onlyNatural = this.config.getBoolean("onlyNatural", true);
    }

}
