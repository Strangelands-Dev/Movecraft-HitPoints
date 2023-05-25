package me.goodroach.movecrafthp.config;

import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public class Settings {
    //These are the global settings that apply to all craft types that turned on the HP system for them.
    public static double BaseHitPointMultiplier = 1.0;
    public static double BaseDamageMultiplier = 1.0;
    public static double DamageThreshold = 1.0;
    public static double CriticalThreshold = 0.3;
    public static double TorpedoDamageMultiplier = 100.0;
    public static Map<Material, Double> BlockDamageMultiplier = new HashMap<>();
    public static Map<Material, Double> HitPointModifierBlocks = new HashMap<>();
}
