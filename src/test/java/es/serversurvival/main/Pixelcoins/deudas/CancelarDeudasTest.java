package es.serversurvival.main.Pixelcoins.deudas;

import es.serversurvival.mySQL.Deudas;
import es.serversurvival.mySQL.MySQL;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.entity.Player;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static es.serversurvival.validaciones.Validaciones.*;
import static es.serversurvival.validaciones.Validaciones.False;

public class CancelarDeudasTest {
    private final String sender = "JaimeTruman";

    @Before
    public void setUp () {
        MySQL.conectar();
    }

    @Test
    public void legit () {
        String[] args = new String[]{"cancelar", "1"};
        Assert.assertTrue(check(args).isSuccessful());
    }

    @Test
    public void args () {
        String[] args = new String[]{"cancelar", "1", "12", "12"};
        Assert.assertTrue(check(args).isFailed());

        String[] args2 = new String[]{"cancelar"};
        Assert.assertTrue(check(args2).isFailed());
    }

    @Test
    public void noNaturalNumber () {
        String[] args = new String[]{"cancelar", "-1"};
        Assert.assertTrue(check(args).isFailed());
    }

    @Test
    public void deudaNoExiste () {
        /*String[] args = new String[]{"cancelar", "10000"};
        Assert.assertTrue(check(args).isFailed());*/
    }

    @Test
    public void noAcredor () {
        /*String[] args = new String[]{"cancelar", "2"};
        Assert.assertTrue(check(args).isFailed());*/
    }

    private ValidationResult check (String[] args) {
        return ValidationsService.startValidating(args.length, Same.as(2, "1"))
                .andMayThrowException(() -> args[1], "2", NaturalNumber)
                .andMayThrowException(() -> existeDeuda(args), "3", True.of("No hay ninguna deuda con ese id"))
                .andMayThrowException(() -> acredorDeDeuda(args), "4", True.of("No eres el acredor de esa deuda"))
                .validateAll();
    }

    private boolean existeDeuda (String[] args) {
        try{
            return Deudas.INSTANCE.getDeuda(Integer.parseInt(args[1])) != null;
        }catch (Exception e) {
            return false;
        }
    }

    private boolean acredorDeDeuda (String[] args) {
        try{
            return Deudas.INSTANCE.getDeuda(Integer.parseInt(args[1])).getAcredor().equalsIgnoreCase(sender);
        }catch (Exception e) {
            return false;
        }
    }
}
