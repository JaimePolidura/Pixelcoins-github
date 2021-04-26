package es.serversurvival.nfs.utils.validaciones.misValidaciones;

import es.serversurvival.nfs.empresas.mysql.Empresas;
import main.ValidationResult;
import main.validators.Validator;

public class OwnerDeEmpresa implements Validator {
    private String messageOnFailed;
    private String jugador;

    public OwnerDeEmpresa(String messageOnFailed) {
        this.messageOnFailed = messageOnFailed;
    }

    public OwnerDeEmpresa(String messageOnFailed, String jugador) {
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
            String empresaNombre = String.valueOf(o);

            return Empresas.INSTANCE.getEmpresa(empresaNombre).getOwner().equalsIgnoreCase(jugador) ?
                    ValidationResult.success() :
                    ValidationResult.failed(messageOnFailed);
        }catch (Exception e) {
            return ValidationResult.failed(messageOnFailed);
        }
    }

    public OwnerDeEmpresa of (String jugador) {
        return new OwnerDeEmpresa(messageOnFailed, jugador);
    }
}
