package es.serversurvival.validaciones.misValidaciones;

import es.serversurvival.mySQL.Jugadores;
import main.ValidationResult;
import main.validators.Validator;

public class JugadorRegistrado implements Validator {
    private String messageOnFailed;
    private String jugador;

    public JugadorRegistrado(String messageOnFailed) {
        this.messageOnFailed = messageOnFailed;
    }

    public JugadorRegistrado(String messageOnFailed, String jugador) {
        this.messageOnFailed = messageOnFailed;
        this.jugador = jugador;
    }

    @Override
    public String getMessageOnFailed() {
        return messageOnFailed;
    }

    @Override
    public ValidationResult check(Object o) {
        try{
            String playerName = (String) o;

            return Jugadores.INSTANCE.getJugador(playerName) != null ?
                    ValidationResult.success() :
                    ValidationResult.failed(messageOnFailed);

        }catch (Exception e) {
            return ValidationResult.failed(messageOnFailed);
        }
    }
}
