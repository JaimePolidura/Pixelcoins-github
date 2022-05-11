package es.serversurvival.bolsa.other.vercarterajugador;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival.bolsa.other._shared.posicionesabiertas.mysql.PosicionAbierta;
import es.serversurvival._shared.comandos.PixelcoinCommand;
import es.serversurvival._shared.utils.Funciones;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

@Command(
        value = "bolsa vercartera",
        args = {"jugador"},
        explanation = "Ver la cartera de la bolsa de otro jugador"
)
public class VerCarteraJugadorComandoRunner extends PixelcoinCommand implements CommandRunnerArgs<VerCarteraJugadorComando> {
    @Override
    public void execute(VerCarteraJugadorComando comando, CommandSender player) {
        String nombreJugadorAVer = comando.getJugador();

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
