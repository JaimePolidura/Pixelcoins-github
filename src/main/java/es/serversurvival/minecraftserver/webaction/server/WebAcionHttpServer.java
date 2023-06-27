package es.serversurvival.minecraftserver.webaction.server;

import com.sun.net.httpserver.HttpServer;
import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival._shared.ConfigurationVariables;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.net.InetSocketAddress;

@Service
@RequiredArgsConstructor
public final class WebAcionHttpServer {
    private final HttpRequestHandler webAcionRequestHandler;

    private HttpServer httpServer;

    public void iniciar() throws IOException {
        new Thread(() -> {
            try {
                httpServer = HttpServer.create(new InetSocketAddress(ConfigurationVariables.WEB_ACTIONS_SERVER_IP, ConfigurationVariables.WEB_ACTIONS_BACKEND_SERVER_PORT), 0);
                httpServer.createContext("/webaction", webAcionRequestHandler);

                httpServer.start();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        })
                .start();
    }

    public void stop() {
        this.httpServer.stop(1);
    }
}
