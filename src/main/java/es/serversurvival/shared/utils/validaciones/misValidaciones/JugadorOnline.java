package es.serversurvival.shared.utils.validaciones.misValidaciones;

import main.ValidationResult;
import main.validators.Validator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class JugadorOnline implements Validator {
    private final String messageOnFailed;

    public JugadorOnline(String messageOnFailed) {
        this.messageOnFailed = messageOnFailed;
    }

    @Override
    public String getMessageOnFailed() {
        return messageOnFailed;
    }

    @Override
    public ValidationResult check(Object o) {
        try{
            String playerName = String.valueOf(o);

            Player player = Bukkit.getPlayer(String.valueOf(o));

            return player != null && player.getName().equalsIgnoreCase(playerName) ?
                    ValidationResult.success() :
                    ValidationResult.failed(messageOnFailed);

        }catch (Exception e) {
            return ValidationResult.failed(messageOnFailed);
        }
    }
}
