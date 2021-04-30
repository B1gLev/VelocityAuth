package me.biglev.paper;

import me.biglev.paper.listeners.PlayerHandler;
import me.biglev.paper.listeners.PluginMessage;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class Main extends JavaPlugin {

    private static Main instance;
    public HashMap<String, Boolean> db = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;
        getServer().getMessenger().registerIncomingPluginChannel(this, "auth:main", new PluginMessage());
        getServer().getPluginManager().registerEvents(new PlayerHandler(), this);
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public static Main getInstance() {
        return instance;
    }
}
