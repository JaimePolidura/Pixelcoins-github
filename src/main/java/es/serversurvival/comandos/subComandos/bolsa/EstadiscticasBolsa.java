package es.serversurvival.comandos.subComandos.bolsa;

import es.serversurvival.objetos.mySQL.PosicionesCerradas;
import es.serversurvival.objetos.mySQL.tablasObjetos.PosicionCerrada;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public class EstadiscticasBolsa extends BolsaSubCommand {
    private final String SCNombre = "estadisticas";
    private final String sintaxis = "/bolsa estadisticas";
    private final String ayuda = "Ver tus estadisticas de inversion";

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
        posicionesCerradasMySQL.conectar();

        player.sendMessage(ChatColor.GOLD + "--------------------------------");
        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "     ESTADISTICAS");


        List<PosicionCerrada> topOperaicnesMasRentables = posicionesCerradasMySQL.getPosicionesCerradasTopRentabilidad(player.getName(), 3);
        player.sendMessage(ChatColor.GOLD + "Mejores operaciones:");;
        for(int i = 0; i < topOperaicnesMasRentables.size(); i++){
            if(topOperaicnesMasRentables.get(i).getRentabilidad() >= 0 ){
                PosicionCerrada operacionCerrada = topOperaicnesMasRentables.get(i);

                String tipo = operacionCerrada.getTipo();
                String nombre = operacionCerrada.getNombre();
                int id = operacionCerrada.getId();
                int cantidad = operacionCerrada.getCantidad();
                double apertura = operacionCerrada.getPrecioApertura();
                double cierre = operacionCerrada.getPrecioCierre();
                double rentabilidad = operacionCerrada.getRentabilidad();

                player.sendMessage(ChatColor.GOLD + "" + (i+1) + ": " + tipo + " -> " + nombre + " : " + ChatColor.GREEN + "+" + rentabilidad + "% -> +" + formatea.format((cantidad * cierre) - (cantidad * apertura)) + " PC "
                        + ChatColor.DARK_AQUA + "ID: " + id);
            }
        }

        List<PosicionCerrada> topOpereacionesMenosRentables = posicionesCerradasMySQL.getPosicionesCerradasTopMenosRentabilidad(player.getName(), 3);
        player.sendMessage(ChatColor.GOLD + "Peores operaciones:");
        for(int i = 0; i < topOpereacionesMenosRentables.size(); i++){
            if(topOpereacionesMenosRentables.get(i).getRentabilidad() < 0){
                PosicionCerrada operacionCerrada = topOpereacionesMenosRentables.get(i);

                String tipo = operacionCerrada.getTipo();
                String nombre = operacionCerrada.getNombre();
                int id = operacionCerrada.getId();
                int cantidad = operacionCerrada.getCantidad();
                double apertura = operacionCerrada.getPrecioApertura();
                double cierre = operacionCerrada.getPrecioCierre();
                double rentabilidad = operacionCerrada.getRentabilidad();

                player.sendMessage(ChatColor.GOLD + "" + (i + 1) + ": " + tipo  + " -> " + nombre + " : " + ChatColor.RED + rentabilidad + "% -> " + formatea.format((cantidad * cierre) - (cantidad * apertura)) + " PC "
                        + ChatColor.DARK_AQUA + "ID:" + id);
            }
        }

        player.sendMessage(ChatColor.GOLD + "Si quieres ver todas las operaciones que has hecho y que tienes: " + ChatColor.AQUA + "/bolsa operacionesCerradas /bolsa cartera");
        player.sendMessage(ChatColor.GOLD + "--------------------------------");
        posicionesCerradasMySQL.desconectar();
    }
}