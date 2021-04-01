package es.serversurvival.comandos.subComandos.bolsa;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.comandos.PixelcoinCommand;
import es.serversurvival.util.Funciones;
import es.serversurvival.mySQL.tablasObjetos.PosicionCerrada;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

@Command(name = "bolsa operacionescerradas")
public class OperacionesCerradasBolsa extends PixelcoinCommand implements CommandRunner {

    public void execute(CommandSender player, String[] args) {
        List<PosicionCerrada> posicionCerradas = posicionesCerradasMySQL.getPosicionesCerradasJugador(player.getName());

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
            nombre = posicionCerrada.getNombre_activo();
            nAcciones = posicionCerrada.getCantidad();
            precioApertura = posicionCerrada.getPrecio_apertura();
            fechaApertura = posicionCerrada.getFecha_apertura();
            precioCierre = posicionCerrada.getPrecio_cierre();
            fechaCierre = posicionCerrada.getFecha_cierre();
            rentabilidad = posicionCerrada.getRentabilidad();
            revalorizacion = Funciones.redondeoDecimales(nAcciones * precioCierre, 3);
            beneficio = Funciones.redondeoDecimales(revalorizacion - (nAcciones * precioApertura), 3);

            if (beneficio <= 0) {
                player.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "ID: " + posicionCerrada.getId() + ChatColor.RESET + "" + ChatColor.GOLD + ": " + nombre + " nº unidades (acciones, barriles lo que sea xd) a: " + nAcciones + " precio de apertura: " + ChatColor.GREEN
                                + formatea.format(precioApertura) + " PC " + ChatColor.GOLD + " fecha de apertura: " + fechaApertura + " - precio de cierre: " + ChatColor.GREEN +
                                formatea.format(precioCierre) + " PC " + ChatColor.GOLD + "fecha cierre: " + fechaCierre +
                                "rentabilidad: " + ChatColor.RED + formatea.format(rentabilidad) + "% " + ChatColor.GOLD + " ingresos: " + ChatColor.GREEN + formatea.format(revalorizacion) + " de ellos perdidas: "
                                + ChatColor.RED + formatea.format(beneficio) + " PC");
            } else {
                player.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "ID: " + posicionCerrada.getId() + ChatColor.RESET + "" + ": " + ChatColor.GOLD + nombre + " unidades (acciones, barriles lo que sea xd) a " + nAcciones + " precio de apertura: " + ChatColor.GREEN
                                + formatea.format(precioApertura) + " PC " + ChatColor.GOLD + " fecha de apertura: " + fechaApertura + " - precio de cierre: " + ChatColor.GREEN +
                                formatea.format(precioCierre) + " PC " + ChatColor.GOLD + "fecha cierre: " + fechaCierre +
                                "rentabilidad: " + ChatColor.GREEN + "+" + formatea.format(rentabilidad) + "% " + ChatColor.GOLD + " ingresos: " + ChatColor.GREEN + formatea.format(revalorizacion) + " de ellos beneficios: "
                                + ChatColor.GREEN + "+" + formatea.format(beneficio) + " PC");
            }
            player.sendMessage("      ");
        }
        player.sendMessage(ChatColor.GOLD + "--------------------------------");
    }
}
