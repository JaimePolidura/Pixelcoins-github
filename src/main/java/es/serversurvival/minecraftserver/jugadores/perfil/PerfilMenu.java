package es.serversurvival.minecraftserver.jugadores.perfil;

import es.bukkitbettermenus.Menu;
import es.bukkitbettermenus.MenuService;
import es.bukkitbettermenus.configuration.MenuConfiguration;
import es.bukkitclassmapper._shared.utils.ItemBuilder;
import es.dependencyinjector.dependencies.DependenciesRepository;
import es.serversurvival.minecraftserver.empresas.vertodas.VerTodasEmpresasMenu;
import es.serversurvival.pixelcoins.bolsa._shared.activos.aplicacion.ActivosBolsaService;
import es.serversurvival.pixelcoins.jugadores.patrimonio.CalculadorPatrimonioService;
import es.serversurvival.pixelcoins.jugadores.patrimonio.TipoCuentaPatrimonio;
import es.serversurvival.pixelcoins.transacciones.TransaccionesService;
import es.serversurvival.v1.bolsa.posicionesabiertas.vercartera.VerBolsaCarteraMenu;
import es.serversurvival.v1.deudas.ver.VerDeudasMenu;
import es.serversurvival.v1.empresas.empleados.misempleos.VerEmpleosMenu;
import es.serversurvival.v1.tienda.vertienda.TiendaMenu;
import es.serversurvival.minecraftserver.jugadores.top.TopMenu;
import es.serversurvival.pixelcoins.bolsa._shared.posiciones.Posicion;
import es.serversurvival.pixelcoins.bolsa._shared.posiciones.PosicionesService;
import es.serversurvival.pixelcoins.empresas._shared.accionistas.AccionistaEmpresa;
import es.serversurvival.pixelcoins.empresas._shared.accionistas.AccionistasEmpresasService;
import es.serversurvival.pixelcoins.empresas._shared.empleados.Empleado;
import es.serversurvival.pixelcoins.empresas._shared.empleados.EmpleadosService;
import es.serversurvival.pixelcoins.empresas._shared.empresas.Empresa;
import es.serversurvival.pixelcoins.empresas._shared.empresas.EmpresasService;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

import static es.serversurvival._shared.utils.Funciones.*;
import static org.bukkit.ChatColor.*;

@AllArgsConstructor
public final class PerfilMenu extends Menu {
    private final CalculadorPatrimonioService calculadorPatrimonioService;
    private final AccionistasEmpresasService accionistasEmpresasService;
    private final DependenciesRepository dependenciesRepository;
    private final TransaccionesService transaccionesService;
    private final ActivosBolsaService activosBolsaService;
    private final PosicionesService posicionesService;
    private final EmpleadosService empleadosService;
    private final EmpresasService empresasService;
    private final MenuService menuService;

