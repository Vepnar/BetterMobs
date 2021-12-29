package vepnar.utils;

import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class PotionUtil {

    public static List<PotionEffectType> parsePotionEffects(List<String> names) {
        List<PotionEffectType> potionEffects = new ArrayList<>();
        for (String name : names) {
            PotionEffectType effect = PotionEffectType.getByName(name);
            if (effect != null && !potionEffects.contains(effect)) {
                potionEffects.add(effect);
            }
        }
        return potionEffects;
    }

    public static int naturalDecay(int maxDuration, double distance, double decay) {
        double distanceDecay = Math.exp(-1 * (distance * decay));
        return (int) (maxDuration * distanceDecay);
    }
}
