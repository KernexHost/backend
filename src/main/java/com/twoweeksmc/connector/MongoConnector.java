package com.twoweeksmc.connector;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.twoweeksmc.config.DatabaseConfig;
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

    public MongoConnector(DatabaseConfig databaseConfiguration) {
        this.mongoClient = MongoClients.create(
                databaseConfiguration.getProtocol()
                        + "://" + databaseConfiguration.getUser()
                        + ":" + databaseConfiguration.getPassword()
                + "@" + databaseConfiguration.getHost()
                + ":" + databaseConfiguration.getPort()
                + "/?authSource=" + databaseConfiguration.getDatabase()
        );
        this.mongoDatabase = this.mongoClient.getDatabase(databaseConfiguration.getDatabase());
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
