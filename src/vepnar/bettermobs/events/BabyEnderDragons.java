package vepnar.bettermobs.events;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EnderDragonChangePhaseEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import vepnar.bettermobs.EventClass;
import vepnar.bettermobs.Main;
import vepnar.bettermobs.util;

public class BabyEnderDragons implements EventClass {
	int radius, amount, chance, damagebooster;
	boolean regen;

	final EntityType companion = EntityType.PHANTOM;
	final String companionname = "§c§lBaby Dragon";
	final boolean newtarget = true;

	/**
	 * Receive values from the configuration file.
	 */
	@Override
	public String configName(Main m) {
		radius = m.getConfig().getInt("babyEnderDragons.spawnRadius");
		chance = m.getConfig().getInt("babyEnderDragons.spawnChance");
		amount = m.getConfig().getInt("babyEnderDragons.spawnAmount");
		damagebooster = m.getConfig().getInt("babyEnderDragons.damageBooster");
		regen = m.getConfig().getBoolean("babyEnderDragons.dragonRegen");
		return "babyEnderDragons";
	}

	private boolean checkName(Entity monster) {
		return util.checkCompanion(monster, companion, companionname);
	}

	/*
	 * Handle spawn moments
	 */
	public void DragonPhaseEvent(EnderDragonChangePhaseEvent e) {
		System.out.println(e.getCurrentPhase());
		// Check if the dragon is in the correct phase
		// Calculate chance.
		if (util.random(1, chance) != 1)
			return;

		// Generate random values.
		int randomRadius = util.random(0, radius) + 5;
		int randomAmount = util.random(1, amount) + 1;

		// Check if there are players in range
		if (util.filterPlayers(e.getEntity().getLocation(), 150).size() == 0)
			return;

		Location[] spawnLocations = util.getArcSpots(e.getEntity().getLocation(), randomRadius, randomAmount);
		for (Location spawnLocation : spawnLocations) {

			// Check if the baby dragon can spawn.
			spawnLocation = util.shouldSpawn(spawnLocation, randomRadius, 1);
			if (spawnLocation == null)
				continue;

			// Spawn particles for the new entities.
			spawnLocation.getWorld().spawnParticle(Particle.DRAGON_BREATH, spawnLocation, 60);

			// Spawn phantoms with names and special effects
			LivingEntity monster = (LivingEntity) spawnLocation.getWorld().spawnEntity(spawnLocation, companion);
			monster.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 9999, damagebooster), false);
			monster.setCustomName(companionname);
			monster.setCustomNameVisible(false);
		}
	}

	public void newTarget(PlayerMoveEvent e) {
		Player player = e.getPlayer();
		for (Entity livingent : player.getNearbyEntities(50, 50, 50)) {
			if (!checkName(livingent))
				continue;
			Mob entity = (Mob) livingent;
			if (entity.getTarget() == null)
				entity.setTarget(player);

		}
	}

	/**
	 * Handle damage event for dragon regeneration.
	 */
	@SuppressWarnings("deprecation")
	public void EntityDamage(EntityDamageByEntityEvent e) {

		// The dragon can't damage her own babies!
		if (e.getEntity() instanceof EnderDragon) {
			if (checkName(e.getDamager()))
				e.setCancelled(true);

			// Check if the damage is done on a player by a phantom
		} else if (!(e.getEntity() instanceof Player))
			return;
		if (!checkName(e.getDamager()))
			return;

		// Receive entities
		LivingEntity monster = (LivingEntity) e.getDamager();
		LivingEntity player = (LivingEntity) e.getEntity();

		// Check if the given name matches our companion name
		if (!(monster.getCustomName() != null && monster.getCustomName().equals(companionname)))
			return;

		// Get dragon and remove phantom when there is no dragon
		List<LivingEntity> dragons = util.filterEntity(player.getLocation(), 150, EntityType.ENDER_DRAGON);
		if (dragons.size() == 0) {
			e.setCancelled(true);
			monster.remove();
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
		if (checkName(e.getEntity())) {
			e.getDrops().clear();

			// Check if the entity is a enderdragon and remove all the baby dragons.
		} else if (e.getEntity() instanceof EnderDragon) {
			EnderDragon dragon = (EnderDragon) e.getEntity();
			for (LivingEntity living : util.filterEntity(dragon.getLocation(), 150, companion)) {
				if (checkName(living))
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
		} else if (e instanceof EntityDamageByEntityEvent) {
			EntityDamage((EntityDamageByEntityEvent) e);
		} else
			newTarget((PlayerMoveEvent) e);
	}

	@Override
	public boolean canBeCalled(Event e) {
		return e instanceof EnderDragonChangePhaseEvent || e instanceof EntityDeathEvent
				|| (e instanceof EntityDamageByEntityEvent && regen) || (e instanceof PlayerMoveEvent && newtarget);
	}

}
