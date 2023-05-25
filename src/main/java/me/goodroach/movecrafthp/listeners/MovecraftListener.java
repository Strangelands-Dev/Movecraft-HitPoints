package me.goodroach.movecrafthp.listeners;

import me.goodroach.movecrafthp.MovecraftHitPoints;
import me.goodroach.movecrafthp.config.Keys;
import net.countercraft.movecraft.craft.PlayerCraft;
import net.countercraft.movecraft.events.CraftDetectEvent;
import net.countercraft.movecraft.events.CraftReleaseEvent;
import net.countercraft.movecraft.events.CraftSinkEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class MovecraftListener implements Listener {
    @EventHandler(priority = EventPriority.LOW)
    public void onCraftPilot (CraftDetectEvent event) {
        if (!(event.getCraft() instanceof PlayerCraft) | event.isCancelled()) {
            return;
        }
        if (event.getCraft().getType().getBoolProperty(Keys.USE_HIT_POINT)) {
            MovecraftHitPoints.getInstance().getHitPointManager().registerCraft(event.getCraft());
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onCraftRelease (CraftReleaseEvent event) {
        if(!(event.getCraft() instanceof PlayerCraft)
                | (event.isCancelled()
                | MovecraftHitPoints.getInstance().getHitPointManager().getCraftHitPoints(event.getCraft())==null)) {
            return;
        }
        MovecraftHitPoints.getInstance().getHitPointManager().getCraftHitPoints(event.getCraft()).removeBossBar();
        MovecraftHitPoints.getInstance().getHitPointManager().removeHitPointCraft(event.getCraft());
    }

    @EventHandler
    public void onCraftSink (CraftSinkEvent event) {
        if(!(event.getCraft() instanceof PlayerCraft)
                | (event.isCancelled()
                | MovecraftHitPoints.getInstance().getHitPointManager().getCraftHitPoints(event.getCraft())==null)) {
            return;
        }
        MovecraftHitPoints.getInstance().getHitPointManager().getCraftHitPoints(event.getCraft()).removeBossBar();
        MovecraftHitPoints.getInstance().getHitPointManager().removeHitPointCraft(event.getCraft());
    }
}
