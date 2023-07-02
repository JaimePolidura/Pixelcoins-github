package es.serversurvival.minecraftserver.bolsa;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.serversurvival.pixelcoins.bolsa._shared.posiciones.domain.TipoBolsaApuesta;
import es.serversurvival.pixelcoins.bolsa.abrir.PosicionBolsaAbierta;
import es.serversurvival.pixelcoins.jugadores._shared.jugadores.JugadoresService;
import es.serversurvival.pixelcoins.mensajes._shared.application.EnviadorMensajes;
import lombok.AllArgsConstructor;

import java.util.UUID;

import static es.serversurvival._shared.utils.Funciones.*;
import static es.serversurvival.minecraftserver._shared.MinecraftUtils.*;
import static org.bukkit.ChatColor.*;
import static org.bukkit.Sound.ENTITY_PLAYER_LEVELUP;

@EventHandler
@AllArgsConstructor
public final class OnPosicionAbierta {
    private final EnviadorMensajes enviadorMensajes;
    private final JugadoresService jugadoresService;

    @EventListener
    public void on(PosicionBolsaAbierta evento) {
        UUID jugadorId = evento.getJugadorId();
        String nombre = jugadoresService.getNombreById(jugadorId);

        if(evento.isPremarket()){
            enviadorMensajes.enviarMensaje(jugadorId, GOLD + "La compra no se ha podida ejecutar por que el mercado esta cerrado, cuando abra se ejecutara "
                    + AQUA + "/bolsa premarket");
            return;
        }

        if(evento.getTipoApuesta() == TipoBolsaApuesta.LARGO){
            broadcastExcept(jugadorId, GOLD + nombre + " ha comprado " + evento.getCantidad() + " " + evento.getActivoBolsa().getTipoActivo().getNombreUnidadPlural() + " de "
                    + evento.getActivoBolsa().getNombreLargo() + " a " + formatPixelcoins(evento.getPrecioPorUnidad()));

            enviadorMensajes.enviarMensajeYSonido(jugadorId, ENTITY_PLAYER_LEVELUP, GOLD + "Has comprado " + formatNumero(evento.getCantidad())
                    + " cantidad a " + formatPixelcoins(evento.getPrecioPorUnidad()) + " que es un total de " +
                    formatPixelcoins(- evento.getCosteTotal()) + "Comandos: " +  AQUA + "/bolsa cartera");
        }
        if(evento.getTipoApuesta() == TipoBolsaApuesta.CORTO){
            broadcastExcept(jugadorId, GOLD + nombre + " has vendido en corto " + evento.getCantidad() + " " + evento.getActivoBolsa().getTipoActivo().getNombreUnidadPlural() + " de "
                    + evento.getActivoBolsa().getNombreLargo() + " a " + formatPixelcoins(evento.getPrecioPorUnidad()));

            enviadorMensajes.enviarMensajeYSonido(jugadorId, ENTITY_PLAYER_LEVELUP,GOLD + "Has vendido en corto " + formatNumero(evento.getCantidad())
                    + " cantidad a " + formatPixelcoins(evento.getPrecioPorUnidad()) + "que es un total de " + formatPixelcoins(- evento.getCosteTotal()) + GOLD + "Comandos: " + AQUA + "/bolsa cartera");
        }

    }
}
