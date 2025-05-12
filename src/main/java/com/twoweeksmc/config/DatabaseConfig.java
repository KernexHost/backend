package com.twoweeksmc.config;

import de.eztxm.ezlib.config.annotation.JsonConfig;
import de.eztxm.ezlib.config.annotation.JsonValue;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonConfig(fileName = "database.json")
public class DatabaseConfig {
    @JsonValue(name = "protocol")
    private String protocol;
    @JsonValue(name = "host")
    private String host;
    @JsonValue(name = "port")
    private int port;
    @JsonValue(name = "database")
    private String database;
    @JsonValue(name = "user")
    private String user;
    @JsonValue(name = "password")
    private String password;

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
