package fr.funpvp.sdk.api.player;

import com.mongodb.client.model.Filters;
import fr.funpvp.sdk.SDK;
import fr.funpvp.sdk.api.database.DatabaseApi;
import fr.funpvp.sdk.api.rank.RankApi;
import fr.funpvp.sdk.manager.ApiType;
import io.lettuce.core.api.async.RedisAsyncCommands;
import lombok.SneakyThrows;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.json.JsonMode;
import org.bson.json.JsonWriterSettings;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/*
 * Project : fun-pvp
 * Author  : TRAO
 * Date    : 26/08/2025
 *
 * Description :
 * This file is part of the fun-pvp project.
 * Any unauthorized reproduction or distribution is strictly prohibited.
 */
public class PlayerRegistry {

    private final DatabaseApi databaseApi;

    public PlayerRegistry(DatabaseApi databaseApi) {
        this.databaseApi = databaseApi;
    }

    @SneakyThrows
    public List<FunPlayer> getAllUsersOnline() {
        try {
            RedisAsyncCommands<String, String> commands = databaseApi.getRedisDatabase().getAsyncCommands();
            Map<String, String> allUsers = commands.hgetall("users").get();
            databaseApi.getRedisDatabase().returnConnection(commands.getStatefulConnection());

            return allUsers.values()
                    .stream()
                    .map(FunPlayer::fromJson)
                    .collect(Collectors.toList());

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public boolean createUser(UUID uniqueId, String name) throws Exception {
        FunPlayer player = getUser(uniqueId, Type.MONGODB);
        boolean isNew = false;

        if (player == null) {
            int newId = getUsers().size() + 1;
            player = new FunPlayer(newId, uniqueId, name);
            player.addRank(SDK.get().getApiManagement().get(ApiType.RANKS, RankApi.class).getRegistry().getRank("default"));
            isNew = true;
        } else {
            player = FunPlayer.fromDocument(FunPlayer.checkIfValid(player.toDocument()));
        }

        if (!Objects.equals(player.getName(), name)) {
            player.setName(name);
        }

        player.saveCache();
        player.saveMongo();
        return isNew;
    }

    public FunPlayer getUser(UUID uniqueId) {
        FunPlayer profile = getUser(uniqueId, Type.REDIS);
        if (profile == null) profile = getUser(uniqueId, Type.MONGODB);
        return profile;
    }

    public FunPlayer getUserOffline(UUID uniqueId) {
        return getUser(uniqueId, Type.MONGODB);
    }

    public FunPlayer getUserOnline(UUID uniqueId) {
        return getUser(uniqueId, Type.REDIS);
    }

    public FunPlayer getUserOnline(String name) {
        return getAllUsersOnline().stream()
                .filter(user -> user.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    @SneakyThrows
    public FunPlayer getUser(UUID uniqueId, Type type) {
        if (type == Type.REDIS) {
            try {
                RedisAsyncCommands<String, String> commands = databaseApi.getRedisDatabase().getAsyncCommands();
                String json = commands.hget("users", uniqueId.toString()).get();
                databaseApi.getRedisDatabase().returnConnection(commands.getStatefulConnection());
                return FunPlayer.fromJson(json);

            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return null;
            }
        }

        if (type == Type.MONGODB) {
            Document document = databaseApi.getMongoDatabase().getDatabase()
                    .getCollection("users")
                    .find(Filters.eq("uniqueId", uniqueId.toString()))
                    .first();
            if (document == null) return null;
            return FunPlayer.fromJson(document.toJson(
                    JsonWriterSettings.builder().outputMode(JsonMode.RELAXED).build()
            ));
        }

        return null;
    }

    public List<FunPlayer> getUsers() {
        List<Document> documents = databaseApi.getMongoDatabase().getDatabase()
                .getCollection("users")
                .find()
                .into(new ArrayList<>());

        return documents.stream()
                .map(doc -> FunPlayer.fromJson(doc.toJson(
                        JsonWriterSettings.builder().outputMode(JsonMode.RELAXED).build()
                )))
                .collect(Collectors.toList());
    }

    public FunPlayer getUserByName(String name, Type type) {
        if (type == Type.MONGODB) {
            Bson filter = Filters.eq("name", Pattern.compile("^" + Pattern.quote(name) + "$", Pattern.CASE_INSENSITIVE));
            Document document = databaseApi.getMongoDatabase().getDatabase()
                    .getCollection("users")
                    .find(filter)
                    .first();
            if (document == null) return null;
            return FunPlayer.fromJson(document.toJson(
                    JsonWriterSettings.builder().outputMode(JsonMode.RELAXED).build()
            ));
        }

        if (type == Type.REDIS) {
            return getAllUsersOnline().stream()
                    .filter(profile -> profile.getName().equalsIgnoreCase(name))
                    .findFirst()
                    .orElse(null);
        }

        return null;
    }

    public FunPlayer getUser(String name) {
        FunPlayer user = getUserByName(name, Type.REDIS);
        if (user == null) user = getUserByName(name, Type.MONGODB);
        return user;
    }

    public int getTotalUsersOnline() {
        return getAllUsersOnline().size();
    }

    public enum Type {
        MONGODB, REDIS
    }
}
