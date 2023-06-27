package es.serversurvival.minecraftserver.deudas.vendermercado;

import es.bukkitbettermenus.menustate.AfterShow;
import es.serversurvival.minecraftserver._shared.menus.NumberSelectorMenu;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.minecraftserver.deudas._shared.DeudaItemLore;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseBus;
import es.serversurvival.pixelcoins.deudas._shared.domain.Deuda;
import es.serversurvival.pixelcoins.deudas.ponerventa.PonerVentaDeudaParametros;
import es.serversurvival.pixelcoins.jugadores._shared.jugadores.JugadoresService;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

import static org.bukkit.ChatColor.*;

@RequiredArgsConstructor
public final class PonerVentaDeudaMercadoPrecioSelectorMenu extends NumberSelectorMenu<Deuda> implements AfterShow {
    private final JugadoresService jugadoresService;
    private final DeudaItemLore deudaItemMercadoLore;
    private final UseCaseBus useCaseBus;

    private String deudorJugadorNombre;

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
    public List<String> loreItemAceptar(double precio) {
        return deudaItemMercadoLore.buildOfertaDeudaMercado(
                precio,
                getPlayer().getName(),
                getState().getInteres(),
                getState().getNominal(),
                getState().getPeriodoPagoCuotaMs(),
                getState().getCuotasRestantes(),
                getState().getNCuotasImpagadas(),
                getState().getPixelcoinsRestantesDePagar(),
                false,
                deudorJugadorNombre
        );
    }

    @Override
    public void onAccept(Player player, double precio, InventoryClickEvent event) {
        useCaseBus.handle(PonerVentaDeudaParametros.builder()
                .deudaId(getState().getDeudaId())
                .precio(precio)
                .jugadorId(player.getUniqueId())
                .build());

        player.sendMessage(GOLD + "Has puesto la deuda en el mercado por " + Funciones.FORMATEA.format(precio) + " PC " +
                GOLD + "Para ver el mercado " + AQUA + " /deudas mercado");
    }

    @Override
    public void afterShow(Player player) {
        this.deudorJugadorNombre = jugadoresService.getNombreById(getState().getDeudorJugadorId());
    }
}
