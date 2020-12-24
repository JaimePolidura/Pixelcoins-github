package es.serversurvival.socketWeb;

import es.serversurvival.socketWeb.messagges.IsOnlineMessagge;
import es.serversurvival.socketWeb.messagges.SendNumberMessagge;
import es.serversurvival.socketWeb.messagges.SocketMessaggeExecutor;

import java.util.HashMap;
import java.util.Map;

public class SocketMessaggeProcessor {
    private Map<String, SocketMessaggeExecutor> socketMessagge = new HashMap<>();

    public SocketMessaggeProcessor () {
        fillHashMap();
    }

    public String findAndExecute (SocketMessagge messagge) {
        return socketMessagge.get(messagge.getName()).execute(messagge);
    }

    private void fillHashMap () {
        socketMessagge.put("sendnumber", new SendNumberMessagge());
        socketMessagge.put("isonline", new IsOnlineMessagge());
    }
}
