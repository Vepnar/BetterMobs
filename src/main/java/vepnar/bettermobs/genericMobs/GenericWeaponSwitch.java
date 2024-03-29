package vepnar.bettermobs.genericMobs;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import vepnar.bettermobs.IntervalEvent;
import vepnar.bettermobs.Main;
import vepnar.bettermobs.utils.EntityUtil;
import vepnar.bettermobs.utils.ItemUtil;

import java.util.ArrayList;
import java.util.List;


public abstract class GenericWeaponSwitch extends GenericMob {
    private int meleeRadius;
    private int scanRadius;

    public GenericWeaponSwitch(Main javaPlugin, String name, int version, int apiVersion) {
        super(javaPlugin, name, version, apiVersion);
    }

    /**
     * Remove all entities which are ineligible to be processed by this module.
     * Only filter based on entity type and not on the inventory or other features.
     * @param entity Random nearby entity.
     * @return true if eligible for weapon updates.
     */
    protected abstract boolean filterEntity(Entity entity);

    /**
     * Material to which the weapon should be changed.
     * NOTE: the only weapon switch possible is between ranged weapons and melee weapons.
     * @param entity Living entity which is eligible for a weapon update.
     * @param playerNearby True if the player is within the given radius.
     * @return
     */
    protected abstract Material changeMaterial(LivingEntity entity, boolean playerNearby);


    private List<LivingEntity> filterEntities(List<Entity> entities) {
        List<LivingEntity> result = new ArrayList<>();
        for (Entity entity : entities) {
           if(filterEntity(entity)) {
               result.add((LivingEntity) entity);
           }
        }
        return result;
    }

    protected boolean shouldChange(LivingEntity entity, boolean playerNearby) {
        EntityEquipment equipment = entity.getEquipment();
        ItemStack firstItem = equipment.getItemInMainHand();

        // Check if the entity has all items.
        if (ItemUtil.isEmpty(firstItem)) return false;
        if (!firstItem.getEnchantments().isEmpty()) return false;

        ItemUtil.ItemType firstType = ItemUtil.getItemType(firstItem.getType());

        if (playerNearby && firstType.equals(ItemUtil.ItemType.RANGED)) return true;
        return !playerNearby && firstType.equals(ItemUtil.ItemType.MELEE);
    }

    protected void changeItem(LivingEntity entity, Material material) {
        ItemStack newWeapon = new ItemStack(material);

        // Retain original drop chance
        float dropChance = entity.getEquipment().getItemInMainHandDropChance();
        entity.getEquipment().setItemInMainHand(newWeapon);
        entity.getEquipment().setItemInMainHandDropChance(dropChance);
    }

    @SuppressWarnings("unused")
    @EventHandler(priority = EventPriority.NORMAL)
    public void onInterval(IntervalEvent event) {
        for (Player player : CORE.getServer().getOnlinePlayers()) {
            List<LivingEntity> livingEntities = filterEntities(player.getNearbyEntities(scanRadius, scanRadius, scanRadius));
            for (LivingEntity target : livingEntities) {
                boolean playerInRange = EntityUtil.isPlayerNearby(target, meleeRadius);
                if (shouldChange(target, playerInRange)) {
                    // Create new weapon.
                    Material targetMaterial = changeMaterial(target, playerInRange);
                    changeItem(target, targetMaterial);

                }
            }
        }
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();
        meleeRadius = config.getInt("meleeRadius", 0);
        scanRadius = config.getInt("scanRadius", 0);
    }
}
