package es.serversurvival.main.Pixelcoins.bolsa;

import es.serversurvival.mySQL.MySQL;
import es.serversurvival.mySQL.enums.TipoPosicion;
import main.ValidationResult;
import main.ValidationsService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static es.serversurvival.validaciones.Validaciones.*;
import static es.serversurvival.validaciones.Validaciones.NaturalNumber;

public class BolsaVenderTest {
    private final String sender = "JaimeTruman";

    @Before
    public void setup () {
        MySQL.conectar();
    }

    @Test
    public void legit () {
        String[] args = new String[]{"vender", "19"};
        Assert.assertTrue(check(args).isSuccessful());

        String[] args2 = new String[]{"vender", "19", "20"};
        Assert.assertTrue(check(args2).isSuccessful());
    }

    public void noNaturalNumber () {
        String[] args = new String[]{"vender", "12sa", "-1"};
        Assert.assertTrue(check(args).isFailed());

        String[] args2 = new String[]{"vender", "19", "-1"};
        Assert.assertTrue(check(args2).isFailed());
    }

    public void badArguments () {
        String[] args = new String[]{"vender", "19", "10", "kjahs"};
        Assert.assertTrue(check(args).isFailed());
    }

    public void noOwnerPosicionAbierta () {
        String[] args = new String[]{"vender", "83", "10"};
        Assert.assertTrue(check(args).isFailed());

        String[] args2 = new String[]{"vender", "369", "10"};
        Assert.assertTrue(check(args2).isFailed());
    }

    public ValidationResult check (String[] args) {
        return ValidationsService.startValidating(args.length != 3 && args.length != 2, False.of("true"))
                .andMayThrowException(() -> args[1], "error", NaturalNumber, OwnerPosicionAbierta.de(sender, TipoPosicion.LARGO))
                .andIfExists(() -> args[2], NaturalNumber)
                .validateAll();
    }
}
