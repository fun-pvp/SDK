package fr.funpvp.sdk.manager;

import fr.funpvp.sdk.api.database.DatabaseApi;
import fr.funpvp.sdk.api.player.PlayerApi;
import fr.funpvp.sdk.api.rank.RankApi;
import fr.funpvp.sdk.api.sanction.SanctionApi;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
@AllArgsConstructor
public enum ApiType {
    PLAYERS(PlayerApi.class),
    DATABASE(DatabaseApi.class),
    RANKS(RankApi.class),
    ECONOMY(null),
    SANCTION(SanctionApi.class),
    INVENTORY(null),
    COMMANDS(null);

    private Class<? extends Api> aClass;

    public void setAClass(Class<? extends Api> aClass) {
        this.aClass = aClass;
    }
}

