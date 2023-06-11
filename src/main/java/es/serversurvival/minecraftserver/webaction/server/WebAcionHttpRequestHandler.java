package es.serversurvival.minecraftserver.webaction.server;

import com.sun.net.httpserver.HttpServer;
import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival._shared.Configuration;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.net.InetSocketAddress;

@Service
@RequiredArgsConstructor
public final class WebAcionHttpRequestHandler {
    private final HttpRequestHandler webAcionRequestHandler;

    private HttpServer httpServer;

    public void iniciar() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(Configuration.WEB_ACTIONS_SERVER_IP, Configuration.WEB_ACTIONS_SERVER_PORT), 0);
        httpServer.createContext("/webaction", webAcionRequestHandler);

        httpServer.start();
    }
}
