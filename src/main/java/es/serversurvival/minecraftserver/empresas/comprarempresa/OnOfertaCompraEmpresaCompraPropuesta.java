package es.serversurvival.minecraftserver.empresas.comprarempresa;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.serversurvival.pixelcoins.empresas._shared.accionistas.applicaion.AccionistasEmpresasService;
import es.serversurvival.pixelcoins.empresas._shared.empresas.application.EmpresasService;
import es.serversurvival.pixelcoins.empresas._shared.empresas.domain.Empresa;
import es.serversurvival.pixelcoins.empresas.adquirir.proponer.OfertaCompraEmpresaPropuesta;
import es.serversurvival.pixelcoins.jugadores._shared.jugadores.JugadoresService;
import es.serversurvival.pixelcoins.mensajes._shared.application.EnviadorMensajes;
import lombok.AllArgsConstructor;

import static es.serversurvival._shared.utils.Funciones.formatPixelcoins;
import static org.bukkit.ChatColor.AQUA;
import static org.bukkit.ChatColor.GOLD;
import static org.bukkit.Sound.ENTITY_PLAYER_LEVELUP;

@EventHandler
@AllArgsConstructor
public class OnOfertaCompraEmpresaCompraPropuesta {
    private final AccionistasEmpresasService accionistasEmpresasService;
    private final EnviadorMensajes enviadorMensajes;
    private final JugadoresService jugadoresService;
    private final EmpresasService empresasService;

    public void on(OfertaCompraEmpresaPropuesta e) {
        Empresa empresaAComprar = empresasService.getById(e.getEmpresaAComprarId());

        enviadorMensajes.enviarMensajeYSonido(e.getJugadorId(), ENTITY_PLAYER_LEVELUP, GOLD + "Has propuesto comprar la empresa " + empresaAComprar.getNombre() +
                " por " + formatPixelcoins(e.getPrecioTotal()) + ". Ahora el resto de accionistas tendran que votar a favor. " +
                "Para ver las votaciones " + AQUA + "/empresas votaciones " + empresaAComprar.getNombre());

        accionistasEmpresasService.findByEmpresaId(e.getEmpresaAComprarId()).forEach(accionista -> {
            enviadorMensajes.enviarMensaje(accionista.getAccionisaJugadorId(), "En la empresa donde eres accionista "+empresaAComprar.getNombre() +
                    " " + getCompradorNombre(e) + " ha propuesto comprar todas las acciones a "+formatPixelcoins(e.getPrecioTotal() / empresaAComprar.getNTotalAcciones()) +
                    ". Un total de " + formatPixelcoins(e.getPrecioTotal()) + ". En total recibiras " + formatPixelcoins((e.getPrecioTotal() / empresaAComprar.getNTotalAcciones()) * accionista.getNAcciones()) +
                    ". Ahora tendras que votar "  + AQUA + "/empresas votaciones " + empresaAComprar.getNombre());
        });
    }

    private String getCompradorNombre(OfertaCompraEmpresaPropuesta e) {
        return switch (e.getTipoComprador()) {
            case JUGADOR -> jugadoresService.getNombreById(e.getCompradorId()) + " (jugador)";
            case EMPRESA -> empresasService.getById(e.getCompradorId()).getNombre() + "(empresa)";
        };
    }
}

