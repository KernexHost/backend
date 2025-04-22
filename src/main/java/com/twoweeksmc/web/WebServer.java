package com.twoweeksmc.web;

import com.sun.net.httpserver.HttpServer;
import com.twoweeksmc.util.Method;
import lombok.Getter;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Getter
public class WebServer {
    private final int port;
    private final HttpServer httpServer;
    private final List<Request> requests;

    public WebServer(int port) {
        try {
            this.requests = new ArrayList<>();
            this.port = port;
            this.httpServer = HttpServer.create(new InetSocketAddress(this.port), 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public WebServer() {
        try {
            this.requests = new ArrayList<>();
            this.port = 8080;
            this.httpServer = HttpServer.create(new InetSocketAddress(this.port), 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void handle(Method method, String path, Consumer<HttpContext> context) {
        this.requests.add(new Request(path, method.name().toUpperCase(), context));
    }

    private void handleServer() {
        this.requests.forEach(request -> this.httpServer.createContext(request.path(), handle ->  {
            if (handle.getRequestMethod().equals(request.requestType().toUpperCase())) {
                request.context().accept(new HttpContext(handle));
            }
        }));
    }

    public void start() {
        handleServer();
        this.httpServer.setExecutor(null);
        this.httpServer.start();
    }

    public void stop() {
        this.httpServer.stop(0);
    }
}
