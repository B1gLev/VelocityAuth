package me.biglev.velocityauth.utils.api.mojang;

import java.net.HttpURLConnection;
import java.net.URL;

public class MojangAPI {

    public static void callAPI(String paramString) {
        String api = "https://api.mojang.com/users/profiles/minecraft/" + paramString;

        try {
            URL url = new URL(api);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();

            int code = urlConnection.getResponseCode();

            if (code == 200) {
                //Stuff
            } else {
                urlConnection.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
