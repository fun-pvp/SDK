package fr.funpvp.sdk.api.rank;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import fr.funpvp.sdk.SDK;
import fr.funpvp.sdk.api.database.DatabaseApi;
import fr.funpvp.sdk.manager.ApiType;
import fr.funpvp.sdk.utils.GsonProvider;
import fr.funpvp.sdk.utils.GsonUtils;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;

import java.util.List;

@Getter
@Setter
public class Rank {
    private String name;
    private String prefix;
    private String tabPrefix;
    private String color;
    private int priority;

    private List<String> permissions;

    private int friendsLimits;
    private int groupsLimits;
    private int coinsBooster;

    public Rank(String name, String prefix, String tabPrefix, String color, int priority, List<String> permissions, int friendsLimits, int groupsLimits, int coinsBooster) {
        this.name = name;
        this.prefix = prefix;
        this.tabPrefix = tabPrefix;
        this.color = color.replace("&", "§");
        this.priority = priority;
        this.permissions = permissions;
        this.friendsLimits = friendsLimits;
        this.groupsLimits = groupsLimits;
        this.coinsBooster = coinsBooster;
    }

    public String toJson() {
        return GsonProvider.GSON.toJson(this);
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        return GsonUtils.fromJson(json, clazz);
    }

    public Document toDocument() {
        return Document.parse(toJson());
    }

    public void save() {
        SDK.get().getApiManagement().get(ApiType.DATABASE, DatabaseApi.class)
                .getMongoDatabase()
                .getDatabase()
                .getCollection("ranks")
                .updateOne(
                        Filters.eq("name", this.name),
                        new Document("$set", toDocument()),
                        new UpdateOptions().upsert(true)
                );
    }

    public void delete() {
        SDK.get().getApiManagement().get(ApiType.DATABASE, DatabaseApi.class)
                .getMongoDatabase()
                .getDatabase()
                .getCollection("ranks")
                .deleteOne(Filters.eq("name", this.name));
    }
}