package es.serversurvival.bolsa.posicionescerradas.verestadisticas;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerNonArgs;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.posicionescerradas._shared.application.PosicionesCerradasService;
import es.serversurvival.bolsa.posicionescerradas._shared.domain.PosicionCerrada;
import es.serversurvival._shared.comandos.PixelcoinCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

import static es.serversurvival.bolsa.posicionescerradas._shared.application.PosicionesCerradasService.*;

@Command(value = "bolsa estadisticas", explanation = "Ver tus estadisticas en la bolsa")
public class EstadiscticasBolsaComandoRunner extends PixelcoinCommand implements CommandRunnerNonArgs {
    private static final int LIMIT = 5;

    private final PosicionesCerradasService posicionesCerradasService;

    public EstadiscticasBolsaComandoRunner() {
        this.posicionesCerradasService = DependecyContainer.get(PosicionesCerradasService.class);
    }

    @Override
    public void execute(CommandSender player) {
        player.sendMessage(ChatColor.GOLD + "--------------------------------");
        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "     ESTADISTICAS");

        List<PosicionCerrada> topOperaicnesMasRentables = this.posicionesCerradasService.findByJugador(player.getName(), SORT_BY_RENTABILIDADES_DESC);
        player.sendMessage(ChatColor.GOLD + "Mejores operaciones:");;
        for(int i = 0; i < topOperaicnesMasRentables.size() || i < LIMIT; i++){
            if(topOperaicnesMasRentables.get(i).calculateRentabildiad() >= 0 ){
                PosicionCerrada operacionCerrada = topOperaicnesMasRentables.get(i);

                String nombre = operacionCerrada.getNombreActivo();
                int cantidad = operacionCerrada.getCantidad();
                double apertura = operacionCerrada.getPrecioApertura();
                double cierre = operacionCerrada.getPrecioCierre();
                double rentabilidad = operacionCerrada.calculateRentabildiad();

                player.sendMessage(ChatColor.GOLD + "" + (i+1) + "º " + nombre + ": " + ChatColor.GREEN + "+" + rentabilidad + "% -> +" +
                        formatea.format((cantidad * cierre) - (cantidad * apertura)) + " PC");
            }
        }

        List<PosicionCerrada> topOpereacionesMenosRentables = this.posicionesCerradasService.findByJugador(player.getName(), SORT_BY_RENTABILIDADES_ASC);
        player.sendMessage(ChatColor.GOLD + "Peores operaciones:");
        for(int i = 0; i < topOpereacionesMenosRentables.size() || i < LIMIT; i++){
            if(topOpereacionesMenosRentables.get(i).calculateRentabildiad() < 0){
                PosicionCerrada operacionCerrada = topOpereacionesMenosRentables.get(i);

                String nombre = operacionCerrada.getNombreActivo();
                int cantidad = operacionCerrada.getCantidad();
                double apertura = operacionCerrada.getPrecioApertura();
                double cierre = operacionCerrada.getPrecioCierre();
                double rentabilidad = operacionCerrada.calculateRentabildiad();

                player.sendMessage(ChatColor.GOLD + "" + (i + 1) + "º " + nombre  + ": " + ChatColor.RED + rentabilidad + "% -> " +
                        formatea.format((cantidad * cierre) - (cantidad * apertura)) + " PC ");
            }
        }

        player.sendMessage(ChatColor.GOLD + "Si quieres ver todas las operaciones que has hecho y que tienes: " + ChatColor.AQUA + "/bolsa operacionesCerradas /bolsa cartera");
        player.sendMessage(ChatColor.GOLD + "--------------------------------");
    }
}
