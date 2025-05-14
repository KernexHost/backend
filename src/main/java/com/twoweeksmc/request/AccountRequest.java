package com.twoweeksmc.request;

import com.twoweeksmc.BackendCore;
import com.twoweeksmc.console.JLineConsole;
import de.eztxm.ezlib.config.object.JsonObject;
import io.javalin.Javalin;
import org.bson.Document;

public class AccountRequest {
    private final Javalin webServer;
    private final JLineConsole console;

    public AccountRequest(Javalin webServer, JLineConsole console) {
        this.webServer = webServer;
        this.console = console;
    }

    public void create() {
        this.webServer.post("/api/v1/account/create", context -> {
            BackendCore.getBackend().getMongoConnector().getUserModel().createUser(JsonObject.parse(context.body()));
            BackendCore.getBackend().getConsole().print("Account Created!");
            context.status(200);
        });
    }

    public void edit() {
        this.webServer.post("/api/v1/account/edit", context -> {
            JsonObject body = JsonObject.parse(context.body());
            BackendCore.getBackend().getMongoConnector().getUserModel().updateUser(body.getConverted("username").asString(), body.getConverted("updates").asJsonArray(JsonObject.class));
            BackendCore.getBackend().getConsole().print("Account Updated!");
            context.status(200);
        });
    }

    public void delete() {
        this.webServer.post("/api/v1/account/delete", context -> {
            JsonObject body = JsonObject.parse(context.body());
            BackendCore.getBackend().getMongoConnector().getUserModel().deleteUser(body.getConverted("username").asString());
            BackendCore.getBackend().getConsole().print("Account Deleted!");
            context.status(200);
        });
    }

    public void information() {
        this.webServer.post("/api/v1/account/information", context -> {
            JsonObject body = JsonObject.parse(context.body());
            Document user = BackendCore.getBackend().getMongoConnector().getUserModel().getUser(body.getConverted("username").asString());
            context.json(user.toJson());
            BackendCore.getBackend().getConsole().print("Account Information send!");
            context.status(200);
        });
    }
}
