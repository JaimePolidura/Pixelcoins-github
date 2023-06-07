package es.serversurvival.minecraftserver.bolsa.vercartera;

import es.bukkitbettermenus.MenuService;
import es.bukkitbettermenus.menustate.BeforeShow;
import es.dependencyinjector.dependencies.DependenciesRepository;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.minecraftserver._shared.menus.NumberSelectorMenu;
import es.serversurvival.pixelcoins.bolsa._shared.activos.dominio.TipoApuestaService;
import es.serversurvival.pixelcoins.bolsa.cerrar.CerrarPosicionParametros;
import es.serversurvival.pixelcoins.bolsa.cerrar.CerrarPosicionUseCase;
import es.serversurvival.pixelcoins.bolsa._shared.posiciones.Posicion;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

import static org.bukkit.ChatColor.*;

@AllArgsConstructor
public final class BolsaCerrarPosicionMenu extends NumberSelectorMenu<Posicion> implements BeforeShow {
    private final DependenciesRepository dependenciesRepository;
    private final CerrarPosicionUseCase cerrarPosicionUseCase;
    private final MenuService menuService;

    private double valorTotalPorAccion;
    private double resultadoPorAccion;

    @Override
    public void onAccept(Player player, double cantidad, InventoryClickEvent event) {
        cerrarPosicionUseCase.cerrar(CerrarPosicionParametros.builder()
                .posicionAbiertaId(getState().getPosicionId())
                .cantidad((int) cantidad)
                .jugadorId(player.getUniqueId())
                .build());
    }

    @Override
    public void onCancel(Player player, InventoryClickEvent event) {
        this.menuService.open(player, VerBolsaCarteraMenu.class);
    }

    @Override
    public double maxValue() {
        return getState().getCantidad();
    }

    @Override
    public double initialValue() {
        return getState().getCantidad();
    }

    @Override
    public List<String> loreItemAceptar(double cantidad) {
        return List.of(
                GOLD + "Cantidad: " + Funciones.FORMATEA.format(cantidad),
                GOLD + "Valor total: " + GREEN + Funciones.FORMATEA.format(Funciones.redondeoDecimales(valorTotalPorAccion * cantidad, 2)) + " PC",
                resultadoPorAccion >= 0?
                        GOLD + "Resultado: " + GREEN + "+" + Funciones.FORMATEA.format(Funciones.redondeoDecimales(resultadoPorAccion * cantidad, 2)) + "PC" :
                        GOLD + "Resultado: " + RED + Funciones.FORMATEA.format(Funciones.redondeoDecimales(resultadoPorAccion * cantidad, 2)) + "PC"
        );
    }

    @Override
    public void beforeShow(Player player) {
        TipoApuestaService tipoApuestaService = dependenciesRepository.get(getState().getTipoApuesta().getTipoApuestaService());

        this.valorTotalPorAccion = tipoApuestaService.getPixelcoinsCerrarPosicion(getState().getPosicionId(), 1);
        this.resultadoPorAccion = tipoApuestaService.calcularBeneficiosOPerdidas(getState().getPrecioApertura(), getState().getPrecioCierre(), 1);
    }
}
