package es.serversurvival._shared.utils.validaciones.misValidaciones;

import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.empresas.empresas._shared.application.EmpresasService;
import main.ValidationResult;
import main.validators.Validator;

public final class OwnerDeEmpresa implements Validator {
    private final String messageOnFailed;
    private EmpresasService empresasService;
    private String jugador;

    public OwnerDeEmpresa(String messageOnFailed) {
        this.messageOnFailed = messageOnFailed;
        this.empresasService = DependecyContainer.get(EmpresasService.class);
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

            return this.empresasService.getEmpresaByNombre(empresaNombre).getOwner().equalsIgnoreCase(jugador) ?
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
