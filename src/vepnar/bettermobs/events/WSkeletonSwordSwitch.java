package vepnar.bettermobs.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

public class WSkeletonSwordSwitch extends SkeletonSwordSwitch{

	@Override
	public String configName() {
		return "witherSkeletonSwordSwitch";
	}
	
	@Override
	public List<LivingEntity> filterEntities(List<Entity> entities) {

		List<LivingEntity> lEntities = new ArrayList<LivingEntity>();
		for (Entity entity : entities)
			if(entity.getType() == EntityType.WITHER_SKELETON) 
				lEntities.add((LivingEntity) entity);
		return lEntities;
			
	}
	
	@Override
	public void updateClose(List<LivingEntity> entities) {
		for (LivingEntity entity : entities) {
			EntityEquipment equipment = entity.getEquipment();
			ItemStack hand = equipment.getItemInMainHand();

			if (hand.getEnchantments().size() == 0 && hand.getType() == Material.BOW)
				equipment.setItemInMainHand(createItem(Material.STONE_SWORD));
			
		}
		
	}
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
