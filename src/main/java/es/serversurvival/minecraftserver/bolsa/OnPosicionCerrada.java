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
                    + " de " + evento.getActivoBolsa().getNombreLargo() + " por " + GREEN + formatNumero(evento.getPrecioCierre()) + " PC / " + evento.getActivoBolsa().getTipoActivo().getNombreUnidadSingular().toLowerCase()
                    + GOLD + " Has obtenido " + GREEN + formatNumero(evento.getPixelcoinsTotales()) + " PC " + GOLD + "con una rentabilidad del " +
                    (evento.getRentabilidad() >= 0 ? "+" + GREEN + formatPorcentaje(evento.getRentabilidad()) : RED + formatPorcentaje(evento.getRentabilidad())) + "%", Sound.ENTITY_PLAYER_LEVELUP);

            broadcastExcept(player, GOLD + player.getName() + " ha obtenido una rentabilidad del " + (evento.getRentabilidad() >= 0 ? "+" + formatPorcentaje(evento.getRentabilidad()) :
                    formatPorcentaje(evento.getRentabilidad())) + "%" + GOLD + " vendiendo " + evento.getCantidad() + " " +
                    evento.getActivoBolsa().getTipoActivo().getNombreUnidadPlural() + " de " + evento.getActivoBolsa().getNombreLargo()
                    + " por " + GREEN + formatNumero(evento.getPrecioCierre()) + " PC / " + evento.getActivoBolsa().getTipoActivo().getNombreUnidadSingular().toLowerCase());
        }else{
            enviarMensajeYSonido(player, GOLD + "Has cerrado la posicion en corto de " + evento.getCantidad() + " " + evento.getActivoBolsa().getTipoActivo().getNombreUnidadPlural()
                    + " de " + evento.getActivoBolsa().getNombreLargo() + " por " + GREEN + formatNumero(evento.getPrecioCierre()) + " PC / " + evento.getActivoBolsa().getTipoActivo().getNombreUnidadSingular().toLowerCase()
                    + GOLD + " Has obtenido " + GREEN + formatNumero(evento.getPixelcoinsTotales()) + " PC " + GOLD + "con una rentabilidad de " +
                    (evento.getRentabilidad() >= 0 ? "+" + GREEN + formatPorcentaje(evento.getRentabilidad()) : RED + formatPorcentaje(evento.getRentabilidad())) + "%", Sound.ENTITY_PLAYER_LEVELUP);

            broadcastExcept(player, player.getName() + " ha obtenido rentabilidad del " + (evento.getRentabilidad() >= 0 ? "+" + formatPorcentaje(evento.getRentabilidad()) :
                    formatPorcentaje(evento.getRentabilidad())) + "%" + GOLD + " comprando en corto " + evento.getCantidad() + " " +
                    evento.getActivoBolsa().getTipoActivo().getNombreUnidadPlural() + " de " + evento.getActivoBolsa().getNombreLargo()
                    + " por " + GREEN + formatNumero(evento.getPrecioCierre()) + " PC / " + evento.getActivoBolsa().getTipoActivo().getNombreUnidadSingular().toLowerCase());
        }
    }
}
