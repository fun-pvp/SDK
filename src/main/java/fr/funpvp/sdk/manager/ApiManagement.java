package fr.funpvp.sdk.manager;

import fr.funpvp.sdk.logger.SdkLogger;

import java.util.ArrayList;
import java.util.List;

/*
 * Project : fun-pvp
 * Author  : TRAO
 * Date    : 26/08/2025
 *
 * Description :
 * This file is part of the fun-pvp project.
 * Any unauthorized reproduction or distribution is strictly prohibited.
 */
public class ApiManagement {
    private final ApiRegistry registry;

    public ApiManagement() {
        registry = new ApiRegistry();
    }

    public void enable(Api api) {
        registry.enable(api);

        SdkLogger.info(api.getClass().getSimpleName() + " has been enabled.");
    }

    public void disable(Api api) {
        registry.disable(api);

        SdkLogger.info(api.getClass().getSimpleName() + " has been disabled.");
    }

    public <T extends Api> T get(ApiType type, Class<T> clazz) {
        Api api = registry.get(type);
        if (!clazz.isInstance(api)) {
            throw new IllegalStateException("API stored is not of type " + clazz.getSimpleName());
        }
        return clazz.cast(api);
    }

    @SuppressWarnings("unchecked")
    public <T extends Api> T get(ApiType type) {
        Api api = registry.get(type);
        if (api == null) return null;

        return (T) api;
    }


    public List<Api> getApis() {
        return registry.getApis();
    }

    public void disableAll() {
        for(Api api : new ArrayList<>(registry.getApis())) {
            disable(api);
        }
        registry.getApis().clear();
    }
}
