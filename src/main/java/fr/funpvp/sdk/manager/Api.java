package fr.funpvp.sdk.manager;

import fr.funpvp.sdk.SDK;
import lombok.Getter;

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
@Getter
public abstract class Api {
    private final ApiType type;
    private final List<ApiType> dependencies;

    public Api(ApiType type) {
        this.type = type;
        this.dependencies = List.of();
    }

    public abstract void onApiLoad();
    public abstract void onApiUnload();

    public void addDependency(ApiType dependency) {
        this.dependencies.add(dependency);
    }

    public ApiManagement getManagement() {
        return SDK.get().getApiManagement();
    }
}