package vepnar.bettermobs.events;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.Player;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EnderDragon.Phase;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EnderDragonChangePhaseEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import vepnar.bettermobs.EventClass;
import vepnar.bettermobs.Main;
import vepnar.bettermobs.util;

public class BabyEnderDragons implements EventClass {
	int radius, amount, chance, damagebooster;
	boolean regen;

	/**
	 * Receive values from the configuration file.
	 */
	@Override
	public String configName(Main m) {
		radius = m.getConfig().getInt("BabyEnderDragons.spawnRadius");
		chance = m.getConfig().getInt("BabyEnderDragons.spawnchance");
		amount = m.getConfig().getInt("BabyEnderDragons.spawnAmount");
		damagebooster = m.getConfig().getInt("BabyEnderDragons.damageBooster");
		regen = m.getConfig().getBoolean("BabyEnderDragons.dragonRegen");
		return "BabyEnderDragons";
	}

	/* 
	 * Handle spawn moments
	 */
	public void DragonPhaseEvent(EnderDragonChangePhaseEvent e) {
		// Check if the dragon is in the correct phase
		if (e.getNewPhase() != Phase.HOVER)
			return;
		
		// Calculate chance.
		if (util.random(0, chance) != 1)
			return;

		// Generate random values.
		int random_radius = (int) (Math.random() * radius) + 2;
		int random_amount = (int) (Math.random() * amount) + 1;

		Location[] spawnLocations = util.getArcSpots(e.getEntity().getLocation(), random_radius, random_amount);
		for (Location spawnLocation : spawnLocations) {

			// Check if the baby dragon can spawn.
			spawnLocation = util.shouldSpawn(spawnLocation, random_radius, 1);
			if (spawnLocation == null)
				continue;

			// Spawn particles for the new entities.
			spawnLocation.getWorld().spawnParticle(Particle.DRAGON_BREATH, spawnLocation, 60);
			
			// Spawn phantoms with names and special effects
			Phantom monster = (Phantom) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.PHANTOM);
			monster.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 9999, damagebooster), false);
			monster.setCustomName("§c§lBaby dragon");
			monster.setCustomNameVisible(false);
		}
	}

	/**
	 * Handle damage event for dragon regeneration.
	 */
	@SuppressWarnings("deprecation")
	public void EntityDamage(EntityDamageByEntityEvent e) {
		
		// Check if the damage is done on a player by a phantom
		if (!(e.getDamager() instanceof Phantom && e.getEntity() instanceof Player))
			return;
		
		// Receive entities
		LivingEntity phantom = (LivingEntity) e.getDamager();
		LivingEntity player = (LivingEntity) e.getEntity();

		// Get dragon and remove phantom when there is no dragon
		List<LivingEntity> dragons = util.filterEntity(player.getLocation(), 150, EntityType.ENDER_DRAGON);
		if (dragons.size() == 0) {
			e.setCancelled(true);
			phantom.remove();
			return;
		}

		// Regen the dragon when it is found.
		LivingEntity dragon = dragons.get(0);
		double health = dragon.getHealth() + e.getDamage();
		if (health > dragon.getMaxHealth()) {
			dragon.setHealth(dragon.getMaxHealth());
		} else
			dragon.setHealth(health);

	}

	/**
	 * Handle the death of the baby dragons and the dragon itself
	 */
	public void deathEvent(EntityDeathEvent e) {
		
		// Check if the entity is a baby dragon and disable the drops if it is found.
		if (e.getEntity() instanceof Phantom) {
			Phantom monster = (Phantom) e.getEntity();
			if (monster.getCustomName() != null && monster.getCustomName().equals("§c§lBaby dragon"))
				e.getDrops().clear();
			
		// Check if the entity is a enderdragon and remove all the baby dragons.
		} else if (e.getEntity() instanceof EnderDragon) {
			EnderDragon dragon = (EnderDragon) e.getEntity();
			for (LivingEntity living : util.filterEntity(dragon.getLocation(), 150, EntityType.PHANTOM)) {
				if (living.getCustomName().equals("§c§lBaby dragon"))
					living.remove();
			}
		}
	}

	/**
	 * Handle all events for this event
	 */
	@Override
	public void callEvent(Event e) {
		if (e instanceof EnderDragonChangePhaseEvent) {
			DragonPhaseEvent((EnderDragonChangePhaseEvent) e);
		} else if (e instanceof EntityDeathEvent) {
			deathEvent((EntityDeathEvent) e);
		} else
			EntityDamage((EntityDamageByEntityEvent) e);
	}

	@Override
	public boolean canBeCalled(Event e) {
		return e instanceof EnderDragonChangePhaseEvent || e instanceof EntityDeathEvent
				|| (e instanceof EntityDamageByEntityEvent && regen);
	}

}
