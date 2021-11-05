package es.serversurvival._shared.utils.validaciones.misValidaciones;

import es.serversurvival._shared.utils.Funciones;
import main.ValidationResult;
import main.validators.Validator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class InventarioNoLleno implements Validator {
    private final String messageOnFailed;

    public InventarioNoLleno(String messageOnFailed) {
        this.messageOnFailed = messageOnFailed;
    }

    @Override
    public String getMessageOnFailed() {
        return messageOnFailed;
    }

    @Override
    public ValidationResult check(Object o) {
        try{
            Player player = Bukkit.getPlayer(String.valueOf(o));

            return Funciones.getEspaciosOcupados(player.getInventory()) == 36 ?
                    ValidationResult.failed(messageOnFailed) :
                    ValidationResult.success();
        }catch (Exception e) {
            return ValidationResult.failed(messageOnFailed);
        }
    }
}
