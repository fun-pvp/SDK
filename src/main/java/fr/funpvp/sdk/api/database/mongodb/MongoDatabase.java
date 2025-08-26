package fr.funpvp.sdk.api.database.mongodb;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import fr.funpvp.sdk.logger.SdkLogger;
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
public class MongoDatabase {

    private final MongoClient mongoClient;
    private final com.mongodb.client.MongoDatabase database;

    public MongoDatabase(String uri, String dbName) {
        ConnectionString connectionString = new ConnectionString(uri);

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .retryWrites(true)
                .build();

        this.mongoClient = MongoClients.create(settings);
        this.database = mongoClient.getDatabase(dbName);

        SdkLogger.info("Connected to MongoDB.");
    }

    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
            SdkLogger.info("MongoDB client has been closed.");
        }
    }
}
