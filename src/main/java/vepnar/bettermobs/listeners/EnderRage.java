package vepnar.bettermobs.listeners;

import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.projectiles.ProjectileSource;
import vepnar.bettermobs.Main;
import vepnar.bettermobs.genericMobs.GenericMob;
import vepnar.bettermobs.utils.EntityUtil;

@SuppressWarnings("unused")
public class EnderRage extends GenericMob {

    public static final String ENDER_RAGE_COOL_DOWN = "enderRage";
    private double angerProbability;
    private int coolDown;
    private int maxEnderManRage;
    private int scanRadius;
    private boolean includeDragon;
    private boolean includeCrystal;
    private boolean noInternalAnger;

    public EnderRage(Main javaPlugin) {
        super(javaPlugin, "EnderRage", 1, 13);
    }

    private Player getPlayerFromDamager(Entity entity) {
        if (entity instanceof Player) return (Player) entity;

        if (entity instanceof Projectile) {
            Projectile projectile = (Projectile) entity;
            ProjectileSource shooter = projectile.getShooter();
            if (shooter instanceof Player) return (Player) shooter;
        }
        return null;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onHitEvent(EntityDamageByEntityEvent event) {
        Player offender = getPlayerFromDamager(event.getDamager());
        // Prevent further processing if the attacker is not a player.
        if (offender == null) return;

        // Check if the victim is a player or crystal.
        Entity victim = event.getEntity();
        if (!((victim instanceof EnderDragon && includeDragon) || (victim instanceof EnderCrystal && includeCrystal)))
            return;

        // Check if the victim has a cool down.
        if (EntityUtil.hasCoolDown(victim, ENDER_RAGE_COOL_DOWN, coolDown)) return;

        // Check if the RNG agrees.
        if (!shouldOccur(angerProbability)) return;

        int enderManAngered = 1 + (int) (Math.random() * maxEnderManRage);

        for (Entity entity : EntityUtil.getNearbyEntitiesOfType(offender, scanRadius, Enderman.class)) {
            if (enderManAngered == 0) break;
            Enderman guard = (Enderman) entity;
            if (guard.getTarget() instanceof Player) continue;
            guard.setTarget(offender);
            enderManAngered--;
        }
        EntityUtil.setLong(victim, ENDER_RAGE_COOL_DOWN, System.currentTimeMillis());


    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInternalAnger(EntityTargetLivingEntityEvent event) {
        if (!noInternalAnger) return;
        if (event.getEntity() instanceof Enderman && event.getTarget() instanceof EnderDragon) {
            event.setCancelled(true);
        }
    }

    @Override
    public void readConfig() {
        scanRadius = config.getInt("scanRadius", 32);
        maxEnderManRage = config.getInt("maxEnderManRage", 0);
        coolDown = config.getInt("coolDown", 600) * 50; // Convert tick => second.
        angerProbability = config.getDouble("angerPercentageChance", 0) / 100;


        noInternalAnger = config.getBoolean("noInternalAnger", true);
        includeCrystal = config.getBoolean("includeCrystal", false);
        includeDragon = config.getBoolean("includeDragon", false);
    }

}
