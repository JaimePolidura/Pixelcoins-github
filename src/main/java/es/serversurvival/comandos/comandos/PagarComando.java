package es.serversurvival.comandos.comandos;

import es.serversurvival.comandos.Comando;
import es.serversurvival.objetos.mySQL.Transacciones;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PagarComando extends Comando {
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
        Transacciones t = new Transacciones();
        String scantidad = "";
        String tname = "";
        double cantidad = 0;
        boolean done = false;

        //Comprobamos que haya metido dos argumentos
        if (args.length == 2) {
            tname = args[0];
            scantidad = args[1];
            //Comprobamos que el segundo argumento sea texto
            try {
                cantidad = Double.parseDouble(scantidad);
                done = true;
            } catch (NumberFormatException e) {
                p.sendMessage(ChatColor.DARK_RED + "Introduce un numero, no texto de tal manera: /pagar <nombre del jugador> <cantidad a pagar>");
                return;
            }
        } else {
            p.sendMessage(ChatColor.DARK_RED + "Uso incorrecto: /pagar <nombreDelJugador> <precio>");
            return;
        }

        if (done && cantidad > 0) {
            String destinatario = "";
            Player tp = Bukkit.getServer().getPlayer(tname);

            try {
                destinatario = tp.getName();
            } catch (Exception e) {
                p.sendMessage(ChatColor.DARK_RED + "Solo puedes pagar pixelcoins a jugadores que esten online");
                return;
            }

            //Comprobamos que el jugador a pagar este online
            if (!destinatario.equalsIgnoreCase(p.getName())) {
                t.conectar();
                t.realizarPagoManual(p.getName(), tp.getName(), cantidad, p, "", Transacciones.TIPO.JUGADOR_PAGO_MANUAL);
                t.desconectar();
            } else {
                p.sendMessage(ChatColor.DARK_RED + "Tu y yo sabemos que eso no va a pasar xd");
                return;
            }
        } else if (done && cantidad <= 0) {
            p.sendMessage(ChatColor.DARK_RED + "A ser posible hay que pagar una cantidad de dinero que no sea negativa o que no sea 0");
        }
    }
}
