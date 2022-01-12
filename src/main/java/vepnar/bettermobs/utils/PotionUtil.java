package vepnar.bettermobs.utils;

import org.bukkit.potion.PotionEffect;
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

    public static PotionEffect RNGPotionEffect(PotionEffectType effect, int duration , int amplifier) {
        int effectAmplifier = (int) (Math.random() * (amplifier + 1));
        int effectDuration = (int) (Math.random() * (duration + 1));
        return new PotionEffect(effect, effectDuration, effectAmplifier);
    }

    public static PotionEffect RNGPotionEffects(Set<PotionEffectType> effects, int duration, int amplifier) {
        PotionEffectType effect = MathsUtil.randomElemSet(effects);
        return RNGPotionEffect(effect, duration, amplifier);
    }
}
