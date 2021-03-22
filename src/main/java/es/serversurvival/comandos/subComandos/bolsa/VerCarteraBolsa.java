package es.serversurvival.comandos.subComandos.bolsa;

import es.serversurvival.main.Funciones;
import es.serversurvival.objetos.mySQL.PosicionesAbiertas;
import es.serversurvival.objetos.mySQL.tablasObjetos.PosicionAbierta;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class VerCarteraBolsa extends BolsaSubCommand {
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

        posicionesAbiertasMySQL.conectar();
        double totalInvertido = posicionesAbiertasMySQL.getTotalInvertido(player.getName());
        HashMap<PosicionAbierta, Integer> carteraDeValoresConPeso = Funciones.sortMapByValueDecre(posicionesAbiertasMySQL.getPosicionesAbiertasConPesoJugador(player.getName(), totalInvertido));
        posicionesAbiertasMySQL.desconectar();

        player.sendMessage(ChatColor.GOLD + "" + "------------------------------");
        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "   LAS CARTERA DE " + nombreJugadorAVer);
        for(Map.Entry<PosicionAbierta, Integer> entry : carteraDeValoresConPeso.entrySet()){
            if(entry.getValue() == 0){
                player.sendMessage(ChatColor.GOLD + entry.getKey().getNombre() + " ( " + entry.getKey().getTipo() + " ) peso: 0% - 1% %");
            }else {
                player.sendMessage(ChatColor.GOLD + entry.getKey().getTipo() + "( " + entry.getKey().getNombre() + " ) peso: " + formatea.format(entry.getValue()) + "%");
            }
        }
        player.sendMessage(ChatColor.GOLD + "" + "------------------------------");
    }
}