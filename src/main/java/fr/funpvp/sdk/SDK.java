package fr.funpvp.sdk;

import org.bukkit.plugin.java.JavaPlugin;

public final class SDK extends JavaPlugin {
    private static SDK instance;

    @Override
    public void onEnable() {
        instance = this;
    }

    @Override
    public void onDisable() {
    }

    public static SDK get() {
        return instance;
    }
}
