package vepnar.bettermobs.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Piglin;
import vepnar.bettermobs.Main;
import vepnar.bettermobs.genericMobs.GenericWeaponSwitch;

import java.util.ArrayList;
import java.util.List;

public class PiglinTactics extends GenericWeaponSwitch {
    public PiglinTactics(Main javaPlugin) {
        super(javaPlugin, "PiglinTactics", 1, 16);
    }

    @Override
    protected List<LivingEntity> filterEntity(List<Entity> entities) {
        List<LivingEntity> result = new ArrayList<>();
        for (Entity entity : entities) {
            if (!(entity instanceof Piglin)) continue;
            LivingEntity livingEntity = (LivingEntity) entity;

            if (!(livingEntity.isInvulnerable() || !livingEntity.hasAI())) {
                result.add(livingEntity);
            }
        }
        return result;
    }

    @Override
    protected Material changeItem(LivingEntity entity, boolean playerInRange) {
        if (playerInRange) {
            return Material.GOLDEN_SWORD;
        } else {
            return Material.CROSSBOW;
        }
    }
}
