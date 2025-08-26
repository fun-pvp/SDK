package fr.funpvp.sdk.api.player;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import fr.funpvp.sdk.SDK;
import fr.funpvp.sdk.api.database.DatabaseApi;
import fr.funpvp.sdk.api.rank.Rank;
import fr.funpvp.sdk.api.rank.RankApi;
import fr.funpvp.sdk.utils.GsonProvider;
import fr.funpvp.sdk.manager.ApiType;
import fr.funpvp.sdk.utils.GsonUtils;
import io.lettuce.core.api.async.RedisAsyncCommands;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.*;
import java.util.concurrent.ExecutionException;

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
@Setter
public class FunPlayer {
    private final int id;
    private final UUID uniqueId;
    private String name;

    private List<Rank> ranks;

    public FunPlayer(int id, UUID uniqueId, String name) {
        this.id = id;
        this.uniqueId = uniqueId;
        this.name = name;

        this.ranks = new ArrayList<>();
    }

    public Rank getRank() {
        if (this.ranks.isEmpty()) {
            return null;
        }

        return this.ranks.stream().findFirst().orElse(null);
    }

    public void addRank(Rank rank) {
        if (!this.ranks.contains(rank.getName())) {
            this.ranks.add(rank);
        }
    }

    public static FunPlayer fromJson(String json) {
        return (FunPlayer) GsonUtils.fromJson(json, FunPlayer.class);
    }

    public String toJson() {
        return GsonProvider.GSON.toJson(this);
    }

    public void saveMongo() {
        Document document = Document.parse(toJson());
        SDK.get().getApiManagement().get(ApiType.DATABASE, DatabaseApi.class).getMongoDatabase().getDatabase().getCollection("users")
                .updateOne(Filters.eq("uniqueId", this.uniqueId.toString()),
                        (Bson) new Document("$set", checkIfValid(document)), new UpdateOptions().upsert(true));
    }

    public FunPlayer saveCache() throws Exception {
        try {
            RedisAsyncCommands<String, String> commands = SDK.get().getApiManagement().get(ApiType.DATABASE, DatabaseApi.class)
                    .getRedisDatabase()
                    .getAsyncCommands();

            commands.hset("users", this.uniqueId.toString(), toJson()).get();
            SDK.get().getApiManagement().get(ApiType.DATABASE, DatabaseApi.class).getRedisDatabase().returnConnection(commands.getStatefulConnection());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return this;
    }

    public void deleteCache() throws Exception {
        try {
            RedisAsyncCommands<String, String> commands = SDK.get().getApiManagement().get(ApiType.DATABASE, DatabaseApi.class)
                    .getRedisDatabase()
                    .getAsyncCommands();

            commands.hdel("users", this.uniqueId.toString()).get();
            SDK.get().getApiManagement().get(ApiType.DATABASE, DatabaseApi.class).getRedisDatabase().returnConnection(commands.getStatefulConnection());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }


    public boolean isOnline() {
        return SDK.get().getApiManagement().get(ApiType.PLAYERS, PlayerApi.class).getRegistry().getAllUsersOnline().contains(this.uniqueId);
    }

    public void save() throws Exception {
        if (isOnline()) {
            saveCache();
        } else {
            saveMongo();
        }
    }

    public static Document checkIfValid(Document playerDocument) {
        String defaultJson = new FunPlayer(playerDocument.getInteger("id"), UUID.fromString(playerDocument.get("uniqueId").toString()), playerDocument.getString("name")).toJson();
        Document defaultDocument = Document.parse(defaultJson);
        if (defaultDocument.size() >= playerDocument.size()) {
            Set<String> allKeys = defaultDocument.keySet();
            for (String key : allKeys) {
                if (!playerDocument.containsKey(key))
                    playerDocument.append(key, defaultDocument.get(key));
            }
        }
        return playerDocument;
    }

    public Document toDocument() {
        return Document.parse(toJson());
    }

    public static FunPlayer fromDocument(Document document) {
        return fromJson(document.toJson());
    }

    public static Object fromJson(Object o) {
        return GsonProvider.GSON.fromJson(GsonProvider.GSON.toJson(o), o.getClass());
    }
}
