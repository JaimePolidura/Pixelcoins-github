package es.serversurvival.minecraftserver.empresas.miempresa;

import es.bukkitbettermenus.Menu;
import es.bukkitbettermenus.MenuService;
import es.bukkitbettermenus.configuration.MenuConfiguration;
import es.bukkitbettermenus.menustate.AfterShow;
import es.bukkitbettermenus.modules.pagination.PaginationConfiguration;
import es.bukkitclassmapper._shared.utils.ItemBuilder;
import es.bukkitclassmapper._shared.utils.ItemUtils;
import es.serversurvival.minecraftserver._shared.menus.MenuItems;
import es.serversurvival.minecraftserver.empresas.repatirdividendos.RepartirDividendosConfirmacionMenu;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.minecraftserver._shared.MinecraftUtils;
import es.serversurvival.minecraftserver.empresas.cerrar.CerrarEmpresaConfirmacionMenu;
import es.serversurvival.minecraftserver.empresas.votaciones.VerVotacionesEmpresaMenu;
import es.serversurvival.minecraftserver.jugadores.perfil.PerfilMenu;
import es.serversurvival.pixelcoins.empresas._shared.accionistas.applicaion.AccionistasEmpresasService;
import es.serversurvival.pixelcoins.empresas._shared.empleados.domain.Empleado;
import es.serversurvival.pixelcoins.empresas._shared.empleados.application.EmpleadosService;
import es.serversurvival.pixelcoins.empresas._shared.empresas.domain.Empresa;
import es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votaciones.domain.Votacion;
import es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votaciones.application.VotacionesService;
import es.serversurvival.pixelcoins.jugadores._shared.jugadores.JugadoresService;
import es.serversurvival.pixelcoins.transacciones.TransaccionesService;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static es.serversurvival._shared.utils.Funciones.*;
import static java.lang.String.format;
import static org.bukkit.ChatColor.*;
import static org.bukkit.ChatColor.GOLD;

@RequiredArgsConstructor
public final class MiEmpresaMenu extends Menu<Empresa> implements AfterShow {
    private final AccionistasEmpresasService accionistasEmpresasService;
    private final TransaccionesService transaccionesService;
    private final VotacionesService votacionesService;
    private final EmpleadosService empleadosService;
    private final JugadoresService jugadoresService;
    private final MenuService menuService;

    @Override
    public int[][] items() {
        return new int[][] {
                {1, 2, 5, 0, 0, 0, 0,  10, 3},
                {6, 0, 0, 0, 0, 0, 0,   0, 0},
                {0, 0, 0, 0, 0, 0, 0,   0, 0},
                {0, 0, 0, 0, 0, 0, 0,   0, 0},
                {0, 0, 0, 0, 0, 0, 0,   0, 0},
                {0, 0, 0, 0, 0, 0, 7,   8, 9}
        };
    }

    @Override
    public MenuConfiguration configuration() {
        return MenuConfiguration.builder()
                .fixedItems()
                .title(format(DARK_RED + "" + BOLD + "Empresa: %s", this.getState().getNombre()))
                .item(1, buildItemInfo())
                .item(2, buildItemEmpresaStats())
                .item(3, buildItemAccionistas())
                .item(4, buildItemRepartirDividendo(), this::repartirDividendos)
                .item(10, buildItemVotaciones(), this::openMenuVotaciones)
                .item(5, buildItemCerrarEmpresa(), this::abrirCerrarEmpresaConfirmacion)
                .items(6, buildItemsEmpleados(), this::abrirMenuOpccionesEmpleado)
                .breakpoint(7, MenuItems.GO_BACK, this::goBackToProfileMenu)
                .paginated(PaginationConfiguration.builder()
                        .forward(9, Material.GREEN_WOOL)
                        .backward(8, Material.RED_WOOL)
                        .build())
                .build();
    }

