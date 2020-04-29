package vepnar.bettermobs.events;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import vepnar.bettermobs.EventClass;

public class SkeletonSwordSwitch implements EventClass {

	@Override
	public String configName() {
		return "skeletonSwordSwitch";
	}
	
	/**
	 * Subtract entities from a list with another list.
	 * 
	 * @param minuend The list of entities you want to subtract from.
	 * @param subtrahend The list of entities you subtract
	 */
	public void subtractListfromList(List<LivingEntity> minuend, List<LivingEntity> subtrahend) {
		
		for (Iterator<LivingEntity> mIterator = minuend.listIterator(); mIterator.hasNext(); ) {
			LivingEntity entity = mIterator.next();

			if(subtrahend.contains(entity))
				mIterator.remove();
		}
	}
	
	/**
	 * Remove all unused entities from a entity list.
	 * 
	 * @param entities you want to be filtered
	 * @return List of living entities filtered
	 */
	public List<LivingEntity> filterEntities(List<Entity> entities) {

		List<LivingEntity> lEntities = new ArrayList<LivingEntity>();
		for (Entity entity : entities)
			if(entity.getType() == EntityType.SKELETON) 
				lEntities.add((LivingEntity) entity);
		return lEntities;
			
	}

	public ItemStack createItem(Material m) {
		ItemStack is = new ItemStack(m);
		Damageable damageable = (Damageable) is.getItemMeta();
	
		int damage = (int) (Math.random() * 25 + 10);
		damageable.setDamage(damage);
		return is;
	}
	
	public void updateClose(List<LivingEntity> entities) {
		for (LivingEntity entity : entities) {
			EntityEquipment equipment = entity.getEquipment();
			ItemStack hand = equipment.getItemInMainHand();

			if (hand.getEnchantments().size() == 0 && hand.getType() == Material.BOW)
				equipment.setItemInMainHand(createItem(Material.WOODEN_SWORD));
			
		}
		
	}
	public void updateFar(List<LivingEntity> entities) {
		for (LivingEntity entity : entities) {
			EntityEquipment equipment = entity.getEquipment();
			ItemStack hand = equipment.getItemInMainHand();

			if (hand.getEnchantments().size() == 0 && hand.getType() == Material.WOODEN_SWORD)
				equipment.setItemInMainHand(createItem(Material.BOW));
			
		}
		
	}
	
	@Override
	public void callEvent(Event e) {
		PlayerMoveEvent MoveEvent = (PlayerMoveEvent) e;
		Player player = MoveEvent.getPlayer();
		
		// Get entities in a radius.
		List<LivingEntity> farEntities = filterEntities(player.getNearbyEntities(14, 8, 14));
		List<LivingEntity> closestEntities = filterEntities(player.getNearbyEntities(6, 5, 6));
		
		
		// Remove closest entities from far entities.
		subtractListfromList(farEntities, closestEntities);
		
		updateFar(farEntities);
		updateClose(closestEntities);		
		
	}

	@Override
	public boolean canBeCalled(Event e) {
		return e instanceof PlayerMoveEvent;
	}

}
