package me.goodroach.movecrafthp.hitpoints;

import me.goodroach.movecrafthp.MovecraftHitPoints;
import me.goodroach.movecrafthp.config.Settings;
import net.countercraft.movecraft.MovecraftLocation;
import net.countercraft.movecraft.craft.Craft;
import net.countercraft.movecraft.craft.CraftManager;
import net.countercraft.movecraft.craft.PlayerCraft;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;

public class CraftHitPoints {
    private double hitPoints;
    private double currentHitPoints;
    private CraftHitPointState hitPointState;
    private final BossBar bossBar;
    private final Craft craft;

    public CraftHitPoints(Craft craft) {
        this.craft = craft;
        double baseHPMult = Settings.BaseHitPointMultiplier;
        double addedHP = 0;

        if (!Settings.HitPointModifierBlocks.isEmpty()) {
            for (MovecraftLocation location : craft.getHitBox()) {
                Material type = craft.getWorld().getBlockAt(location.getX(), location.getY(), location.getZ()).getType();
                if (type == Material.AIR || type == Material.CAVE_AIR || type == Material.FIRE) {
                    continue;
                }
                if (Settings.HitPointModifierBlocks.containsKey(type)) {
                    addedHP += Settings.HitPointModifierBlocks.get(type);
                }
            }
        }

        this.hitPoints = baseHPMult * craft.getOrigBlockCount() + addedHP;
        this.currentHitPoints = hitPoints;
        this.hitPointState = CraftHitPointState.FULL;

        this.bossBar = Bukkit.createBossBar("Hull: " + currentHitPoints + " / " + hitPoints, BarColor.GREEN, BarStyle.SEGMENTED_10);
        if (craft instanceof PlayerCraft) {
            this.bossBar.addPlayer(((PlayerCraft) this.craft).getPilot());
        }
    }
    public double getHitPoints() {
        return hitPoints;
    }

    public double getCurrentHitPoints() {
        return currentHitPoints;
    }

    public CraftHitPointState getHitPointState() {
        return hitPointState;
    }

    public void removeHitPoints(Double hitPointDamage) {
        currentHitPoints -= hitPointDamage;
        currentHitPoints = Math.round((currentHitPoints)*10)/10d;
        if (currentHitPoints <= 0) {
            CraftManager.getInstance().sink(craft);
            return;
        }
        updateBossBar();
    }

    private void updateBossBar () {
        bossBar.setTitle("HP :" + currentHitPoints + " / " + hitPoints);
        if(currentHitPoints <= Settings.CriticalThreshold * hitPoints) {
            hitPointState = CraftHitPointState.CRITICAL;
            bossBar.setColor(BarColor.RED);
        } else if (currentHitPoints <= Settings.DamageThreshold * hitPoints) {
            hitPointState = CraftHitPointState.DAMAGED;
            bossBar.setColor(BarColor.YELLOW);
        } else {
            hitPointState = CraftHitPointState.FULL;
            bossBar.setColor(BarColor.GREEN);
        }
        bossBar.setProgress(Math.min(1.0, currentHitPoints/hitPoints));
        bossBar.setVisible(hitPoints != 0.0);
    }

    public void removeBossBar () {
        bossBar.removeAll();
    }


}
