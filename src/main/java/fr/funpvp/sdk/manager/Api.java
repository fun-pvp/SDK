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

    protected void addDependency(ApiType dependency) {
        this.dependencies.add(dependency);
    }

    protected void addDependencies(ApiType... dependencies) {
        this.dependencies.addAll(List.of(dependencies));
    }

    protected <T extends Api> T getDependency(ApiType dependency) {
        return (T) getManagement().get(dependency, dependency.getAClass());
    }

    protected ApiManagement getManagement() {
        return SDK.get().getApiManagement();
    }
}