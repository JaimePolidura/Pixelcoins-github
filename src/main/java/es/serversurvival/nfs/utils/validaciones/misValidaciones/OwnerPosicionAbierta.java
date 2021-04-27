package es.serversurvival.nfs.utils.validaciones.misValidaciones;

import es.serversurvival.nfs.bolsa.posicionesabiertas.mysql.PosicionesAbiertas;
import es.serversurvival.nfs.bolsa.posicionescerradas.mysql.TipoPosicion;
import main.ValidationResult;
import main.validators.Validator;

public class OwnerPosicionAbierta implements Validator {
    private final String messageOnFailed;
    private String owner;
    private TipoPosicion tipoPosicion;

    public OwnerPosicionAbierta(String messageOnFailed) {
        this.messageOnFailed = messageOnFailed;
    }

    public OwnerPosicionAbierta(String messageOnFailed, String owner) {
        this.messageOnFailed = messageOnFailed;
        this.owner = owner;
    }

    public OwnerPosicionAbierta(String messageOnFailed, String owner, TipoPosicion tipoPosicion) {
        this.messageOnFailed = messageOnFailed;
        this.owner = owner;
        this.tipoPosicion = tipoPosicion;
    }

    @Override
    public String getMessageOnFailed() {
        return messageOnFailed;
    }

    @Override
    public ValidationResult check(Object o) {
        try{
            int id = Integer.parseInt(String.valueOf(o));

            if(tipoPosicion != null){
                return PosicionesAbiertas.INSTANCE.getPosicionAbierta(id, owner, tipoPosicion) != null ?
                        ValidationResult.success() :
                        ValidationResult.failed(messageOnFailed);
            }else{
                return PosicionesAbiertas.INSTANCE.getPosicionAbierta(id, owner) != null ?
                        ValidationResult.success() :
                        ValidationResult.failed(messageOnFailed);
            }

        }catch (Exception e) {
            return ValidationResult.failed(messageOnFailed);
        }
    }

    public OwnerPosicionAbierta de (String jugador) {
        return new OwnerPosicionAbierta(messageOnFailed, jugador);
    }

    public OwnerPosicionAbierta de (String jugador, TipoPosicion tipoPosicion) {
        return new OwnerPosicionAbierta(messageOnFailed, jugador, tipoPosicion);
    }
}
