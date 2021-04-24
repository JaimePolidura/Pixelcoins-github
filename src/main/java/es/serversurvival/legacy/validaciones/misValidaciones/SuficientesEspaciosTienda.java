package es.serversurvival.legacy.validaciones.misValidaciones;

import es.serversurvival.legacy.mySQL.Ofertas;
import main.ValidationResult;
import main.validators.Validator;

public class SuficientesEspaciosTienda implements Validator {
    private final String messageOnFailed;

    public SuficientesEspaciosTienda(String messageOnFailed) {
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

            int espacios = Ofertas.INSTANCE.getOfertasJugador(jugador).size();

            return espacios > Ofertas.MAX_ESPACIOS ?
                    ValidationResult.failed(messageOnFailed) :
                    ValidationResult.success();

        }catch (Exception e) {
            return ValidationResult.failed(messageOnFailed);
        }
    }
}
