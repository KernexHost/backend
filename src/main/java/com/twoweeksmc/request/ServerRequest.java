package com.twoweeksmc.request;

import com.twoweeksmc.console.JLineConsole;
import com.twoweeksmc.util.Method;
import com.twoweeksmc.web.WebServer;
import de.eztxm.ezlib.config.object.JsonObject;
import io.javalin.Javalin;

public class ServerRequest {
    private final Javalin webServer;
    private final JLineConsole console;

    public ServerRequest(Javalin webServer, JLineConsole console) {
        this.webServer = webServer;
        this.console = console;
    }

    public void create() {
        this.webServer.post("/api/v1/server/create", context -> {
            JsonObject body = JsonObject.parse(context.body());
            String ownerId = body.getConverted("ownerId").asString();
            String type = body.getConverted("type").asString();
            int maxPlayers = body.getConverted("maxPlayers").asInteger();
            this.console.print(ownerId + " : " + type + " : " + maxPlayers);
            context.status(200);
        });
    }

    public void edit() {
        this.webServer.post("/api/v1/server/edit", context -> {

        });
    }

    public void backup() {
        this.webServer.post("/api/v1/server/backup", context -> {

        });
    }

    public void suspend() {
        this.webServer.post("/api/v1/server/suspend(", context -> {

        });
    }

    public void delete() {
        this.webServer.post("/api/v1/server/delete", context -> {

        });
    }

    public void information() {
        this.webServer.post("/api/v1/server/information", context -> {

        });
    }
}
