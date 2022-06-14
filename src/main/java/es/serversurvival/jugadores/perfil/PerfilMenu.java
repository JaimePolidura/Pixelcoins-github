package es.serversurvival.jugadores.perfil;

import es.jaimetruman.ItemBuilder;
import es.jaimetruman.menus.Menu;
import es.jaimetruman.menus.MenuService;
import es.jaimetruman.menus.configuration.MenuConfiguration;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.bolsa.posicionesabiertas._shared.application.PosicionesUtils;
import es.serversurvival.bolsa.posicionesabiertas.vercartera.VerBolsaCarteraMenu;
import es.serversurvival.bolsa.posicionescerradas._shared.application.PosicionesCerradasService;
import es.serversurvival.bolsa.posicionescerradas._shared.domain.PosicionCerrada;
import es.serversurvival.deudas._shared.application.DeudasService;
import es.serversurvival.deudas.ver.VerDeudasMenu;
import es.serversurvival.empresas.empleados._shared.application.EmpleadosService;
import es.serversurvival.empresas.empleados._shared.domain.Empleado;
import es.serversurvival.empresas.empleados.misempleos.VerEmpleosMenu;
import es.serversurvival.empresas.empresas._shared.application.EmpresasService;
import es.serversurvival.empresas.empresas._shared.domain.Empresa;
import es.serversurvival.empresas.empresas.vertodas.VerTodasEmpresasMenu;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import es.serversurvival.jugadores._shared.domain.Jugador;
import es.serversurvival.jugadores.top.TopMenu;
import es.serversurvival.tienda.vertienda.menu.TiendaMenu;
import es.serversurvival.web.cuentasweb._shared.application.CuentasWebService;
import es.serversurvival.web.cuentasweb._shared.domain.CuentaWeb;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static es.serversurvival._shared.utils.CollectionUtils.getPoisitionOfKeyInMap;
import static es.serversurvival._shared.utils.Funciones.*;
import static es.serversurvival._shared.utils.Funciones.FORMATEA;
import static org.bukkit.ChatColor.*;

public final class PerfilMenu extends Menu {
    private static final String TITULO = DARK_RED + "" + BOLD + "           TU PERFIL";

    private final Jugador jugador;
    private final CuentasWebService cuentasWebService;
    private final JugadoresService jugadoresService;
    private final DeudasService deudasService;
    private final EmpresasService empresasService;
    private final PosicionesCerradasService posicionesCerradasService;
    private final EmpleadosService empleadosService;
    private final MenuService menuService;

    public PerfilMenu(String jugadorNombre) {
        this.cuentasWebService = DependecyContainer.get(CuentasWebService.class);
        this.jugadoresService = DependecyContainer.get(JugadoresService.class);
        this.jugador = this.jugadoresService.getByNombre(jugadorNombre);
        this.deudasService = DependecyContainer.get(DeudasService.class);
        this.empresasService = DependecyContainer.get(EmpresasService.class);
        this.posicionesCerradasService = DependecyContainer.get(PosicionesCerradasService.class);
        this.empleadosService = DependecyContainer.get(EmpleadosService.class);
        this.menuService = DependecyContainer.get(MenuService.class);
    }

