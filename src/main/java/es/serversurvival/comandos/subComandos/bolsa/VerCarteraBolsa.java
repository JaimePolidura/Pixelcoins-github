package es.serversurvival.comandos.subComandos.bolsa;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.comandos.ComandoUtilidades;
import es.serversurvival.mySQL.MySQL;
import es.serversurvival.mySQL.tablasObjetos.PosicionAbierta;
import es.serversurvival.util.Funciones;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@Command(name = "bolsa vercartera")
public class VerCarteraBolsa extends ComandoUtilidades implements CommandRunner {
    @Override
    public void execute(CommandSender player, String[] args) {
        if (args.length != 2) {
            player.sendMessage(ChatColor.DARK_RED + "Uso incorrecto: /bolsa vercartera <jugador>");
            return;
        }
        String nombreJugadorAVer = args[1];

        MySQL.conectar();
        double totalInvertido = posicionesAbiertasMySQL.getAllPixeloinsEnAcciones(nombreJugadorAVer);
        Map<String, Integer> posicionesConPeso = getPesoCarteraAcciones(nombreJugadorAVer, totalInvertido);

        player.sendMessage(ChatColor.GOLD + "" + "------------------------------");
        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "   LAS CARTERA DE " + nombreJugadorAVer);
        for(Map.Entry<String, Integer> entry : posicionesConPeso.entrySet()){
            String nombreValor = llamadasApiMySQL.getLlamadaAPI(entry.getKey()).getNombre_activo();

            if(entry.getValue() == 0){
                player.sendMessage(ChatColor.GOLD + nombreValor + ": 0% - 1% %");
            }else {
                player.sendMessage(ChatColor.GOLD + nombreValor + ": " + formatea.format(entry.getValue()) + "%");
            }
        }
        player.sendMessage(ChatColor.GOLD + "" + "------------------------------");

        MySQL.desconectar();
    }

    private Map<String, Integer> getPesoCarteraAcciones (String jugador, double totalInvertido){
        Map<PosicionAbierta, Integer> posicionAbiertasPesoSinOrdenar = posicionesAbiertasMySQL.getPosicionesAbiertasConPesoJugador(jugador, totalInvertido);
        Map<String, Integer> posicionesAbiertasOrednadas = new HashMap<>();

        for(Map.Entry<PosicionAbierta, Integer> entry : posicionAbiertasPesoSinOrdenar.entrySet()){
            if(posicionesAbiertasOrednadas.get(entry.getKey().getNombre_activo()) != null){
                posicionesAbiertasOrednadas.put(entry.getKey().getNombre_activo(), posicionesAbiertasOrednadas.get(entry.getKey().getNombre_activo()) + entry.getValue());
            }else{
                posicionesAbiertasOrednadas.put(entry.getKey().getNombre_activo(), entry.getValue());
            }
        }

        posicionesAbiertasOrednadas = Funciones.sortMapByValueDecre(posicionesAbiertasOrednadas);

        return posicionesAbiertasOrednadas;
    }
}
