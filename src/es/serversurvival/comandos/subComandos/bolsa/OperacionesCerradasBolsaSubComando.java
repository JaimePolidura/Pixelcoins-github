package es.serversurvival.comandos.subComandos.bolsa;

import es.serversurvival.main.Funciones;
import es.serversurvival.objetos.mySQL.PosicionesCerradas;
import es.serversurvival.objetos.mySQL.tablasObjetos.PosicionCerrada;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class OperacionesCerradasBolsaSubComando extends BolsaSubCommand {
    private final String SCNombre = "operacionescerradas";
    private final String sintaxis = "/bolsa operacionescerradas";
    private final String ayuda = "Ver todas las operaciones que has hecho en bolsa en todo el tiempo";

    public String getSCNombre() {
        return SCNombre;
    }

    public String getSintaxis() {
        return sintaxis;
    }

    public String getAyuda() {
        return ayuda;
    }

    public void execute(Player player, String[] args) {
        PosicionesCerradas posicionesCerradas = new PosicionesCerradas();
        posicionesCerradas.conectar();
        List<PosicionCerrada> posicionCerradas = posicionesCerradas.getIdPosiciones(player.getName());

        String nombre;
        int nAcciones;
        double precioApertura;
        String fechaApertura;
        double precioCierre;
        String fechaCierre;
        double rentabilidad;
        double revalorizacion;
        double beneficio;

        player.sendMessage(ChatColor.GOLD + "--------------------------------");
        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + " TODAS TUS OPERACIONES");
        for (PosicionCerrada posicionCerrada : posicionCerradas) {
            nombre = posicionCerrada.getNombre();
            nAcciones = posicionCerrada.getCantidad();
            precioApertura = posicionCerrada.getPrecioApertura();
            fechaApertura = posicionCerrada.getFechaApertura();
            precioCierre = posicionCerrada.getPrecioCierre();
            fechaCierre = posicionCerrada.getFechaCierre();
            rentabilidad = posicionCerrada.getRentabilidad();
            revalorizacion = Funciones.redondeoDecimales(nAcciones * precioCierre, 3);
            beneficio = Funciones.redondeoDecimales(revalorizacion - (nAcciones * precioApertura), 3);

            if (beneficio <= 0) {
                player.sendMessage(
                        ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "ID: " + posicionCerrada.getId() + ChatColor.RESET + "" + ChatColor.GOLD + ": " + nombre + " nº unidades (acciones, barriles lo que sea xd) a: " + nAcciones + " precio de apertura: " + ChatColor.GREEN
                                + formatea.format(precioApertura) + " PC " + ChatColor.GOLD + " fecha de apertura: " + fechaApertura + " - precio de cierre: " + ChatColor.GREEN +
                                formatea.format(precioCierre) + " PC " + ChatColor.GOLD + "fecha cierre: " + fechaCierre +
                                "rentabilidad: " + ChatColor.RED + formatea.format(rentabilidad) + "% " + ChatColor.GOLD + " ingresos: " + ChatColor.GREEN + formatea.format(revalorizacion) + " de ellos perdidas: "
                                + ChatColor.RED + formatea.format(beneficio) + " PC"
                );
            } else {
                player.sendMessage(
                        ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "ID: " + posicionCerrada.getId() + ChatColor.RESET + "" + ": " + ChatColor.GOLD + nombre + " unidades (acciones, barriles lo que sea xd) a " + nAcciones + " precio de apertura: " + ChatColor.GREEN
                                + formatea.format(precioApertura) + " PC " + ChatColor.GOLD + " fecha de apertura: " + fechaApertura + " - precio de cierre: " + ChatColor.GREEN +
                                formatea.format(precioCierre) + " PC " + ChatColor.GOLD + "fecha cierre: " + fechaCierre +
                                "rentabilidad: " + ChatColor.GREEN + "+" + formatea.format(rentabilidad) + "% " + ChatColor.GOLD + " ingresos: " + ChatColor.GREEN + formatea.format(revalorizacion) + " de ellos beneficios: "
                                + ChatColor.GREEN + "+" + formatea.format(beneficio) + " PC"
                );
            }
            player.sendMessage("      ");
        }
        player.sendMessage(ChatColor.GOLD + "--------------------------------");
    }
}