package fr.funpvp.sdk.api.database.redis;

import fr.funpvp.sdk.logger.SdkLogger;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.support.ConnectionPoolSupport;
import lombok.Getter;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.time.Duration;

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
public class RedisDatabase {

    private final RedisClient redisClient;
    private GenericObjectPool<StatefulRedisConnection<String, String>> pool;

    public RedisDatabase(String host, int port, String password) {
        RedisURI redisURI = RedisURI.Builder.redis(host, port)
                .withTimeout(Duration.ofSeconds(10))
                .build();

        if (password != null && !password.isEmpty()) {
            redisURI.setPassword(password);
        }

        this.redisClient = RedisClient.create(redisURI);

        GenericObjectPoolConfig<StatefulRedisConnection<String, String>> poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setMaxTotal(10);
        poolConfig.setMaxIdle(5);
        poolConfig.setMinIdle(1);
        poolConfig.setBlockWhenExhausted(true);

        this.pool = ConnectionPoolSupport.createGenericObjectPool(
                () -> redisClient.connect(), poolConfig
        );

        SdkLogger.info("Redis client initialized.");
    }

    public RedisAsyncCommands<String, String> getAsyncCommands() throws Exception {
        StatefulRedisConnection<String, String> connection = pool.borrowObject();
        return connection.async();
    }

    public void returnConnection(StatefulRedisConnection<String, String> connection) {
        if (connection != null) {
            pool.returnObject(connection);
        }
    }

    public void shutdown() {
        if (pool != null) {
            pool.close();
        }
        if (redisClient != null) {
            SdkLogger.info("Shutting down redis client...");
            redisClient.shutdown();
        }
    }
}
