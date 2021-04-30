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
import me.biglev.velocityauth.commands.PremiumCommand;
import me.biglev.velocityauth.commands.RegisterCommand;
import me.biglev.velocityauth.listeners.PlayerCommandHandler;
import me.biglev.velocityauth.listeners.PlayerLogin;
import me.biglev.velocityauth.listeners.PlayerMessageHandler;
import me.biglev.velocityauth.utils.api.PlayerAPI;
import me.biglev.velocityauth.utils.settings.Manager;
import me.biglev.velocityauth.utils.sql.Mysql;
import org.slf4j.Logger;

import java.nio.file.Path;

@Plugin(
        id = "auth",
        name = "VelocityAuth",
        version = "1.6-SNAPSHOT",
        description = "This is the authentication plugin",
        authors = {"BigLev"}
)
public class Main {

    private static Logger logger;
    private static ProxyServer server;
    private static Path path;
    private static Mysql mysql;
    private static Main main;
    private static PlayerAPI playerAPI;
    public static final LegacyChannelIdentifier LEGACY_BUNGEE_CHANNEL = new LegacyChannelIdentifier("BungeeCord");
    public static final MinecraftChannelIdentifier MODERN_BUNGEE_CHANNEL = MinecraftChannelIdentifier.create("auth", "main");

    @Inject
    public Main(Logger logger, ProxyServer server, @DataDirectory Path path) {
        this.logger = logger;
        this.server = server;
        this.path = path;
        main = this;
        playerAPI = new PlayerAPI();
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        CommandManager commandManager = server.getCommandManager();

        //Json manager
        Manager manager = new Manager();
        manager.setupConfig();

        //Command meta
        CommandMeta reg = commandManager.metaBuilder("register")
                .aliases("reg")
                .build();
        CommandMeta login = commandManager.metaBuilder("login")
                .aliases("l")
                .build();

        CommandMeta premium = commandManager.metaBuilder("premium").build();

        //Commands
        commandManager.register(reg, new RegisterCommand());
        commandManager.register(login, new LoginCommand());

        if (Manager.getSettings().getPremiumAuthentication().isPremiumCommand()) {
            commandManager.register(premium, new PremiumCommand());
        }

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

    public static PlayerAPI getPlayerAPIList() {
        return playerAPI;
    }

}
