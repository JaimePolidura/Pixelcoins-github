package es.serversurvival.v1.empresas.accionistasserver.misacciones.vender;

import es.bukkitbettermenus.MenuService;
import es.serversurvival.v1._shared.menus.NumberSelectorMenu;
import es.serversurvival.v1.empresas.accionistasserver._shared.application.AccionistasServerService;
import es.serversurvival.v1.empresas.accionistasserver._shared.domain.AccionistaServer;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

import static org.bukkit.ChatColor.*;

@AllArgsConstructor
public final class VenderAccionEmpresaCantidadSelectorMenu extends NumberSelectorMenu<VenderAccionesEmpresaCantidadSelectorMenuState> {
    private final AccionistasServerService accionistasServerService;
    private final MenuService menuService;

    @Override
    public void onAccept(Player player, double cantidadAccionesAVender, InventoryClickEvent event) {
        AccionistaServer accionAVender = accionistasServerService.getById(getState().accionistaId());

        this.menuService.open(player, VenderAccionEmpresaPrecioSelectorMenu.class, VenderAccionEmpresaPrecioSelectorMenuState.from((int) cantidadAccionesAVender, accionAVender));
    }

    //By this we can open another menu when item aceptar is clicked
    @Override
    public boolean closeOnAction() {
        return false;
    }

    @Override
    public double maxValue() {
        return this.getState().maxCantidadThatCanBeSold();
    }

    @Override
    public double initialValue() {
        return this.getState().maxCantidadThatCanBeSold();
    }

    @Override
    protected String titulo() {
        return DARK_RED + "" + BOLD + "  SELECCIONA CANTIDAD";
    }

    @Override
    public List<String> loreItemAceptar(double cantidad) {
        return List.of(
                GOLD + "Vender " + cantidad + " cantidad de " + getState().empresaNombreVender()
        );
    }

    @Override
    public String itemAcceptTitle() {
        return GREEN + "" + BOLD + "Siguiente ->";
    }
}
