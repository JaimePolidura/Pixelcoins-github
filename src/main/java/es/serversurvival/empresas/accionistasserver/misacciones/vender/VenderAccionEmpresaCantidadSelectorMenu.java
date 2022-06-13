package es.serversurvival.empresas.accionistasserver.misacciones.vender;

import es.jaimetruman.menus.MenuService;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival._shared.menus.NumberSelectorMenu;
import es.serversurvival.empresas.accionistasserver._shared.domain.AccionistaServer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;
import java.util.UUID;

import static org.bukkit.ChatColor.*;

public final class VenderAccionEmpresaCantidadSelectorMenu extends NumberSelectorMenu {
    private final int maxCantidadThatCanBeSold;
    private final UUID accionistaId;
    private final String empresaNombreVender;
    private final MenuService menuService;

    public VenderAccionEmpresaCantidadSelectorMenu(int maxCantidad, AccionistaServer accionistaServer) {
        this.maxCantidadThatCanBeSold = maxCantidad;
        this.accionistaId = accionistaServer.getAccionistaServerId();
        this.empresaNombreVender = accionistaServer.getEmpresa();
        this.menuService = DependecyContainer.get(MenuService.class);
    }

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
        return this.maxCantidadThatCanBeSold;
    }

    @Override
    public double initialValue() {
        return this.maxCantidadThatCanBeSold;
    }

    @Override
    protected String titulo() {
        return DARK_RED + "" + BOLD + "  SELECCIONA CANTIDAD";
    }

    @Override
    public List<String> loreItemAceptar(double cantidad) {
        return List.of(
                GOLD + "Vender " + cantidad + " acciones de " + empresaNombreVender
        );
    }

    @Override
    public String itemAcceptTitle() {
        return GREEN + "" + BOLD + "Siguiente ->";
    }
}
