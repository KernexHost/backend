package com.twoweeksmc.config;

import de.eztxm.ezlib.config.annotation.JsonConfig;
import lombok.Getter;

@Getter
@JsonConfig(fileName = "database.json")
public class DatabaseConfig {
    private final String protocol;
    private final String host;
    private final int port;
    private final String database;
    private final String user;
    private final String password;

    public DatabaseConfig() {
        this.protocol = "mongodb";
        this.host = "localhost";
        this.port = 27017;
        this.database = "";
        this.user = "";
        this.password = "";
    }

    public DatabaseConfig(String protocol, String host, int port, String database, String user, String password) {
        this.protocol = protocol;
        this.host = host;
        this.port = port;
        this.database = database;
        this.user = user;
        this.password = password;
    }
}
