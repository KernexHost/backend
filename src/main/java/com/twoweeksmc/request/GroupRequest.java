package com.twoweeksmc.request;

import com.twoweeksmc.connector.model.GroupModel;
import com.twoweeksmc.console.JLineConsole;
import com.twoweeksmc.util.Method;
import com.twoweeksmc.web.WebServer;
import de.eztxm.ezlib.config.object.JsonObject;
import io.javalin.Javalin;
import org.bson.Document;

public class GroupRequest {
    private final Javalin webServer;
    private final JLineConsole console;
    private final GroupModel groupModel;

    public GroupRequest(Javalin webServer, JLineConsole console, GroupModel groupModel) {
        this.webServer = webServer;
        this.console = console;
        this.groupModel = groupModel;
    }

    public void create() {
        this.webServer.post("/api/v1/group/create", context -> {
            JsonObject body = JsonObject.parse(context.body());
            this.groupModel.createGroup(
                body.getConverted("name").asString(),
                body.getConverted("permissions").asList()
            );
            this.console.print("Group '" + body.getConverted("name").asString() + "' created!");
            context.status(200);
        });
    }

    public void edit() {
        this.webServer.post("/api/v1/group/edit", context -> {
            JsonObject body = JsonObject.parse(context.body());
            this.groupModel.editGroup(
                    body.getConverted("name").asString(),
                    body.getConverted("newName").asString(),
                    body.getConverted("permissions").asList()
            );
            this.console.print("Group '" + body.getConverted("name").asString() + "' updated!");
            context.status(200);
        });
    }

    public void delete() {
        this.webServer.post("/api/v1/group/delete", context -> {
            JsonObject body = JsonObject.parse(context.body());
            this.groupModel.deleteGroup(
                    body.getConverted("name").asString()
            );
            this.console.print("Group '" + body.getConverted("name").asString() + "' delete!");
            context.status(200);
        });
    }

    public void information() {
        this.webServer.post("/api/v1/group/information", context -> {
            JsonObject body = JsonObject.parse(context.body());
            Document group = this.groupModel.infoGroup(
                    body.getConverted("name").asString()
            );
            this.console.print("Group '" + body.getConverted("name").asString() + "' info!");
            context.status(200).json(group.toJson());
        });
    }
}
