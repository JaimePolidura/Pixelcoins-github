package es.serversurvival.jugadores.perfil;

import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.empresas.empresas._shared.application.EmpresasService;
import es.serversurvival.empresas.empresas._shared.domain.Empresa;
import es.serversurvival._shared.scoreboards.SingleScoreboard;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival._shared.utils.MinecraftUtils;
import es.serversurvival.jugadores._shared.newformat.application.JugadoresService;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.List;

import static es.serversurvival._shared.utils.MinecraftUtils.addLineToScoreboard;

public class StatsDisplayScoreboard implements SingleScoreboard {
    private final JugadoresService jugadoresService;
    private final EmpresasService empresasService;

    public StatsDisplayScoreboard(){
        this.empresasService = DependecyContainer.get(EmpresasService.class);
        this.jugadoresService = DependecyContainer.get(JugadoresService.class);
    }

    @Override
    public Scoreboard createScoreborad(String jugador) {
        Scoreboard scoreboard = MinecraftUtils.createScoreboard("dinero", ChatColor.GOLD + "" + ChatColor.BOLD + "JUGADOR");
        Objective objective = scoreboard.getObjective("dinero");

        double dineroJugador = jugadoresService.getJugadorByNombre(jugador).getPixelcoins();

        addLineToScoreboard(objective, ChatColor.GOLD + "Tus ahorros: " + ChatColor.GREEN + formatea.format(Math.round(dineroJugador)) + " PC", 1);
        addLineToScoreboard(objective, "     ", 0);
        addLineToScoreboard(objective, ChatColor.GOLD + "-------Empresas-----", -2);

        List<Empresa> empresas = sortEmpresaByPixelcoins(empresasService.getByOwner(jugador));
        for(int i = 0; i < empresas.size(); i++){
            Empresa empresa = empresas.get(i);

            String mensaje = ChatColor.GOLD + "- " + empresa.getNombre() + " (" + ChatColor.GREEN + formatea.format(empresa.getPixelcoins()) + " PC ";
            mensaje = mensaje + calcularRentabilidadEmpresaYFormatear(empresa);
            mensaje = cambiarLongitudDelMensajeSiEsNecesario(mensaje, empresa);

            addLineToScoreboard(objective, mensaje, i - 100);
        }

        return scoreboard;
    }

    private String cambiarLongitudDelMensajeSiEsNecesario (String mensaje, Empresa empresa) {
        if (mensaje.length() > 40) {
            mensaje = ChatColor.GOLD + "- " + empresa.getNombre() + " (" + ChatColor.GREEN + formatea.format(empresa.getPixelcoins()) + " PC";
            if (mensaje.length() > 40) {
                mensaje = ChatColor.GOLD + "- " + empresa.getNombre();
            }
        }
        return mensaje;
    }

    private String calcularRentabilidadEmpresaYFormatear (Empresa empresa) {
        double rentabilidad = Funciones.rentabilidad(empresa.getIngresos(), empresa.getIngresos() - empresa.getGastos());

        if(rentabilidad < 0){
            return ChatColor.RED + "" + (int) rentabilidad + "%" + ChatColor.GOLD + " )";
        }else{
            return ChatColor.GREEN + "" + (int) rentabilidad + "%" + ChatColor.GOLD + " )";
        }
    }

    private List<Empresa> sortEmpresaByPixelcoins (List<Empresa> empresas) {
        empresas.sort((o1, o2) -> {
            if (o1.getPixelcoins() >= o2.getPixelcoins())
                return -1;
            else
                return 1;
        });

        return empresas;
    }
}
