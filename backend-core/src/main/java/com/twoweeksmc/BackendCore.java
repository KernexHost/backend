package com.twoweeksmc;

import com.twoweeksmc.console.JLineConsole;
import com.twoweeksmc.web.WebServer;
import de.eztxm.ezlib.config.object.JsonObject;
import lombok.val;

public class BackendCore {
    private WebServer webServer;
    private JLineConsole console;

    public BackendCore() {

        this.webServer = new WebServer(8000);
        registerRequests();
        this.webServer.start();
        this.startConsole();
    }

    private void registerRequests() {
        this.webServer.post("/api/v1/server/create", ctx -> {
            JsonObject body = JsonObject.parse(ctx.requestBody());
            String ownerId = body.getConverted("ownerId").asString();
            String type = body.getConverted("type").asString();
            int maxPlayers = body.getConverted("maxPlayers").asInteger();
            this.console.print(ownerId + " : " + type + " : " + maxPlayers);
            ctx.status(202);
        });
        this.webServer.post("/api/v1/server/edit", ctx -> {

        });
        this.webServer.post("/api/v1/server/backup", ctx -> {

        });
        this.webServer.post("/api/v1/server/suspend", ctx -> {

        });
        this.webServer.post("/api/v1/server/delete", ctx -> {

        });
    }

    private void startConsole() {
        this.console = new JLineConsole(this.webServer);
        this.initializeConsole();
        this.console.start();
    }

    private void initializeConsole() {
        this.console.print(this.console.prefix(), false);
    }

    public static void main(String[] args) {
        new BackendCore();
    }
}
