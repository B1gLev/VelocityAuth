package me.biglev.velocityauth.listeners;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.connection.PreLoginEvent;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.util.GameProfile;
import com.velocitypowered.api.util.UuidUtils;
import me.biglev.velocityauth.Main;
import me.biglev.velocityauth.utils.ComponentFormat;
import me.biglev.velocityauth.utils.Core;
import me.biglev.velocityauth.utils.api.PlayerProfile;
import me.biglev.velocityauth.utils.settings.Manager;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayerLogin {

    @Subscribe(order = PostOrder.FIRST)
    public void preLogin(PreLoginEvent e) {
        Core.connectionHandler(e);
    }

    @Subscribe(order = PostOrder.NORMAL)
    public void preConnect(ServerPreConnectEvent e) {
        Player player = e.getPlayer();

        if (Manager.getSettings().getPremiumAuthentication().isOfflineUUID()) {
            PlayerProfile playerProfile = Main.getPlayerAPIList().searchPlayer(player.getUsername());
            if (playerProfile.isPremium()) {
                try {
                    GameProfile gameProfile = player.getGameProfile();
                    Field field = gameProfile.getClass().getDeclaredField("id");
                    field.setAccessible(true);
                    field.set(gameProfile, UuidUtils.generateOfflinePlayerUuid(player.getUsername()));

                } catch (NoSuchFieldException | IllegalAccessException n) {
                    n.printStackTrace();
                }
            }
        }
    }

    @Subscribe(order = PostOrder.NORMAL)
    public void onLogin(PostLoginEvent e) {
        Player player = e.getPlayer();

        String regex = "^[a-zA-Z0-9]{" + Manager.getSettings().getRestrictions().getMinNicknameLength() + "," + Manager.getSettings().getRestrictions().getMaxNicknameLength() + "}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(player.getUsername());

        if (!matcher.matches()) {
            player.disconnect(ComponentFormat.format(Manager.getMessage().getError_messages().getName_length()));
        }
        Core.playerExists(player);
    }

    @Subscribe(order = PostOrder.NORMAL)
    public void isLeave(DisconnectEvent e) {
        Player player = e.getPlayer();
        PlayerProfile playerProfile = Main.getPlayerAPIList().searchPlayer(player.getUsername());
        playerProfile.setLogged(false);
    }
}
