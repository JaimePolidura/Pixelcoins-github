package es.serversurvival.scoreboeards;

import es.serversurvival.mySQL.tablasObjetos.LlamadaApi;
import es.serversurvival.mySQL.tablasObjetos.PosicionAbierta;
import es.serversurvival.mySQL.enums.TipoPosicion;
import es.serversurvival.util.Funciones;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.*;

import static es.serversurvival.util.MinecraftUtils.*;

public class BolsaScoreboard implements SingleScoreboard {
    private Map<String, LlamadaApi> llamadasApiMap;

    public BolsaScoreboard () {
        this.llamadasApiMap = llamadasApiMySQL.getMapOfAllLlamadasApi();
    }

    @Override
    public Scoreboard createScoreborad(String jugador) {

        Scoreboard scoreboard = createScoreboard("bolsa", ChatColor.GOLD + "" + ChatColor.BOLD + "TUS MEJORES ACCIONES");
        Objective objective = scoreboard.getObjective("bolsa");

        Map<PosicionAbierta, Double> posicionAbiertas = calcularTopPosicionesAbiertas(jugador);
        int loops = 0;
        int pos = 0;
        for(Map.Entry<PosicionAbierta, Double> entry : posicionAbiertas.entrySet()){
            if(loops == 4) break;

            String linea = buildLinea(entry.getKey(), entry.getValue());

            addLineToScoreboard(objective, linea, pos);

            pos--;
            loops++;
        }

        addLineToScoreboard(objective, ChatColor.GOLD + "       ", -10);
        addLineToScoreboard(objective, ChatColor.GOLD + "--------------------------", -20);
        addLineToScoreboard(objective, ChatColor.GOLD + "Para invertir /bosla valores", -30);
        addLineToScoreboard(objective, ChatColor.GOLD + "Tus acciones /bolsa cartera", -40);

        return scoreboard;
    }

    private Map<PosicionAbierta, Double> calcularTopPosicionesAbiertas (String jugador) {
        List<PosicionAbierta> posicionAbiertas = posicionesAbiertasMySQL.getPosicionesAbiertasJugadorCondicion(jugador, PosicionAbierta::noEsTipoAccionServerYLargo);
        Map<PosicionAbierta, Double> posicionAbiertasConRentabilidad = new HashMap<>();

        for (PosicionAbierta posicion : posicionAbiertas) {
            double precioInicial = posicion.getPrecio_apertura();
            double precioActual = llamadasApiMap.get(posicion.getNombre_activo()).getPrecio();
            double rentabildad;

            if(posicion.getTipo_posicion() == TipoPosicion.LARGO){
                rentabildad = Funciones.redondeoDecimales(Funciones.diferenciaPorcntual(precioInicial, precioActual), 2);
            }else{
                rentabildad = Math.abs(Funciones.redondeoDecimales(Funciones.diferenciaPorcntual(precioActual, precioInicial), 2));
            }

            posicionAbiertasConRentabilidad.put(posicion, rentabildad);
        }

        return Funciones.sortMapByValueDecre(posicionAbiertasConRentabilidad);
    }

    private String buildLinea (PosicionAbierta posicion, Double rentabilidad) {
        String nombreEmpresa = llamadasApiMap.get(posicion.getNombre_activo()).getNombre_activo();
        String linea;

        if(rentabilidad >= 0){
            linea = ChatColor.GOLD + nombreEmpresa + ": " + ChatColor.GREEN + "+" + rentabilidad + "%";
        }else{
            linea = ChatColor.GOLD + nombreEmpresa + ": " + ChatColor.RED + rentabilidad + "%";
        }

        if(linea.length() > 40){
            if(rentabilidad >= 0)
                linea = ChatColor.GOLD + posicion.getNombre_activo() + ": " + ChatColor.GREEN + "+" + rentabilidad + "%";
            else
                linea = ChatColor.GOLD + posicion.getNombre_activo() + ": " + ChatColor.RED + rentabilidad + "%";
        }

        return linea;
    }
}
