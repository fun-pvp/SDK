package fr.funpvp.sdk.api.rank;

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
public class RankApi extends Api {
    private RankRegistry registry;

    public RankApi() {
        super(ApiType.RANKS);

        this.addDependency(ApiType.DATABASE);
        this.registry = new RankRegistry();
    }

    @Override
    public void onApiLoad() {
        this.registry.createRanks();
    }

    @Override
    public void onApiUnload() {

    }
}
