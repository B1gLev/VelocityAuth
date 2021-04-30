package me.biglev.velocityauth.listeners;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;
import me.biglev.velocityauth.Main;
import me.biglev.velocityauth.utils.ComponentFormat;
import me.biglev.velocityauth.utils.api.PlayerProfile;
import me.biglev.velocityauth.utils.settings.Manager;

public class PlayerMessageHandler {

    @Subscribe(order = PostOrder.NORMAL)
    public void onChat(PlayerChatEvent e) {
        Player player = e.getPlayer();

        PlayerProfile playerProfile = Main.getPlayerAPIList().searchPlayer(player.getUsername());
        if (!playerProfile.isLogged()) {
            e.setResult(PlayerChatEvent.ChatResult.denied());
            player.sendMessage(ComponentFormat.format(Manager.getMessage().getError_messages().getLogin_required()));
        }
    }
}
