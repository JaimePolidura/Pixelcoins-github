package es.serversurvival.web.webconnection;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import es.serversurvival.web.conversacionesweb.desconectar.ChatDisconnectMessage;
import es.serversurvival.web.conversacionesweb.enviarmensaje.ChatMessage;
import es.serversurvival.web.conversacionesweb.isonline.IsOnlineMessagge;
import es.serversurvival.web.webconnection.socketmessages.CanRedirect;
import es.serversurvival.web.webconnection.socketmessages.SocketMessagge;
import es.serversurvival.web.webconnection.socketmessages.SocketMessaggeExecutor;
import es.serversurvival.web.webconnection.verificacioncuentas.enviarnumero.SendNumberMessagge;
import lombok.SneakyThrows;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class RabbitMQConsumer {
    private final Map<String, SocketMessaggeExecutor> socketMessaggeExecutors = new HashMap<>();
    private final String QUEUE_NAME = "chat";
    private final ServerSocketWeb socket = ServerSocketWeb.INSTANCE;
    private Connection connection;

    @SneakyThrows
    public RabbitMQConsumer () {
        fillSocketMessaggeExecutors();

        ConnectionFactory factory = new ConnectionFactory();
        this.connection = factory.newConnection();
    }

    public void listenForMessages() throws IOException {
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        channel.basicConsume(QUEUE_NAME, true, (tag, rawMessage) -> processMessageAndExecute(rawMessage.getBody()), s -> {});
    }

    private void processMessageAndExecute(byte[] rawMessage) {
        String rawStringMessage = new String(rawMessage, StandardCharsets.UTF_8);
        SocketMessagge processedMessage = new SocketMessagge(rawStringMessage);

        SocketMessaggeExecutor messaggeExecutor = socketMessaggeExecutors.get(processedMessage.getName());
        SocketMessagge response = messaggeExecutor.execute(processedMessage);

        if(messaggeExecutor instanceof CanRedirect){
            socket.enviarMensaje(response);
        }

    }

    private void fillSocketMessaggeExecutors() {
        socketMessaggeExecutors.put("chat", new ChatMessage());
        socketMessaggeExecutors.put("chatdisconnect", new ChatDisconnectMessage());
        socketMessaggeExecutors.put("isonline", new IsOnlineMessagge());
        socketMessaggeExecutors.put("sendnumber", new SendNumberMessagge());
    }
}
