package vepnar.bettermobs.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

public class WSkeletonSwordSwitch extends SkeletonSwordSwitch{

	/**
	 * Receive configuration name and check if this event is enabled.
	 * Same as in <b>SkeletonSwordSwitch</b>.
	 * 
	 * @see SkeletonSwordSwitch.configname
	 */
	@Override
	public String configName() {
		return "witherSkeletonSwordSwitch";
	}	
	
	/**
	 * Remove all unused entities from a entity list.
	 * All entities that are not Wither Skeletons. almost the same as in SkeletonSwordSwitch.
	 * 
	 * @see SkeletonSwordSwitch.filterEntities
	 * 
	 * @param entities you want to be filtered
	 * @return List of living entities filtered
	 */
	@Override
	public List<LivingEntity> filterEntities(List<Entity> entities) {

		List<LivingEntity> lEntities = new ArrayList<LivingEntity>();
		for (Entity entity : entities)
			if(entity.getType() == EntityType.WITHER_SKELETON) 
				lEntities.add((LivingEntity) entity);
		return lEntities;
			
	}
	
	/**
	 * Update items holding of the closest entities.
	 * 
	 * @see SkeletonSwordSwitch.updateClose
	 * 
	 * @param entities list of entities that should be updated.
	 */
	@Override
	public void updateClose(List<LivingEntity> entities) {
		for (LivingEntity entity : entities) {
			EntityEquipment equipment = entity.getEquipment();
			ItemStack hand = equipment.getItemInMainHand();

			if (hand.getEnchantments().size() == 0 && hand.getType() == Material.BOW)
				equipment.setItemInMainHand(createItem(Material.STONE_SWORD));
			
		}
		
	}
	/**
	 * The same as updateClose but for entities that are far away.
	 * 
	 * @see updateClose
	 * 
	 * @param entities entities list of entities that should be updated.
	 */
	@Override
	public void updateFar(List<LivingEntity> entities) {
		for (LivingEntity entity : entities) {
			EntityEquipment equipment = entity.getEquipment();
			ItemStack hand = equipment.getItemInMainHand();

			if (hand.getEnchantments().size() == 0 && hand.getType() == Material.STONE_SWORD);
				equipment.setItemInMainHand(createItem(Material.BOW));
			
		}
		
	}
}
