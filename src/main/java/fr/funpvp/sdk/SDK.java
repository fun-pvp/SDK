package fr.funpvp.sdk;

import fr.funpvp.sdk.manager.ApiManagement;
import lombok.Getter;

@Getter
public final class SDK {
    private static SDK instance;

    private ApiManagement apiManagement;

    public void load() {
        instance = this;

        apiManagement = new ApiManagement();
    }

    public void unload() {
        apiManagement.disableAll();
    }

    public static SDK get() {
        return instance;
    }
}