    @Override
    public int[][] items() {
        return new int[][] {
                {1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 2, 0, 0, 3, 0, 0, 4, 1},
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
                .title(TITULO)
                .item(1, Material.BLACK_STAINED_GLASS_PANE)
                .item(2, buildItemWeb())
                .item(3, buildItemStats(), (p, e) -> this.menuService.open(p, new TopMenu()))
                .item(4, buildItemTienda(), (p, e) -> this.menuService.open(p, new TiendaMenu(p.getName())))
                .item(5, buildItemDeudas(), (p, e) -> this.menuService.open(p, new VerDeudasMenu(p.getName())))
                .item(6, buildItemBolsa(), (p, e) -> this.menuService.open(p, new VerBolsaCarteraMenu(p.getName())))
                .item(7, buildItemEmpresa(), (p, e) -> this.menuService.open(p, new VerTodasEmpresasMenu(p)))
                .item(8, buildItemEmpleos(), (p, e) -> this.menuService.open(p, new VerEmpleosMenu(p)))
                .build();
    }

    private ItemStack buildItemEmpleos () {
        String displayName = ChatColor.GOLD + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "CLICK PARA VER TUS EMPLEOS";

        List<String> lore = new ArrayList<>();
        lore.add("  ");
        List<Empleado> empleos = empleadosService.findByJugador(jugador.getNombre());
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
        List<Empresa> empresas = empresasService.getByOwner(jugador.getNombre());
        empresas.forEach( (empresa) -> {
            lore.add(ChatColor.GOLD + "- " + empresa.getNombre() + " ( " + ChatColor.GREEN +
                    FORMATEA.format(empresa.getPixelcoins()) + " PC" +  ChatColor.GOLD + ")");
        });

        return ItemBuilder.of(Material.GOLD_BLOCK).title(displayName).lore(lore).build();
    }

    private ItemStack buildItemBolsa () {
        String displayName = ChatColor.GOLD + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "CLICK VER TUS ACCIONES";

        List<PosicionCerrada> posicionCerradas = posicionesCerradasService.findByJugador(jugador.getNombre()).stream()
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

        double totalQueLeDeben = deudasService.getAllPixelcoinsDeudasAcredor(jugador.getNombre());
        double totalQueDebe = deudasService.getAllPixelcoinsDeudasDeudor(jugador.getNombre());

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
        metaStats.setOwningPlayer(Bukkit.getPlayer(jugador.getNombre()));
        metaStats.setDisplayName(GOLD + "" + BOLD + "" + UNDERLINE + "CLICK PARA VER EL TOP JUGADORES");

        if(jugador == null) return stats;

        double totalAhorrado = jugador.getPixelcoins();
        double totalDebe = deudasService.getAllPixelcoinsDeudasDeudor(jugador.getNombre());
        double totalDeben = deudasService.getAllPixelcoinsDeudasAcredor(jugador.getNombre());
        double totalEnAcciones = PosicionesUtils.getAllPixeloinsEnValores(jugador.getNombre());
        double totalEmpresas = empresasService.getAllPixelcoinsEnEmpresas(jugador.getNombre());
        double resultado = (totalAhorrado + totalDeben + totalEnAcciones + totalEmpresas) - totalDebe;

        double beneficios = jugador.getIngresos() - jugador.getGastos();
        double rentabilidad = jugador.getIngresos() == 0 ? -100 : rentabilidad(jugador.getIngresos(), beneficios);

        int posTopRicps = getPoisitionOfKeyInMap(crearMapaTopPatrimonioPlayers(false), k -> k.equalsIgnoreCase(jugador.getNombre()));
        int posTopVendedores = jugadoresService.sortJugadoresBy(Comparator.comparingInt(Jugador::getNventas)).indexOf(jugador) + 1;

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
        lore.add(GOLD + "Ingresos: " + GREEN + FORMATEA.format(jugador.getIngresos()) + " PC");
        lore.add(GOLD + "Gastos: " + GREEN + FORMATEA.format(jugador.getGastos()) + " PC");
        lore.add(GOLD + "Beneficios: " + (beneficios >= 0 ? GREEN : RED) + FORMATEA.format(beneficios) +" PC");
        lore.add(GOLD + "Rentabilidad: " + (beneficios >= 0 ? GREEN + "+" : RED) + FORMATEA.format((int) rentabilidad) + " %");
        lore.add("   ");
        lore.add(GOLD + "Nº de ventas en la tienda: " + jugador.getNombre());
        lore.add(GOLD + "Posicion en top vendedores: " + posTopVendedores);
        lore.add("   ");
        lore.add(GOLD + "Numero de veces pagada la deuda: " + jugador.getNpagosDeuda());
        lore.add(GOLD + "Numero de veces de inpago de la deuda: " + RED +  jugador.getNinpagosDeuda());

        metaStats.setLore(lore);
        stats.setItemMeta(metaStats);

        return stats;
    }

    private ItemStack buildItemWeb() {
        String displayName = AQUA + "" + BOLD + "      WEB http://serversurvival.ddns.net";
        List<String> lore = new ArrayList<>();
        lore.add("  ");

        Optional<CuentaWeb> cuentaWebOptional = this.cuentasWebService.findByUsername(jugador.getNombre());
        if(cuentaWebOptional.isEmpty()){
            int numeroCuenta = jugadoresService.getByNombre(jugador.getNombre()).getNumeroVerificacionCuenta();
            lore.add(DARK_AQUA + "No tienes cuenta, para registrarse:");
            lore.add(DARK_AQUA + "" + UNDERLINE + "http://serversurvival.ddns.net/registrarse");
            lore.add(DARK_AQUA + "Tu numero de cuenta: " + BOLD + numeroCuenta);
        }else{
            lore.add(DARK_AQUA + "Ya tienes cuenta");
            lore.add(DARK_AQUA + "" + UNDERLINE + "http//serversurvival.ddns.net/iniciarsesion");
        }
        lore.add("  ");
        lore.add(DARK_AQUA + "Con la web podras acceder a todas tus estadisticas");
        lore.add(DARK_AQUA + "y comprar cantidad, realizar transacciones etc.");

        return ItemBuilder.of(Material.PAPER).title(displayName).lore(lore).build();
    }
}
