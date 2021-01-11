package es.serversurvival.main.Pixelcoins.comandos;

import es.serversurvival.mySQL.MySQL;
import main.ValidationResult;
import main.ValidationsService;
import main.ValidationsService;
import main.validators.strings.EqualsIgnoreCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static es.serversurvival.validaciones.Validaciones.*;
import static es.serversurvival.validaciones.Validaciones.SuficientesPixelcoins;

public class PagarTest {
    private String sender = "JaimeTruman";

    @Before
    public void setUp () {
        MySQL.conectar();
    }

    @Test
    public void pagarTestLegit () {
        String[] args = new String[]{"juanxli", "100"};
        Assert.assertTrue(check(args).isSuccessful());
    }

    @Test
    public void pagarTestUnoMismo () {
        String[] args = new String[]{"JaimeTruman", "100"};
        Assert.assertTrue(check(args).isFailed());
    }

    @Test
    public void pagarTestNull1 () {
        String[] args = new String[]{"JaimeTruman", null};
        Assert.assertTrue(check(args).isFailed());
    }

    @Test
    public void pagarTestNull2 () {
        String[] args = new String[]{null, null};
        Assert.assertTrue(check(args).isFailed());
    }

    @Test
    public void pagarTestNull3 () {
        String[] args = new String[]{};
        Assert.assertTrue(check(args).isFailed());
    }

    @Test
    public void pagarTestDemasiadasPixelcoins () {
        String[] args = new String[]{"MOLONXX7", "100000000"};
        Assert.assertTrue(check(args).isFailed());
    }

    @Test
    public void pagarTestNoEncontrado () {
        String[] args = new String[]{"juanpedro", "100"};
        Assert.assertTrue(check(args).isFailed());
    }

    private ValidationResult check (String[] args) {
        return ValidationsService.startValidating(args.length, Same.as(2, "Same"))
                .andMayThrowException(() -> args[1], "Introduce un numero no texto", PositiveNumber, SuficientesPixelcoins.of(sender, () -> args[1]))
                .andMayThrowException(() -> args[0], "uso incorrecto", JugadorRegistrado, NotEqualsIgnoreCase.of(sender))
                .validateAll();
    }
}
