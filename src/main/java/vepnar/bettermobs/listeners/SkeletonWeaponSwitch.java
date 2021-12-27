package vepnar.bettermobs.listeners;

import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import vepnar.bettermobs.Main;
import vepnar.bettermobs.genericMobs.GenericWeaponSwitch;
import vepnar.utils.ItemUtil;

import java.util.ArrayList;
import java.util.List;

public class SkeletonWeaponSwitch extends GenericWeaponSwitch {

    private boolean includeWitherSkeletons;
    private boolean witherSkeletonShootsFire;

    public SkeletonWeaponSwitch(Main javaPlugin) {
        super(javaPlugin, "SkeletonWeaponSwitch", 1);

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
    protected List<LivingEntity> filterEntity(List<Entity> entities) {
        List<LivingEntity> result = new ArrayList<>();
        for (Entity entity : entities) {
            // Filter all non skeleton entities.
            if (!(entity instanceof Skeleton || entity instanceof WitherSkeleton)) continue;

            // Remove all wither skeletons if not enabled.
            if (!includeWitherSkeletons && entity instanceof WitherSkeleton) continue;
            LivingEntity skeleton = (LivingEntity) entity;
            if (skeleton.isInvulnerable() || !skeleton.hasAI()) continue;

            result.add(skeleton);
        }
        return result;
    }

    @Override
    protected Material changeItem(LivingEntity entity, boolean playerInRange) {
        if (entity instanceof WitherSkeleton) {
            if (playerInRange) {
                return Material.STONE_SWORD;
            } else {
                return Material.BOW;
            }
        } else {
            if (playerInRange) {
                return Material.WOODEN_SWORD;
            } else {
                return Material.BOW;
            }
        }
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


    @Override
    public void reloadConfig() {
        super.reloadConfig();
        includeWitherSkeletons = config.getBoolean("includeWitherSkeleton", false);
        witherSkeletonShootsFire = config.getBoolean("witherSkeletonShootFire", false);
    }


}

