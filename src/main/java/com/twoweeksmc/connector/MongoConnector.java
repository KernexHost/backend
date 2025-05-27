package com.twoweeksmc.connector;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.twoweeksmc.config.DatabaseConfig;
import com.twoweeksmc.connector.model.GroupModel;
import com.twoweeksmc.connector.model.PermissionModel;
import com.twoweeksmc.connector.model.ServerModel;
import com.twoweeksmc.connector.model.UserModel;
import de.eztxm.ezlib.config.JsonConfig;
import lombok.Getter;
import org.bson.Document;

@Getter
public class MongoConnector {
    private final MongoClient mongoClient;
    private final MongoDatabase mongoDatabase;
    private final MongoCollection<Document> userCollection;
    private final UserModel userModel;
    private final MongoCollection<Document> serverCollection;
    private final ServerModel serverModel;
    private final MongoCollection<Document> groupCollection;
    private final GroupModel groupModel;
    private final MongoCollection<Document> permissionCollection;
    private final PermissionModel permissionModel;

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
        this.userCollection = this.getOrCreateCollection("users");
        this.userModel = new UserModel(this);
        this.serverCollection = this.getOrCreateCollection("servers");
        this.serverModel = new ServerModel(this);
        this.groupCollection = this.getOrCreateCollection("groups");
        this.groupModel = new GroupModel(this);
        this.permissionCollection = this.getOrCreateCollection("permissions");
        this.permissionModel = new PermissionModel(this);
    }

    public MongoCollection<Document> getOrCreateCollection(String collectionName) {
        boolean exists = this.mongoDatabase.listCollectionNames()
                .into(new java.util.ArrayList<>())
                .contains(collectionName);
        if (!exists) this.mongoDatabase.createCollection(collectionName);
        return this.mongoDatabase.getCollection(collectionName);
    }
}
