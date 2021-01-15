package es.serversurvival.main.Pixelcoins.deudas;

import es.serversurvival.mySQL.Deudas;
import es.serversurvival.mySQL.MySQL;
import es.serversurvival.mySQL.tablasObjetos.Deuda;
import main.ValidationResult;
import main.ValidationsService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static es.serversurvival.validaciones.Validaciones.*;
import static es.serversurvival.validaciones.Validaciones.SuficientesPixelcoins;

public class PagarDeudaDeudasTest {
    private final String sender = "JaimeTruman";

    @Before
    public void setup () {
        MySQL.conectar();
    }

    @Test
    public void legit () {
        String[] args = new String[]{"pagar", "1"};
        //Assert.assertTrue(check(args).isSuccessful());
    }

    @Test
    public void noNaturalNumber () {
        String[] args = new String[]{"pagar", "1s"};
        //Assert.assertTrue(check(args).isFailed());
    }

    @Test
    public void noDeudorDeDeuda () {
        String[] args = new String[]{"pagar", "12000"};
        //Assert.assertTrue(check(args).isFailed());
    }

    @Test
    public void noSuficientesPixelcoins () {
        String[] args = new String[]{"pagar", "2"};
        Assert.assertTrue(check(args).isFailed());
    }

    private ValidationResult check (String[] args) {
        return ValidationsService.startValidating(args.length, Same.as(2, "error1"))
                .andMayThrowException(() -> args[1], "error2", NaturalNumber)
                .andMayThrowException(() -> String.valueOf(Deudas.INSTANCE.getDeuda(() -> args[1]).getPixelcoins_restantes()), "error3", SuficientesPixelcoins.of(sender, "error4"))
                .validateAll();
    }
}
