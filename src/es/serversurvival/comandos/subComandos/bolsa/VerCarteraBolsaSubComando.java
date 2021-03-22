package es.serversurvival.comandos.subComandos.bolsa;

import es.serversurvival.main.Funciones;
import es.serversurvival.objetos.mySQL.PosicionesAbiertas;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VerCarteraBolsaSubComando extends BolsaSubCommand {
    private final String SCNombre = "vercartera";
    private final String sintaxis = "/bolsa vercartera <jugador>";
    private final String ayuda = "ver la cartera de acciones de otros jugadores";

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
        if (args.length != 2) {
            player.sendMessage(ChatColor.DARK_RED + "Uso incorrecto: " + this.sintaxis);
            return;
        }
        String nombreJugadorAVer = args[1];
        if (nombreJugadorAVer.equalsIgnoreCase(player.getName())) {
            CarteraBolsaSubComando carteraBolsaSubComando = new CarteraBolsaSubComando();
            carteraBolsaSubComando.execute(player, args);
            return;
        }
        PosicionesAbiertas posicionesAbiertas = new PosicionesAbiertas();
        posicionesAbiertas.conectar();

        double precioApertura;
        int nAcciones;
        double dineroInvertidoAccion = 0;
        double dineroInvertidoTotal = 0;
        String ticker;

        ArrayList<Integer> ids = posicionesAbiertas.getIdsPosicionesJugador(nombreJugadorAVer);
        //Mapa <id accion, dinero Invertido por accion (precio apertura * nºAcciones) ) >
        HashMap<Integer, Double> acciones = new HashMap<>();

        for (int id : ids) {
            precioApertura = posicionesAbiertas.getPreciopApertura(id);
            nAcciones = posicionesAbiertas.getNAcciones(id);
            dineroInvertidoAccion = precioApertura * nAcciones;
            dineroInvertidoTotal = dineroInvertidoTotal + dineroInvertidoAccion;

            acciones.put(id, dineroInvertidoAccion);
        }
        for (Map.Entry<Integer, Double> entry : acciones.entrySet()) {
            entry.setValue(Funciones.redondeoDecimales(Funciones.rentabilidad(dineroInvertidoTotal, entry.getValue()), 1));
        }
        acciones = Funciones.sortByValueDecreIntDou(acciones);

        player.sendMessage(ChatColor.GOLD + "" + "------------------------------");
        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "   LAS ACCIONES DE " + nombreJugadorAVer);
        int pos = 1;
        for (Map.Entry<Integer, Double> entry : acciones.entrySet()) {
            ticker = posicionesAbiertas.getTicker(entry.getKey());
            player.sendMessage(ChatColor.GOLD + "" + pos + "º: " + ticker + " con un peso del: " + formatea.format(entry.getValue()) + "%");
            pos++;
        }
        player.sendMessage(ChatColor.GOLD + "" + "------------------------------");
        posicionesAbiertas.desconectar();
    }
}