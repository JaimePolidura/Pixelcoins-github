package es.serversurvival.nfs.utils.validaciones.misValidaciones;

import es.serversurvival.nfs.empleados.mysql.Empleados;
import main.ValidationResult;
import main.validators.Validator;

import java.util.function.Supplier;

public class NoTrabajaEmpresa implements Validator {
    private final String messageOnFailed;
    private String nombreEmpresa;
    private boolean prefailed = false;

    public NoTrabajaEmpresa(String messageOnFailed) {
        this.messageOnFailed = messageOnFailed;
    }

    public NoTrabajaEmpresa(String messageOnFailed, String nombreEmprsa) {
        this.messageOnFailed = messageOnFailed;
        this.nombreEmpresa = nombreEmprsa;
    }

    public NoTrabajaEmpresa(String messageOnFailed, String nombreEmprsa, boolean prefailed) {
        this.messageOnFailed = messageOnFailed;
        this.nombreEmpresa = nombreEmprsa;
        this.prefailed = prefailed;
    }

    @Override
    public String getMessageOnFailed() {
        return messageOnFailed;
    }

    @Override
    public ValidationResult check(Object o) {
        if(prefailed)
            return ValidationResult.failed(messageOnFailed);

        try{
            String nombreEmpleado = String.valueOf(o);

            return Empleados.INSTANCE.trabajaEmpresa(nombreEmpleado, nombreEmpresa) ?
                    ValidationResult.failed(messageOnFailed) :
                    ValidationResult.success();
        }catch (Exception e) {
            return ValidationResult.failed(messageOnFailed);
        }
    }

    public NoTrabajaEmpresa en (Supplier<? extends String> empresaSupplier) {
        try{
            String nombreEmpresa = empresaSupplier.get();

            return new NoTrabajaEmpresa(messageOnFailed, nombreEmpresa);
        }catch (Exception e) {
            return new NoTrabajaEmpresa(messageOnFailed, nombreEmpresa, true);
        }
    }
}
