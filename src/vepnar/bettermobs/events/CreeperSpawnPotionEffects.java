package vepnar.bettermobs.events;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import vepnar.bettermobs.EventClass;
import vepnar.bettermobs.Main;

public class CreeperSpawnPotionEffects implements EventClass {
	int spawnchance, effectduration;
	boolean positiveeffect, negativeeffect, rareeffect;

	final PotionEffectType[] POSITIVEEFFECTS = { PotionEffectType.ABSORPTION, PotionEffectType.DAMAGE_RESISTANCE,
			PotionEffectType.INVISIBILITY, PotionEffectType.FAST_DIGGING, PotionEffectType.FIRE_RESISTANCE,
			PotionEffectType.HEAL, PotionEffectType.INCREASE_DAMAGE, PotionEffectType.JUMP,
			PotionEffectType.FIRE_RESISTANCE, PotionEffectType.SPEED, PotionEffectType.WATER_BREATHING };

	final PotionEffectType[] NEGATIVEEFFECTS = { PotionEffectType.BLINDNESS, PotionEffectType.WEAKNESS,
			PotionEffectType.SLOW_DIGGING, PotionEffectType.CONFUSION, PotionEffectType.POISON, PotionEffectType.WITHER,
			PotionEffectType.HUNGER };

	final PotionEffectType[] RAREEFFECTS = { PotionEffectType.LEVITATION, PotionEffectType.DOLPHINS_GRACE,
			PotionEffectType.GLOWING, PotionEffectType.LUCK,
			PotionEffectType.SATURATION, PotionEffectType.BAD_OMEN };

	/**
     * Get configuration data of the configuration file, also return the title of this handler.
	 * 
	 * @param JavaPlugin
	 * @return title of this event in the configuration file
	 */
	@Override
	public String configName(Main m) {
		spawnchance = m.getConfig().getInt("creeperSpawnPotionEffect.spawnChance");
		effectduration = m.getConfig().getInt("creeperSpawnPotionEffect.effectDuration") * 20;
		positiveeffect = m.getConfig().getBoolean("creeperSpawnPotionEffect.positiveEffects");
		negativeeffect = m.getConfig().getBoolean("creeperSpawnPotionEffect.negativeEffects");
		rareeffect = m.getConfig().getBoolean("creeperSpawnPotionEffect.rareEffects");
		return "creeperSpawnPotionEffect";
	}

	/**
	 * Handle the event.
	 * 
	 * @param e
	 */
	@Override
	public void callEvent(Event e) {
		CreatureSpawnEvent spawn = (CreatureSpawnEvent) e;
		// Check if given entity is a creeper and the spawn reason is natural.
		if (spawn.getSpawnReason() != SpawnReason.NATURAL || spawn.getEntityType() != EntityType.CREEPER)
			return;
		// Check if the creeper should spawn with special effects.
		if ((int) (Math.random() * spawnchance) != 1)
			return;

		// Merge all enabled effects within one big array.
		Object[] effects = {};
		if (positiveeffect)
			effects = ArrayUtils.addAll(effects, POSITIVEEFFECTS);
		if (negativeeffect)
			effects = ArrayUtils.addAll(effects, NEGATIVEEFFECTS);
		if (rareeffect)
			effects = ArrayUtils.addAll(effects, RAREEFFECTS);
		
		// Check if there are any added effects.
		if (effects.length == 0)
			return;

		// Select a random effect of the big list and address it to the creeper.
		int rand = (int) (Math.random() * effects.length);
		PotionEffect effect = new PotionEffect((PotionEffectType) effects[rand], effectduration, 0, false);
		((LivingEntity) spawn.getEntity()).addPotionEffect(effect);

	}

	/**
	 * Check if this class is compatible with the given event.
	 * 
	 * @param e event that should be checked.
	 * @return true when it is compatible and false when it is not.
	 */
	@Override
	public boolean canBeCalled(Event e) {
		return e instanceof CreatureSpawnEvent;
	}

}
