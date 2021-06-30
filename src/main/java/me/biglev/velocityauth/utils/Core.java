package me.biglev.velocityauth.utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.velocitypowered.api.event.connection.PreLoginEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import me.biglev.velocityauth.Main;
import me.biglev.velocityauth.utils.api.PlayerProfile;
import me.biglev.velocityauth.utils.api.mojang.MojangAPI;
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
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class Core {

    public static boolean playerExists(Player player) {
        PlayerProfile playerProfile = Main.getPlayerAPIList().searchPlayer(player.getUsername());
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet res = null;

        try {
            con = Main.getMysql().getHikariDataSource().getConnection();
            ps = con.prepareStatement("SELECT * FROM auth WHERE realname=?");
            ps.setString(1, player.getUsername());
            res = ps.executeQuery();

            if (res.next()) {

                if (!res.getBoolean("premium")) {
                    Main.getServer().getScheduler().buildTask(Main.getMain(), () -> {
                        Title.Times times = Title.Times.of(Ticks.duration(15), Duration.ofMillis(6000), Ticks.duration(20));
                        Title title = Title.title(
                                sTitle(Manager.getMessage().getTitle_settings().getLogin().get(0).getMainTitle(), 1),
                                sTitle(Manager.getMessage().getTitle_settings().getLogin().get(0).getSubTitle(), 2),
                                times
                        );
                        player.showTitle(title);
                        player.sendMessage(ComponentFormat.format(Manager.getMessage().getLogin().getLogin_request()));
                        sendToDate(player, playerProfile.isLogged());
                    }).delay(1L, TimeUnit.SECONDS).schedule();
                    return true;
                }
                playerProfile.setLogged(true);
                return true;
            }

            Main.getServer().getScheduler().buildTask(Main.getMain(), () -> {
                Title.Times times = Title.Times.of(Ticks.duration(15), Duration.ofMillis(6000), Ticks.duration(20));
                Title title = Title.title(
                        sTitle(Manager.getMessage().getTitle_settings().getRegister().get(0).getMainTitle(), 1),
                        sTitle(Manager.getMessage().getTitle_settings().getRegister().get(0).getSubTitle(), 2),
                        times
                );
                player.showTitle(title);
                player.sendMessage(ComponentFormat.format(Manager.getMessage().getRegistration().getRegister_request()));

                sendToDate(player, playerProfile.isLogged());
            }).delay(1L, TimeUnit.SECONDS).schedule();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (res != null) {
                    res.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
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
        PreparedStatement ps3 = null;
        ResultSet res = null;
        ResultSet res2 = null;

        try {
            con = Main.getMysql().getHikariDataSource().getConnection();

            ps2 = con.prepareStatement("SELECT * FROM auth WHERE realname=?");
            ps2.setString(1, player.getUsername());
            res = ps2.executeQuery();

            if (res.next()) {
                player.sendMessage(ComponentFormat.format(Manager.getMessage().getError_messages().getLogged_in()));
                return;
            }

            String ipaddress = player.getRemoteAddress().getAddress().toString();
            String[] regex_ip = ipaddress.split("/");

            ps3 = con.prepareStatement("SELECT COUNT(ipaddress) FROM auth WHERE ipaddress=?");
            ps3.setString(1, regex_ip[1]);
            res2 = ps3.executeQuery();
            if (res2.next()) {
                int count = res2.getInt("COUNT(ipaddress)");
                if (count == Manager.getSettings().getRestrictions().getMaxRegPerIp()) {
                    player.sendMessage(ComponentFormat.format(Manager.getMessage().getRegistration().getMaxregIP()));
                    return;
                }
            }
            ps = con.prepareStatement("INSERT INTO auth (realname, uuid, password, premium, registration_ipaddress, login_ipaddress, registration_time) VALUES (?,?,?,?,?,?,?)");

            ps.setString(1, player.getUsername());
            ps.setString(2, String.valueOf(player.getUniqueId()));
            ps.setString(3, EncryptionUtils.hashPassword(password, Type.valueOf(Manager.getSettings().getSecurity().getPasswordHash())));
            ps.setBoolean(4, false);
            ps.setString(5, regex_ip[1]);
            ps.setString(6, regex_ip[1]);
            ps.setString(7, new Date().toString());
            ps.executeUpdate();

            PlayerProfile playerProfile = Main.getPlayerAPIList().searchPlayer(player.getUsername());
            playerProfile.setLogged(true);
            sendToDate(player, playerProfile.isLogged());

            player.sendMessage(ComponentFormat.format(Manager.getMessage().getRegistration().getSuccess()));

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (res != null) {
                    res.close();
                    res2.close();
                }
                if (ps != null) {
                    ps.close();
                    ps2.close();
                    ps3.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
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

            if (res.next()) {
                PlayerProfile playerProfile = Main.getPlayerAPIList().searchPlayer(player.getUsername());
                if (playerProfile.isLogged()) {
                    player.sendMessage(ComponentFormat.format(Manager.getMessage().getError_messages().getLogged_in()));
                    return;
                }

                String hash = res.getString("password");
                if (EncryptionUtils.verifyPassword(password, hash, Type.valueOf(Manager.getSettings().getSecurity().getPasswordHash()))) {
                    playerProfile.setLogged(true);
                    sendToDate(player, playerProfile.isLogged());
                    player.sendMessage(ComponentFormat.format(Manager.getMessage().getLogin().getSuccess()));

                    String ipaddress = player.getRemoteAddress().getAddress().toString();
                    String[] regex_ip = ipaddress.split("/");

                    con = Main.getMysql().getHikariDataSource().getConnection();
                    ps = con.prepareStatement("UPDATE auth SET login_ipaddress=? WHERE realname=??");
                    ps.setString(1, regex_ip[1]);
                    ps.setString(2, player.getUsername());
                    ps.executeUpdate();
                    return;
                }

                if (Manager.getSettings().getRestrictions().isKickOnWrongPassword()) {
                    player.disconnect(ComponentFormat.format(Manager.getMessage().getLogin().getWrong_password()));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (res != null) {
                    res.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void premiumCommand(Player player) {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement insert = null;
        ResultSet res = null;

        try {
            con = Main.getMysql().getHikariDataSource().getConnection();
            ps = con.prepareStatement("SELECT * FROM auth WHERE realname=?");
            ps.setString(1, player.getUsername());
            res = ps.executeQuery();

            if (res.next()) {
                boolean isPremium = res.getBoolean("premium");

                if (isPremium) {
                    player.sendMessage(ComponentFormat.format(Manager.getMessage().getPremium().getAlready_premium()));
                    return;
                }

                if (isPremiumUser(player.getUsername())) {
                    insert = con.prepareStatement("UPDATE auth SET premium=? WHERE realname=?");
                    insert.setBoolean(1, true);
                    insert.setString(2, player.getUsername());
                    insert.executeUpdate();

                    player.sendMessage(ComponentFormat.format(Manager.getMessage().getPremium().getSuccess()));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (res != null) {
                    res.close();
                }
                if (ps != null && insert != null) {
                    ps.close();
                    insert.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public static void connectionHandler(PreLoginEvent event) {
        PlayerProfile playerProfile = new PlayerProfile();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet res = null;

        try {
            con = Main.getMysql().getHikariDataSource().getConnection();
            ps = con.prepareStatement("SELECT * FROM auth WHERE realname=?");
            ps.setString(1, event.getUsername());
            res = ps.executeQuery();

            if (res.next()) {
                boolean isPremium = res.getBoolean("premium");

                playerProfile.setName(event.getUsername());
                playerProfile.setLogged(false);
                playerProfile.setPremium(false);

                if (isPremium) {
                    playerProfile.setPremium(true);
                    playerProfile.setLogged(true);
                    event.setResult(PreLoginEvent.PreLoginComponentResult.forceOnlineMode());
                }
                Main.getPlayerAPIList().addPlayer(playerProfile);
                return;
            }

            playerProfile.setName(event.getUsername());
            playerProfile.setLogged(false);
            playerProfile.setPremium(false);
            Main.getPlayerAPIList().addPlayer(playerProfile);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (res != null) {
                    res.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isPremiumUser(String paramString) {
        return !MojangAPI.Mojang(paramString).equals("NONE");
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

    public static void sendToDate(Player player, boolean paramBoolean) {
        Optional<ServerConnection> server = player.getCurrentServer();

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("auth");
        out.writeUTF(player.getUsername());
        out.writeBoolean(paramBoolean);

        server.ifPresent(serverConnection -> serverConnection.sendPluginMessage(Main.MODERN_BUNGEE_CHANNEL, out.toByteArray()));

    }
}
