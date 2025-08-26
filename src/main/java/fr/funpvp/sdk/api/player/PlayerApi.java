package fr.funpvp.sdk.api.player;

import fr.funpvp.sdk.manager.Api;
import fr.funpvp.sdk.manager.ApiType;

/*
 * Project : fun-pvp
 * Author  : TRAO
 * Date    : 26/08/2025
 *
 * Description :
 * This file is part of the fun-pvp project.
 * Any unauthorized reproduction or distribution is strictly prohibited.
 */
public class PlayerApi extends Api {
    public PlayerApi() {
        super(ApiType.PLAYERS);

        this.addDependency(ApiType.DATABASE);
    }

    @Override
    public void onApiLoad() {

    }

    @Override
    public void onApiUnload() {

    }
}
