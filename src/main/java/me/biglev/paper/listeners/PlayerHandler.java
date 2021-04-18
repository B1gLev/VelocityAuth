package me.biglev.paper.listeners;

import me.biglev.paper.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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
}

