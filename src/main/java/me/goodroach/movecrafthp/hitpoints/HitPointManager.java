package me.goodroach.movecrafthp.hitpoints;

import net.countercraft.movecraft.craft.Craft;

import java.util.HashMap;
import java.util.Map;

public class HitPointManager {
    private final Map<Craft, CraftHitPoints> hitPointsMap = new HashMap<>();

    public void registerCraft(Craft craft) {
        hitPointsMap.put(craft, new CraftHitPoints(craft));
    }

    public void removeHitPointCraft(Craft craft) {
        hitPointsMap.remove(craft);
    }

    public CraftHitPoints getCraftHitPoints(Craft craft) {
        return hitPointsMap.get(craft);
    }
}
