package es.serversurvival.comandos.subComandos.deudas;

import es.serversurvival.main.Funciones;
import es.serversurvival.objetos.mySQL.Jugador;
import es.serversurvival.objetos.solicitudes.PrestamosSolicitud;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PrestarDeudasSubComando extends DeudasSubCommand {
    private final String scnombre = "prestar";
    private final String sintaxis = "/deudas prestar <jugador> <dinero> <dias> [interes]";
    private final String ayuda = "prestar una deuda en pixelcoins en un plazo de unos diads. /ayuda deudas";

    public String getSCNombre() {
        return scnombre;
    }

    public String getSintaxis() {
        return sintaxis;
    }

    public String getAyuda() {
        return ayuda;
    }

    public void execute(Player p, String[] args) {
        int dinero = 0;
        int dias = 0;
        int interes = 0;
        Player tp = null;
        String destinatario = "";
        /** /deudas prestar <jugador> <dinero> <dias> [interes]"   */

        if (args.length == 4 || args.length == 5) {
            destinatario = "";
            tp = Bukkit.getServer().getPlayer(args[1]);

            try {
                destinatario = tp.getName();
            } catch (Exception e) {
                p.sendMessage(ChatColor.DARK_RED + "Solo puedes prestar dinero a jugadores que esten online");
                return;
            }

            if (!(destinatario.equalsIgnoreCase(p.getName()))) {

                try {
                    dinero = Integer.parseInt(args[2]);
                    dias = Integer.parseInt(args[3]);
                    if (args.length == 5) {
                        interes = Integer.parseInt(args[4]);
                    }

                    if (dinero >= 0 && dias > 0 && interes >= 0) {
                        if (dinero < dias) {
                            p.sendMessage(ChatColor.DARK_RED + "Introduce valores de tal modo que el dinero sea superior a los dias");
                            return;
                        }
                    } else {
                        p.sendMessage(ChatColor.DARK_RED + "Introduce valores que no sean negativos o dias mayores de 0 o que no sean decimales");
                        return;
                    }
                } catch (NumberFormatException e) {
                    p.sendMessage(ChatColor.DARK_RED + "Introduce numeros no texto");
                    return;
                }
            } else {
                p.sendMessage(ChatColor.DARK_RED + "Como que no :v");
                return;
            }
        } else {
            p.sendMessage(ChatColor.DARK_RED + "Uso incorrecto: " + this.sintaxis);
            return;
        }
        if (PrestamosSolicitud.haSidoSolicitado(tp.getName())) {
            p.sendMessage(ChatColor.DARK_RED + "A ese jugador ya le has enviado una solicitud / ya le han enviado una solicitud");
            return;
        }
        Jugador j = new Jugador();

        j.conectar();
        double pixelcoins = j.getDinero(p.getName());
        int aPrestar = Funciones.interes(dinero, interes);

        if (aPrestar > pixelcoins) {
            p.sendMessage(ChatColor.DARK_RED + "No puedes prestar mas dinero del que tienes");
            return;
        }
        j.desconectar();

        PrestamosSolicitud prestamosSolicitud = new PrestamosSolicitud(p.getName(), tp.getName(), dinero, dias, interes);
        prestamosSolicitud.enviarSolicitud();
    }
}