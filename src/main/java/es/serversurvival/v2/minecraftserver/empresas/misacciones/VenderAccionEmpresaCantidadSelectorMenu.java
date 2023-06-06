package es.serversurvival.v2.minecraftserver.empresas.misacciones;

import es.bukkitbettermenus.MenuService;
import es.serversurvival.v1._shared.menus.NumberSelectorMenu;
import es.serversurvival.v2.pixelcoins.empresas._shared.accionistas.AccionistaEmpresa;
import es.serversurvival.v2.pixelcoins.empresas._shared.accionistas.AccionistasEmpresasService;
import es.serversurvival.v2.pixelcoins.empresas._shared.empresas.Empresa;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;
import java.util.UUID;

import static org.bukkit.ChatColor.*;

@AllArgsConstructor
public final class VenderAccionEmpresaCantidadSelectorMenu extends NumberSelectorMenu<VenderAccionEmpresaCantidadSelectorMenu.VenderAccionesEmpresaCantidadSelectorMenuState> {
    private final AccionistasEmpresasService accionistasEmpresasService;
    private final MenuService menuService;

    @Override
    public void onAccept(Player player, double cantidadAccionesAVender, InventoryClickEvent event) {
        AccionistaEmpresa accionAVender = accionistasEmpresasService.getById(getState().accionistaId());

        this.menuService.open(player, VenderAccionEmpresaPrecioSelectorMenu.class,
                VenderAccionEmpresaPrecioSelectorMenu.of((int) cantidadAccionesAVender, accionAVender, getState().empresa));
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
                GOLD + "Vender " + cantidad + " cantidad de " + getState().empresa.getNombre()
        );
    }

    @Override
    public String itemAcceptTitle() {
        return GREEN + "" + BOLD + "Siguiente ->";
    }

    public static VenderAccionesEmpresaCantidadSelectorMenuState of(int maxCantidadThatCanBeSold, UUID accionistaId, Empresa empresa){
        return new VenderAccionesEmpresaCantidadSelectorMenuState(maxCantidadThatCanBeSold, accionistaId, empresa);
    }

    public record VenderAccionesEmpresaCantidadSelectorMenuState(int maxCantidadThatCanBeSold, UUID accionistaId, Empresa empresa) { }
}
