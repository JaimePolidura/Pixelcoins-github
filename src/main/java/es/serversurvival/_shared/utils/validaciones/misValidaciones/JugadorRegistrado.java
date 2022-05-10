package es.serversurvival._shared.utils.validaciones.misValidaciones;

import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import main.ValidationResult;
import main.validators.Validator;

public class JugadorRegistrado implements Validator {
    private String messageOnFailed;
    private String jugador;

    public JugadorRegistrado(String messageOnFailed) {
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

            return DependecyContainer.get(JugadoresService.class).getByNombre(playerName) != null ?
                    ValidationResult.success() :
                    ValidationResult.failed(messageOnFailed);

        }catch (Exception e) {
            return ValidationResult.failed(messageOnFailed);
        }
    }
}
