package me.biglev.velocityauth;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.LegacyChannelIdentifier;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import me.biglev.velocityauth.commands.LoginCommand;
import me.biglev.velocityauth.commands.RegisterCommand;
import me.biglev.velocityauth.listeners.PlayerCommandHandler;
import me.biglev.velocityauth.listeners.PlayerLogin;
import me.biglev.velocityauth.listeners.PlayerMessageHandler;
import me.biglev.velocityauth.utils.api.PlayerAPIList;
import me.biglev.velocityauth.utils.settings.Manager;
import me.biglev.velocityauth.utils.sql.Mysql;
import org.slf4j.Logger;

import java.nio.file.Path;

@Plugin(
        id = "auth",
        name = "VelocityAuth",
        version = "1.0-SNAPSHOT",
        description = "This is the authentication plugin",
        authors = {"BigLev"}
)
public class Main {

    private static Logger logger;
    private static ProxyServer server;
    private static Path path;
    private static Mysql mysql;
    private static Main main;
    private static PlayerAPIList playerAPIList;
    public static final LegacyChannelIdentifier LEGACY_BUNGEE_CHANNEL = new LegacyChannelIdentifier("BungeeCord");
    public static final MinecraftChannelIdentifier MODERN_BUNGEE_CHANNEL = MinecraftChannelIdentifier.create("bungeecord", "main");

    @Inject
    public Main(Logger logger, ProxyServer server, @DataDirectory Path path) {
        this.logger = logger;
        this.server = server;
        this.path = path;
        main = this;
        playerAPIList = new PlayerAPIList();
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        CommandManager commandManager = server.getCommandManager();

        //Command meta
        CommandMeta meta = commandManager.metaBuilder("register")
                .aliases("reg")
                .build();
        CommandMeta meta1 = commandManager.metaBuilder("login")
                .aliases("l")
                .build();

        //Commands
        commandManager.register(meta, new RegisterCommand());
        commandManager.register(meta1, new LoginCommand());

        //Json manager
        Manager manager = new Manager();
        manager.setupConfig();

        //Mysql connection
        mysql = new Mysql();
        mysql.setupDatabase();
        mysql.createTable();

        server.getEventManager().register(this, new PlayerLogin());
        server.getEventManager().register(this, new PlayerMessageHandler());
        server.getEventManager().register(this, new PlayerCommandHandler());

        server.getChannelRegistrar().register(LEGACY_BUNGEE_CHANNEL, MODERN_BUNGEE_CHANNEL);
    }

    public static Mysql getMysql() {
        return mysql;
    }

    public static Path getPath() {
        return path;
    }

    public static Logger getLogger() {
        return logger;
    }

    public static ProxyServer getServer() {
        return server;
    }

    public static Main getMain() {
        return main;
    }

    public static PlayerAPIList getPlayerAPIList() {
        return playerAPIList;
    }

}
