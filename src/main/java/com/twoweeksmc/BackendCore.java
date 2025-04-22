package com.twoweeksmc;

import com.twoweeksmc.config.DatabaseConfig;
import com.twoweeksmc.console.JLineConsole;
import com.twoweeksmc.request.AccountRequest;
import com.twoweeksmc.request.GroupRequest;
import com.twoweeksmc.request.ServerRequest;
import com.twoweeksmc.web.WebServer;
import de.eztxm.ezlib.config.reflect.JsonProcessor;
import lombok.Getter;

@Getter
public class BackendCore {
    private final JsonProcessor<DatabaseConfig> databaseConfigProcessor;
    private final DatabaseConfig databaseConfig;
    private final WebServer webServer;
    private JLineConsole console;

    public BackendCore() {
        this.databaseConfigProcessor = JsonProcessor.loadConfiguration(DatabaseConfig.class);
        this.databaseConfig = this.databaseConfigProcessor.getInstance();
        this.webServer = new WebServer(8000);
        this.registerRequests();
        this.webServer.start();
        this.startConsole();
    }

    private void registerRequests() {
        ServerRequest serverRequest = new ServerRequest(this.webServer, this.console);
        serverRequest.create();
        serverRequest.edit();
        serverRequest.backup();
        serverRequest.suspend();
        serverRequest.delete();
        serverRequest.information();
        AccountRequest accountRequest = new AccountRequest(this.webServer, this.console);
        accountRequest.create();
        accountRequest.edit();
        accountRequest.delete();
        accountRequest.information();
        GroupRequest groupRequest = new GroupRequest(this.webServer, this.console);
        groupRequest.create();
        groupRequest.edit();
        groupRequest.delete();
        groupRequest.information();
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
