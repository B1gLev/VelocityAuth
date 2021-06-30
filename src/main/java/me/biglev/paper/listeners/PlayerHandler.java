package me.biglev.paper.listeners;

import me.biglev.paper.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerHandler implements Listener {

    @EventHandler
    public void isMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (Main.getInstance().db.containsKey(player.getName())) {
            if (!Main.getInstance().db.get(player.getName())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        Main.getInstance().db.remove(player.getName());
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        if (Main.getInstance().db.containsKey(player.getName())) {
            if (!Main.getInstance().db.get(player.getName())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (Main.getInstance().db.containsKey(player.getName())) {
            if (!Main.getInstance().db.get(player.getName())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            if (Main.getInstance().db.containsKey(e.getEntity().getName())) {
                if (!Main.getInstance().db.get(e.getEntity().getName())) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onBreakBlock(BlockBreakEvent e) {
        Player player = e.getPlayer();
        if (Main.getInstance().db.containsKey(player.getName())) {
            if (!Main.getInstance().db.get(player.getName())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        if (Main.getInstance().db.containsKey(player.getName())) {
            if (!Main.getInstance().db.get(player.getName())) {
                e.setCancelled(true);
            }
        }
    }
}

