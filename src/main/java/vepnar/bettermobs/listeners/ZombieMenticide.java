package vepnar.bettermobs.listeners;

import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityTargetEvent;
import vepnar.bettermobs.Main;
import vepnar.bettermobs.genericMobs.GenericMountMob;

import java.util.ArrayList;
import java.util.List;

public class ZombieMenticide extends GenericMountMob {

    boolean brainWash;

    public ZombieMenticide(Main javaPlugin) {
        super(javaPlugin, "ZombieMenticide", 1, 13);
    }

    public List<LivingEntity> findValidRider(List<Entity> entities) {
        List<LivingEntity> foundEntities = new ArrayList<>();

        for (Entity entity : entities) {
            // Filter unneeded entities
            if (!(entity instanceof Zombie)) continue;
            Zombie lEntity = (Zombie) entity;

            // Check if the entity is restricted in some matter.
            if (isInvalidEntity(lEntity) || lEntity.isAdult()) continue;

            foundEntities.add(lEntity);
        }
        return foundEntities;
    }

    public List<LivingEntity> findValidVehicle(List<Entity> entities) {
        List<LivingEntity> foundEntities = new ArrayList<>();

        for (Entity entity : entities) {
            // Filter unneeded entities depending on the configuration
            if (brainWash && !(entity instanceof Animals)) continue;
            if (!brainWash && !(entity instanceof Chicken)) continue;
            Animals animal = (Animals) entity;

            // Tamed animals have a stronger mind and can resist the brainwashing.
            if (animal instanceof Tameable && brainWash) {
                Tameable tameable = (Tameable) animal;
                if (tameable.isTamed()) continue;
            }
            // Check if the entity is restricted in some matter.
            if (isInvalidEntity(animal)) continue;

            foundEntities.add(animal);
        }
        return foundEntities;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onTargetUpdate(EntityTargetEvent event) {
        if (!brainWash) return; // Only update anger when brainwash is enabled

        // Only update anger when zombies change their target and the target is alive.
        if (!(event.getEntity() instanceof Zombie && event.getTarget() instanceof LivingEntity)) return;
        Zombie entity = (Zombie) event.getEntity();
        LivingEntity target = (LivingEntity) event.getTarget();

        // Update the anger of the vehicle if they are riding an animal.
        if (!entity.isInsideVehicle()) return;
        if (!(entity.getVehicle() instanceof Animals)) return;
        Animals vehicle = (Animals) entity.getVehicle();
        vehicle.setTarget(target);
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();
        brainWash = config.getBoolean("canBrainWash", false);
    }
}
