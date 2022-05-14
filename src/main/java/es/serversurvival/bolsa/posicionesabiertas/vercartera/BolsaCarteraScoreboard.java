package es.serversurvival.bolsa.posicionesabiertas.vercartera;

import es.serversurvival.bolsa.activosinfo._shared.domain.ActivoInfo;
import es.serversurvival.bolsa.posicionesabiertas._shared.application.PosicionesUtils;
import es.serversurvival.bolsa.posicionesabiertas._shared.domain.PosicionAbierta;
import es.serversurvival._shared.scoreboards.SingleScoreboard;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.*;

import static es.serversurvival._shared.utils.MinecraftUtils.*;

public class BolsaCarteraScoreboard implements SingleScoreboard {
    private Map<String, ActivoInfo> llamadasApiMap;

    @Override
    public Scoreboard createScoreborad(String jugador) {
        this.llamadasApiMap = AllMySQLTablesInstances.llamadasApiMySQL.getMapOfAllLlamadasApi();

        Scoreboard scoreboard = createScoreboard("bolsa", ChatColor.GOLD + "" + ChatColor.BOLD + "TUS MEJORES ACCIONES");
        Objective objective = scoreboard.getObjective("bolsa");

        Map<PosicionAbierta, Double> posicionAbiertas = PosicionesUtils.calcularTopPosicionesAbiertas(jugador);
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

    private String buildLinea (PosicionAbierta posicion, Double rentabilidad) {
        String nombreEmpresa = llamadasApiMap.get(posicion.getNombreActivo()).getNombreActivoLargo();
        String linea;

        if(rentabilidad >= 0){
            linea = ChatColor.GOLD + nombreEmpresa + ": " + ChatColor.GREEN + "+" + rentabilidad + "%";
        }else{
            linea = ChatColor.GOLD + nombreEmpresa + ": " + ChatColor.RED + rentabilidad + "%";
        }

        if(linea.length() > 40){
            if(rentabilidad >= 0)
                linea = ChatColor.GOLD + posicion.getNombreActivo() + ": " + ChatColor.GREEN + "+" + rentabilidad + "%";
            else
                linea = ChatColor.GOLD + posicion.getNombreActivo() + ": " + ChatColor.RED + rentabilidad + "%";
        }

        return linea;
    }
}
