package es.serversurvival.comandos.subComandos.bolsa;

import es.serversurvival.main.Funciones;
import es.serversurvival.objetos.mySQL.PosicionesAbiertas;
import es.serversurvival.objetos.mySQL.tablasObjetos.PosicionAbierta;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        int cantidad;
        double dineroInvertidoAccion = 0;
        double dineroInvertidoTotal = 0;
        String tipo;
        String nombre;

        List<PosicionAbierta> posicionAbiertasJugador = posicionesAbiertas.getPosicionesJugador(nombreJugadorAVer);
        //Mapa <id accion, dinero Invertido por accion (precio apertura * nºAcciones) ) >
        HashMap<Integer, Double> posiciones = new HashMap<>();

        for (PosicionAbierta posicionAbierta : posicionAbiertasJugador) {
            precioApertura = posicionAbierta.getPrecioApertura();
            cantidad = posicionAbierta.getCantidad();
            dineroInvertidoAccion = precioApertura * cantidad;
            dineroInvertidoTotal = dineroInvertidoTotal + dineroInvertidoAccion;

            posiciones.put(posicionAbierta.getId(), dineroInvertidoAccion);
        }
        for (Map.Entry<Integer, Double> entry : posiciones.entrySet()) {
            entry.setValue(Funciones.redondeoDecimales(Funciones.rentabilidad(dineroInvertidoTotal, entry.getValue()), 1));
        }
        posiciones = Funciones.sortByValueDecreIntDou(posiciones);

        player.sendMessage(ChatColor.GOLD + "" + "------------------------------");
        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "   LAS CARTERA DE " + nombreJugadorAVer);
        int pos = 1;
        for (Map.Entry<Integer, Double> entry : posiciones.entrySet()) {
            tipo = posicionesAbiertas.getTipo(entry.getKey());
            nombre = posicionesAbiertas.getNombre(entry.getKey());
            player.sendMessage(ChatColor.GOLD + "" + pos + "º: " + tipo + "( " + nombre + " ) " + "con un peso del: " + formatea.format(entry.getValue()) + "%");
            pos++;
        }
        player.sendMessage(ChatColor.GOLD + "" + "------------------------------");
        posicionesAbiertas.desconectar();
    }
}