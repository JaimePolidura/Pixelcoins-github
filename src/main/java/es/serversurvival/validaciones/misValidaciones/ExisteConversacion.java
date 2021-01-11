package es.serversurvival.validaciones.misValidaciones;

import es.serversurvival.mySQL.ConversacionesWeb;
import es.serversurvival.mySQL.tablasObjetos.ConversacionWeb;
import main.ValidationResult;
import main.validators.Validator;
import org.bukkit.ChatColor;

public class ExisteConversacion implements Validator {
    private final String messageOnFailed;

    public ExisteConversacion(String messageOnFailed) {
        this.messageOnFailed = messageOnFailed;
    }

    @Override
    public String getMessageOnFailed() {
        return messageOnFailed;
    }

    @Override
    public ValidationResult check(Object o) {
        try{
            String playerName = (String) o;

            ConversacionWeb conversacion = ConversacionesWeb.INSTANCE.getConversacionServer(playerName);

            return conversacion != null ?
                    ValidationResult.success() :
                    ValidationResult.failed(messageOnFailed);

        }catch (Exception e) {
            return ValidationResult.failed(messageOnFailed);
        }
    }

    public static ExisteConversacion mensaje (String mensaje) {
        return new ExisteConversacion(mensaje);
    }
}
