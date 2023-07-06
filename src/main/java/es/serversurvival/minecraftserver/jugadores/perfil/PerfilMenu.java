package es.serversurvival.minecraftserver.jugadores.perfil;

import es.bukkitbettermenus.Menu;
import es.bukkitbettermenus.MenuService;
import es.bukkitbettermenus.configuration.MenuConfiguration;
import es.bukkitbettermenus.menustate.AfterShow;
import es.bukkitclassmapper._shared.utils.ItemBuilder;
import es.dependencyinjector.dependencies.DependenciesRepository;
import es.serversurvival.Pixelcoin;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.minecraftserver.bolsa.vercartera.MiCarteraBolsaMenu;
import es.serversurvival.minecraftserver.deudas.verdeudas.MisDeudasMenu;
import es.serversurvival.minecraftserver.empresas.misempleos.MisEmpleosMenu;
import es.serversurvival.minecraftserver.empresas.vertodas.VerTodasEmpresasMenu;
import es.serversurvival.minecraftserver.tienda.vertienda.TiendaMenu;
import es.serversurvival.pixelcoins.bolsa._shared.activos.aplicacion.ActivosBolsaService;
import es.serversurvival.pixelcoins.jugadores.patrimonio.CalculadorPatrimonioService;
import es.serversurvival.pixelcoins.jugadores.patrimonio.TipoCuentaPatrimonio;
import es.serversurvival.minecraftserver.jugadores.top.TopMenu;
import es.serversurvival.pixelcoins.bolsa._shared.posiciones.domain.Posicion;
import es.serversurvival.pixelcoins.bolsa._shared.posiciones.application.PosicionesService;
import es.serversurvival.pixelcoins.empresas._shared.accionistas.domain.AccionistaEmpresa;
import es.serversurvival.pixelcoins.empresas._shared.accionistas.applicaion.AccionistasEmpresasService;
import es.serversurvival.pixelcoins.empresas._shared.empleados.domain.Empleado;
import es.serversurvival.pixelcoins.empresas._shared.empleados.application.EmpleadosService;
import es.serversurvival.pixelcoins.empresas._shared.empresas.domain.Empresa;
import es.serversurvival.pixelcoins.empresas._shared.empresas.application.EmpresasService;
import es.serversurvival.pixelcoins.transacciones.application.MovimientosService;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;
import java.util.concurrent.Executor;

import static es.serversurvival._shared.utils.Funciones.*;
import static es.serversurvival._shared.utils.Funciones.formatPixelcoins;
import static es.serversurvival.minecraftserver._shared.menus.MenuItems.*;
import static org.bukkit.ChatColor.*;

@AllArgsConstructor
public final class PerfilMenu extends Menu implements AfterShow {
    private final CalculadorPatrimonioService calculadorPatrimonioService;
    private final AccionistasEmpresasService accionistasEmpresasService;
    private final DependenciesRepository dependenciesRepository;
    private final ActivosBolsaService activosBolsaService;
    private final MovimientosService movimientosService;
    private final PosicionesService posicionesService;
    private final EmpleadosService empleadosService;
    private final EmpresasService empresasService;
    private final MenuService menuService;
    private final Executor executor;

    @Override
    public int[][] items() {
        return new int[][] {
                {1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 3, 0, 0, 0, 0, 0, 4, 1},
                {1, 0, 0, 0, 6, 0, 0, 0, 1},
                {1, 0, 5, 0, 0, 0, 7, 0, 1},
                {1, 0, 0, 0, 8, 0, 0, 0, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1}
        };
    }

    @Override
    public MenuConfiguration configuration() {
        return MenuConfiguration.builder()
                .fixedItems()
                .title(DARK_RED + "" + BOLD + "           TU PERFIL")
                .item(1, Material.BLACK_STAINED_GLASS_PANE)
                .item(3, buildItemStats(), (p, e) -> this.menuService.open(p, TopMenu.class))
                .item(4, buildItemTienda(), (p, e) -> this.menuService.open(p, TiendaMenu.class))
                .item(5, buildItemDeudas(), (p, e) -> this.menuService.open(p, MisDeudasMenu.class))
                .item(6, buildItemBolsa(), (p, e) -> this.menuService.open(p, MiCarteraBolsaMenu.class))
                .item(7, buildItemTodasEmpresas(), (p, e) -> this.menuService.open(p, VerTodasEmpresasMenu.class))
                .item(8, buildItemEmpleos(), (p, e) -> this.menuService.open(p, MisEmpleosMenu.class))
                .build();
    }

    private ItemStack buildItemEmpleos () {
        String displayName = CLICKEABLE + "VER TUS EMPLEOS";

        List<String> lore = new ArrayList<>();
        lore.add("  ");
        List<Empleado> empleos = empleadosService.findByEmpleadoJugadorId(getPlayer().getUniqueId());
        empleos.forEach( (emp) -> {
            Empresa empresa = empresasService.getById(emp.getEmpresaId());

            lore.add(GOLD + "" + empresa.getNombre() + " " + Funciones.formatPixelcoins(emp.getSueldo()) +
                    GOLD + "/ " + emp.periodoSueldoToDias() + " dias");
        });

        return ItemBuilder.of(Material.GOLDEN_APPLE).title(displayName).lore(lore).build();
    }

