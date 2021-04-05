package me.biglev.velocityauth.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class ComponentFormat {

    public static Component format(String str) {
        final Component component = LegacyComponentSerializer.legacyAmpersand().deserialize(str);
        return component;
    }
}
