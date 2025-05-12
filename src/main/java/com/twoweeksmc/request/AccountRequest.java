package com.twoweeksmc.request;

import com.twoweeksmc.console.JLineConsole;
import com.twoweeksmc.util.Method;
import com.twoweeksmc.web.WebServer;
import io.javalin.Javalin;

public class AccountRequest {
    private final Javalin webServer;
    private final JLineConsole console;

    public AccountRequest(Javalin webServer, JLineConsole console) {
        this.webServer = webServer;
        this.console = console;
    }

    public void create() {
        this.webServer.post("/api/v1/account/create", context -> {

        });
    }

    public void edit() {
        this.webServer.post("/api/v1/account/edit", context -> {

        });
    }

    public void delete() {
        this.webServer.post("/api/v1/account/delete", context -> {

        });
    }

    public void information() {
        this.webServer.post("/api/v1/account/information", context -> {

        });
    }
}
