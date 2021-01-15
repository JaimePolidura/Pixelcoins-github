package es.serversurvival.main.Pixelcoins.deudas;

import es.serversurvival.menus.MenuManager;
import es.serversurvival.mySQL.MySQL;
import es.serversurvival.util.Funciones;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.Bukkit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.function.Supplier;

import static es.serversurvival.validaciones.Validaciones.*;

public class PrestarDeudasTest {
    private final String sender = "JaimeTruman";

    @Before
    public void setup () {
        MySQL.conectar();
    }

    @Test
    public void noArgs () {
        String[] args = new String[]{"prestar"};
        Assert.assertTrue(check(args).isFailed());
    }

    @Test
    public void legit () {
        String[] args = new String[]{"prestar", "juanxli", "100", "10", "10"};
        Assert.assertTrue(check(args).isSuccessful());
    }

    @Test
    public void noNaturalNumber () {
        String[] args = new String[]{"prestar", "juanxli", "-1", "s"};
        Assert.assertTrue(check(args).isFailed());

        String[] args2 = new String[]{"prestar", "juanxli", "100", "10", "-1"};
        Assert.assertTrue(check(args2).isFailed());
    }

    @Test
    public void tiempoSuperiorPixelcoins () {
        String[] args = new String[]{"prestar", "juanxli", "10", "100"};
        Assert.assertTrue(check(args).isFailed());
    }

    @Test
    public void suficientesPixlecoins () {
        String[] args = new String[]{"prestar", "juanxli", "1000000", "10"};
        Assert.assertTrue(check(args).isFailed());
    }

    private ValidationResult check (String[] args) {
        return ValidationsService.startValidating(args.length == 4 || args.length == 5, True.of("1"))
                //.andMayThrowException(() -> Bukkit.getPlayer(args[1]), "ex2", JugadorOnline)
                .andMayThrowException(() -> args[1], "ex3", NotEqualsIgnoreCase.of(sender, "No puedes ser tu mimsmo"))
                .andMayThrowException(() -> args[2], "ex4", NaturalNumber)
                .andMayThrowException(() -> args[3], "ex5", NaturalNumber)
                .andIfExists(() -> args[4], NaturalNumber)
                .andMayThrowException(() -> Integer.parseInt(args[2]) >= Integer.parseInt(args[3]), "ex6", True.of("Los dias no pueden ser superior a las pixelcoins"))
                .andMayThrowException(() -> true, "ex7", True.of("Ya le han enviado una solicitud"))
                .andMayThrowException(pixelcoinsDeudaConIntereses(args), "ex8", SuficientesPixelcoins.of(sender))
                .validateAll();
    }


    public static Supplier<String> pixelcoinsDeudaConIntereses (String[] args) {
        try{
            int interes = 0;
            int dinero = Integer.parseInt(args[2]);

            if(args.length == 5){
                interes = Integer.parseInt(args[4]);
            }

            int finalInteres = interes;
            return () -> String.valueOf(Funciones.aumentarPorcentaje(dinero, finalInteres));
        }catch (Exception e) {
            return () -> "";
        }
    }
}
