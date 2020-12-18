package es.serversurvival.scoreboeards;

import es.serversurvival.mySQL.LlamadasApi;
import es.serversurvival.mySQL.PosicionesAbiertas;
import es.serversurvival.mySQL.tablasObjetos.PosicionAbierta;
import es.serversurvival.mySQL.enums.TipoPosicion;
import es.serversurvival.util.Funciones;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.*;

public class BolsaScoreboard implements SingleScoreboard{
    private PosicionesAbiertas posicionesAbiertasMySQL = PosicionesAbiertas.INSTANCE;
    private LlamadasApi llamadasApiMySQL = LlamadasApi.INSTANCE;

    @Override
    public Scoreboard createScoreborad(String jugador) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("bolsa", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "TUS MEJORES ACCIONES");

        Map<PosicionAbierta, Double> posicionAbiertas = calcularTopPosicionesAbiertas(jugador);
        int loops = 0;
        int pos = 0;
        for(Map.Entry<PosicionAbierta, Double> entry : posicionAbiertas.entrySet()){
            if(loops == 4) break;

            String linea = buildLinea(entry.getKey(), entry.getValue());

            Score score = objective.getScore(linea);
            score.setScore(pos);

            pos--;
            loops++;
        }
        
        Score score0 = objective.getScore(ChatColor.GOLD + "       ");
        score0.setScore(-10);

        Score score = objective.getScore(ChatColor.GOLD + "--------------------------");
        score.setScore(-20);

        Score score2 = objective.getScore(  ChatColor.GOLD + "Para invertir /bosla valores");
        score2.setScore(-30);

        Score score3 = objective.getScore(ChatColor.GOLD + "Tus acciones /bolsa cartera");
        score3.setScore(-40);

        return scoreboard;
    }

    private Map<PosicionAbierta, Double> calcularTopPosicionesAbiertas (String jugador) {
        List<PosicionAbierta> posicionAbiertas = posicionesAbiertasMySQL.getPosicionesAbiertasJugadorCondicion(jugador, PosicionAbierta::esLargo);
        Map<PosicionAbierta, Double> posicionAbiertasConRentabilidad = new HashMap<>();

        for (PosicionAbierta posicion : posicionAbiertas) {
            double precioInicial = posicion.getPrecio_apertura();
            double precioActual = llamadasApiMySQL.getLlamadaAPI(posicion.getNombre_activo()).getPrecio();
            double rentabildad;

            if(posicion.getTipo_posicion().equalsIgnoreCase(TipoPosicion.LARGO.toString())){
                rentabildad = Funciones.redondeoDecimales(Funciones.diferenciaPorcntual(precioInicial, precioActual), 2);
            }else{
                rentabildad = Math.abs(Funciones.redondeoDecimales(Funciones.diferenciaPorcntual(precioActual, precioInicial), 2));
            }

            posicionAbiertasConRentabilidad.put(posicion, rentabildad);
        }

        return  Funciones.sortMapByValueDecre(posicionAbiertasConRentabilidad);
    }

    private String buildLinea (PosicionAbierta posicion, Double rentabilidad) {
        String nombreEmpresa = llamadasApiMySQL.getLlamadaAPI(posicion.getNombre_activo()).getNombre_activo();
        String linea;

        if(rentabilidad >= 0){
            linea = ChatColor.GOLD + nombreEmpresa + ": " + ChatColor.GREEN + "+" + rentabilidad + "%";
        }else{
            linea = ChatColor.GOLD + nombreEmpresa + ": " + ChatColor.RED + rentabilidad + "%";
        }

        if(linea.length() > 40){
            if(rentabilidad >= 0){
                linea = ChatColor.GOLD + posicion.getNombre_activo() + ": " + ChatColor.GREEN + "+" + rentabilidad + "%";
            }else{
                linea = ChatColor.GOLD + posicion.getNombre_activo() + ": " + ChatColor.RED + rentabilidad + "%";
            }
        }

        return linea;
    }

}
