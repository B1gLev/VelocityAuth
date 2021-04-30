package me.biglev.velocityauth.utils.api.mojang;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class MojangAPI {

    public static String Mojang(String paramString) {
        String api = "https://api.mojang.com/users/profiles/minecraft/" + paramString;

        try {
            URL url = new URL(api);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();

            int code = urlConnection.getResponseCode();

            if (code == 200) {
                Scanner scanner = new Scanner(urlConnection.getInputStream());
                String line = scanner.nextLine();
                scanner.close();
                JsonObject obj = new Gson().fromJson(line, JsonObject.class);
                urlConnection.disconnect();
                return obj.get("id").getAsString();
            } else {
                urlConnection.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "NONE";
    }
}
