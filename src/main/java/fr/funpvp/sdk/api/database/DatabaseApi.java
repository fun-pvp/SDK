package fr.funpvp.sdk.api.database;

import fr.funpvp.sdk.api.database.mongodb.MongoDatabase;
import fr.funpvp.sdk.api.database.redis.RedisDatabase;
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
public class DatabaseApi extends Api {
    private MongoDatabase mongoDatabase;
    private RedisDatabase redisDatabase;

    public DatabaseApi() {
        super(ApiType.DATABASE);
    }

    @Override
    public void onApiLoad() {
        this.mongoDatabase = new MongoDatabase(DatabaseConfiguration.MONGODB_URI, DatabaseConfiguration.DATABASE_NAME);
        this.redisDatabase = new RedisDatabase(DatabaseConfiguration.REDIS_HOST, DatabaseConfiguration.REDIS_PORT, DatabaseConfiguration.REDIS_PASSWORD);
    }

    @Override
    public void onApiUnload() {
        this.redisDatabase.shutdown();
        this.mongoDatabase.close();
    }
}
