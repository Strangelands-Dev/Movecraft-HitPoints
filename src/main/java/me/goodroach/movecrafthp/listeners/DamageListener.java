package me.goodroach.movecrafthp.listeners;

import me.goodroach.movecrafthp.MovecraftHitPoints;
import me.goodroach.movecrafthp.config.Keys;
import me.goodroach.movecrafthp.config.Settings;
import me.goodroach.movecrafthp.hitpoints.CraftHitPointState;
import me.goodroach.movecrafthp.hitpoints.CraftHitPoints;
import net.countercraft.movecraft.MovecraftLocation;
import net.countercraft.movecraft.craft.Craft;
import net.countercraft.movecraft.craft.CraftManager;
import net.countercraft.movecraft.craft.PlayerCraft;
import net.countercraft.movecraft.events.ExplosionEvent;
import net.countercraft.movecraft.util.MathUtils;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class DamageListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityExplode(EntityExplodeEvent event) {
        Location location = event.getLocation();
        PlayerCraft craft = fastNearestPlayerCraftToLoc(location);
        if (craft == null || event.isCancelled()) {
            return;
        }

        double damagePerHit = Settings.BaseDamageMultiplier;
        double hitPointDamage = 0;
        double craftDamagePerHit = craft.getType().getDoubleProperty(Keys.CRAFT_DAMAGE_MODIFIER);
        CraftHitPoints craftHP = MovecraftHitPoints.getInstance().getHitPointManager().getCraftHitPoints(craft);

        Iterator<Block> iterator = event.blockList().iterator();
        while (iterator.hasNext()) {
            Block block = iterator.next();
            if (block == null || block.getBlockData() == null) {
                continue;
            }

            boolean protectCraftBlock = false;
            double modifier = 1.0;

            if (craft.getHitBox().contains(MathUtils.bukkit2MovecraftLoc(block.getLocation()))) {
                hitPointDamage += modifier * damagePerHit * craftDamagePerHit;
            }

            if (event.getEntityType().equals(EntityType.PRIMED_TNT)
                && !craftHP.getHitPointState().equals(CraftHitPointState.CRITICAL)) {
                protectCraftBlock = true;
            }

            if (!craft.getHitBox().contains(MathUtils.bukkit2MovecraftLoc(block.getLocation()))) {
                protectCraftBlock = false;
            }

            if (protectCraftBlock) {
                iterator.remove();
            }
        }

        craftHP.removeHitPoints(hitPointDamage);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onTorpedoExplode(ExplosionEvent e) {
        Location loc = e.getExplosionLocation();
        PlayerCraft craft = fastNearestPlayerCraftToLoc(loc);
        if (craft == null || e.isCancelled())
            return;

        if (!craft.getHitBox().contains(MathUtils.bukkit2MovecraftLoc(loc))) {
            return;
        }

        double hitPointDamage = e.getExplosionStrength() * Settings.TorpedoDamageMultiplier;
        MovecraftHitPoints.getInstance().getHitPointManager().getCraftHitPoints(craft).removeHitPoints(hitPointDamage);
    }

    private PlayerCraft fastNearestPlayerCraftToLoc(@NotNull Location source) {
        MovecraftLocation loc = MathUtils.bukkit2MovecraftLoc(source);
        PlayerCraft closest = null;
        long closestDistSquared = Long.MAX_VALUE;
        for (Craft other : CraftManager.getInstance()) {
            if (other.getWorld() != source.getWorld())
                continue;
            if (!(other instanceof PlayerCraft))
                continue;

            long distSquared = other.getHitBox().getMidPoint().distanceSquared(loc);
            if (distSquared < closestDistSquared) {
                closestDistSquared = distSquared;
                closest = (PlayerCraft) other;
            }
        }
        return closest;
    }
}
