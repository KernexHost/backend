package com.twoweeksmc.request;

import com.twoweeksmc.console.JLineConsole;
import com.twoweeksmc.util.Method;
import com.twoweeksmc.web.WebServer;

public class AccountRequest {
    private final WebServer webServer;
    private final JLineConsole console;

    public AccountRequest(WebServer webServer, JLineConsole console) {
        this.webServer = webServer;
        this.console = console;
    }

    public void create() {
        this.webServer.handle(Method.POST, "/api/v1/account/create", context -> {

        });
    }

    public void edit() {
        this.webServer.handle(Method.POST, "/api/v1/account/edit", context -> {

        });
    }

    public void delete() {
        this.webServer.handle(Method.POST, "/api/v1/account/delete", context -> {

        });
    }

    public void information() {
        this.webServer.handle(Method.POST, "/api/v1/account/information", context -> {

        });
    }
}
