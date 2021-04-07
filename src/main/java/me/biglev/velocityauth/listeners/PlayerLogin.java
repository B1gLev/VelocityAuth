package me.biglev.velocityauth.listeners;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.connection.PreLoginEvent;
import com.velocitypowered.api.proxy.Player;
import me.biglev.velocityauth.Main;
import me.biglev.velocityauth.utils.Core;
import me.biglev.velocityauth.utils.api.PlayerAPI;

public class PlayerLogin {

    @Subscribe(order = PostOrder.NORMAL)
    public void onLogin(PostLoginEvent e) {
        Player player = e.getPlayer();

        Core.playerExists(player);
    }

    @Subscribe(order = PostOrder.NORMAL)
    public void preLogin(PreLoginEvent e) {
//        e.setResult(PreLoginEvent.PreLoginComponentResult.forceOnlineMode());
    }

    @Subscribe(order = PostOrder.NORMAL)
    public void isLeave(DisconnectEvent e) {
        Player player = e.getPlayer();

        PlayerAPI playerAPI = Main.getPlayerAPIList().searchPlayer(player.getUsername());
        playerAPI.setLogged(false);
    }
}
