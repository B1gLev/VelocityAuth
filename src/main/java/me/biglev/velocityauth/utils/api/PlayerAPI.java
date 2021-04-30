package me.biglev.velocityauth.utils.api;

import java.util.HashMap;

public class PlayerAPI {

    private HashMap<String, PlayerProfile> playerAPIS = new HashMap<>();

    public void addPlayer(PlayerProfile paramPlayer){
        playerAPIS.putIfAbsent(paramPlayer.getName(), paramPlayer);
    }

    public PlayerProfile searchPlayer(String paramString) {
        PlayerProfile playerProfile = playerAPIS.get(paramString);
        if (playerProfile != null){
            return playerProfile;
        }
        return null;
    }
}
