package me.biglev.velocityauth.utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import me.biglev.velocityauth.Main;
import me.biglev.velocityauth.utils.api.PlayerAPI;
import me.biglev.velocityauth.utils.encryption.EncryptionUtils;
import me.biglev.velocityauth.utils.encryption.Type;
import me.biglev.velocityauth.utils.settings.Manager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.util.Ticks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class Core {

    public static boolean playerExists(Player player) {
        PlayerAPI playerAPI = new PlayerAPI();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet res = null;

        try {
            con = Main.getMysql().getHikariDataSource().getConnection();
            ps = con.prepareStatement("SELECT * FROM auth WHERE realname=?");
            ps.setString(1, player.getUsername());
            res = ps.executeQuery();

            if (res.next()) {
                playerAPI.setName(player.getUsername());
                playerAPI.setLogged(false);
                playerAPI.setPremium(false);
                Main.getPlayerAPIList().addPlayer(playerAPI);

                Main.getServer().getScheduler().buildTask(Main.getMain(), () -> {

                    Title.Times times = Title.Times.of(Ticks.duration(15), Duration.ofMillis(3000), Ticks.duration(20));
                    Title title = Title.title(
                            sTitle(Manager.getMessage().getTitle_settings().getLogin().get(0).getMainTitle(), 1),
                            sTitle(Manager.getMessage().getTitle_settings().getLogin().get(0).getSubTitle(), 2),
                            times
                    );
                    player.showTitle(title);
                    player.sendMessage(ComponentFormat.format(Manager.getMessage().getLogin().getLogin_request()));

                    sendToDate(player, playerAPI.isLogged());
                }).delay(1L, TimeUnit.SECONDS).schedule();
                return true;
            }

            playerAPI.setName(player.getUsername());
            playerAPI.setLogged(false);
            playerAPI.setPremium(false);
            Main.getPlayerAPIList().addPlayer(playerAPI);

            Main.getServer().getScheduler().buildTask(Main.getMain(), () -> {

                Title.Times times = Title.Times.of(Ticks.duration(15), Duration.ofMillis(3000), Ticks.duration(20));
                Title title = Title.title(
                        sTitle(Manager.getMessage().getTitle_settings().getRegister().get(0).getMainTitle(), 1),
                        sTitle(Manager.getMessage().getTitle_settings().getRegister().get(0).getSubTitle(), 2),
                        times
                );
                player.showTitle(title);
                player.sendMessage(ComponentFormat.format(Manager.getMessage().getRegistration().getRegister_request()));

                sendToDate(player, playerAPI.isLogged());
            }).delay(1L, TimeUnit.SECONDS).schedule();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                res.close();
                ps.close();
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static void regCommand(Player player, String password) {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        ResultSet res = null;

        try {
            con = Main.getMysql().getHikariDataSource().getConnection();

            ps2 = con.prepareStatement("SELECT * FROM auth WHERE realname=?");
            ps2.setString(1, player.getUsername());
            res = ps2.executeQuery();

            if (res.next()) {
                player.sendMessage(ComponentFormat.format(Manager.getMessage().getError_messages().getLogged_in()));
                return;
            }

            ps = con.prepareStatement("INSERT INTO auth (realname, uuid, password, regip) VALUES (?,?,?,?)");

            ps.setString(1, player.getUsername());
            ps.setString(2, String.valueOf(player.getUniqueId()));
            ps.setString(3, EncryptionUtils.hashPassword(password, Type.valueOf(Manager.getSettings().getSecurity().getPasswordHash())));
            ps.setString(4, String.valueOf(player.getRemoteAddress().getAddress()));
            ps.executeUpdate();

            PlayerAPI playerAPI = Main.getPlayerAPIList().searchPlayer(player.getUsername());
            playerAPI.setLogged(true);
            sendToDate(player, playerAPI.isLogged());

            player.sendMessage(ComponentFormat.format(Manager.getMessage().getRegistration().getSuccess()));

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                res.close();
                ps2.close();
                ps.close();
                con.close();
            } catch (SQLException e) {

            }
        }
    }

    public static void loginCommand(Player player, String password) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet res = null;

        try {
            con = Main.getMysql().getHikariDataSource().getConnection();
            ps = con.prepareStatement("SELECT * FROM auth WHERE realname=?");
            ps.setString(1, player.getUsername());
            res = ps.executeQuery();

            if (res.next()){
                PlayerAPI playerAPI = Main.getPlayerAPIList().searchPlayer(player.getUsername());
                if (playerAPI.isLogged()) {
                    player.sendMessage(ComponentFormat.format(Manager.getMessage().getError_messages().getLogged_in()));
                    return;
                }

                String hash = res.getString("password");
                if (EncryptionUtils.verifyPassword(password, hash, Type.valueOf(Manager.getSettings().getSecurity().getPasswordHash()))){
                    playerAPI.setLogged(true);
                    sendToDate(player, playerAPI.isLogged());
                    player.sendMessage(ComponentFormat.format(Manager.getMessage().getLogin().getSuccess()));
                    return;
                }

                if (Manager.getSettings().getRestrictions().isKickOnWrongPassword()){
                    player.disconnect(ComponentFormat.format(Manager.getMessage().getLogin().getWrong_password()));
                }
                return;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                res.close();
                ps.close();
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isPremiumUser(String user) {
        //Stuff
        return false;
    }

    public static Component sTitle(String title, int paramInt) {
        Component mainTitle;
        Component subTitle;

        switch (paramInt) {
            case 1:
                mainTitle = ComponentFormat.format(title);
                return mainTitle;
            case 2:
                subTitle = ComponentFormat.format(title);
                return subTitle;
        }
        return null;
    }

    private static void sendToDate(Player player, boolean paramBoolean) {
        Optional<ServerConnection> server = player.getCurrentServer();

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("auth");
        out.writeUTF(player.getUsername());
        out.writeBoolean(paramBoolean);

        server.ifPresent(serverConnection -> serverConnection.sendPluginMessage(Main.LEGACY_BUNGEE_CHANNEL, out.toByteArray()));

    }
}
