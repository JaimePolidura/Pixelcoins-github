package es.serversurvival.bolsa.estadisticas;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.bolsa.posicionescerradas.mysql.PosicionCerrada;
import es.serversurvival.shared.comandos.PixelcoinCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

@Command("bolsa estadisticas")
public class EstadiscticasBolsaComando extends PixelcoinCommand implements CommandRunner {
    @Override
    public void execute(CommandSender player, String[] args) {
        player.sendMessage(ChatColor.GOLD + "--------------------------------");
        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "     ESTADISTICAS");

        List<PosicionCerrada> topOperaicnesMasRentables = posicionesCerradasMySQL.getPosicionesCerradasTopRentabilidad(player.getName(), 3);
        player.sendMessage(ChatColor.GOLD + "Mejores operaciones:");;
        for(int i = 0; i < topOperaicnesMasRentables.size(); i++){
            if(topOperaicnesMasRentables.get(i).getRentabilidad() >= 0 ){
                PosicionCerrada operacionCerrada = topOperaicnesMasRentables.get(i);

                String valorNombre = operacionCerrada.getSimbolo();
                String tipo = operacionCerrada.getTipo_activo().toString();
                String nombre = operacionCerrada.getNombre_activo();
                int id = operacionCerrada.getId();
                int cantidad = operacionCerrada.getCantidad();
                double apertura = operacionCerrada.getPrecio_apertura();
                double cierre = operacionCerrada.getPrecio_cierre();
                double rentabilidad = operacionCerrada.getRentabilidad();

                player.sendMessage(ChatColor.GOLD + "" + (i+1) + "º " + valorNombre + ": " + ChatColor.GREEN + "+" + rentabilidad + "% -> +" + formatea.format((cantidad * cierre) - (cantidad * apertura)) + " PC "
                        + ChatColor.DARK_AQUA + "ID: " + id);
            }
        }

        List<PosicionCerrada> topOpereacionesMenosRentables = posicionesCerradasMySQL.getPosicionesCerradasTopMenosRentabilidad(player.getName(), 3);
        player.sendMessage(ChatColor.GOLD + "Peores operaciones:");
        for(int i = 0; i < topOpereacionesMenosRentables.size(); i++){
            if(topOpereacionesMenosRentables.get(i).getRentabilidad() < 0){
                PosicionCerrada operacionCerrada = topOpereacionesMenosRentables.get(i);

                String nombreValor = operacionCerrada.getSimbolo();
                String nombre = operacionCerrada.getNombre_activo();
                int id = operacionCerrada.getId();
                int cantidad = operacionCerrada.getCantidad();
                double apertura = operacionCerrada.getPrecio_apertura();
                double cierre = operacionCerrada.getPrecio_cierre();
                double rentabilidad = operacionCerrada.getRentabilidad();

                player.sendMessage(ChatColor.GOLD + "" + (i + 1) + "º " + nombreValor  + ": " + ChatColor.RED + rentabilidad + "% -> " + formatea.format((cantidad * cierre) - (cantidad * apertura)) + " PC "
                        + ChatColor.DARK_AQUA + "ID:" + id);
            }
        }

        player.sendMessage(ChatColor.GOLD + "Si quieres ver todas las operaciones que has hecho y que tienes: " + ChatColor.AQUA + "/bolsa operacionesCerradas /bolsa cartera");
        player.sendMessage(ChatColor.GOLD + "--------------------------------");
    }
}
