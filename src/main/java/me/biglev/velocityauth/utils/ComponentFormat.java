package me.biglev.velocityauth.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class ComponentFormat {

    public static Component format(String str) {
        return MiniMessage.get().parse(str);
    }
}
