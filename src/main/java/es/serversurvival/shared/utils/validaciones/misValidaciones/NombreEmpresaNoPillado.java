package es.serversurvival.shared.utils.validaciones.misValidaciones;

import es.serversurvival.empresas.mysql.Empresas;
import main.ValidationResult;
import main.validators.Validator;

public class NombreEmpresaNoPillado implements Validator {
    private final String messageOnFailed;

    public NombreEmpresaNoPillado(String messageOnFailed) {
        this.messageOnFailed = messageOnFailed;
    }

    @Override
    public String getMessageOnFailed() {
        return messageOnFailed;
    }

    @Override
    public ValidationResult check(Object o) {
        try{
            String empresaNombre = String.valueOf(o);

            return Empresas.INSTANCE.getEmpresa(empresaNombre) != null ?
                    ValidationResult.failed(messageOnFailed) :
                    ValidationResult.success();

        }catch (Exception e) {
            return ValidationResult.failed(messageOnFailed);
        }
    }
}
