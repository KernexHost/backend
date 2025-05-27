package com.twoweeksmc;

import com.twoweeksmc.config.DatabaseConfig;
import com.twoweeksmc.connector.MongoConnector;
import com.twoweeksmc.console.JLineConsole;
import com.twoweeksmc.request.AccountRequest;
import com.twoweeksmc.request.GroupRequest;
import com.twoweeksmc.request.ServerRequest;
import de.eztxm.ezlib.config.object.JsonUtil;
import de.eztxm.ezlib.config.reflect.JsonProcessor;
import io.javalin.Javalin;
import lombok.Getter;

@Getter
public class BackendCore {
    private static BackendCore BACKEND;
    private final JsonProcessor<DatabaseConfig> databaseConfigProcessor;
    private final DatabaseConfig databaseConfig;
    private final MongoConnector mongoConnector;
    private final Javalin webServer;
    private JLineConsole console;

    public BackendCore() {
        BACKEND  = this;
        JsonUtil.prettyPrint = true;
        this.databaseConfigProcessor = JsonProcessor.loadConfiguration(DatabaseConfig.class);
        this.databaseConfigProcessor.saveConfiguration();
        this.databaseConfig = this.databaseConfigProcessor.getInstance();
        System.out.println("Database Config Loaded!");

        this.mongoConnector = new MongoConnector(this.databaseConfig);
        System.out.println("Database Connected!");

        this.webServer = Javalin.create(config -> {
            config.showJavalinBanner = false;
        });

        this.console = new JLineConsole(this.webServer);

        this.registerRequests();

        this.webServer.exception(Exception.class, (e, ctx) -> {
            // Dies ist der "print"-Teil, den du suchst!
            // System.err.println gibt die Ausgabe in den Fehlerstrom aus, oft rot in IDEs.
            System.err.println("--------------------------------------------------");
            System.err.println("Ein interner Serverfehler ist aufgetreten!");
            System.err.println("Fehlertyp: " + e.getClass().getName());
            System.err.println("Fehlermeldung: " + e.getMessage());
            System.err.println("--------------------------------------------------");

            // e.printStackTrace() ist extrem wichtig, da es den vollständigen Stack-Trace ausgibt.
            // Der Stack-Trace zeigt dir genau, in welcher Datei und Zeile der Fehler aufgetreten ist.
            e.printStackTrace();
            System.err.println("--------------------------------------------------");

            // Sende eine 500er-Antwort an den Client
            ctx.status(500).result("Ein unerwarteter Fehler ist aufgetreten. Bitte überprüfen Sie die Server-Logs.");
            // Alternativ:
            // throw new InternalServerErrorResponse("Ein unerwarteter Fehler ist aufgetreten.");
        });

        this.webServer.start(8080);
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
        AccountRequest accountRequest = new AccountRequest(this.webServer, this.console, this.mongoConnector.getUserModel());
        accountRequest.create();
        accountRequest.edit();
        accountRequest.delete();
        accountRequest.information();
        GroupRequest groupRequest = new GroupRequest(this.webServer, this.console, this.mongoConnector.getGroupModel());
        groupRequest.create();
        groupRequest.edit();
        groupRequest.delete();
        groupRequest.information();
    }

    private void startConsole() {
        this.initializeConsole();
        this.console.start();
    }

    public static BackendCore getBackend() {
        return BACKEND;
    }

    private void initializeConsole() {
        this.console.print(this.console.prefix(), false);
    }

    public static void main(String[] args) {
        new BackendCore();
    }
}
