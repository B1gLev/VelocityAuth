package me.biglev.velocityauth.utils.settings;

import com.google.gson.Gson;
import me.biglev.velocityauth.Main;

import java.io.*;
import java.nio.file.Files;

public class Manager {

    private static Gson gson;
    private static File message = new File(Main.getPath().toFile() + "/message.json");
    private static File settings = new File(Main.getPath().toFile() + "/settings.json");

    public void setupConfig(){
        if (!Main.getPath().toFile().exists()){
            Main.getPath().toFile().mkdirs();

            if (!new File(Main.getPath().toString() + "message.json").exists() ) {

                try (InputStream in1 = Main.class.getResourceAsStream("/" + message.getName())) {
                    if (in1 != null && in1 != null){
                        Files.copy(in1, message.toPath());
                        Main.getLogger().info("Message.json was created!");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (!new File(Main.getPath().toString() + "settings.json").exists()){
                try (InputStream in2 = Main.class.getResourceAsStream("/" + settings.getName())) {

                    if (in2 != null && in2 != null){
                        Files.copy(in2, settings.toPath());
                        Main.getLogger().info("Settings.json  was created!");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Message getMessage(){
        gson = new Gson();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(message));
            Message message = gson.fromJson(reader, Message.class);

            return message;
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public static Settings getSettings(){
        gson = new Gson();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(settings));
            Settings settings = gson.fromJson(reader, Settings.class);

            return settings;
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

}
