package es.serversurvival.v1._shared.scoreboards.displays;

import es.serversurvival.v1._shared.scoreboards.ScoreboardCreator;
import es.serversurvival.v1._shared.scoreboards.ServerScoreboardCreator;
import es.serversurvival.v1.bolsa.activosinfo._shared.application.ActivosInfoService;
import es.serversurvival.v1.bolsa.posicionesabiertas._shared.application.PosicionesUtils;
import es.serversurvival.v1.bolsa.posicionesabiertas._shared.domain.PosicionAbierta;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.*;

import static es.serversurvival.v1._shared.utils.MinecraftUtils.*;

@ScoreboardCreator
@RequiredArgsConstructor
public class BolsaCarteraScoreboard implements ServerScoreboardCreator {
    private final ActivosInfoService activoInfoService;
    private final PosicionesUtils posicionesUtils;

    @Override
    public boolean isGlobal() {
        return false;
    }

    @Override
    public Scoreboard create(Player player) {
        Scoreboard scoreboard = createScoreboard("bolsa", ChatColor.GOLD + "" + ChatColor.BOLD + "TUS MEJORES ACCIONES");
        Objective objective = scoreboard.getObjective("bolsa");

        Map<PosicionAbierta, Double> posicionAbiertas = this.posicionesUtils.calcularTopPosicionesAbiertas(player.getName());
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
        addLineToScoreboard(objective, ChatColor.GOLD + "Tus cantidad /bolsa cartera", -40);

        return scoreboard;
    }

    private String buildLinea (PosicionAbierta posicion, Double rentabilidad) {
        String nombreEmpresa = this.activoInfoService.getByNombreActivo(posicion.getNombreActivo(), posicion.getTipoActivo()).getNombreActivoLargo();
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
