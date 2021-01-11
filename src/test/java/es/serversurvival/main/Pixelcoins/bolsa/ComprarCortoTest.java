package es.serversurvival.main.Pixelcoins.bolsa;

import es.serversurvival.mySQL.MySQL;
import es.serversurvival.mySQL.PosicionesAbiertas;
import es.serversurvival.mySQL.enums.TipoPosicion;
import es.serversurvival.mySQL.tablasObjetos.PosicionAbierta;
import main.ValidationResult;
import main.ValidationsService;
import main.ValidationsService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static es.serversurvival.validaciones.Validaciones.*;

public class ComprarCortoTest {
    @Before
    public void setup () {
        MySQL.conectar();
    }

    @Test
    public void legit () {
        String[] args = new String[]{"comprarcorto", "277", "1"};
        Assert.assertTrue(check(args).isSuccessful());
    }

    @Test
    public void noNaturalNumebr () {
        String[] args = new String[]{"comprarcorto", "19", "-s1"};
        Assert.assertTrue(check(args).isFailed());
    }

    //Cuando la id es 1 y no encuentra la posicion abierta por algun motivo se bugua y no funciona. Pero cuando lo encuentra funciona sin problemas
    @Test
    public void noOwnerPosicionAbierta () {
        String[] args = new String[]{"comprarcorto", "16", "1"};
        Assert.assertTrue(check(args).isFailed());
    }

    public ValidationResult check (String[] args) {
         return ValidationsService.startValidating(args.length, Same.as(3, "error"))
                .andMayThrowException(() -> args[1], "error2", OwnerPosicionAbierta.de("JaimeTruman", TipoPosicion.CORTO))
                .andMayThrowException(() -> args[2], "error3", NaturalNumber)
                .validateAll();
    }
}
