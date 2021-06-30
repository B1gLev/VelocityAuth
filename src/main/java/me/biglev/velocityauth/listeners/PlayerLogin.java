package me.biglev.velocityauth.listeners;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.connection.PreLoginEvent;
import com.velocitypowered.api.proxy.Player;
import me.biglev.velocityauth.Main;
import me.biglev.velocityauth.utils.ComponentFormat;
import me.biglev.velocityauth.utils.Core;
import me.biglev.velocityauth.utils.api.PlayerProfile;
import me.biglev.velocityauth.utils.settings.Manager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayerLogin {

    @Subscribe(order = PostOrder.FIRST)
    public void preLogin(PreLoginEvent e) {
        String regex = "^[a-zA-Z0-9&_-]{" + Manager.getSettings().getRestrictions().getMinNicknameLength() + "," + Manager.getSettings().getRestrictions().getMaxNicknameLength() + "}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(e.getUsername());
        if (!matcher.matches()) e.setResult(PreLoginEvent.PreLoginComponentResult.denied(ComponentFormat.format(Manager.getMessage().getError_messages().getName_length())));
        Core.connectionHandler(e);
    }

//    @Subscribe(order = PostOrder.NORMAL)
//    public void preConnect(ServerPreConnectEvent e) {
//        System.out.println("Valami");
//        Player player = e.getPlayer();
//        if (Manager.getSettings().getPremiumAuthentication().isOfflineUUID()) {
//            PlayerProfile playerProfile = Main.getPlayerAPIList().searchPlayer(player.getUsername());
//            if (playerProfile.isPremium()) {
//                try {
//
//                    Field field = player.getGameProfile().getClass().getDeclaredField("id");
//                    field.setAccessible(true);
//                    field.set(player.getGameProfile(), UuidUtils.generateOfflinePlayerUuid(player.getUsername()));
//                } catch (NoSuchFieldException | IllegalAccessException n) {
//                    n.printStackTrace();
//                }
//            }
//        }
//    }

    @Subscribe(order = PostOrder.NORMAL)
    public void onLogin(PostLoginEvent e) {
        Player player = e.getPlayer();
        Core.playerExists(player);
    }

    @Subscribe(order = PostOrder.NORMAL)
    public void isLeave(DisconnectEvent e) {
        Player player = e.getPlayer();
        PlayerProfile playerProfile = Main.getPlayerAPIList().searchPlayer(player.getUsername());
        playerProfile.setLogged(false);
    }
}
