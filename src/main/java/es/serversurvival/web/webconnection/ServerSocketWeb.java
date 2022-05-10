package es.serversurvival.web.webconnection;


import es.serversurvival.web.webconnection.socketmessages.SocketMessagge;
import lombok.SneakyThrows;

import java.io.*;
import java.net.Socket;

public class ServerSocketWeb {
    public final static ServerSocketWeb INSTANCE = new ServerSocketWeb();

    private final String LOCALHOST = "127.0.0.1";
    private final int BACKEND_PORT = 9999;

    private ServerSocketWeb () {}

    @SneakyThrows
    public void enviarMensaje (SocketMessagge socketMessagge) {
        Socket serverSocket = new Socket(LOCALHOST, BACKEND_PORT);
        DataOutputStream output = new DataOutputStream(serverSocket.getOutputStream());
        output.writeUTF(socketMessagge.toString());

        output.close();
        serverSocket.close();
    }
}
