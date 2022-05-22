package es.serversurvival.bolsa.posicionesabiertas.vercarterajugador;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.activosinfo._shared.application.ActivosInfoService;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.SupportedTipoActivo;
import es.serversurvival.bolsa.posicionesabiertas._shared.application.PosicionesUtils;
import es.serversurvival.bolsa.posicionesabiertas._shared.domain.PosicionAbierta;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

import static es.serversurvival._shared.utils.CollectionUtils.*;
import static es.serversurvival._shared.utils.Funciones.FORMATEA;

@Command(
        value = "bolsa vercartera",
        args = {"jugador"},
        explanation = "Ver la cartera de la bolsa de otro jugador"
)
public class VerCarteraJugadorComandoRunner implements CommandRunnerArgs<VerCarteraJugadorComando> {
    private final ActivosInfoService activoInfoService;

    public VerCarteraJugadorComandoRunner() {
        this.activoInfoService = DependecyContainer.get(ActivosInfoService.class);
    }

    @Override
    public void execute(VerCarteraJugadorComando comando, CommandSender player) {
        String nombreJugadorAVer = comando.getJugador();

        double totalInvertido = PosicionesUtils.getAllPixeloinsEnValores(nombreJugadorAVer);
        Map<String, AccionPesoUsuario> posicionesConPeso = getPesoCarteraAcciones(nombreJugadorAVer, totalInvertido);

        player.sendMessage(ChatColor.GOLD + "" + "------------------------------");
        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "   LAS CARTERA DE " + nombreJugadorAVer);
        for(Map.Entry<String, AccionPesoUsuario> entry : posicionesConPeso.entrySet()){
            String nombreValor = activoInfoService.getByNombreActivo(entry.getKey(), entry.getValue().tipoActivo())
                    .getNombreActivoLargo();
            double precio = entry.getValue().peso;

            if(precio == 0){
                player.sendMessage(ChatColor.GOLD + nombreValor + ": 0% - 1% %");
            }else {
                player.sendMessage(ChatColor.GOLD + nombreValor + ": " + FORMATEA.format(entry.getValue()) + "%");
            }
        }
        player.sendMessage(ChatColor.GOLD + "" + "------------------------------");
    }

    private Map<String, AccionPesoUsuario> getPesoCarteraAcciones (String jugador, double totalInvertido){
        Map<PosicionAbierta, Integer> posicionAbiertasPesoSinOrdenar = PosicionesUtils.getPosicionesAbiertasConPesoJugador(jugador, totalInvertido);
        Map<String, AccionPesoUsuario> posicionesAbiertasOrednadas = new HashMap<>();

        for(Map.Entry<PosicionAbierta, Integer> entry : posicionAbiertasPesoSinOrdenar.entrySet()){
            if(posicionesAbiertasOrednadas.get(entry.getKey().getNombreActivo()) != null){
                int peso = posicionesAbiertasOrednadas.get(entry.getKey().getNombreActivo()).peso + entry.getValue();

                posicionesAbiertasOrednadas.put(entry.getKey().getNombreActivo(), new AccionPesoUsuario(
                        peso,
                        entry.getKey().getTipoActivo())
                );
            }else{
                posicionesAbiertasOrednadas.put(entry.getKey().getNombreActivo(), new AccionPesoUsuario(
                        entry.getValue(),
                        entry.getKey().getTipoActivo()
                ));
            }
        }

        return sortMapByValueDecre(posicionesAbiertasOrednadas);
    }

    private record AccionPesoUsuario(int peso, SupportedTipoActivo tipoActivo) implements Comparable<AccionPesoUsuario>{
        @Override
        public int compareTo(AccionPesoUsuario o) {
            return peso - o.peso;
        }
    }
}
