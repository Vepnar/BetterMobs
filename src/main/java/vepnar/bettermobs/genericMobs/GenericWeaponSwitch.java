package vepnar.bettermobs.genericMobs;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import vepnar.bettermobs.IntervalEvent;
import vepnar.bettermobs.Main;
import vepnar.bettermobs.utils.EntityUtil;
import vepnar.bettermobs.utils.ItemUtil;

import java.util.ArrayList;
import java.util.List;


public class GenericWeaponSwitch extends GenericMob {
    private int meleeRadius;
    private int scanRadius;


    public GenericWeaponSwitch(Main javaPlugin, String name, int version) {
        super(javaPlugin, name, version);
    }

    protected List<LivingEntity> filterEntity(List<Entity> entities) {
        List<LivingEntity> result = new ArrayList<>();
        for (Entity entity : entities) {
            if (!(entity instanceof LivingEntity)) continue;
            result.add((LivingEntity) result);
        }
        return result;
    }

    protected Material changeItem(LivingEntity entity, boolean playerNearby) {
        return entity.getEquipment().getItemInMainHand().getType();
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

    @EventHandler(priority = EventPriority.NORMAL)
    public void onInterval(IntervalEvent event) {
        for (Player player : CORE.getServer().getOnlinePlayers()) {
            List<LivingEntity> livingEntities = filterEntity(player.getNearbyEntities(scanRadius, scanRadius, scanRadius));
            for (LivingEntity target : livingEntities) {
                boolean playerInRange = EntityUtil.isPlayerNearby(target, meleeRadius);
                if (shouldChange(target, playerInRange)) {
                    Material targetMaterial = changeItem(target, playerInRange);
                    ItemStack newWeapon = new ItemStack(targetMaterial);
                    target.getEquipment().setItemInMainHand(newWeapon);
                    target.getEquipment().setItemInMainHandDropChance(0.01F);
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
