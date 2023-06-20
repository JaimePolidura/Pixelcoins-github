package es.serversurvival.minecraftserver.jugadores.cambio.ingresaritem;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.minecraftserver._shared.MinecraftUtils;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseBus;
import es.serversurvival.pixelcoins.jugadores.cambiar.TipoCambioPixelcoins;
import es.serversurvival.pixelcoins.jugadores.cambiar.ingresarItem.IngresarItemParametros;
import es.serversurvival.pixelcoins.transacciones.TransaccionesService;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

import static es.serversurvival._shared.utils.Funciones.FORMATEA;
import static org.bukkit.ChatColor.GOLD;
import static org.bukkit.ChatColor.GREEN;

@Service
@RequiredArgsConstructor
public final class IngresadorItem {
    private final TransaccionesService transaccionesService;
    private final UseCaseBus useCaseBus;

    public void ingresarItemInMano(Player player, TipoCambioPixelcoins... tipoCambioPixelcoinsPermitidos) {
        ItemStack itemEnMano = player.getInventory().getItemInMainHand();

        if(itemEnMano == null || !elTipoDeCambioDelItemEnLaManoEstaPermitido(itemEnMano.getType(), tipoCambioPixelcoinsPermitidos)){
            player.sendMessage(ChatColor.DARK_RED + "Debes de tener un bloque en la mano para intecambiarlo con pixelcoins");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
            return;
        }

        TipoCambioPixelcoins tipoCambio = TipoCambioPixelcoins.valueOf(itemEnMano.getType().name());

        useCaseBus.handle(IngresarItemParametros.of(player.getUniqueId(), tipoCambio, itemEnMano.getAmount()));

        player.getInventory().clear(player.getInventory().getHeldItemSlot());

        enviarMensaje(player, itemEnMano, tipoCambio);
    }

    private void enviarMensaje(Player player, ItemStack itemEnMano, TipoCambioPixelcoins tipoCambio) {
        double pixelcoinsAnadidas = tipoCambio.cambio * itemEnMano.getAmount();
        double pixelcoinsJugador = transaccionesService.getBalancePixelcoins(player.getUniqueId());
        MinecraftUtils.enviarMensajeYSonido(player, GOLD + "Se ha añadido: " + GREEN + FORMATEA.format(pixelcoinsAnadidas)
                + GOLD + " Tienes " + FORMATEA.format(pixelcoinsJugador) + " PC", Sound.ENTITY_PLAYER_LEVELUP);
    }

    private boolean elTipoDeCambioDelItemEnLaManoEstaPermitido(Material materialItemEnLaMano, TipoCambioPixelcoins[] tipoCambioPixelcoinsPermitidos) {
        return Arrays.stream(tipoCambioPixelcoinsPermitidos)
                .anyMatch(it -> it.name().equalsIgnoreCase(materialItemEnLaMano.name()));
    }
}
