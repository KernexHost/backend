package com.twoweeksmc.connector.model;

import com.mongodb.client.model.Filters;
import com.twoweeksmc.connector.MongoConnector;
import com.twoweeksmc.connector.util.Encryption;
import de.eztxm.ezlib.config.object.JsonArray;
import de.eztxm.ezlib.config.object.JsonObject;
import org.bson.Document;

import java.util.UUID;

public class UserModel {
    private final MongoConnector connector;

    public UserModel(MongoConnector connector) {
        this.connector = connector;
    }

    public boolean createUser(JsonObject userData) {
        if (userExists(userData.getConverted("email").asString(), userData.getConverted("username").asString())) {
            return false;
        }
        Document document = new Document();
        document.put("uuid", UUID.randomUUID().toString());
        document.put("username", userData.getConverted("username").asString());
        document.put("password", Encryption.hashPassword(userData.getConverted("password").asString()));
        document.put("firstname", userData.getConverted("firstname").asString());
        document.put("lastname", userData.getConverted("lastname").asString());
        document.put("email", userData.getConverted("email").asString());
        document.put("country", userData.getConverted("country").asString());
        document.put("city", userData.getConverted("city").asString());
        document.put("state", userData.getConverted("state").asString());
        document.put("zip", userData.getConverted("zip").asString());
        document.put("phone", userData.getConverted("phone").asString());
        document.put("company", userData.getConverted("company").asString());
        document.put("ip", userData.getConverted("ip").asString());
        document.put("gems", 100);
        return this.connector.getUserCollection().insertOne(document).wasAcknowledged();
    }

    public boolean updateUser(String username, JsonArray<JsonObject> updates) {
        if (!userExists(username)) {
            return false;
        }
        for (JsonObject update : updates) {
            boolean success = this.connector.getUserCollection().updateOne(
                    Filters.eq("username", username), new Document("$set",
                            new Document(update.getConverted("key").asString(), update.getConverted("value").asObject())
                    )
            ).wasAcknowledged();
            if (!success) {
                return false;
            }
        }
        return true;
    }

    public boolean deleteUser(String username) {
        if (!userExists(username)) {
            return false;
        }
        return this.connector.getUserCollection().deleteOne(Filters.eq("username", username)).wasAcknowledged();
    }

    public Document getUser(String username) {
        if (!userExists(username)) {
            return null;
        }
        return this.connector.getUserCollection().find(Filters.eq("username", username)).first();
    }

    public boolean userExists(String username) {
        return this.connector.getUserCollection().find(Filters.eq("username", username)).iterator().hasNext();
    }

    public boolean userExists(String email, String username) {
        return this.connector.getUserCollection().find(Filters.or(Filters.eq("email", email), Filters.eq("username", username))).iterator().hasNext();
    }
}
