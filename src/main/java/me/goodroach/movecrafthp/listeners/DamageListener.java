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
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DamageListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onTNTExplode(EntityExplodeEvent event) {
        if (event.blockList().isEmpty()) return;
        if (!event.getEntityType().equals(EntityType.PRIMED_TNT)) return;
        if (event.isCancelled()) return;

        double damagePerHit = Settings.BaseDamageMultiplier;
        Map<PlayerCraft, Double> blocksBrokenPerCraft = new HashMap<>();
        Set<Block> protectedBlocks = new HashSet<>();

        for (Block block : event.blockList()) {
            if (block == null) continue;
            Location location = block.getLocation();
            PlayerCraft craft = fastNearestPlayerCraftToLoc(location);

            if (MovecraftHitPoints.getInstance().getHitPointManager().getCraftHitPoints(craft) == null) continue;
            CraftHitPoints craftHP = MovecraftHitPoints.getInstance().getHitPointManager().getCraftHitPoints(craft);
            if (!blocksBrokenPerCraft.containsKey(craft)) blocksBrokenPerCraft.put(craft, 0.0);
            if (canProtectBlock(craft, block, craftHP)) {
                protectedBlocks.add(block);
            }
            double modifier = Settings.BlockDamageMultiplier.getOrDefault(block.getType(), 1.0);
            blocksBrokenPerCraft.replace(craft, blocksBrokenPerCraft.get(craft) + modifier);
        }
        event.blockList().removeAll(protectedBlocks);
        for (PlayerCraft craft : blocksBrokenPerCraft.keySet()) {
            @NotNull CraftHitPoints craftHP = MovecraftHitPoints.getInstance().getHitPointManager().getCraftHitPoints(craft);
            double craftDamagePerHit = craft.getType().getDoubleProperty(Keys.CRAFT_DAMAGE_MODIFIER);
            craftHP.removeHitPoints(blocksBrokenPerCraft.get(craft) * damagePerHit * craftDamagePerHit);
        }
    }

    //All blocks that return true is removed from the event.blocklist
    private boolean canProtectBlock(PlayerCraft craft, Block block, CraftHitPoints craftHP) {
        if (!craft.getHitBox().contains(MathUtils.bukkit2MovecraftLoc(block.getLocation()))) return false;
        if (craftHP.getHitPointState().equals(CraftHitPointState.CRITICAL)) return false;
        if (Settings.IgnoreBlockProtection.contains(block.getType())) return false;
        if (isPartOfMesh(block) ) return false;
        return true;
    }

    private boolean isPartOfMesh(Block block) {
        int counter = 0;
        if (block.getRelative(BlockFace.UP).isEmpty()) counter++;
        if (block.getRelative(BlockFace.DOWN).isEmpty()) counter++;
        if (block.getRelative(BlockFace.SOUTH).isEmpty()) counter++;
        if (block.getRelative(BlockFace.WEST).isEmpty()) counter++;
        if (block.getRelative(BlockFace.NORTH).isEmpty()) counter++;
        if (block.getRelative(BlockFace.EAST).isEmpty()) counter++;
        System.out.println(counter);
        return counter >= 4;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onTorpedoExplode(ExplosionEvent e) {
        Location loc = e.getExplosionLocation();
        PlayerCraft craft = fastNearestPlayerCraftToLoc(loc);
        if (craft == null || e.isCancelled()) return;
        if (!craft.getHitBox().contains(MathUtils.bukkit2MovecraftLoc(loc))) return;

        CraftHitPoints craftHP = MovecraftHitPoints.getInstance().getHitPointManager().getCraftHitPoints(craft);
        if (craftHP == null) return;

        double hitPointDamage = e.getExplosionStrength() * Settings.TorpedoDamageMultiplier;
        craftHP.removeHitPoints(hitPointDamage);
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