    @Override
    public int[][] items() {
        return new int[][] {
                {1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 3, 0, 0, 0, 0, 0, 4, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 5, 0, 6, 0, 7, 0, 1},
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
                .item(5, buildItemDeudas(), (p, e) -> this.menuService.open(p, VerDeudasMenu.class))
                .item(6, buildItemBolsa(), (p, e) -> this.menuService.open(p, VerBolsaCarteraMenu.class))
                .item(7, buildItemEmpresa(), (p, e) -> this.menuService.open(p, VerTodasEmpresasMenu.class))
                .item(8, buildItemEmpleos(), (p, e) -> this.menuService.open(p, VerEmpleosMenu.class))
                .build();
    }

    private ItemStack buildItemEmpleos () {
        String displayName = ChatColor.GOLD + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "CLICK PARA VER TUS EMPLEOS";

        List<String> lore = new ArrayList<>();
        lore.add("  ");
        List<Empleado> empleos = empleadosService.findByEmpleadoJugadorId(getPlayer().getUniqueId());
        empleos.forEach( (emp) -> {
            Empresa empresa = empresasService.getById(emp.getEmpresaId());

            lore.add(ChatColor.GOLD + "" + empresa.getNombre() + " " + ChatColor.GREEN + FORMATEA.format(emp.getSueldo()) +
                    " PC " + ChatColor.GOLD + "/ " + emp.periodoSueldoToDias() + " dias");
        });

        return ItemBuilder.of(Material.GOLDEN_APPLE).title(displayName).lore(lore).build();
    }

    private ItemStack buildItemEmpresa() {
        String displayName = ChatColor.GOLD + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "CLICK PARA VER TUS EMPRESAS";
        List<String> lore = new ArrayList<>();
        lore.add("  ");

        List<AccionistaEmpresa> acciones = accionistasEmpresasService.findByJugadorId(getPlayer().getUniqueId());
        for (AccionistaEmpresa accion : acciones) {
            Empresa empresa = empresasService.getById(accion.getEmpresaId());
            double porcentajeEmpresa = (double) accion.getNAcciones() / empresa.getNTotalAcciones();
            double pixelcoinsCorrespondientes = porcentajeEmpresa * transaccionesService.getBalancePixelcions(empresa.getEmpresaId());

            lore.add(GOLD + "- " + empresa.getNombre() + " " + FORMATEA.format(porcentajeEmpresa * 100)
                    + "% Pixelcoins: " + GREEN + FORMATEA.format(pixelcoinsCorrespondientes) + " PC");
        }

        return ItemBuilder.of(Material.BOOK).title(displayName).lore(lore).build();
    }

    private ItemStack buildItemBolsa () {
        String displayName = ChatColor.GOLD + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "CLICK VER TUS ACCIONES";

        List<Posicion> posicionCerradas = posicionesService.findPosicionesCerradasByJugadorId(getPlayer().getUniqueId()).stream()
                .limit(7).toList();
        List<String> lore = new ArrayList<>();
        lore.add("   ");
        lore.add(ChatColor.GOLD + "Tus posiciones cerradas:");

        for (Posicion pos : posicionCerradas) {
            String nombreActivo = activosBolsaService.getById(pos.getActivoBolsaId()).getNombreLargo();
            double rentabilidad = dependenciesRepository.get(pos.getTipoApuesta().getTipoApuestaService())
                    .calcularRentabilidad(pos.getPrecioApertura(), pos.getPrecioCierre()) * 100;

            lore.add(ChatColor.GOLD + "" + nombreActivo + " -> " + (rentabilidad >= 0 ? GREEN : RED) +
                    redondeoDecimales(rentabilidad, 2) + "% : " + redondeoDecimales((int) ((pos.getCantidad() *
                    pos.getPrecioApertura()) - pos.getCantidad() * pos.getPrecioCierre()), 2) + " PC");
        }

        return ItemBuilder.of(Material.BOOK).title(displayName).lore(lore).build();
    }

    private ItemStack buildItemDeudas () {
        String displayName = ChatColor.GOLD + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "CLICK PARA VER TUS DEUDAS";

        double totalQueLeDeben = calculadorPatrimonioService.calcularCuenta(TipoCuentaPatrimonio.DEUDA_ACREDOR, getPlayer().getUniqueId());
        double totalQueDebe = calculadorPatrimonioService.calcularCuenta(TipoCuentaPatrimonio.DEUDA_DEUDOR, getPlayer().getUniqueId());

        List<String> lore = new ArrayList<>() {{
            add("    ");
            add(ChatColor.GOLD + "Total que debes: " + ChatColor.GREEN + totalQueDebe + " PC");
            add(ChatColor.GOLD + "Total que te deben: " + ChatColor.GREEN + totalQueLeDeben + " PC");
        }};

        return ItemBuilder.of(Material.DIAMOND_SWORD).title(displayName).lore(lore).build();
    }

    private ItemStack buildItemTienda () {
        return ItemBuilder.of(Material.CHEST).title(GOLD + "" + BOLD + "" + UNDERLINE + "CLICK PARA VER LA TIENDA").build();
    }

    private ItemStack buildItemStats() {
        ItemStack stats = new ItemStack(Material.PLAYER_HEAD);

        SkullMeta metaStats = (SkullMeta) stats.getItemMeta();
        metaStats.setOwningPlayer(getPlayer());
        metaStats.setDisplayName(GOLD + "" + BOLD + "" + UNDERLINE + "CLICK PARA VER EL TOP JUGADORES");

        if(getPlayer() == null) return stats;

        Map<TipoCuentaPatrimonio, Double> patrimonioDesglosado = calculadorPatrimonioService.calcularDesglosadoPorCuentas(getPlayer().getUniqueId());

        double totalEfectivo = patrimonioDesglosado.get(TipoCuentaPatrimonio.EFECTIVO);
        double totalDeudasDeudor = patrimonioDesglosado.get(TipoCuentaPatrimonio.DEUDA_DEUDOR);
        double totalDeudasAcredor = patrimonioDesglosado.get(TipoCuentaPatrimonio.DEUDA_ACREDOR);
        double totalBolsa = patrimonioDesglosado.get(TipoCuentaPatrimonio.BOLSA);
        double totalAccionesEmpresas = patrimonioDesglosado.get(TipoCuentaPatrimonio.EMPRESAS_ACCIONES);
        double patrimonioNeto = totalEfectivo + totalDeudasAcredor + totalBolsa + totalAccionesEmpresas + totalDeudasDeudor;

        int posTopRicps = calculadorPatrimonioService.getPosicionTopRicos(getPlayer().getName());

        List<String> lore = new ArrayList<>();
        lore.add("  ");
        lore.add(GOLD + "" + BOLD + "       TUS ESTADISTICAS");
        lore.add("   ");
        lore.add(GOLD + "Liquidez (ahorrado): " + GREEN + FORMATEA.format(totalEfectivo) + " PC");
        lore.add(GOLD + "Total en empresas: " + GREEN + FORMATEA.format(totalAccionesEmpresas) + " PC");
        lore.add(GOLD + "Total en cantidad: " + GREEN + FORMATEA.format(totalBolsa) + " PC");
        lore.add(GOLD + "Total que te deben: " + GREEN + FORMATEA.format(totalDeudasAcredor) + " PC");
        lore.add(GOLD + "Total que debes: " + RED + FORMATEA.format(totalDeudasDeudor) + " PC");
        lore.add(GOLD + "" + BOLD + "Patrimonio neto: " + (patrimonioNeto >= 0 ? GREEN + FORMATEA.format(patrimonioNeto) : RED + "-" + FORMATEA.format(patrimonioNeto)) + " PC");
        lore.add(GOLD + "Posicion top ricos: " + posTopRicps);
        lore.add("    ");

        metaStats.setLore(lore);
        stats.setItemMeta(metaStats);

        return stats;
    }
}
