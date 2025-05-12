package com.twoweeksmc.request;

import com.twoweeksmc.console.JLineConsole;
import com.twoweeksmc.util.Method;
import com.twoweeksmc.web.WebServer;
import io.javalin.Javalin;

public class GroupRequest {
    private final Javalin webServer;
    private final JLineConsole console;

    public GroupRequest(Javalin webServer, JLineConsole console) {
        this.webServer = webServer;
        this.console = console;
    }

    public void create() {
        this.webServer.post("/api/v1/group/create", context -> {

        });
    }

    public void edit() {
        this.webServer.post("/api/v1/group/edit", context -> {

        });
    }

    public void delete() {
        this.webServer.post("/api/v1/group/delete", context -> {

        });
    }

    public void information() {
        this.webServer.post("/api/v1/group/information", context -> {

        });
    }
}
