package es.serversurvival.v2.minecraftserver.deudas.vendermercado;

import es.serversurvival.v1._shared.menus.NumberSelectorMenu;
import es.serversurvival.v1._shared.utils.Funciones;
import es.serversurvival.v2.minecraftserver.deudas._shared.DeudaItemMercadoLore;
import es.serversurvival.v2.pixelcoins.deudas._shared.Deuda;
import es.serversurvival.v2.pixelcoins.deudas.ponerventa.PonerVentaDeudaParametros;
import es.serversurvival.v2.pixelcoins.deudas.ponerventa.PonerVentaDeudaUseCase;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

import static org.bukkit.ChatColor.*;

@RequiredArgsConstructor
public final class PonerVentaDeudaMercadoPrecioSelectorMenu extends NumberSelectorMenu<Deuda> {
    private final PonerVentaDeudaUseCase ponerVentaDeudaUseCase;
    private final DeudaItemMercadoLore deudaItemMercadoLore;

    @Override
    public double maxValue() {
        return Double.MAX_VALUE;
    }

    @Override
    public double initialValue() {
        return getState().getPixelcoinsRestantesDePagar();
    }

    @Override
    public String itemAcceptTitle(){
        return GREEN + "" + BOLD + "VENDER";
    }

    @Override
    public List<String> loreItemAceptar(double cantidad) {
        return deudaItemMercadoLore.build(getState());
    }

    @Override
    public void onAccept(Player player, double precio, InventoryClickEvent event) {
        ponerVentaDeudaUseCase.vender(PonerVentaDeudaParametros.builder()
                .deudaId(getState().getDeudaId())
                .precio(precio)
                .jugadorId(player.getUniqueId())
                .build());

        player.sendMessage(GOLD + "Has puesto la deuda en el mercado por " + Funciones.FORMATEA.format(precio) + " PC " +
                GOLD + "Para ver el mercado " + AQUA + " /deudas mercado");
    }
}
