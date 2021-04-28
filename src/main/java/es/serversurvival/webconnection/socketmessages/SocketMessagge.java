package es.serversurvival.webconnection.socketmessages;


import java.util.*;

/**
 * name-key=value&key=value&...
 */
public class SocketMessagge {
    private final String name;
    private final Map<String, String> content;

    public SocketMessagge (String unprocessedMessagge) {
        String[] splitedMessagge = unprocessedMessagge.split("-");

        String name = splitedMessagge[0];
        String[] body = splitedMessagge[1].split("&");
        Map<String, String> contents = new HashMap<>();

        for(String content : body){
            String[] contentSplitted = content.split("=");

            contents.put(contentSplitted[0], contentSplitted[1]);
        }

        this.name = name;
        this.content = contents;
    }

    public String getName() {
        return name;
    }

    public String get (String value) {
        return content.get(value);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(name).append("-");

        int count = 1;
        for(Map.Entry<String, String> entry : content.entrySet()){
            if(count == content.size())
                builder.append(entry.getKey()).append("=").append(entry.getValue());
            else
                builder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");

            count++;
        }

        return builder.toString();
    }
}
