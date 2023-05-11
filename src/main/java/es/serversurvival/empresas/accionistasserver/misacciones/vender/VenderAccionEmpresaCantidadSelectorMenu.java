package es.serversurvival.empresas.accionistasserver.misacciones.vender;

import es.bukkitbettermenus.MenuService;
import es.serversurvival._shared.menus.NumberSelectorMenu;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

import static org.bukkit.ChatColor.*;

@AllArgsConstructor
public final class VenderAccionEmpresaCantidadSelectorMenu extends NumberSelectorMenu<VenderAccionesEmpresaCantidadSelectorMenuState> {
    private final MenuService menuService;

    @Override
    public void onAccept(Player player, InventoryClickEvent event) {
        int cantidadAccionesAVender = (int) super.getPropertyDouble("cantidad");

        this.menuService.open(player, new VenderAccionEmpresaPrecioSelectorMenu(
                cantidadAccionesAVender, this.accionistaId)
        );
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
