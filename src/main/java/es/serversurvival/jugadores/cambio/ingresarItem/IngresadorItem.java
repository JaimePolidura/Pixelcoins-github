package es.serversurvival.jugadores.cambio.ingresarItem;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.jugadores.cambio.TipoCambioPixelcoins;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public final class IngresadorItem {
    private final IngresarItemUseCase ingresarItemUseCase;

    public void ingresarItemInMano(Player player, TipoCambioPixelcoins... tipoCambioPixelcoinsPermitidos) {
        ItemStack itemEnMano = player.getInventory().getItemInMainHand();
        TipoCambioPixelcoins tipoCambioItemEnMano = TipoCambioPixelcoins.valueOf(itemEnMano.getType().name());

        if(itemEnMano == null || !elTipoDeCambioDelItemEnLaManoEstaPermitido(tipoCambioItemEnMano, tipoCambioPixelcoinsPermitidos)){
            player.sendMessage(ChatColor.DARK_RED + "Debes de tener un bloque de cuarzo en la mano para intecambiarlo con pixelcoins");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
            return;
        }

        ingresarItemUseCase.ingresarItem(player.getName(), TipoCambioPixelcoins.valueOf(itemEnMano.toString()), itemEnMano.getAmount());
        player.getInventory().clear(player.getInventory().getHeldItemSlot());
    }

    private boolean elTipoDeCambioDelItemEnLaManoEstaPermitido(TipoCambioPixelcoins tipoCambioItemEnMano, TipoCambioPixelcoins[] tipoCambioPixelcoinsPermitidos) {
        return Arrays.stream(tipoCambioPixelcoinsPermitidos)
                .anyMatch(it -> it == tipoCambioItemEnMano);
    }
}