    private void openMenuVotaciones(Player player, InventoryClickEvent event) {
        this.menuService.open(player, VerVotacionesEmpresaMenu.class, getState());
    }

    private void repartirDividendos(Player player, InventoryClickEvent event) {
        this.menuService.open(player, RepartirDividendosConfirmacionMenu.class, this.getState());
    }

    private void abrirCerrarEmpresaConfirmacion(Player player, InventoryClickEvent event) {
        this.menuService.open(player, CerrarEmpresaConfirmacionMenu.class, getState());
    }

    private void abrirMenuOpccionesEmpleado(Player player, InventoryClickEvent event) {
        UUID empleadoId = MinecraftUtils.getLastLineOfLore(event.getCurrentItem(), 0);
        UUID jugadorId = MinecraftUtils.getLastLineOfLore(event.getCurrentItem(), 1);

        if(!getState().getDirectorJugadorId().equals(getPlayer().getUniqueId()))
            return;
        if(jugadorId.equals(getState().getDirectorJugadorId()))
            return;

        Empleado empleado = empleadosService.getById(empleadoId);

        menuService.open(player, OpccionesEmpleadoMenu.class, empleado);
    }

    private void goBackToProfileMenu(Player player, InventoryClickEvent event) {
        this.menuService.open(player, PerfilMenu.class);
    }

    private ItemStack buildItemVotaciones() {
        int numeroVotacionesPendientes = (int) votacionesService.findByEmpresaId(getState().getEmpresaId()).stream()
                .filter(Votacion::estaAbierta)
                .count();
        boolean hayVotacionesPendienets = numeroVotacionesPendientes > 0;

        return ItemBuilder.of(Material.WHITE_BANNER)
                .title(hayVotacionesPendienets ? MenuItems.CLICKEABLE + "VER LAS VOTACIONES" : GOLD + "" + BOLD + "VOTACIONES")
                .lore(hayVotacionesPendienets ? RED + "Hay "+numeroVotacionesPendientes+" votaciones pendientes" : GOLD + "No hay votaciones pendientes").build();
    }

    private List<ItemStack> buildItemsEmpleados() {
        return this.empleadosService.findEmpleoActivoByEmpresaId(this.getState().getEmpresaId()).stream()
                .map(this::buildEmpleadoItem)
                .toList();
    }

    private ItemStack buildEmpleadoItem(Empleado empleado) {
        boolean esDirector = getState().getDirectorJugadorId().equals(getPlayer().getUniqueId());
        return ItemBuilder.of(Material.PLAYER_HEAD)
                .title(empleado.getEmpleadoJugadorId().equals(getState().getDirectorJugadorId()) ?
                                DARK_RED + "" + BOLD  + "DIRECTOR DE LA EMPRESA" :
                                (esDirector ? MenuItems.CLICKEABLE + "VER OPCCIONES" : GOLD + "Empleado"))
                .lore(List.of(
                        "   ",
                        GOLD + "Empleado: " + jugadoresService.getNombreById(empleado.getEmpleadoJugadorId()),
                        GOLD + "Cargo: " + empleado.getDescripccion(),
                        GOLD + "Sueldo: " + GREEN + formatNumero(empleado.getSueldo()) + " PC / " + GOLD + millisToDias(empleado.getPeriodoPagoMs()) + " dias",
                        GOLD + "Ultima fecha paga: " + Funciones.toString(empleado.getFechaUltimoPago()),
                        "   ",
                        "/empresas editarempleado " + getState().getNombre() + " " + jugadoresService.getNombreById(empleado.getEmpleadoJugadorId()),
                        " ",
                        empleado.getEmpleadoJugadorId().toString(),
                        empleado.getEmpleadoId().toString()
                ))
                .build();
    }

    private ItemStack buildItemCerrarEmpresa() {
        return !getState().isEsCotizada() ?
                ItemBuilder.of(Material.BARRIER).title(MenuItems.CLICKEABLE + "CERRAR LA EMPRESA").build() :
                ItemBuilder.of(Material.AIR).build();
    }

