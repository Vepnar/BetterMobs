package vepnar.bettermobs.listeners;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import vepnar.bettermobs.Main;
import vepnar.bettermobs.genericMobs.GenericMob;

@SuppressWarnings("unused")
public class ZombiePassage extends GenericMob {

    private double spawnProbability;
    private boolean onlyNatural;

    public ZombiePassage(Main javaPlugin) {
        super(javaPlugin, "ZombiePassage", 1, 13);
    }


    @EventHandler(priority = EventPriority.HIGH)
    public void onZombieDeath(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Zombie)) return;
        Zombie zombie = (Zombie) event.getEntity();

        // Only allow the passage event when the zombie is killed.
        // Baby zombies are not allowed to use the ability.
        if (zombie.getHealth() - event.getFinalDamage() > 0 || !zombie.isAdult()) return;

        ItemStack primary = zombie.getEquipment().getItemInMainHand();
        if (primary != null && primary.getType() == Material.CLOCK) {
            event.setCancelled(true); // Prevent the attack
            zombie.setBaby(); // Send the zombie back in time and turn it into a baby.

            // Restore health of the zombie.
            double maxHealth = zombie.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
            zombie.setHealth(maxHealth);

            zombie.getWorld().spawnParticle(Particle.FLASH, zombie.getLocation(), 1);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onSpawn(CreatureSpawnEvent event) {
        if (!(event.getEntity() instanceof Zombie)) return;
        Zombie zombie = (Zombie) event.getEntity();

        // Only allow adult zombies & zombie without an item in their main hand.
        if (!zombie.isAdult() || zombie.getEquipment().getItemInMainHand() == null) return;

        // Only natural zombies if enabled
        boolean naturalSpawnReason = event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.NATURAL);
        if (onlyNatural && !naturalSpawnReason) return;

        // Verify if RNG thinks we should spawn quick zombies.
        if (!shouldOccur(spawnProbability)) return;

        // Set a clock in the main hand of the zombie.
        zombie.getEquipment().setItemInMainHand(new ItemStack(Material.CLOCK));
    }

    @Override
    public void readConfig() {
        onlyNatural = config.getBoolean("onlyNatural", true);
        spawnProbability = config.getDouble("spawnPercentageChance", 0) / 100;
    }
}
