package es.serversurvival.minecraftserver.empresas.comprarempresa;

import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.minecraftserver._shared.menus.MenuItems;
import es.serversurvival.minecraftserver._shared.menus.NumberSelectorMenu;
import es.serversurvival.pixelcoins.empresas._shared.empresas.domain.Empresa;
import es.serversurvival.pixelcoins.transacciones.application.MovimientosService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;
import java.util.Optional;

import static org.bukkit.ChatColor.BOLD;
import static org.bukkit.ChatColor.DARK_RED;

@AllArgsConstructor
public class SeleccionPrecioComprarEmpresaMenu extends NumberSelectorMenu<SeleccionPrecioComprarEmpresaMenu.Estado> {
    private final MovimientosService movimientosService;

    @Override
    public double maxValue() {
        return switch (getState().getTipoComprador()) {
            case EMPRESA -> movimientosService.getBalance(getState().getEmpresaCompradora().get().getEmpresaId());
            case JUGADOR -> movimientosService.getBalance(getPlayer().getUniqueId());
        };
    }

    @Override
    public double initialValue() {
        return 0;
    }

    @Override
    protected String titulo() {
        return DARK_RED + "" + BOLD  + "SELECCIONAR PRECIO";
    }

    @Override
    public String itemAcceptTitle() {
        return MenuItems.CLICKEABLE + "COMRPAR";
    }

    @Override
    public List<String> loreItemAceptar(double cantidad) {
        return List.of(
                ChatColor.GOLD + "Comprar " + getState().getEmpresaAComprar().getNombre() + " por " + Funciones.formatPixelcoins(cantidad)
        );
    }

    @Override
    public void onAccept(Player player, double cantidad, InventoryClickEvent event) {
        Empresa empresaAComprar = getState().getEmpresaAComprar();
        if (empresaAComprar.isEsCotizada()) {

        }
    }

    public static Estado tipoCompradorEmpresa(Empresa empresaAComprar, Empresa empresaCompradora) {
        return new Estado(empresaAComprar, TipoComprador.EMPRESA, Optional.of(empresaCompradora));
    }

    public static Estado tipoCompradorJugador(Empresa empresaAComprar) {
        return new Estado(empresaAComprar, TipoComprador.JUGADOR, Optional.empty());
    }

    @Getter
    @AllArgsConstructor
    public static class Estado {
        private final Empresa empresaAComprar;
        private final TipoComprador tipoComprador;
        private final Optional<Empresa> empresaCompradora;
    }

    public enum TipoComprador {
        EMPRESA,
        JUGADOR
    }
}
