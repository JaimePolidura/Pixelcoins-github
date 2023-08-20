package es.serversurvival.minecraftserver.webaction.server;

import com.sun.net.httpserver.HttpServer;
import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival._shared.ConfigurationVariables;
import es.serversurvival.pixelcoins.config._shared.application.Configuration;
import es.serversurvival.pixelcoins.config._shared.domain.ConfigurationKey;
import es.serversurvival.pixelcoins.retos._shared.retos.application.PixelcoinsRecompensador;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.net.InetSocketAddress;

@Service
@RequiredArgsConstructor
public final class WebAcionHttpServer {
    private final HttpRequestHandler webAcionRequestHandler;
    private final Configuration configuration;

    private HttpServer httpServer;

    public void iniciar() throws IOException {
        new Thread(() -> {
            try {
                int port = configuration.getInt(ConfigurationKey.WEB_ACTIONS_BACKEND_SERVER_PORT);
                String host = configuration.get(ConfigurationKey.WEB_ACTIONS_SERVER_IP);

                httpServer = HttpServer.create(new InetSocketAddress(host, port), 0);
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
