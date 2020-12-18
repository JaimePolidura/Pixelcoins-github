package es.serversurvival.comandos.comandos;

import es.serversurvival.comandos.Comando;
import es.serversurvival.menus.menus.solicitudes.VenderJugadorSolicitud;
import es.serversurvival.mySQL.MySQL;
import es.serversurvival.util.Funciones;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class VenderJugador extends Comando {
    private final String cnombre = "venderjugador";
    private final String sintaxis = "/venderjugador <jugador> <precio>";
    private final String ayuda = "Vender un objeto en especifica a un precio a otro jugador";

    @Override
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
    public void execute(Player player, String[] args) {
        if(args.length != 2){
            player.sendMessage(ChatColor.DARK_RED + "Uso incorrecto: " + sintaxis);
            return;
        }
        Player jugadorAVender = Bukkit.getPlayer(args[0]);
        if(jugadorAVender == null){
            player.sendMessage(ChatColor.DARK_RED + "Solo puedes vender a jugadores que esten online");
            return;
        }
        if(jugadorAVender.getName().equalsIgnoreCase(player.getName())){
            player.sendMessage(ChatColor.DARK_RED + "No te puedes vender a ti mismo");
            return;
        }
        if(Funciones.getEspaciosOcupados(jugadorAVender.getInventory()) == 36 ){
            player.sendMessage(ChatColor.DARK_RED + "El jugador tiene el inventario lleno");
            jugadorAVender.sendMessage(ChatColor.DARK_RED + player.getName() + " quiere venderte un objeto, pero tienes el inventario lleno");
            return;
        }
        if(!Funciones.esDouble(args[1])){
            player.sendMessage(ChatColor.DARK_RED + "El segundo argumento ha de ser un numero. Uso: " + sintaxis);
            return;
        }
        double precio = Double.parseDouble(args[1]);
        if(precio <= 0){
            player.sendMessage(ChatColor.DARK_RED + "El precio ha de ser positivo o no igual a 0");
            return;
        }
        ItemStack itemEnMano = player.getInventory().getItemInMainHand();
        if(itemEnMano == null || Funciones.esDeTipoItem(itemEnMano, "AIR")){
            player.sendMessage(ChatColor.DARK_RED + "Has de tener seleccionado en la mano el objeto que quieres vender");
            return;
        }
        MySQL.conectar();
        double jugadorAVenderDinero = jugadoresMySQL.getJugador(jugadorAVender.getName()).getPixelcoins();
        if(precio > jugadorAVenderDinero){
            player.sendMessage(ChatColor.DARK_RED + jugadorAVender.getName() + " no tiene " + formatea.format(precio) + " PC");
            MySQL.desconectar();
            return;
        }
        MySQL.desconectar();

        VenderJugadorSolicitud solicitud = new VenderJugadorSolicitud(player, jugadorAVender, itemEnMano, precio);
        solicitud.enviarSolicitud();
    }
}
