package com.twoweeksmc.request;

import com.twoweeksmc.connector.model.UserModel;
import com.twoweeksmc.console.JLineConsole;
import de.eztxm.ezlib.config.object.JsonObject;
import io.javalin.Javalin;
import org.bson.Document;

public class AccountRequest {
    private final Javalin webServer;
    private final JLineConsole console;
    private final UserModel userModel;

    public AccountRequest(Javalin webServer, JLineConsole console, UserModel userModel) {
        this.webServer = webServer;
        this.console = console;
        this.userModel = userModel;
    }

    public void create() {
        this.webServer.post("/api/v1/account/create", context -> {
            JsonObject body = JsonObject.parse(context.body());
            this.userModel.createUser(body);
            this.console.print("Account '" + body.getConverted("uuid").asString() + "' created!");
            context.status(200);
        });
    }

    public void edit() {
        this.webServer.post("/api/v1/account/edit", context -> {
            JsonObject body = JsonObject.parse(context.body());
            this.userModel.updateUser(body.getConverted("uuid").asString(), body.getConverted("updates").asJsonArray(JsonObject.class));
            this.console.print("Account '" + body.getConverted("uuid").asString() + "' updated!");
            context.status(200);
        });
    }

    public void delete() {
        this.webServer.post("/api/v1/account/delete", context -> {
            JsonObject body = JsonObject.parse(context.body());
            this.userModel.deleteUser(body.getConverted("uuid").asString());
            this.console.print("Account '" + body.getConverted("uuid").asString() + "' deleted!");
            context.status(200);
        });
    }

    public void information() {
        this.webServer.post("/api/v1/account/information", context -> {
            JsonObject body = JsonObject.parse(context.body());
            Document user = this.userModel.getUser(body.getConverted("uuid").asString());
            context.status(200).json(user.toJson());
            this.console.print("Account information for '" + body.getConverted("uuid").asString() + "' sent!");
        });
    }
}
