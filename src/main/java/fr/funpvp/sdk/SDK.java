package fr.funpvp.sdk;

import fr.funpvp.sdk.manager.ApiManagement;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class SDK extends JavaPlugin {
    private static SDK instance;

    private ApiManagement apiManagement;

    @Override
    public void onEnable() {
        instance = this;

        apiManagement = new ApiManagement();
    }

    @Override
    public void onDisable() {
        apiManagement.disableAll();
    }

    public static SDK get() {
        return instance;
    }
}
