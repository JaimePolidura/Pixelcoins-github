package es.serversurvival.main.Pixelcoins.comandos;

import es.serversurvival.mySQL.MySQL;
import main.ValidationResult;
import main.ValidationsService;
import main.ValidationsService;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static es.serversurvival.validaciones.Validaciones.*;
import static es.serversurvival.validaciones.Validaciones.SuficientesEspaciosTienda;

//El maximo de los espacios de la tienda ha sido testeado y funciona
public class VenderTest {
    private ItemStack itemLegit = new ItemStack(Material.ACACIA_BUTTON);
    private ItemStack itemNoLegit = new ItemStack(Material.SPLASH_POTION);
    private String sender = "JaimeTruman";

    @Before
    public void setup () {
        MySQL.conectar();
    }

    @Test
    public void legit () {
        String[] args = new String[]{"1231"};
        Assert.assertTrue(check(args, itemLegit).isSuccessful());
    }

    @Test
    public void itemIncorrecto () {
        String[] args = new String[]{"123"};
        Assert.assertTrue(check(args, itemNoLegit).isFailed());
    }

    @Test
    public void itemIncorrecto2 () {
        String[] args = new String[]{"123"};
        Assert.assertTrue(check(args, new ItemStack(Material.SPLASH_POTION)).isFailed());
    }

    @Test
    public void precioNegativo () {
        String[] args = new String[]{"-1"};
        Assert.assertTrue(check(args, itemLegit).isFailed());
    }

    @Test
    public void precioTexto () {
        String[] args = new String[]{"asasasas"};
        Assert.assertTrue(check(args, itemLegit).isFailed());
    }

    @Test
    public void usoIncorrecto () {
        String[] args = new String[]{"1234", "sasas"};
        Assert.assertTrue(check(args, itemLegit).isFailed());
    }

    private ValidationResult check (String[] args, ItemStack item) {
        return ValidationsService.startValidating(args.length, Same.as(1))
                .and(item.getType().toString(), NotEqualsIgnoreCase.of("AIR", "Tienes que tener un objeto en la mano"), ItemNotBaneadoTienda)
                .andMayThrowException(() -> args[0], "Uso incorrecto", PositiveNumber)
                //.and(itemLegit, NoHaSidoCompradoItem) tendriamos que tener el servido encendido para que funcine
                .and(sender, SuficientesEspaciosTienda)
                .validateAll();
    }
}
