package com.twoweeksmc.request;

import com.twoweeksmc.console.JLineConsole;
import com.twoweeksmc.util.Method;
import com.twoweeksmc.web.WebServer;

public class GroupRequest {
    private final WebServer webServer;
    private final JLineConsole console;

    public GroupRequest(WebServer webServer, JLineConsole console) {
        this.webServer = webServer;
        this.console = console;
    }

    public void create() {
        this.webServer.handle(Method.POST, "/api/v1/group/create", context -> {

        });
    }

    public void edit() {
        this.webServer.handle(Method.POST, "/api/v1/group/edit", context -> {

        });
    }

    public void delete() {
        this.webServer.handle(Method.POST, "/api/v1/group/delete", context -> {

        });
    }

    public void information() {
        this.webServer.handle(Method.POST, "/api/v1/group/information", context -> {

        });
    }
}
