package com.twoweeksmc.connector.model;

import com.mongodb.client.model.Filters;
import com.twoweeksmc.connector.MongoConnector;
import de.eztxm.ezlib.config.object.JsonObject;
import org.bson.Document;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class GroupModel {
    private final MongoConnector connector;

    public GroupModel(MongoConnector connector) {
        this.connector = connector;
    }

    public boolean createGroup(String name, List<Object> permissions) {
        Document document = new Document();
        document.put("name", name);
        document.put("permissions", permissions);
        return this.connector.getGroupCollection().insertOne(document).wasAcknowledged();
    }

    public boolean editGroup(String oldName, String newName, List<Object> updatedPermissions) {
        Document oldGroupDoc = this.connector.getGroupCollection().find(Filters.eq("name", oldName)).first();
        Document newGroupDoc = new Document();
        if (oldGroupDoc == null)
            return false;
        if(newName == null)
            newGroupDoc.put("name", oldName);
        if(newName != null)
            newGroupDoc.put("name", newName);
        List<String> oldPermissions = oldGroupDoc.getList("permissions", String.class, new ArrayList<>());

        Set<String> resultSet = new LinkedHashSet<>(oldPermissions);

        for (Object item : updatedPermissions) {
            if (resultSet.contains((String) item)) {
                resultSet.remove((String) item);
            } else {
                resultSet.add((String) item);
            }
        }
        newGroupDoc.put("permissions", new ArrayList<>(resultSet));
        return this.connector.getGroupCollection().updateOne(oldGroupDoc, newGroupDoc).wasAcknowledged();
    }

    public boolean deleteGroup(String name) {
        Document group = this.connector.getGroupCollection().find(Filters.eq("name", name)).first();
        if(group == null)
            return false;
        return this.connector.getGroupCollection().deleteOne(group).wasAcknowledged();
    }

    public Document infoGroup(String name) {
        Document group = this.connector.getGroupCollection().find(Filters.eq("name", name)).first();
        if(group == null)
            return new Document();
        return group;
    }
}
