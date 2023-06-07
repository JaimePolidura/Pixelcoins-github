package es.serversurvival.minecraftserver.empresas.repatirdividendos;

import es.bukkitbettermenus.menustate.BeforeShow;
import es.serversurvival.minecraftserver._shared.menus.NumberSelectorMenu;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.v1.mensajes._shared.application.EnviadorMensajes;
import es.serversurvival.pixelcoins.empresas._shared.empresas.Empresa;
import es.serversurvival.pixelcoins.empresas.repartirdividendos.RepartirDividendosParametros;
import es.serversurvival.pixelcoins.empresas.repartirdividendos.RepartirDividendosUseCase;
import es.serversurvival.pixelcoins.transacciones.TransaccionesService;
import lombok.AllArgsConstructor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

import static org.bukkit.ChatColor.*;
import static org.bukkit.ChatColor.BOLD;

@AllArgsConstructor
public final class RepartirDividendosConfirmacionMenu extends NumberSelectorMenu<Empresa> implements BeforeShow {
    private final RepartirDividendosUseCase repartirDividendosUseCase;
    private final TransaccionesService transaccionesService;
    private final EnviadorMensajes enviadorMensajes;

    private double pixelcoinsEmpresa;

    @Override
    public void onAccept(Player player, double dividendoPorAccion, InventoryClickEvent event) {
        repartirDividendosUseCase.repartirDividendos(RepartirDividendosParametros.builder()
                .empresaId(getState().getEmpresaId())
                .jugadorId(player.getUniqueId())
                .dividendoPorAccion(dividendoPorAccion)
                .build());

        enviadorMensajes.enviarMensajeYSonido(player, GOLD + "Se han pagado todos los dividendos", Sound.ENTITY_PLAYER_LEVELUP);
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
                GOLD + "Dividendo/Accion: " + GREEN + Funciones.FORMATEA.format(cantidad),
                GOLD + "Total a pagar: " + GREEN + Funciones.FORMATEA.format(cantidad * this.getState().getNTotalAcciones()),
                GOLD + "Pixelcoins empresa: " + GREEN + Funciones.FORMATEA.format(pixelcoinsEmpresa)
        );
    }

    @Override
    public void beforeShow(Player player) {
        pixelcoinsEmpresa = transaccionesService.getBalancePixelcions(getState().getEmpresaId());
    }
}
