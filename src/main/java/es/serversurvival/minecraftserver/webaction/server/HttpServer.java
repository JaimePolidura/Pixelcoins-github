package es.serversurvival.minecraftserver.webaction.server;

import es.dependencyinjector.dependencies.annotations.Service;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.net.InetSocketAddress;

@Service
@RequiredArgsConstructor
public final class HttpServer {
    public static final String HTTP_WEB_ACTION_SERVER_IP = "localhost";
    public static final int HTTP_WEB_ACTION_SERVER_PORT= 8080;

    private final HttpRequestHandler webAcionRequestHandler;

    private com.sun.net.httpserver.HttpServer httpServer;

    public void iniciar() throws IOException {
        httpServer = com.sun.net.httpserver.HttpServer.create(new InetSocketAddress(HTTP_WEB_ACTION_SERVER_IP, HTTP_WEB_ACTION_SERVER_PORT), 0);
        httpServer.createContext("/webaction", webAcionRequestHandler);

        httpServer.start();
    }
}
