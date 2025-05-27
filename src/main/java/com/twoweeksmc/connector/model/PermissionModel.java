package com.twoweeksmc.connector.model;

import com.mongodb.client.model.Filters;
import com.twoweeksmc.connector.MongoConnector;

public class PermissionModel {
    private final MongoConnector connector;

    public PermissionModel(MongoConnector connector) {
        this.connector = connector;
    }

    public String getPermission(String name) {
        return (String) this.connector.getPermissionCollection().find(Filters.eq("name", name)).first().get("permission");
    }
}
