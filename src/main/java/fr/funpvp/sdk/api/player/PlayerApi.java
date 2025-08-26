package fr.funpvp.sdk.api.player;

import fr.funpvp.sdk.manager.Api;
import fr.funpvp.sdk.manager.ApiType;
import lombok.Getter;

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
public class PlayerApi extends Api {
    private final PlayerRegistry registry;

    public PlayerApi() {
        super(ApiType.PLAYERS);

        this.addDependencies(ApiType.DATABASE, ApiType.RANKS, ApiType.ECONOMY);
        this.registry = new PlayerRegistry(this.getDependency(ApiType.DATABASE));
    }

    @Override
    public void onApiLoad() {}

    @Override
    public void onApiUnload() {}
}
