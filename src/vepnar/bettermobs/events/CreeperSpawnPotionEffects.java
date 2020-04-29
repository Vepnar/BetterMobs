package vepnar.bettermobs.events;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.entity.Creeper;
import org.bukkit.event.Event;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import vepnar.bettermobs.EventClass;
import vepnar.bettermobs.Main;

public class CreeperSpawnPotionEffects implements EventClass {
	int spawnchance = 100;
	boolean positiveeffect = true;
	boolean negativeeffect = true;
	boolean rareeffect = true;
	
	final PotionEffectType[] POSITIVEEFFECTS = {PotionEffectType.ABSORPTION, PotionEffectType.DAMAGE_RESISTANCE,
			PotionEffectType.INVISIBILITY, PotionEffectType.FAST_DIGGING, PotionEffectType.FIRE_RESISTANCE,
			PotionEffectType.HEAL, PotionEffectType.INCREASE_DAMAGE, PotionEffectType.JUMP,
			PotionEffectType.FIRE_RESISTANCE, PotionEffectType.SPEED, PotionEffectType.WATER_BREATHING};
	
	final PotionEffectType[] NEGATIVEEFFECTS = {PotionEffectType.BLINDNESS, PotionEffectType.WEAKNESS,
			PotionEffectType.SLOW_DIGGING, PotionEffectType.CONFUSION, PotionEffectType.POISON,
			PotionEffectType.WITHER, PotionEffectType.HUNGER};
	
	final PotionEffectType[] RAREEFFECTS = {PotionEffectType.LEVITATION, PotionEffectType.DOLPHINS_GRACE,
			PotionEffectType.GLOWING, PotionEffectType.CONDUIT_POWER, PotionEffectType.LUCK, 
			PotionEffectType.SATURATION, PotionEffectType.BAD_OMEN};

	/**
	 * Receive configuration information from the configuration file.
	 * And return the name of this event
	 * @param JavaPlugin
	 * @return Name of this event in the configuration file
	 */
	@Override
	public String configName(Main m) {
		spawnchance = m.getConfig().getInt("creeperSpawnPotionEffect.spawnChance");
		positiveeffect = m.getConfig().getBoolean("creeperSpawnPotionEffect.positiveEffects");
		negativeeffect = m.getConfig().getBoolean("creeperSpawnPotionEffect.negativeEffects");
		rareeffect = m.getConfig().getBoolean("creeperSpawnPotionEffect.rareEffects");
		return "creeperSpawnPotionEffect";
	}
	
	/**
	 * Handle the event.
	 * @param e
	 */
	@Override
	public void callEvent(Event e) {
		CreatureSpawnEvent spawn = (CreatureSpawnEvent) e;
		if (!(spawn.getEntity() instanceof Creeper)) return;
		int rand = (int) (Math.random() * spawnchance);
		if (rand != 1) return;
		
		// Combine all effects in one big list when they are enabled.
		Object[] effects = {};
		if (positiveeffect) effects = ArrayUtils.addAll(effects, POSITIVEEFFECTS);
		if (negativeeffect) effects = ArrayUtils.addAll(effects, NEGATIVEEFFECTS);
		if (rareeffect) effects = ArrayUtils.addAll(effects, RAREEFFECTS);
		if(effects.length == 0) return;
		
		// Give the entity an random effect
		rand = (int) (Math.random() * effects.length);
		PotionEffect effect = new PotionEffect((PotionEffectType) effects[rand], 3600, 1);
		spawn.getEntity().addPotionEffect(effect);

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
