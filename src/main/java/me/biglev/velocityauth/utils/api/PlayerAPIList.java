package me.biglev.velocityauth.utils.api;

import java.util.HashMap;

public class PlayerAPIList {

    private HashMap<String, PlayerAPI> playerAPIS = new HashMap<>();

    public void addPlayer(PlayerAPI paramPlayer){
        if (playerAPIS.get(paramPlayer.getName()) == null)
            playerAPIS.put(paramPlayer.getName(), paramPlayer);
    }

    public PlayerAPI searchPlayer(String paramString) {
        PlayerAPI playerAPI = playerAPIS.get(paramString);
        if (playerAPI != null){
            return playerAPI;
        }
        return null;
    }
}
