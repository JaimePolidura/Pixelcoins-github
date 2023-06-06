package es.serversurvival.v1.jugadores.perfil;

import es.bukkitbettermenus.Menu;
import es.bukkitbettermenus.MenuService;
import es.bukkitbettermenus.configuration.MenuConfiguration;
import es.bukkitclassmapper._shared.utils.ItemBuilder;
import es.serversurvival.v1.bolsa.posicionesabiertas._shared.application.PosicionesUtils;
import es.serversurvival.v1.bolsa.posicionesabiertas.vercartera.VerBolsaCarteraMenu;
import es.serversurvival.v1.bolsa.posicionescerradas._shared.application.PosicionesCerradasService;
import es.serversurvival.v1.bolsa.posicionescerradas._shared.domain.PosicionCerrada;
import es.serversurvival.v1.deudas._shared.application.DeudasService;
import es.serversurvival.v1.deudas.ver.VerDeudasMenu;
import es.serversurvival.v1.empresas.empleados._shared.application.EmpleadosService;
import es.serversurvival.v1.empresas.empleados._shared.domain.Empleado;
import es.serversurvival.v1.empresas.empleados.misempleos.VerEmpleosMenu;
import es.serversurvival.v1.empresas.empresas._shared.application.EmpresasService;
import es.serversurvival.v1.empresas.empresas._shared.domain.Empresa;
import es.serversurvival.v2.minecraftserver.empresas.vertodas.VerTodasEmpresasMenu;
import es.serversurvival.v1.jugadores._shared.application.CalculadorPatrimonio;
import es.serversurvival.v1.jugadores._shared.application.JugadoresService;
import es.serversurvival.v1.jugadores.top.TopMenu;
import es.serversurvival.v1.tienda.vertienda.TiendaMenu;
import es.serversurvival.v2.pixelcoins.jugadores._shared.jugadores.Jugador;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static es.serversurvival.v1._shared.utils.Funciones.*;
import static es.serversurvival.v1._shared.utils.Funciones.FORMATEA;
import static org.bukkit.ChatColor.*;

@AllArgsConstructor
public final class PerfilMenu extends Menu {
    private final PosicionesCerradasService posicionesCerradasService;
    private final CalculadorPatrimonio calculadorPatrimonio;
    private final EmpleadosService empleadosService;
    private final JugadoresService jugadoresService;
    private final PosicionesUtils posicionesUtils;
    private final EmpresasService empresasService;
    private final DeudasService deudasService;
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
        List<Empleado> empleos = empleadosService.findByJugador(getPlayer().getName());
        empleos.forEach( (emp) -> {
            lore.add(ChatColor.GOLD + "" + emp.getEmpresa() + " " + ChatColor.GREEN + FORMATEA.format(emp.getSueldo()) +
                    " PC " + ChatColor.GOLD + "/ " + emp.getTipoSueldo().nombre);
        });