    private ItemStack buildItemTodasEmpresas() {
        String displayName = CLICKEABLE + "VER TODAS LAS EMPRESAS";
        List<String> lore = new ArrayList<>();
        lore.add("  ");

        List<AccionistaEmpresa> acciones = accionistasEmpresasService.findByJugadorId(getPlayer().getUniqueId());
        for (AccionistaEmpresa accion : acciones) {
            Empresa empresa = empresasService.getById(accion.getEmpresaId());
            double porcentajeEmpresa = (double) accion.getNAcciones() / empresa.getNTotalAcciones();
            double pixelcoinsCorrespondientes = porcentajeEmpresa * movimientosService.getBalance(empresa.getEmpresaId());

            lore.add(GOLD + "- " + empresa.getNombre() + " " + formatPorcentaje(porcentajeEmpresa)
                    + "Pixelcoins: " + formatPixelcoins(pixelcoinsCorrespondientes));
        }

        return ItemBuilder.of(Material.NETHERITE_SCRAP).title(displayName).lore(lore).build();
    }

    private ItemStack buildItemBolsa () {
        String displayName = CLICKEABLE + "VER TUS ACCIONES EN BOLSA";

        List<Posicion> posicionCerradas = posicionesService.findPosicionesCerradasByJugadorId(getPlayer().getUniqueId()).stream()
                .limit(7).toList();
        List<String> lore = new ArrayList<>();
        lore.add("   ");
        lore.add(GOLD + "Tus posiciones cerradas:");

        for (Posicion pos : posicionCerradas) {
            String nombreActivo = activosBolsaService.getById(pos.getActivoBolsaId()).getNombreLargo();
            double rentabilidad = dependenciesRepository.get(pos.getTipoApuesta().getTipoApuestaService())
                    .calcularRentabilidad(pos.getPrecioApertura(), pos.getPrecioCierre()) * 100;

            lore.add(GOLD + "" + nombreActivo + " -> " + (rentabilidad >= 0 ? GREEN : RED) +
                    redondeoDecimales(rentabilidad, 2) + "% : " + redondeoDecimales((int) ((pos.getCantidad() *
                    pos.getPrecioApertura()) - pos.getCantidad() * pos.getPrecioCierre()), 2) + " PC");
        }

        return ItemBuilder.of(Material.NAME_TAG).title(displayName).lore(lore).build();
    }

    private ItemStack buildItemDeudas () {
        String displayName = CLICKEABLE + "VER TUS DEUDAS";

        double totalQueLeDeben = calculadorPatrimonioService.calcularCuenta(TipoCuentaPatrimonio.DEUDA_ACREDOR, getPlayer().getUniqueId());
        double totalQueDebe = calculadorPatrimonioService.calcularCuenta(TipoCuentaPatrimonio.DEUDA_DEUDOR, getPlayer().getUniqueId());

        List<String> lore = new ArrayList<>() {{
            add("    ");
            add(GOLD + "Total que te deben: " + formatPixelcoins(totalQueLeDeben));
            add(GOLD + "Total que debes: " + formatPixelcoins(totalQueDebe));
        }};

        return ItemBuilder.of(Material.DIAMOND_SWORD).title(displayName).lore(lore).build();
    }

    private ItemStack buildItemTienda () {
        return ItemBuilder.of(Material.CHEST).title(CLICKEABLE + "VER LA TIENDA").build();
    }

    private ItemStack buildItemStats() {
        ItemStack stats = new ItemStack(Material.PLAYER_HEAD);

        SkullMeta metaStats = (SkullMeta) stats.getItemMeta();
        metaStats.setOwningPlayer(getPlayer());
        metaStats.setDisplayName(CLICKEABLE + "VER EL TOP JUGADORES");

        List<String> lore = new ArrayList<>();
        lore.add(CARGANDO);

        metaStats.setLore(lore);
        stats.setItemMeta(metaStats);

        return stats;
    }

    @Override
    public void afterShow(Player player) {
        executor.execute(() -> {
            Map<TipoCuentaPatrimonio, Double> patrimonioDesglosado = calculadorPatrimonioService.calcularDesglosadoPorCuentas(getPlayer().getUniqueId());

            double totalEfectivo = patrimonioDesglosado.get(TipoCuentaPatrimonio.EFECTIVO);
            double totalDeudasDeudor = patrimonioDesglosado.get(TipoCuentaPatrimonio.DEUDA_DEUDOR);
            double totalDeudasAcredor = patrimonioDesglosado.get(TipoCuentaPatrimonio.DEUDA_ACREDOR);
            double totalBolsa = patrimonioDesglosado.get(TipoCuentaPatrimonio.BOLSA);
            double totalAccionesEmpresas = patrimonioDesglosado.get(TipoCuentaPatrimonio.EMPRESAS_ACCIONES);
            double patrimonioNeto = totalEfectivo + totalDeudasAcredor + totalBolsa + totalAccionesEmpresas + totalDeudasDeudor;

            Bukkit.getScheduler().runTask(Pixelcoin.INSTANCE, () -> {
                super.setItemLoreActualPage(10, List.of(
                        "   ",
                        GOLD + "Efectivo: " + formatPixelcoins(totalEfectivo),
                        GOLD + "Bolsa: " + formatPixelcoins(totalBolsa),
                        GOLD + "Empresas: " + formatPixelcoins(totalAccionesEmpresas),
                        GOLD + "Total que te deben: " + formatPixelcoins(totalDeudasAcredor),
                        GOLD + "Total que debes: " + formatPixelcoins(totalDeudasDeudor),
                        "   ",
                        GOLD + "" + BOLD + "Patrimonio neto: " + formatPixelcoins(patrimonioNeto)
                ));
            });
        });
    }
}
