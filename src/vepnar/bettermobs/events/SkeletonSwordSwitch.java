package vepnar.bettermobs.events;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// Yuck
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
import org.bukkit.inventory.meta.ItemMeta;

import vepnar.bettermobs.EventClass;

public class SkeletonSwordSwitch implements EventClass {

	/**
	 * Receive configuration name and check if this event is enabled.
	 */
	@Override
	public String configName() {
		return "skeletonSwordSwitch";
	}
	
	/**
	 * Check if this class is compatible with the given event. 
	 * 
	 * @param e event that should be checked.
	 * @return true when it is compatible and false when it is not.
	 */
	@Override
	public boolean canBeCalled(Event e) {
		return e instanceof PlayerMoveEvent;
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

	/**
	 * Create a damaged item from the given material.
	 * 
	 * @param m Material will be turned into a item stack with damage.
	 * @return An Itemstack made for the given material.
	 */
	public ItemStack createItem(Material m) {
		ItemStack is = new ItemStack(m);
		Damageable damageable = (Damageable) is.getItemMeta();
	
		int damage = (int) (Math.random() * 25 + 10);
		damageable.setDamage(damage);
		is.setItemMeta((ItemMeta) damageable);
		return is;
	}
	
	/**
	 * Update items holding of the closest entities.
	 * 
	 * @param entities list of entities that should be updated.
	 */
	public void updateClose(List<LivingEntity> entities) {
		for (LivingEntity entity : entities) {
			EntityEquipment equipment = entity.getEquipment();
			ItemStack hand = equipment.getItemInMainHand();

			if (hand.getEnchantments().size() == 0 && hand.getType() == Material.BOW)
				equipment.setItemInMainHand(createItem(Material.WOODEN_SWORD));
			
		}
		
	}
	
	/**
	 * The same as updateClose but for entities that are far away.
	 * 
	 * @see updateClose
	 * 
	 * @param entities entities list of entities that should be updated.
	 */
	public void updateFar(List<LivingEntity> entities) {
		for (LivingEntity entity : entities) {
			EntityEquipment equipment = entity.getEquipment();
			ItemStack hand = equipment.getItemInMainHand();

			if (hand.getEnchantments().size() == 0 && hand.getType() == Material.WOODEN_SWORD)
				equipment.setItemInMainHand(createItem(Material.BOW));
			
		}
		
	}
	
	/**
	 * Handle given event. this should only be called from the event handler.
	 * 
	 * @param e event that should be handled
	 */
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

}
