package vepnar.bettermobs.listeners;

import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDeathEvent;
import vepnar.bettermobs.IntervalEvent;
import vepnar.bettermobs.Main;
import vepnar.bettermobs.genericMobs.GenericMob;
import vepnar.bettermobs.utils.EntityUtil;

import java.util.List;

@SuppressWarnings("unused")
public class WitherMinions extends GenericMob {

    public static final String WITHER_MINION_META = "IsMinion";
    public static final String WITHER_MINION_COOL_DOWN_META = "MinionCoolDown";

    private int scanRadius;
    private boolean noMinionDrops;
    private double spawnProbability;
    private double spawnRadius;
    private double spawnCount;
    private int minionCap;
    private int spawnCoolDown;

    public WitherMinions(Main javaPlugin) {
        super(javaPlugin, "WitherMinions", 1, 13);
    }


    /**
     * This prevents players farming wither skeletons with just one wither.
     *
     * @param event
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDeathEvent(EntityDeathEvent event) {
        if (!(event.getEntity() instanceof WitherSkeleton) || !noMinionDrops) return;
        if (!EntityUtil.getBoolean(event.getEntity(), WITHER_MINION_META, false)) return;
        event.getDrops().clear();
        event.setDroppedExp(0);
    }


    @EventHandler(priority = EventPriority.NORMAL)
    public void onIntervalTick(IntervalEvent event) {
        for (LivingEntity entity : EntityUtil.getAllLivingEntities(Wither.class)) {
            final List<Player> players = EntityUtil.getNearbyPlayers(entity, scanRadius);
            Wither wither = (Wither) entity;

            // See if the random number generator want the wither to apply their aura.
            // And of course check if there are any players nearby.
            if (!shouldOccur(spawnProbability) || players.isEmpty()) continue;


            // Check if there is an active cool down on this wither.
            if (EntityUtil.hasCoolDown(wither, WITHER_MINION_COOL_DOWN_META, spawnCoolDown)) continue;

            // Prevent too many minions from lagging the server.
            if (minionCap > 0 && EntityUtil.getAllLivingEntities(WitherSkeleton.class).size() >= minionCap) continue;

            // Decide how many minions should be spawned.
            int minionCount = (int) (Math.random() * (spawnCount + 1));
            int minionRadius = (int) (Math.random() * (spawnRadius + 1));

            // Spawn minions in a circle around the wither.
            List<Entity> minions = EntityUtil.spawnEntityInCircle(EntityType.WITHER_SKELETON, wither.getLocation(), minionRadius, minionCount);

            // If there is no place for the minions there should be no cool down on the wither.
            if (minions.isEmpty()) continue;

            // Some weird internal mechanism requires me to add metadata one tick after spawning
            CORE.getServer().getScheduler().runTaskLater(CORE, () -> {
                for (Entity minion : minions) {
                    EntityUtil.setBoolean(minion, WITHER_MINION_META, true);
                }
            }, 1);
            // Set a cool down on the wither.
            EntityUtil.setLong(wither, WITHER_MINION_COOL_DOWN_META, System.currentTimeMillis());

        }
    }

    @Override
    public void readConfig() {
        scanRadius = config.getInt("scanRadius", 0);
        noMinionDrops = config.getBoolean("noMinionDrops", true);
        spawnProbability = config.getDouble("spawnPercentageChance", 0) / 100;
        spawnRadius = config.getDouble("spawnRadius", 5);
        spawnCount = config.getInt("spawnCount", 0);
        minionCap = config.getInt("minionCap", 1);
        spawnCoolDown = config.getInt("coolDown", 0) * 50;
    }
}