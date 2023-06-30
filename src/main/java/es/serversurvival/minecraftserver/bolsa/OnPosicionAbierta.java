package es.serversurvival.minecraftserver.bolsa;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.pixelcoins.bolsa._shared.posiciones.domain.TipoBolsaApuesta;
import es.serversurvival.pixelcoins.bolsa.abrir.PosicionBolsaAbierta;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static es.serversurvival._shared.utils.Funciones.*;
import static es.serversurvival.minecraftserver._shared.MinecraftUtils.*;
import static org.bukkit.ChatColor.*;
import static org.bukkit.Sound.ENTITY_PLAYER_LEVELUP;

@EventHandler
public final class OnPosicionAbierta {
    @EventListener
    public void on(PosicionBolsaAbierta evento) {
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
            broadcastExcept(player, GOLD + player.getName() + " ha comprado " + evento.getCantidad() + " " + evento.getActivoBolsa().getTipoActivo().getNombreUnidadPlural() + " de "
                    + evento.getActivoBolsa().getNombreLargo() + " a " + formatPixelcoins(evento.getPrecioPorUnidad()));

            enviarMensajeYSonido(player, GOLD + "Has comprado " + formatNumero(evento.getCantidad())
                    + " cantidad a " + formatPixelcoins(evento.getPrecioPorUnidad()) + " que es un total de " +
                    formatPixelcoins(- evento.getCosteTotal()) + "Comandos: " +  AQUA + "/bolsa cartera", ENTITY_PLAYER_LEVELUP);
        }
        if(evento.getTipoApuesta() == TipoBolsaApuesta.CORTO){
            broadcastExcept(player, GOLD + player.getName() + " has vendido en corto " + evento.getCantidad() + " " + evento.getActivoBolsa().getTipoActivo().getNombreUnidadPlural() + " de "
                    + evento.getActivoBolsa().getNombreLargo() + " a " + formatPixelcoins(evento.getPrecioPorUnidad()));

            enviarMensajeYSonido(player, GOLD + "Has vendido en corto " + formatNumero(evento.getCantidad())
                    + " cantidad a " + formatPixelcoins(evento.getPrecioPorUnidad()) + "que es un total de " + formatPixelcoins(- evento.getCosteTotal()) + GOLD + "Comandos: " + AQUA + "/bolsa cartera", ENTITY_PLAYER_LEVELUP);
        }

    }
}
