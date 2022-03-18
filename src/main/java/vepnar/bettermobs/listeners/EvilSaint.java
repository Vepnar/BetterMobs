package vepnar.bettermobs.listeners;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import vepnar.bettermobs.IntervalEvent;
import vepnar.bettermobs.Main;
import vepnar.bettermobs.genericMobs.GenericMob;
import vepnar.bettermobs.utils.EntityUtil;

@SuppressWarnings("unused")
public class EvilSaint extends GenericMob {

    public static final String REGENERATION_COOL_DOWN = "RegenerationCoolDown";
    private double regenerationProbability;
    private double regenerationStrength;
    private int regenerationDelay;

    public EvilSaint(Main javaPlugin) {
        super(javaPlugin, "EvilSaint", 1, 13);
    }

    @SuppressWarnings("unused")
    @EventHandler(priority = EventPriority.NORMAL)
    public void onInterval(IntervalEvent event) {
        for (LivingEntity entity : EntityUtil.getAllLivingEntities(Monster.class)) {
            AttributeInstance maxHealthAttribute = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH);

            // There are many requirements for the entity to regenerate.
            // Such: No active cool down, luck & need to have a max health attribute set.
            if (maxHealthAttribute == null || EntityUtil.hasCoolDown(entity, REGENERATION_COOL_DOWN, regenerationDelay) || !shouldOccur(regenerationProbability) || maxHealthAttribute.getValue() == entity.getHealth())
                continue;
            double maxHealth = maxHealthAttribute.getValue();
            double currentHealth = entity.getHealth();

            // Compute the target health value.
            if (currentHealth + regenerationStrength > maxHealth) {
                entity.setHealth(maxHealth);
            } else {
                entity.setHealth(currentHealth + regenerationStrength);
            }

        }
    }

    @Override
    public void readConfig() {
        regenerationProbability = config.getDouble("regenerationPercentageChance", 0) / 100;
        regenerationDelay = config.getInt("regenerationCoolDown", 0) * 50;
        regenerationStrength = config.getDouble("regenerationStrength", 0);
    }
}
