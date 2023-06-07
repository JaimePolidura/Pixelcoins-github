package es.serversurvival.minecraftserver.jugadores.cambio.ingresaritem;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.minecraftserver._shared.MinecraftUtils;
import es.serversurvival.pixelcoins.jugadores.cambiar.TipoCambioPixelcoins;
import es.serversurvival.pixelcoins.jugadores.cambiar.ingresarItem.IngresarItemParametros;
import es.serversurvival.pixelcoins.jugadores.cambiar.ingresarItem.IngresarItemUseCase;
import es.serversurvival.pixelcoins.transacciones.TransaccionesService;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

import static es.serversurvival._shared.utils.Funciones.FORMATEA;
import static es.serversurvival.minecraftserver._shared.MinecraftUtils.enviarMensajeYSonido;
import static org.bukkit.ChatColor.GOLD;
import static org.bukkit.ChatColor.GREEN;

@Service
@RequiredArgsConstructor
public final class IngresadorItem {
    private final TransaccionesService transaccionesService;
    private final IngresarItemUseCase ingresarItemUseCase;

    public void ingresarItemInMano(Player player, TipoCambioPixelcoins... tipoCambioPixelcoinsPermitidos) {
        ItemStack itemEnMano = player.getInventory().getItemInMainHand();
        TipoCambioPixelcoins tipoCambio = TipoCambioPixelcoins.valueOf(itemEnMano.getType().name());

        if(itemEnMano == null || !elTipoDeCambioDelItemEnLaManoEstaPermitido(tipoCambio, tipoCambioPixelcoinsPermitidos)){
            player.sendMessage(ChatColor.DARK_RED + "Debes de tener un bloque en la mano para intecambiarlo con pixelcoins");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
            return;
        }

        ingresarItemUseCase.ingresarItem(IngresarItemParametros.of(player.getUniqueId(), tipoCambio, itemEnMano.getAmount()));

        player.getInventory().clear(player.getInventory().getHeldItemSlot());

        enviarMensaje(player, itemEnMano, tipoCambio);
    }

    private void enviarMensaje(Player player, ItemStack itemEnMano, TipoCambioPixelcoins tipoCambio) {
        double pixelcoinsAnadidas = tipoCambio.cambio * itemEnMano.getAmount();
        double pixelcoinsJugador = transaccionesService.getBalancePixelcions(player.getUniqueId());
        MinecraftUtils.enviarMensajeYSonido(player, GOLD + "Se ha añadido: " + GREEN + FORMATEA.format(pixelcoinsAnadidas)
                + GOLD + " Tienes " + FORMATEA.format(pixelcoinsJugador) + " PC", Sound.ENTITY_PLAYER_LEVELUP);
    }

    private boolean elTipoDeCambioDelItemEnLaManoEstaPermitido(TipoCambioPixelcoins tipoCambioItemEnMano, TipoCambioPixelcoins[] tipoCambioPixelcoinsPermitidos) {
        return Arrays.stream(tipoCambioPixelcoinsPermitidos)
                .anyMatch(it -> it == tipoCambioItemEnMano);
    }
}
