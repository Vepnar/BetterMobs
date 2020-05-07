package vepnar.bettermobs;

import java.lang.reflect.Field;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.craftbukkit.libs.jline.internal.Nullable;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;

import com.google.common.collect.BiMap;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.types.Type;

import net.minecraft.server.v1_15_R1.BlockPosition;
import net.minecraft.server.v1_15_R1.DataConverterRegistry;
import net.minecraft.server.v1_15_R1.DataConverterTypes;
import net.minecraft.server.v1_15_R1.Entity;
import net.minecraft.server.v1_15_R1.EntityLiving;
import net.minecraft.server.v1_15_R1.EntityTypes;
import net.minecraft.server.v1_15_R1.EnumCreatureType;
import net.minecraft.server.v1_15_R1.EnumMobSpawn;
import net.minecraft.server.v1_15_R1.IRegistry;
import net.minecraft.server.v1_15_R1.MinecraftKey;
import net.minecraft.server.v1_15_R1.RegistryMaterials;
import net.minecraft.server.v1_15_R1.SharedConstants;
import net.minecraft.server.v1_15_R1.WorldServer;

public class CustomEntityType<T extends EntityLiving> {

    @Nullable
    private static Field REGISTRY_MAT_MAP;

    static {
        try {
            REGISTRY_MAT_MAP = RegistryMaterials.class.getDeclaredField("c");
        } catch (ReflectiveOperationException err) {
            err.printStackTrace();
            REGISTRY_MAT_MAP = null;
            // technically should only occur if server version changes or jar is modified in
            // "weird" ways
        }
    }

    private final MinecraftKey key;
    // private final Class<T> clazz;
    private final EntityTypes.b<T> maker;
    private EntityTypes<? super T> parentType;
    private EntityTypes<T> entityType;
    private boolean registered;

    public CustomEntityType(String name, Class<T> customEntityClass, EntityTypes<? super T> parentType,
            EntityTypes.b<T> maker) {
        this.key = MinecraftKey.a(name);
        // this.clazz = customEntityClass;
        this.parentType = parentType;
        this.maker = maker;
    }

    public boolean isRegistered() {
        return registered;
    }

    public org.bukkit.entity.Entity spawn(WorldServer server, Location loc) {
        Entity entity = entityType.spawnCreature(server, null, null, null,
                new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()), EnumMobSpawn.EVENT, true, false);
        return entity == null ? null : entity.getBukkitEntity();
    }

    public org.bukkit.entity.Entity spawn(Location loc) {
        WorldServer server = ((CraftWorld) loc.getWorld()).getHandle();
        return spawn(server, loc);
    }

    @SuppressWarnings("unchecked")
    public void register() throws IllegalStateException {
        if (registered || IRegistry.ENTITY_TYPE.getOptional(key).isPresent()) {
            // throw new IllegalStateException(String.format
            // ("Unable to register entity with key '%s' as it is already registered.",
            // key));
            return;
        }
        Map<Object, Type<?>> dataTypes = (Map<Object, Type<?>>) DataConverterRegistry.a()
                .getSchema(DataFixUtils.makeKey(SharedConstants.getGameVersion().getWorldVersion()))
                .findChoiceType(DataConverterTypes.ENTITY_TREE).types();
        dataTypes.put(key.toString(), dataTypes.get(parentType.h().toString().replace("entity/", "")));
        EntityTypes.a<T> a = EntityTypes.a.a(maker, EnumCreatureType.CREATURE);
        entityType = a.a(key.getKey());
        IRegistry.a(IRegistry.ENTITY_TYPE, key.getKey(), entityType);
        registered = true;
    }

    @SuppressWarnings("unchecked")
    public void unregister() throws IllegalStateException {
        if (!registered) {
            throw new IllegalArgumentException(
                    String.format("Entity with key '%s' could not be unregistered, as it is not in the registry", key));
        }
        Map<Object, Type<?>> dataTypes = (Map<Object, Type<?>>) DataConverterRegistry.a()
                .getSchema(DataFixUtils.makeKey(SharedConstants.getGameVersion().getWorldVersion()))
                .findChoiceType(DataConverterTypes.ENTITY_TREE).types();
        dataTypes.remove(key.toString());
        try {
            if (REGISTRY_MAT_MAP == null) {
                throw new ReflectiveOperationException("Field not initially found");
            }
            REGISTRY_MAT_MAP.setAccessible(true);
            Object o = REGISTRY_MAT_MAP.get(IRegistry.ENTITY_TYPE);
            ((BiMap<MinecraftKey, ?>) o).remove(key);
            REGISTRY_MAT_MAP.set(IRegistry.ENTITY_TYPE, o);
            REGISTRY_MAT_MAP.setAccessible(false);
            registered = false;
        } catch (ReflectiveOperationException err) {
            err.printStackTrace();
        }
    }

}