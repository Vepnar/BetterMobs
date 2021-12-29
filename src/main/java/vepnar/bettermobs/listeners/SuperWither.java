package vepnar.bettermobs.listeners;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import vepnar.bettermobs.IntervalEvent;
import vepnar.bettermobs.Main;
import vepnar.bettermobs.genericMobs.GenericMob;
import vepnar.utils.EntityUtil;
import vepnar.utils.PotionUtil;

import java.util.ArrayList;
import java.util.List;

public class SuperWither extends GenericMob {

    public static final String WITHER_EFFECT_META = "WitherEffect";
    private int scanRadius;
    private boolean witherEffectEnabled;
    private double effectDecay;
    private int effectStrength;
    private int effectDuration;
    private List<PotionEffectType> availableEffects;
    private double effectProbability;
    private int effectCoolDown;

    public SuperWither(Main javaPlugin) {
        super(javaPlugin, "SuperWither", 1);
    }

    private boolean isInvalidWither(Wither wither) {
        return wither.isDead() || !wither.hasAI() || wither.isInvulnerable() || !wither.isEmpty() || wither.isCustomNameVisible();
    }

    private List<Wither> getWithers() {
        final List<Wither> entities = new ArrayList<>();
        for (World world : CORE.getServer().getWorlds()) {
            for (Entity entity : world.getEntitiesByClasses(Wither.class)) {
                Wither wither = (Wither) entity;
                if (isInvalidWither(wither)) continue;
                entities.add(wither);
            }
        }
        return entities;
    }

    private void witherEffect(Wither wither, List<Player> players) {
        if (!witherEffectEnabled || !shouldOccur(effectProbability)) return;
        long lastEventCall = System.currentTimeMillis() - EntityUtil.getLong(wither, WITHER_EFFECT_META, 0);
        // Prevent action from occurring when the effect were applied recently or when there are no available effects.
        if (effectCoolDown > lastEventCall || availableEffects.isEmpty()) return;

        int randomIndex = (int) (Math.random() * availableEffects.size());
        PotionEffectType randomEffect = availableEffects.get(randomIndex);

        for (Player player : players) {
            double distance = EntityUtil.distanceBetweenEntities(player, wither);
            int duration = PotionUtil.naturalDecay(effectDuration, distance, effectDecay);
            int strength = (int) (Math.random() * (effectStrength + 1));
            player.addPotionEffect(new PotionEffect(randomEffect, duration, strength));
        }

        EntityUtil.setLong(wither, WITHER_EFFECT_META, System.currentTimeMillis());
    }


    @EventHandler(priority = EventPriority.NORMAL)
    public void onIntervalTick(IntervalEvent event) {
        for (Wither wither : getWithers()) {
            final List<Player> players = EntityUtil.getNearbyPlayers(wither, scanRadius);
            if (players.isEmpty()) continue;
            witherEffect(wither, players);
        }
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();

        // General configuration
        scanRadius = config.getInt("scanRadius", 0);

        // Wither effect
        witherEffectEnabled = config.getBoolean("witherEffect.enabled", true);
        effectDecay = config.getDouble("witherEffect.decay", 0);
        effectStrength = config.getInt("witherEffect.strength", 0);
        effectDuration = config.getInt("witherEffect.duration", 0);
        effectProbability = config.getDouble("witherEffect.changePercentage", 0) / 100;
        effectCoolDown = config.getInt("witherEffect.coolDown") * 50;
        availableEffects = PotionUtil.parsePotionEffects(config.getStringList("witherEffect.effects"));
    }
}