    private ItemStack buildItemRepartirDividendo() {
        return ItemBuilder.of(Material.GOLD_INGOT)
                .title(GOLD + "" + BOLD + "REPARTIR DIVIDENDOS")
                .lore(GOLD + "Repartir pixelcoins por cada accion")
                .build();
    }

    private ItemStack buildItemAccionistas() {
        List<String> loreAccionistas = accionistasEmpresasService.findByEmpresaId(getState().getEmpresaId()).stream()
                .map(posicion -> new Accionista(
                        jugadoresService.getNombreById(posicion.getAccionisaJugadorId()),
                        posicion.getNAcciones(),
                        (double) posicion.getNAcciones() / getState().getNTotalAcciones()))
                .sorted((a, b) -> b.nAcciones - a.nAcciones)
                .map(this::getLoreAccionista)
                .collect(Collectors.toList());

        loreAccionistas.add(0, GOLD + "Nº Total acciones: " + getState().getNTotalAcciones());
        loreAccionistas.add(1, "");

        return ItemBuilder.of(Material.NETHERITE_SCRAP)
                .title(GOLD + "" + BOLD + "ACCIONISTAS")
                .lore(loreAccionistas)
                .build();
    }

    private record Accionista(String nombreAccionista, int nAcciones, double porcentajeNTotalAcciones) {}

    private String getLoreAccionista(Accionista accionista) {
        String percentajeOwnership = Funciones.formatPorcentaje(accionista.porcentajeNTotalAcciones * 100);

        return GOLD + accionista.nombreAccionista() + ": " + GREEN + percentajeOwnership + "%";
    }

    private ItemStack buildItemEmpresaStats() {
        List<String> lore = new ArrayList<>();
        lore.add(GOLD + "Descripccion:");
        lore.add(1, GOLD + "" + getState().getDescripcion());
        lore.add("  ");
        lore.add(GOLD + "Pixelcoins: " + GREEN + FORMATEA.format(transaccionesService.getBalancePixelcoins(getState().getEmpresaId())) + " PC");
        lore.add(GOLD + "Fundador: " + jugadoresService.getNombreById(getState().getFundadorJugadorId()));
        lore.add(GOLD + "Director: " + jugadoresService.getNombreById(getState().getDirectorJugadorId()));
        lore.add(GOLD + "Nº Total acciones: " + getState().getNTotalAcciones());
        lore.add(GOLD + "Fecha creacion: " + getState().getFechaCreacion().toString());
        lore.add(GOLD + (getState().isEsCotizada() ? "Es publica" : "No es publica"));
        lore.add("   ");
        lore.add("/empresas depositar " + getState().getNombre() + " <pixelcoins>");
        lore.add("/empresas sacar " + getState().getNombre() + " <pixelcoins>");
        lore.add("/empresas logotipo " + getState().getNombre() + "");
        lore.add("Mas info en /ayuda empresario o:");

        return ItemBuilder.of(Material.WRITABLE_BOOK)
                .title(GOLD + "" + BOLD + "" + getState().getNombre().toUpperCase())
                .lore(lore)
                .build();
    }

    //TODO
    private ItemStack buildItemInfo() {
        return ItemBuilder.of(Material.PAPER)
                .title(GOLD + "" + BOLD + "INFO")
                .lore(List.of(
                        "TOOD"
                ))
                .build();
    }

    @Override
    public void afterShow(Player player) {
        for (ItemStack itemEmpleado : super.getAllItemsByItemNum(6)) {
            SkullMeta currentItemMeta = (SkullMeta) itemEmpleado.getItemMeta();
            String empleadoNombre = ItemUtils.getLore(itemEmpleado, 1).split(" ")[1];
            currentItemMeta.setOwningPlayer(Bukkit.getOfflinePlayer(empleadoNombre));

            itemEmpleado.setItemMeta(currentItemMeta);
        }
    }
}
