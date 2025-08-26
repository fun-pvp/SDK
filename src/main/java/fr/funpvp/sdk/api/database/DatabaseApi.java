package fr.funpvp.sdk.api.database;

import fr.funpvp.sdk.api.database.mongodb.MongoDatabase;
import fr.funpvp.sdk.api.database.redis.RedisDatabase;
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
public class DatabaseApi extends Api {
    private MongoDatabase mongoDatabase;
    private RedisDatabase redisDatabase;

    public DatabaseApi() {
        super(ApiType.DATABASE);
    }

    @Override
    public void onApiLoad() {
        this.mongoDatabase = new MongoDatabase();
        this.redisDatabase = new RedisDatabase();
    }

    @Override
    public void onApiUnload() {

    }
}
