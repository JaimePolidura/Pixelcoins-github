package es.serversurvival.main.Pixelcoins.empleados;

import es.serversurvival.mySQL.Empleados;
import es.serversurvival.mySQL.Empresas;
import es.serversurvival.mySQL.MySQL;
import main.ValidationResult;
import main.ValidationsService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.function.Supplier;

import static es.serversurvival.validaciones.Validaciones.Same;
import static es.serversurvival.validaciones.Validaciones.True;

public class IrseEmpleosTest {
    private final String sender = "ockre112YT";

    @Before
    public void setup () {
        MySQL.conectar();
    }

    @Test
    public void legit () {
        String[] args = new String[]{"irse", "Klyter"};
        Assert.assertTrue(check(args, sender).isSuccessful());
    }

    @Test
    public void args () {
        String[] args = new String[]{"irse"};
        Assert.assertTrue(check(args, sender).isFailed());
    }

    @Test
    public void empresaNoExiste () {
        String[] args = new String[]{"irse", "sasas"};
        Assert.assertTrue(check(args, sender).isFailed());
    }

    @Test
    public void jugadorNoTrabajaEmpresa () {
        String[] args = new String[]{"irse", "Klyter"};
        Assert.assertTrue(check(args, "JaimeTruman").isFailed());
    }

    private ValidationResult check (String[] args, String sender) {
        return ValidationsService.startValidating(args.length, Same.as(2, "1"))
                //.andMayThrowException(() -> Empresas.INSTANCE.getEmpresa(args[1]) != null, "2", True.of("Esa empresa no exsiste"))
                //.and(trabajaEnLaEmpresa(() -> args[1], sender), True.of("Ese jugador no trabaja en la empresa"))
                .validateAll();
    }


    private boolean trabajaEnLaEmpresa(Supplier<String> empresaSupplier, String sender) {
        try{
            String empresaNombre = empresaSupplier.get();

            return Empleados.INSTANCE.trabajaEmpresa(sender, empresaNombre);
        }catch (Exception e) {
            return false;
        }
    }
}
