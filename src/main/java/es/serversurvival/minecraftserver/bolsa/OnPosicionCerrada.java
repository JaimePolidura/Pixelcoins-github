package es.serversurvival.minecraftserver.bolsa;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.serversurvival.pixelcoins.bolsa._shared.posiciones.domain.TipoBolsaApuesta;
import es.serversurvival.pixelcoins.bolsa.cerrar.PosicionBolsaCerrada;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import static es.serversurvival._shared.utils.Funciones.*;
import static es.serversurvival.minecraftserver._shared.MinecraftUtils.*;
import static org.bukkit.ChatColor.*;

@EventHandler
public final class OnPosicionCerrada {
    @EventListener
    public void on(PosicionBolsaCerrada evento) {
        Player player = Bukkit.getPlayer(evento.getJugadorId());
        if(player == null){
            return;
        }

        if(evento.isPremarket()){
            player.sendMessage(GOLD + "La compra no se ha podida ejecutar por que el mercado esta cerrado, cuando abra se ejecutara "
                    + AQUA + "/bolsa premarket");
            return;
        }

        if(evento.getTipoApuesta() == TipoBolsaApuesta.LARGO){
            enviarMensajeYSonido(player, GOLD + "Has vendido " + evento.getCantidad() + " " + evento.getActivoBolsa().getTipoActivo().getNombreUnidadPlural()
                    + " de " + evento.getActivoBolsa().getNombreLargo() + " por " + formatPixelcoins(evento.getPrecioCierre()) + "/ " + evento.getActivoBolsa().getTipoActivo().getNombreUnidadSingular().toLowerCase()
                    + GOLD + "Has obtenido " + formatPixelcoins(evento.getPixelcoinsTotales()) + "con una rentabilidad del " + formatRentabilidad(evento.getRentabilidad()), Sound.ENTITY_PLAYER_LEVELUP);

            broadcastExcept(player, GOLD + player.getName() + " ha obtenido una rentabilidad del " + formatRentabilidad(evento.getRentabilidad()) + GOLD + " vendiendo " + evento.getCantidad() + " " +
                    evento.getActivoBolsa().getTipoActivo().getNombreUnidadPlural() + " de " + evento.getActivoBolsa().getNombreLargo()
                    + " por " + formatPixelcoins(evento.getPrecioCierre())  + "/ " + evento.getActivoBolsa().getTipoActivo().getNombreUnidadSingular().toLowerCase());
        }else{
            enviarMensajeYSonido(player, GOLD + "Has cerrado la posicion en corto de " + evento.getCantidad() + " " + evento.getActivoBolsa().getTipoActivo().getNombreUnidadPlural()
                    + " de " + evento.getActivoBolsa().getNombreLargo() + " por " + formatPixelcoins(evento.getPrecioCierre()) + "/ " + evento.getActivoBolsa().getTipoActivo().getNombreUnidadSingular().toLowerCase()
                    + GOLD + " Has obtenido " + formatRentabilidad(evento.getPixelcoinsTotales()) + GOLD + "con una rentabilidad de " +
                    formatRentabilidad(evento.getRentabilidad()), Sound.ENTITY_PLAYER_LEVELUP);

            broadcastExcept(player, player.getName() + " ha obtenido rentabilidad del " + formatRentabilidad(evento.getRentabilidad()) + GOLD + "comprando en corto " + evento.getCantidad() + " " +
                    evento.getActivoBolsa().getTipoActivo().getNombreUnidadPlural() + " de " + evento.getActivoBolsa().getNombreLargo()
                    + " por " + formatPixelcoins(evento.getPrecioCierre()) + "/ " + evento.getActivoBolsa().getTipoActivo().getNombreUnidadSingular().toLowerCase());
        }
    }
}
