package es.serversurvival.socketWeb;

import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSocketWeb extends BukkitRunnable {
    private final SocketMessaggeProcessor socketMessaggeProcessor = new SocketMessaggeProcessor();
    private final String LOCALHOST = "127.0.0.1";
    private final int BUKKIT_PORT = 10000;
    private final int BACKEND_PORT = 9999;

    @Override
    public void run() {
        System.out.println("[Pixelcoins] Socket server running...");

        try{
            ServerSocket pixelcoinsSocket = new ServerSocket(BUKKIT_PORT);

            while (true) {
                Socket socketCliente = pixelcoinsSocket.accept();

                DataInputStream input = new DataInputStream(socketCliente.getInputStream());
                String messagge = input.readUTF();
                socketCliente.close();
                input.close();

                String processedMessagge = processSockettMessagge(new SocketMessagge(messagge));

                Socket socketClientToResponse = new Socket(LOCALHOST,BACKEND_PORT);
                DataOutputStream output = new DataOutputStream(socketClientToResponse.getOutputStream());
                output.writeUTF(processedMessagge);

                socketClientToResponse.close();
                output.close();
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String processSockettMessagge (SocketMessagge socketMessagge) {
        return socketMessaggeProcessor.findAndExecute(socketMessagge);
    }
}
