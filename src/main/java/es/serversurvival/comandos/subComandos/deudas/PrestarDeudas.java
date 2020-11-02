package es.serversurvival.comandos.subComandos.deudas;

import es.serversurvival.mySQL.MySQL;
import es.serversurvival.util.Funciones;
import es.serversurvival.menus.MenuManager;
import es.serversurvival.menus.menus.solicitudes.PrestamoSolicitud;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PrestarDeudas extends DeudasSubCommand {
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

    public void execute(Player jugadorQueEndeudaPlayer, String[] args) {
        if(args.length != 4 && args.length != 5){
            jugadorQueEndeudaPlayer.sendMessage(ChatColor.DARK_RED + "Uso incorrecto: " + this.sintaxis);
            return;
        }
        if(Bukkit.getPlayer(args[1]) == null){
            jugadorQueEndeudaPlayer.sendMessage(ChatColor.DARK_RED + "Solo puedes prestar dinero a jugadores que esten online");
            return;
        }

        Player jugadorAEndeudarPlayer = Bukkit.getPlayer(args[1]);
        if(jugadorAEndeudarPlayer.getName().equalsIgnoreCase(jugadorQueEndeudaPlayer.getName())){
            jugadorQueEndeudaPlayer.sendMessage(ChatColor.DARK_RED + "Como que no :v");
            return;
        }
        if(!Funciones.esInteger(args[2]) || !Funciones.esInteger(args[3]) || (args.length == 5 && !Funciones.esInteger(args[4])) ){
            jugadorQueEndeudaPlayer.sendMessage(ChatColor.DARK_RED + "Introduce numeros no texto");
            return;
        }
        int dinero = Integer.parseInt(args[2]);
        int dias = Integer.parseInt(args[3]);
        int interes = 0;
        if(args.length == 5){
            interes = Integer.parseInt(args[4]);
        }
        if(dinero <= 0 || dias <= 0 || interes < 0){
            jugadorQueEndeudaPlayer.sendMessage(ChatColor.DARK_RED + "Introduce valores que no sean negativos o dias mayores de 0 o que no sean decimales");
            return;
        }
        if(dinero < dias){
            jugadorQueEndeudaPlayer.sendMessage(ChatColor.DARK_RED + "Introduce valores de tal modo que el dinero sea superior a los dias");
            return;
        }
        if(MenuManager.getByPlayer(jugadorAEndeudarPlayer.getName()) != null){
            jugadorQueEndeudaPlayer.sendMessage(ChatColor.DARK_RED + "A ese jugador ya le has enviado una solicitud / ya le han enviado una solicitud");
            return;
        }

        MySQL.conectar();
        double pixelcoins = jugadoresMySQL.getJugador(jugadorQueEndeudaPlayer.getName()).getPixelcoin();
        int aPrestar = Funciones.aumentarPorcentaje(dinero, interes);
        if (aPrestar > pixelcoins) {
            jugadorQueEndeudaPlayer.sendMessage(ChatColor.DARK_RED + "No puedes prestar mas dinero del que tienes");
            MySQL.desconectar();
            return;
        }
        MySQL.desconectar();

        PrestamoSolicitud solicitud = new PrestamoSolicitud(jugadorQueEndeudaPlayer.getName(), jugadorAEndeudarPlayer.getName(), dinero, dias, interes);
        solicitud.enviarSolicitud();
    }
}