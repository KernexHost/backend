package com.twoweeksmc.connector.model;

import com.mongodb.client.model.Filters;
import com.twoweeksmc.connector.MongoConnector;
import de.eztxm.ezlib.config.object.JsonObject;
import org.bson.Document;

import java.time.Instant;
import java.util.UUID;

public class ServerModel {
    private final MongoConnector connector;

    public ServerModel(MongoConnector connector) {
        this.connector = connector;
    }

    public boolean createServer(JsonObject serverObject) {
        Document document = new Document();
        document.put("ownerId", serverObject.getConverted("ownerId").asString());
        document.put("start", Instant.now());
        document.put("weeks", serverObject.getConverted("weeks").asInteger());
        document.put("max-players", serverObject.getConverted("max-players").asInteger());
        document.put("max-memory", serverObject.getConverted("max-memory").asInteger());
        document.put("plugins", serverObject.getConverted("plugins").asJsonArray());
        return this.connector.getServerCollection().insertOne(document).wasAcknowledged();
    }

    public Document getServer(UUID ownerId) {
        return this.connector.getServerCollection().find(Filters.eq("ownerId", ownerId.toString())).first();
    }
}
