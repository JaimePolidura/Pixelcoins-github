package es.serversurvival.nfs.utils.validaciones.misValidaciones;

import es.serversurvival.nfs.tienda.mySQL.ofertas.Ofertas;
import main.ValidationResult;
import main.validators.Validator;

public class ItemNotBaneadoTienda implements Validator {
    private final String messageOnFailed;

    public ItemNotBaneadoTienda(String messageOnFailed) {
        this.messageOnFailed = messageOnFailed;
    }

    @Override
    public String getMessageOnFailed() {
        return messageOnFailed;
    }

    @Override
    public ValidationResult check(Object o) {
        try {
            String nombreItem = (String) o;

            return Ofertas.estaBaneado(nombreItem) ?
                    ValidationResult.failed(messageOnFailed) :
                    ValidationResult.success();
        }catch (Exception e) {
            return ValidationResult.failed(messageOnFailed);
        }
    }
}
