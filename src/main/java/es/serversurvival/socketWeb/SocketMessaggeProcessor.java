package es.serversurvival.socketWeb;

import es.serversurvival.socketWeb.messagges.SendNumberMessagge;
import es.serversurvival.socketWeb.messagges.SocketMessagge;
import es.serversurvival.socketWeb.messagges.SocketMessaggeExecutor;
import jdk.internal.org.objectweb.asm.Handle;

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
    }
}
