package me.goodroach.movecrafthp.config;

import net.countercraft.movecraft.craft.type.CraftType;
import net.countercraft.movecraft.craft.type.property.BooleanProperty;
import net.countercraft.movecraft.craft.type.property.DoubleProperty;
import org.bukkit.NamespacedKey;

public class Keys {
    /*Note this is only for craft files. You can have these settings in your craft files to modify them
    individually.*/
    public static final NamespacedKey CRAFT_HIT_POINT_MODIFIER = build("craft_hit_point_modifier");
    public static final NamespacedKey CRAFT_DAMAGE_MODIFIER = build("craft_damage_modifier");
    public static final NamespacedKey USE_HIT_POINT = build("use_hit_point");

    public static void register() {
        CraftType.registerProperty(new DoubleProperty("CraftHitPointModifier", CRAFT_HIT_POINT_MODIFIER, craftType -> 1.0));
        CraftType.registerProperty(new DoubleProperty("CraftDamageModifier", CRAFT_DAMAGE_MODIFIER, craftType -> 1.0));
        CraftType.registerProperty(new BooleanProperty("IsHitPointCraft", USE_HIT_POINT, craftType -> false));
    }

    private static NamespacedKey build (String key) {return new NamespacedKey("movecraft-hitpoints", key);}
}