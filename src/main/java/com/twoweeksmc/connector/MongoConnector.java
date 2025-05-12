package com.twoweeksmc.connector;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.twoweeksmc.connector.model.ServerModel;
import com.twoweeksmc.connector.model.UserModel;
import de.eztxm.ezlib.config.JsonConfig;
import lombok.Getter;
import org.bson.Document;

@Getter
public class MongoConnector {
    private final MongoClient mongoClient;
    private final MongoDatabase mongoDatabase;
    private final MongoCollection<Document> serverCollection;
    private final MongoCollection<Document> userCollection;
    private final UserModel userModel;
    private final ServerModel serverModel;

    public MongoConnector(JsonConfig databaseConfiguration) {
        this.mongoClient = MongoClients.create(
                databaseConfiguration.get("protocol").asString()
                        + "://" + databaseConfiguration.get("username").asString()
                        + ":" + databaseConfiguration.get("password").asString()
                + "@" + databaseConfiguration.get("host").asString()
                + ":" + databaseConfiguration.get("port").asString()
                + "/?authSource=" + databaseConfiguration.get("database").asString()
        );
        this.mongoDatabase = this.mongoClient.getDatabase(databaseConfiguration.get("database").asString());
        this.serverCollection = this.getOrCreateCollection("servers");
        this.userCollection = this.getOrCreateCollection("users");
        this.userModel = new UserModel(this);
        this.serverModel = new ServerModel(this);
    }

    public MongoCollection<Document> getOrCreateCollection(String collectionName) {
        boolean exists = this.mongoDatabase.listCollectionNames()
                .into(new java.util.ArrayList<>())
                .contains(collectionName);
        if (!exists) this.mongoDatabase.createCollection(collectionName);
        return this.mongoDatabase.getCollection(collectionName);
    }
}
