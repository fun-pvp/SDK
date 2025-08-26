package fr.funpvp.sdk.api.rank;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import fr.funpvp.sdk.SDK;
import fr.funpvp.sdk.api.database.DatabaseApi;
import fr.funpvp.sdk.manager.ApiType;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.json.JsonMode;
import org.bson.json.JsonWriterSettings;

import java.util.ArrayList;
import java.util.List;

public class RankRegistry {

    private static volatile RankRegistry instance;
    private static final JsonWriterSettings JSON_SETTINGS =
            JsonWriterSettings.builder().outputMode(JsonMode.RELAXED).build();

    private final MongoCollection<Document> collection;

    public RankRegistry() {
        this.collection = SDK.get().getApiManagement().get(ApiType.DATABASE, DatabaseApi.class)
                .getMongoDatabase().getDatabase().getCollection("ranks");
        instance = this;
    }

    public static RankRegistry get() {
        if (instance == null) {
            instance = new RankRegistry();
        }
        return instance;
    }

    public void createDefaultRanks(List<Rank> ranks) {
        long count = collection.countDocuments();
        if (count == 0) {
            List<Document> documents = new ArrayList<>();
            for (Rank rank : ranks) {
                documents.add(rank.toDocument());
            }
            if (!documents.isEmpty()) {
                collection.insertMany(documents);
            }
        }
    }

    public void createRanks() {
        List<Rank> ranks = new ArrayList<>();

        Rank adminRank = new Rank(
                "admin",
                "Admin",
                "Admin",
                "&4",
                1,
                List.of("*"),
                Integer.MAX_VALUE,
                Integer.MAX_VALUE,
                200
        );
        ranks.add(adminRank);

        Rank defaultRank = new Rank(
                "default",
                "Default",
                "Player",
                "&7",
                99,
                List.of("PLAYER"),
                8,
                4,
                0
        );
        ranks.add(defaultRank);

        createDefaultRanks(ranks);
    }

    public Rank getRank(String name) {
        Bson filter = Filters.eq("name", name);
        Document doc = collection.find(filter).first();
        return doc != null ? Rank.fromJson(doc.toJson(JSON_SETTINGS), Rank.class) : null;
    }

    public List<Rank> getAllRanks() {
        List<Rank> ranks = new ArrayList<>();
        for (Document doc : collection.find()) {
            ranks.add(Rank.fromJson(doc.toJson(JSON_SETTINGS), Rank.class));
        }
        return ranks;
    }
}
