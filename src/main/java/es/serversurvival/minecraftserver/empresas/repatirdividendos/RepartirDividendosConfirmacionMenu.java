package es.serversurvival.minecraftserver.empresas.repatirdividendos;

import es.bukkitbettermenus.menustate.BeforeShow;
import es.serversurvival.minecraftserver._shared.menus.NumberSelectorMenu;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseBus;
import es.serversurvival.pixelcoins.empresas._shared.empresas.domain.Empresa;
import es.serversurvival.pixelcoins.empresas.repartirdividendos.RepartirDividendosParametros;
import es.serversurvival.pixelcoins.transacciones.TransaccionesBalanceService;
import lombok.RequiredArgsConstructor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

import static es.serversurvival._shared.utils.Funciones.*;
import static es.serversurvival.minecraftserver._shared.MinecraftUtils.*;
import static org.bukkit.ChatColor.*;
import static org.bukkit.ChatColor.BOLD;

@RequiredArgsConstructor
public final class RepartirDividendosConfirmacionMenu extends NumberSelectorMenu<Empresa> implements BeforeShow {
    private final TransaccionesBalanceService transaccionesBalanceService;
    private final UseCaseBus useCaseBus;

    private double pixelcoinsEmpresa;

    @Override
    public void onAccept(Player player, double dividendoPorAccion, InventoryClickEvent event) {
        useCaseBus.handle(RepartirDividendosParametros.builder()
                .empresaId(getState().getEmpresaId())
                .jugadorId(player.getUniqueId())
                .dividendoPorAccion(dividendoPorAccion)
                .build());

        enviarMensajeYSonido(player, GOLD + "Se han pagado todos los dividendos", Sound.ENTITY_PLAYER_LEVELUP);
    }

    @Override
    protected String titulo() {
        return DARK_RED + "" + BOLD + "    REPARTIR DIVIDENDOS";
    }

    @Override
    public double maxValue() {
        return pixelcoinsEmpresa / this.getState().getNTotalAcciones();
    }

    @Override
    public double initialValue() {
        return 1;
    }

    @Override
    public List<String> loreItemAceptar(double cantidad) {
        return List.of(
                GOLD + "Dividendo/Accion: " + formatPixelcoins(cantidad),
                GOLD + "Total a pagar: " + formatPixelcoins(cantidad * this.getState().getNTotalAcciones()),
                GOLD + "Pixelcoins empresa: " + formatPixelcoins(pixelcoinsEmpresa)
        );
    }

    @Override
    public void beforeShow(Player player) {
        pixelcoinsEmpresa = transaccionesBalanceService.get(getState().getEmpresaId());
    }
}
