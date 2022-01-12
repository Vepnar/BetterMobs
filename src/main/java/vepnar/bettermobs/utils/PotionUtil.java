package vepnar.bettermobs.utils;

import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PotionUtil {

    public static Set<PotionEffectType> parsePotionEffects(List<String> names) {
        Set<PotionEffectType> potionEffects = new HashSet<>();
        for (String name : names) {
            PotionEffectType effect = PotionEffectType.getByName(name);
            if (effect != null && !potionEffects.contains(effect)) {
                potionEffects.add(effect);
            }
        }
        return potionEffects;
    }
}
