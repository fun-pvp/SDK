package fr.funpvp.sdk.manager;

import fr.funpvp.sdk.logger.SdkLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/*
 * Project : fun-pvp
 * Author  : TRAO
 * Date    : 26/08/2025
 *
 * Description :
 * This file is part of the fun-pvp project.
 * Any unauthorized reproduction or distribution is strictly prohibited.
 */
public class ApiRegistry {
    private final ConcurrentHashMap<ApiType, Api> apis = new ConcurrentHashMap<>();

    public void enable(Api api) {
        for (ApiType dependency : api.getDependencies()) {
            if (!apis.containsKey(dependency)) {
                SdkLogger.error("API " + api.getType() + " requires " + dependency + " to be enabled.");
                return;
            }
        }

        apis.put(api.getType(), api);
        api.onApiLoad();
    }

    public Api get(ApiType type) {
        return apis.get(type);
    }

    public void disable(Api api) {
        apis.remove(api.getType());

        api.onApiUnload();
    }

    public List<Api> getApis() {
        return new ArrayList<>(apis.values());
    }
}
