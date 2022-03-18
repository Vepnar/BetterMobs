package vepnar.bettermobs.listeners;

import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import vepnar.bettermobs.Main;
import vepnar.bettermobs.genericMobs.GenericWeaponSwitch;
import vepnar.bettermobs.utils.ItemUtil;

import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("unused")
public class SkeletonTactics extends GenericWeaponSwitch {

    private boolean includeWitherSkeletons;
    private boolean witherSkeletonShootsFire;
    private boolean retainOriginalDrops;

    public SkeletonTactics(Main javaPlugin) {
        super(javaPlugin, "SkeletonTactics", 1, 13);

    }

    @Override
    protected boolean shouldChange(LivingEntity entity, boolean playerInRange) {
        long time = entity.getWorld().getTime();
        if (entity instanceof WitherSkeleton || time >= 13000) return super.shouldChange(entity, playerInRange);

        // The normal skeleton should switch to their bow in broad daylight.
        ItemStack firstItem = entity.getEquipment().getItemInMainHand();
        if (ItemUtil.isEmpty(firstItem)) return false;
        if (!firstItem.getEnchantments().isEmpty()) return false;
        ItemUtil.ItemType firstType = ItemUtil.getItemType(firstItem.getType());
        return firstType.equals(ItemUtil.ItemType.MELEE);

    }

    @Override
    protected boolean filterEntity(Entity entity) {
            // Filter all non skeleton entities.
            if (!(entity instanceof Skeleton || entity instanceof WitherSkeleton)) return false;

            // Remove all wither skeletons if not enabled.
            if (!includeWitherSkeletons && entity instanceof WitherSkeleton) return false;
            LivingEntity skeleton = (LivingEntity) entity;
            if (skeleton.isInvulnerable() || !skeleton.hasAI()) return false;
            return true;
    }

    @Override
    protected Material changeMaterial(LivingEntity entity, boolean playerInRange) {
         if (playerInRange) {
             if (entity instanceof WitherSkeleton) {
                 return Material.STONE_SWORD;
             }
             return Material.WOODEN_SWORD;
         }
        return Material.BOW;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onArrowLaunch(EntityShootBowEvent event) {
        // Check if the event should be called.
        if (event.isCancelled() || witherSkeletonShootsFire) return;

        // Verify if we are using the right entities.
        if (!(event.getEntity() instanceof WitherSkeleton)) return;
        if (!(event.getProjectile() instanceof Arrow)) return;

        // Ignite the arrow.
        event.getProjectile().setFireTicks(20 * 5);
    }



    @EventHandler(priority = EventPriority.HIGHEST)
    public void damageEvent(EntityDamageEvent event) {
        // Only switch weapons on target entities.
        if(!filterEntity(event.getEntity()) || !retainOriginalDrops) return;
        LivingEntity entity =  (LivingEntity) event.getEntity();

        // Only return the weapon when the damage is fatal.
        if(entity.getHealth() > event.getFinalDamage()) return;

        if(entity.getType() == EntityType.SKELETON && shouldChange(entity, false)) {
            Material original = changeMaterial(entity,false);
            changeItem(entity, original);
        } else if(entity.getType() == EntityType.WITHER_SKELETON && shouldChange(entity, true)) {
            Material original = changeMaterial(entity,true);
            changeItem(entity, original);
        }
    }

    @Override
    public void readConfig() {
        includeWitherSkeletons = config.getBoolean("includeWitherSkeleton", false);
        witherSkeletonShootsFire = config.getBoolean("witherSkeletonShootFire", false);
        retainOriginalDrops = config.getBoolean("retainOriginalDrops", false);
    }
}

