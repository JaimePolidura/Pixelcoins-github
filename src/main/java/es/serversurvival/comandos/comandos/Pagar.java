package es.serversurvival.comandos.comandos;

import es.serversurvival.comandos.Comando;
import es.serversurvival.mySQL.MySQL;
import es.serversurvival.mySQL.enums.TRANSACCIONES;
import es.serversurvival.util.Funciones;
import es.serversurvival.mySQL.Jugadores;
import es.serversurvival.mySQL.Transacciones;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Pagar extends Comando {
    private final String cnombre = "pagar";
    private final String sintaxis = "/pagar <jugador> <pixelcoins>";
    private final String ayuda = "Pagar a un jugador un numero de pixelcoins";

    public String getCNombre() {
        return cnombre;
    }

    @Override
    public String getSintaxis() {
        return sintaxis;
    }

    @Override
    public String getAyuda() {
        return ayuda;
    }

    @Override
    public void execute(Player p, String[] args) {
        if(args.length != 2){
            p.sendMessage(ChatColor.DARK_RED + "Uso incorrecto: /pagar <nombreDelJugador> <precio>");
            return;
        }
        if(!Funciones.esDouble(args[1])){
            p.sendMessage(ChatColor.DARK_RED + "Introduce un numero, no texto de tal manera: /pagar <nombre del jugador> <cantidad a pagar>");
            return;
        }
        double cantidad = Double.parseDouble(args[1]);
        if(cantidad <= 0){
            p.sendMessage(ChatColor.DARK_RED + "A ser posible hay que pagar una cantidad de dinero que no sea negativa o que no sea 0");
            return;
        }
        if(args[0].equalsIgnoreCase(p.getName())){
            p.sendMessage(ChatColor.DARK_RED + "Tu y yo sabemos que eso no va a pasar xd");
            return;
        }

        MySQL.conectar();

        if(jugadoresMySQL.getJugador(p.getName()).getPixelcoin() < cantidad ){
            p.sendMessage(ChatColor.DARK_RED + "No puedes pagar a un jugador por encima de tu dinero :v");
            MySQL.desconectar();
            return;
        }

        transaccionesMySQL.realizarPagoManual(p.getName(), args[0], cantidad, p, "", TRANSACCIONES.JUGADOR_PAGO_MANUAL);
        MySQL.desconectar();
    }
}