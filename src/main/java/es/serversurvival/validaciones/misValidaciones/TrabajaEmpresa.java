package es.serversurvival.validaciones.misValidaciones;

import es.serversurvival.mySQL.Empleados;
import main.ValidationResult;
import main.validators.Validator;

import java.util.function.Supplier;

public class TrabajaEmpresa implements Validator {
    private final String messageOnFailed;
    private String nombreEmprsa;
    private boolean prefailed = false;
        
    public TrabajaEmpresa(String messageOnFailed) {
        this.messageOnFailed = messageOnFailed;
    }

    public TrabajaEmpresa(String messageOnFailed, String nombreEmprsa) {
        this.messageOnFailed = messageOnFailed;
        this.nombreEmprsa = nombreEmprsa;
    }

    public TrabajaEmpresa(String messageOnFailed, String nombreEmprsa, boolean prefailed) {
        this.messageOnFailed = messageOnFailed;
        this.nombreEmprsa = nombreEmprsa;
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

            return Empleados.INSTANCE.trabajaEmpresa(nombreEmpleado, nombreEmprsa) ?
                    ValidationResult.success() :
                    ValidationResult.failed(messageOnFailed);

        }catch (Exception e) {
            return ValidationResult.failed(messageOnFailed);
        }
    }

    public TrabajaEmpresa en (String empresa) {
        return new TrabajaEmpresa(messageOnFailed, nombreEmprsa);
    }

    public TrabajaEmpresa en (Supplier<? extends String> empresaSupplier) {
        try{
            String nombreEmpresa = empresaSupplier.get();

            return new TrabajaEmpresa(messageOnFailed, nombreEmpresa);
        }catch (Exception e) {
            return new TrabajaEmpresa(messageOnFailed, nombreEmprsa, true);
        }
    }
}
