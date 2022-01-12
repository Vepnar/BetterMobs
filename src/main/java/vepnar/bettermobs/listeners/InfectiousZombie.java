package vepnar.bettermobs.listeners;

import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffectType;
import vepnar.bettermobs.Main;
import vepnar.bettermobs.genericMobs.GenericMob;
import vepnar.bettermobs.utils.PotionUtil;

import java.util.Set;

public class InfectiousZombie extends GenericMob {
    double infectionProbability;
    int effectAmplifier;
    int effectDuration;
    Set<PotionEffectType> effects;

    public InfectiousZombie(Main javaPlugin) {
        super(javaPlugin, "InfectiousZombie", 1, 13);
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent event) {
        // No processing on other entities.
        if(!(event.getEntity() instanceof Player && event.getDamager() instanceof Zombie)) return;

        if(!this.shouldOccur(infectionProbability)) return;

        // Create effect and apply the effect.
        Player player = (Player) event.getEntity();
        player.addPotionEffect(PotionUtil.RNGPotionEffects(effects, effectDuration, effectAmplifier));

    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();
        infectionProbability = config.getDouble("infectionPercentage", 0) / 100;
        effectAmplifier = config.getInt("infectionAmplifier", 0);
        effectDuration = config.getInt("infectionDuration", 0);
        effects = PotionUtil.parsePotionEffects(config.getStringList("infectionType"));
    }
}
