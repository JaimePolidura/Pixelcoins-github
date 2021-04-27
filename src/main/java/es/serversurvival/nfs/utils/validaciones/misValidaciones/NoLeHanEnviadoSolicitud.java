package es.serversurvival.nfs.utils.validaciones.misValidaciones;

import es.serversurvival.nfs.shared.menus.MenuManager;
import main.ValidationResult;
import main.validators.Validator;

public class NoLeHanEnviadoSolicitud implements Validator {
    private final String messageOnFailed;

    public NoLeHanEnviadoSolicitud(String messageOnFailed) {
        this.messageOnFailed = messageOnFailed;
    }

    @Override
    public String getMessageOnFailed() {
        return messageOnFailed;
    }

    @Override
    public ValidationResult check(Object o) {
        try{
            String jugador = String.valueOf(o);

            return MenuManager.getByPlayer(jugador) != null ?
                    ValidationResult.failed(messageOnFailed) :
                    ValidationResult.success();
        }catch (Exception e) {
            return ValidationResult.failed(messageOnFailed);
        }
    }
}
