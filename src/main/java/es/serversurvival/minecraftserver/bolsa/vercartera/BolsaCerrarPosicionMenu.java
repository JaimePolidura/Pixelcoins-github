package es.serversurvival.minecraftserver.bolsa.vercartera;

import es.bukkitbettermenus.MenuService;
import es.bukkitbettermenus.menustate.BeforeShow;
import es.dependencyinjector.dependencies.DependenciesRepository;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.minecraftserver._shared.menus.NumberSelectorMenu;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseBus;
import es.serversurvival.pixelcoins.bolsa._shared.activos.aplicacion.ActivoBolsaUltimosPreciosService;
import es.serversurvival.pixelcoins.bolsa._shared.activos.dominio.TipoApuestaService;
import es.serversurvival.pixelcoins.bolsa.cerrar.CerrarPosicionParametros;
import es.serversurvival.pixelcoins.bolsa._shared.posiciones.domain.Posicion;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;
import java.util.concurrent.Executor;

import static es.serversurvival._shared.utils.Funciones.*;
import static es.serversurvival._shared.utils.Funciones.formatNumero;
import static org.bukkit.ChatColor.*;

@RequiredArgsConstructor
public final class BolsaCerrarPosicionMenu extends NumberSelectorMenu<Posicion> implements BeforeShow {
    private final ActivoBolsaUltimosPreciosService activoBolsaUltimosPreciosService;
    private final DependenciesRepository dependenciesRepository;
    private final MenuService menuService;
    private final UseCaseBus useCaseBus;
    private final Executor executor;

    private double valorTotalPorAccion;
    private double resultadoPorAccion;
    private double precioPorAccionActual;
    private double rentabilidad;

    @Override
    public void onAccept(Player player, double cantidad, InventoryClickEvent event) {
        useCaseBus.handle(CerrarPosicionParametros.builder()
                .posicionAbiertaId(getState().getPosicionId())
                .cantidad((int) cantidad)
                .jugadorId(player.getUniqueId())
                .build());
    }

    @Override
    protected String titulo() {
        return DARK_RED + "" + BOLD  + "   SELECCIONA CANTIDAD";
    }

    @Override
    public void onCancel(Player player, InventoryClickEvent event) {
        this.menuService.open(player, MiCarteraBolsaMenu.class);
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
                GOLD + "Cantidad: " + formatNumero(cantidad),
                GOLD + "Precio apertura: " + formatPixelcoins(getState().getPrecioApertura()),
                GOLD + "Precio actual: " + formatPixelcoins(precioPorAccionActual),
                GOLD + "Valor total: " + formatPixelcoins(valorTotalPorAccion * cantidad),
                GOLD + "Resultado: " + formatPixelcoins(resultadoPorAccion),
                GOLD + "Rentabilidad: " + formatRentabilidad(rentabilidad)
        );
    }

    @Override
    public void beforeShow(Player player) {
        TipoApuestaService tipoApuestaService = dependenciesRepository.get(getState().getTipoApuesta().getTipoApuestaService());

        this.precioPorAccionActual = activoBolsaUltimosPreciosService.getUltimoPrecio(getState().getActivoBolsaId(), null);
        this.valorTotalPorAccion = tipoApuestaService.getPixelcoinsCerrarPosicion(getState().getPosicionId(), player.getUniqueId(), 1);
        this.resultadoPorAccion = tipoApuestaService.calcularBeneficiosOPerdidas(getState().getPrecioApertura(), precioPorAccionActual, 1);
        this.rentabilidad = tipoApuestaService.calcularRentabilidad(getState().getPrecioApertura(), precioPorAccionActual);
    }
}
