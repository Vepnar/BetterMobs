package vepnar.bettermobs.listeners;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import vepnar.bettermobs.IntervalEvent;
import vepnar.bettermobs.Main;
import vepnar.bettermobs.genericMobs.GenericMob;
import vepnar.bettermobs.utils.EntityUtil;
import vepnar.bettermobs.utils.MathsUtil;
import vepnar.bettermobs.utils.PotionUtil;

import java.util.List;
import java.util.Set;

public class WitherAura extends GenericMob {

    public static final String WITHER_AURA_META = "WitherAuraCoolDown";


    private int scanRadius;
    private double effectDecay;
    private int effectStrength;
    private int effectDuration;
    private Set<PotionEffectType> availableEffects;
    private double effectProbability;
    private int effectCoolDown;


    public WitherAura(Main javaPlugin) {
        super(javaPlugin, "WitherAura", 1, 13);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onIntervalTick(IntervalEvent event) {
        for (LivingEntity entity : EntityUtil.getAllLivingEntities(Wither.class)) {
            final List<Player> players = EntityUtil.getNearbyPlayers(entity, scanRadius);
            Wither wither = (Wither) entity;

            // See if the random number generator want the wither to apply their aura.
            // And of course check if there are any players nearby.
            if (!shouldOccur(effectProbability) || players.isEmpty()) continue;

            // Prevent action from occurring when the effect were applied recently or when there are no available effects.
            if (EntityUtil.hasCoolDown(wither, WITHER_AURA_META, effectCoolDown) || availableEffects.isEmpty())
                continue;

            PotionEffectType randomEffect =  MathsUtil.randomElemSet(availableEffects);

            // Apply an effect to all players nearby.
            for (Player player : players) {

                // The euclidean distance between the wither and the player are used to compute the duration of the effect.
                double distance = EntityUtil.distanceBetweenEntities(player, wither);
                int duration = MathsUtil.naturalDecay(effectDuration, distance, effectDecay);
                int strength = (int) (Math.random() * (effectStrength + 1));
                player.addPotionEffect(new PotionEffect(randomEffect, duration, strength));
            }

            // Apply a cool down to the wither.
            EntityUtil.setLong(wither, WITHER_AURA_META, System.currentTimeMillis());

        }
    }


    @Override
    public void reloadConfig() {
        super.reloadConfig();

        // General configuration
        scanRadius = config.getInt("scanRadius", 0);

        // Wither effect
        effectDecay = config.getDouble("distanceDecay", 0);
        effectStrength = config.getInt("strength", 0);
        effectDuration = config.getInt("duration", 0);
        effectProbability = config.getDouble("chancePercentage", 0) / 100;
        effectCoolDown = config.getInt("coolDown", 0) * 50;
        availableEffects = PotionUtil.parsePotionEffects(config.getStringList("effects"));

    }
}