        return ItemBuilder.of(Material.GOLDEN_APPLE).title(displayName).lore(lore).build();
    }

    private ItemStack buildItemEmpresa () {
        String displayName = ChatColor.GOLD + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "CLICK PARA VER TUS EMPRESAS";

        List<String> lore = new ArrayList<>();
        lore.add("  ");
        List<Empresa> empresas = empresasService.getByOwner(getPlayer().getName());
        empresas.forEach( (empresa) -> {
            lore.add(ChatColor.GOLD + "- " + empresa.getNombre() + " ( " + ChatColor.GREEN +
                    FORMATEA.format(empresa.getPixelcoins()) + " PC" +  ChatColor.GOLD + ")");
        });

        return ItemBuilder.of(Material.GOLD_BLOCK).title(displayName).lore(lore).build();
    }

    private ItemStack buildItemBolsa () {
        String displayName = ChatColor.GOLD + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "CLICK VER TUS ACCIONES";

        List<PosicionCerrada> posicionCerradas = posicionesCerradasService.findByJugador(getPlayer().getName()).stream()
                .limit(7).toList();
        List<String> lore = new ArrayList<>();
        lore.add("   ");
        lore.add(ChatColor.GOLD + "Tus posiciones cerradas:");

        for (PosicionCerrada pos : posicionCerradas) {
            lore.add(ChatColor.GOLD + "" + pos.getNombreActivo() + " -> " + (pos.calculateRentabildiad() >= 0 ? GREEN : RED) +
                    redondeoDecimales(pos.calculateRentabildiad(), 2) + "% : " + redondeoDecimales((int) ((pos.getCantidad() *
                    pos.getPrecioApertura()) - pos.getCantidad() * pos.getPrecioCierre()), 2) + " PC");
        }

        return ItemBuilder.of(Material.BOOK).title(displayName).lore(lore).build();
    }

    private ItemStack buildItemDeudas () {
        String displayName = ChatColor.GOLD + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "CLICK PARA VER TUS DEUDAS";

        double totalQueLeDeben = deudasService.getAllPixelcoinsDeudasAcredor(getPlayer().getName());
        double totalQueDebe = deudasService.getAllPixelcoinsDeudasDeudor(getPlayer().getName());

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
        metaStats.setOwningPlayer(Bukkit.getPlayer(getState().getName()));
        metaStats.setDisplayName(GOLD + "" + BOLD + "" + UNDERLINE + "CLICK PARA VER EL TOP JUGADORES");

        if(getState() == null) return stats;

        double totalAhorrado = getPlayer().getPixelcoins();
        double totalDebe = deudasService.getAllPixelcoinsDeudasDeudor(getPlayer().getName());
        double totalDeben = deudasService.getAllPixelcoinsDeudasAcredor(getPlayer().getName());
        double totalEnAcciones = posicionesUtils.getAllPixeloinsEnValores(getPlayer().getName());
        double totalEmpresas = empresasService.getAllPixelcoinsEnEmpresas(getPlayer().getName());
        double resultado = (totalAhorrado + totalDeben + totalEnAcciones + totalEmpresas) - totalDebe;

        double beneficios = getPlayer().getIngresos() - getPlayer().getGastos();
        double rentabilidad = getPlayer().getIngresos() == 0 ? -100 : rentabilidad(getState().getIngresos(), beneficios);

        int posTopRicps = calculadorPatrimonio.getPosicionTopRicos(getState().getName());
        int posTopVendedores = jugadoresService.sortJugadoresBy(Comparator.comparingInt(Jugador::getNventas)).indexOf(getState()) + 1;

        List<String> lore = new ArrayList<>();
        lore.add("  ");
        lore.add(GOLD + "" + BOLD + "       TUS ESTADISTICAS");
        lore.add("   ");
        lore.add(GOLD + "Liquidez (ahorrado): " + GREEN + FORMATEA.format(totalAhorrado) + " PC");
        lore.add(GOLD + "Total en empresas: " + GREEN + FORMATEA.format(totalEmpresas) + " PC");
        lore.add(GOLD + "Total en cantidad: " + GREEN + FORMATEA.format(totalEnAcciones) + " PC");
        lore.add(GOLD + "Total que te deben: " + GREEN + FORMATEA.format(totalDeben) + " PC");
        lore.add(GOLD + "Total que debes: " + RED + "-" + FORMATEA.format(totalDebe) + " PC");
        lore.add(GOLD + "" + BOLD + "Reultado: " + (beneficios >= 0 ? GREEN + FORMATEA.format(resultado) : RED + "-" + FORMATEA.format(resultado)) + " PC");
        lore.add(GOLD + "Posicion top ricos: " + posTopRicps);
        lore.add("    ");
        lore.add(GOLD + "Ingresos: " + GREEN + FORMATEA.format(getState().getIngresos()) + " PC");
        lore.add(GOLD + "Gastos: " + GREEN + FORMATEA.format(getState().getGastos()) + " PC");
        lore.add(GOLD + "Beneficios: " + (beneficios >= 0 ? GREEN : RED) + FORMATEA.format(beneficios) +" PC");
        lore.add(GOLD + "Rentabilidad: " + (beneficios >= 0 ? GREEN + "+" : RED) + FORMATEA.format((int) rentabilidad) + " %");
        lore.add("   ");
        lore.add(GOLD + "Nº de ventas en la tienda: " + getState().getNombre());
        lore.add(GOLD + "Posicion en top vendedores: " + posTopVendedores);
        lore.add("   ");
        lore.add(GOLD + "Numero de veces pagada la deuda: " + getState().getNpagosDeuda());
        lore.add(GOLD + "Numero de veces de inpago de la deuda: " + RED +  getState().getNinpagosDeuda());

        metaStats.setLore(lore);
        stats.setItemMeta(metaStats);

        return stats;
    }
}
