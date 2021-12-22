package vepnar.bettermobs.listeners;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Illusioner;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import vepnar.bettermobs.genericMobs.GenericMob;

public class IllusionerSpawn extends GenericMob {

    private double spawnProbability;
    private boolean onlyNatural;

    public IllusionerSpawn(JavaPlugin javaPlugin) {
        super(javaPlugin);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onSpawn(CreatureSpawnEvent event) {
        if (event.getEntityType() != EntityType.WITCH) return;
        if(event.isCancelled()) return;
        if (spawnProbability < Math.random()) return;

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
    public void reloadConfig() {
        super.reloadConfig();
        spawnProbability = this.config.getDouble("spawnPercentage", 0) / 100;
        onlyNatural = this.config.getBoolean("onlyNatural", true);
    }

    @Override
    public String getName() {
        return "IllusionerSpawn";
    }

    @Override
    public void enable() {
        super.enable();
        core.getServer().getPluginManager().registerEvents(this, core);
    }

    @Override
    public void disable() {
        super.disable();
        HandlerList.unregisterAll(this);
    }
}
