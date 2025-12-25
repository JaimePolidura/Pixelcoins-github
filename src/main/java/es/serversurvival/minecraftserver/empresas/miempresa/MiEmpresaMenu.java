package es.serversurvival.minecraftserver.empresas.miempresa;

import es.bukkitbettermenus.Menu;
import es.bukkitbettermenus.MenuService;
import es.bukkitbettermenus.Page;
import es.bukkitbettermenus.configuration.MenuConfiguration;
import es.bukkitbettermenus.menustate.BeforeShow;
import es.bukkitbettermenus.modules.async.config.AsyncTasksConfiguration;
import es.bukkitbettermenus.modules.pagination.PaginationConfiguration;
import es.bukkitbettermenus.utils.ItemBuilder;
import es.bukkitbettermenus.utils.ItemUtils;
import es.serversurvival.minecraftserver._shared.menus.MenuItems;
import es.serversurvival.minecraftserver.empresas.repatirdividendos.RepartirDividendosConfirmacionMenu;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.minecraftserver._shared.MinecraftUtils;
import es.serversurvival.minecraftserver.empresas.cerrar.CerrarEmpresaConfirmacionMenu;
import es.serversurvival.minecraftserver.empresas.votaciones.VerVotacionesEmpresaMenu;
import es.serversurvival.minecraftserver.jugadores.perfil.PerfilMenu;
import es.serversurvival.minecraftserver.transacciones.VerTransaccionesMenu;
import es.serversurvival.pixelcoins.empresas._shared.accionistas.applicaion.AccionistasEmpresasService;
import es.serversurvival.pixelcoins.empresas._shared.empleados.domain.Empleado;
import es.serversurvival.pixelcoins.empresas._shared.empleados.application.EmpleadosService;
import es.serversurvival.pixelcoins.empresas._shared.empresas.domain.Empresa;
import es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votaciones.domain.Votacion;
import es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votaciones.application.VotacionesService;
import es.serversurvival.pixelcoins.jugadores._shared.jugadores.JugadoresService;
import es.serversurvival.pixelcoins.transacciones.application.MovimientosService;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import static es.serversurvival._shared.utils.Funciones.*;
import static java.lang.String.format;
import static org.bukkit.ChatColor.*;
import static org.bukkit.ChatColor.GOLD;

@RequiredArgsConstructor
public final class MiEmpresaMenu extends Menu<Empresa> implements BeforeShow {
    private final AccionistasEmpresasService accionistasEmpresasService;
    private final MovimientosService movimientosService;
    private final VotacionesService votacionesService;
    private final EmpleadosService empleadosService;
    private final JugadoresService jugadoresService;
    private final MenuService menuService;

    private EstadoEmpresaJugador estadoEmpresaJugador;

    @Override
    public int[][] items() {
        return new int[][] {
                {1, 2, 11, 5, 0, 0, 4,  10, 3},
                {6, 0, 0,  0, 0, 0, 0,   0, 0},
                {0, 0, 0,  0, 0, 0, 0,   0, 0},
                {0, 0, 0,  0, 0, 0, 0,   0, 0},
                {0, 0, 0,  0, 0, 0, 0,   0, 0},
                {0, 0, 0,  0, 0, 0, 7,   8, 9}
        };
    }

    @Override
    public MenuConfiguration configuration() {
        return MenuConfiguration.builder()
                .fixedItems()
                .title(format(DARK_RED + "" + BOLD + "Empresa: %s", this.getState().getNombre()))
                .item(1, buildItemInfo())
                .item(2, buildItemEmpresaStats())
                .item(11, buildItemTransacciones(), goBackDeTransacciones())
                .item(3, buildItemAccionistas())
                .item(5, buildItemCerrarEmpresa(), this::abrirCerrarEmpresaConfirmacion)
                .item(4, buildItemRepartirDividendo(), this::repartirDividendos)
                .item(10, buildItemVotaciones(), this::openMenuVotaciones)
                .items(6, buildItemsEmpleados(), this::abrirMenuOpccionesEmpleado)
                .asyncTasks(AsyncTasksConfiguration.builder()
                        .onPageLoaded(6, this::mostrarCabezasJugadoresEmpleados)
                        .build())
                .breakpoint(7, MenuItems.GO_MENU_BACK, this::goBackToProfileMenu)
                .paginated(PaginationConfiguration.builder()
                        .forward(9, Material.GREEN_WOOL)
                        .backward(8, Material.RED_WOOL)
                        .build())
                .build();
    }

    private void mostrarCabezasJugadoresEmpleados(Page page, Integer integer, ItemStack itemEmpleado) {
        SkullMeta currentItemMeta = (SkullMeta) itemEmpleado.getItemMeta();
        String empleadoNombre = ItemUtils.getLore(itemEmpleado, 1).split(" ")[1];
        currentItemMeta.setOwningPlayer(Bukkit.getOfflinePlayer(empleadoNombre));

        itemEmpleado.setItemMeta(currentItemMeta);
    }

    private BiConsumer<Player, InventoryClickEvent> goBackDeTransacciones() {
        if (!puedeVerTransaccionesEmpresa()) {
            return (ignored1, ignored2) -> {};
        }

        return (p, e) -> menuService.open(p, VerTransaccionesMenu.class, VerTransaccionesMenu.State.builder()
                .entidadId(getState().getEmpresaId())
                .onBack(() -> menuService.open(p, MiEmpresaMenu.class, getState()))
                .nombre(getState().getNombre())
                .menuBackItem(ItemBuilder.of(Material.valueOf(getState().getLogotipo()))
                        .title(MenuItems.GO_BACK_TITLE + " " + getState().getNombre())
                        .build())
                .build());
    }

    private ItemStack buildItemTransacciones() {
        return puedeVerTransaccionesEmpresa() ? ItemBuilder.of(Material.BOOKSHELF)
                .title(MenuItems.CLICKEABLE + "Ver ultimas transacciones")
                .build() :
                ItemBuilder.of(Material.AIR).build();
    }

    private void openMenuVotaciones(Player player, InventoryClickEvent event) {
        if (puedeVerLasVotaciones()) {
            this.menuService.open(player, VerVotacionesEmpresaMenu.class, getState());
        }
    }

    private void repartirDividendos(Player player, InventoryClickEvent event) {
        if(puedeRepartirDividendos()){
            this.menuService.open(player, RepartirDividendosConfirmacionMenu.class, this.getState());
        }
    }

    private void abrirCerrarEmpresaConfirmacion(Player player, InventoryClickEvent event) {
        if(puedeCerrarEmpresa()){
            this.menuService.open(player, CerrarEmpresaConfirmacionMenu.class, getState());
        }
    }

    private void abrirMenuOpccionesEmpleado(Player player, InventoryClickEvent event) {
        if(puedeEditarEmpleadoYEmpresa()) {
            UUID empleadoId = MinecraftUtils.getLastLineOfLore(event.getCurrentItem(), 0);
            UUID empleadoJugadorId = MinecraftUtils.getLastLineOfLore(event.getCurrentItem(), 1);

            if(empleadoJugadorId.equals(getState().getDirectorJugadorId())){
                return;
            }

            Empleado empleado = empleadosService.getById(empleadoId);

            menuService.open(player, OpccionesEmpleadoMenu.class, empleado);
        }
    }

    private void goBackToProfileMenu(Player player, InventoryClickEvent event) {
        this.menuService.open(player, PerfilMenu.class);
    }

    private ItemStack buildItemVotaciones() {
        if (!puedeVerLasVotaciones()) {
            return ItemBuilder.of(Material.AIR).build();
        }

        int numeroVotacionesPendientes = (int) votacionesService.findByEmpresaId(getState().getEmpresaId()).stream()
                .filter(Votacion::estaAbierta)
                .count();
        boolean hayVotacionesPendienets = numeroVotacionesPendientes > 0;

        return puedeVerLasVotaciones() ?
                ItemBuilder.of(Material.WHITE_BANNER)
                        .title(hayVotacionesPendienets ? MenuItems.CLICKEABLE + "VER LAS VOTACIONES" : GOLD + "" + BOLD + "VOTACIONES")
                        .lore(hayVotacionesPendienets ? RED + "Hay "+numeroVotacionesPendientes+" votaciones pendientes" : GOLD + "No hay votaciones pendientes")
                        .build() :
                ItemBuilder.of(Material.AIR)
                        .build();
    }

    private List<ItemStack> buildItemsEmpleados() {
        return this.empleadosService.findEmpleoActivoByEmpresaId(this.getState().getEmpresaId()).stream()
                .map(this::buildEmpleadoItem)
                .toList();
    }

    private ItemStack buildEmpleadoItem(Empleado empleado) {
        return ItemBuilder.of(Material.PLAYER_HEAD)
                .title(empleado.getEmpleadoJugadorId().equals(getState().getDirectorJugadorId()) ?
                                DARK_RED + "" + BOLD  + "DIRECTOR DE LA EMPRESA" :
                                (puedeEditarEmpleadoYEmpresa() ? MenuItems.CLICKEABLE + "VER OPCCIONES" : GOLD + "Empleado"))
                .lore(buildEmpleadoItemLore(empleado))
                .build();
    }

    private List<String> buildEmpleadoItemLore(Empleado empleado) {
        List<String> lore = new ArrayList<>();
        lore.add("   ");
        lore.add(GOLD + "Empleado: " + jugadoresService.getNombreById(empleado.getEmpleadoJugadorId()));
        lore.add(GOLD + "Cargo: " + empleado.getDescripccion());

        if (puedeEditarEmpleadoYEmpresa()) {
            lore.addAll(List.of(
                    GOLD + "Sueldo: " + formatPixelcoins(empleado.getSueldo()) + "/ " + millisToDias(empleado.getPeriodoPagoMs()) + " dias",
                    GOLD + "Ultima fecha paga: " + Funciones.toString(empleado.getFechaUltimoPago()),
                    "   ",
                    "/empresas editarempleado " + getState().getNombre() + " " + jugadoresService.getNombreById(empleado.getEmpleadoJugadorId()),
                    " ",
                    empleado.getEmpleadoJugadorId().toString(),
                    empleado.getEmpleadoId().toString()
            ));
        }

        return lore;
    }

    private ItemStack buildItemCerrarEmpresa() {
        return puedeCerrarEmpresa() ?
                ItemBuilder.of(Material.BARRIER).title(MenuItems.CLICKEABLE + "CERRAR LA EMPRESA").build() :
                ItemBuilder.of(Material.AIR).build();
    }

    private ItemStack buildItemRepartirDividendo() {
        return puedeRepartirDividendos() ?
                ItemBuilder.of(Material.GOLD_INGOT)
                        .title(GOLD + "" + BOLD + "REPARTIR DIVIDENDOS")
                        .lore(GOLD + "Repartir pixelcoins por cada accion")
                        .build() :
                ItemBuilder.of(Material.AIR).build();
    }

    private ItemStack buildItemAccionistas() {
        if(!puedeVerAccionistas()){
            return ItemBuilder.of(Material.AIR).build();
        }

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
        String percentajeOwnership = Funciones.formatPorcentaje(accionista.porcentajeNTotalAcciones);

        return GOLD + accionista.nombreAccionista() + ": " + GREEN + percentajeOwnership;
    }

    private ItemStack buildItemEmpresaStats() {
        List<String> lore = new ArrayList<>();
        lore.add(GOLD + "Descripccion:");
        lore.add(1, GOLD + "" + getState().getDescripcion());
        lore.add("  ");
        lore.add(GOLD + "Pixelcoins: " + GREEN + formatPixelcoins(movimientosService.getBalance(getState().getEmpresaId())));
        lore.add(GOLD + "Fundador: " + jugadoresService.getNombreById(getState().getFundadorJugadorId()));
        lore.add(GOLD + "Director: " + jugadoresService.getNombreById(getState().getDirectorJugadorId()));
        lore.add(GOLD + "Nº Total acciones: " + getState().getNTotalAcciones());
        lore.add(GOLD + "Fecha creacion: " + Funciones.toString(getState().getFechaCreacion()));
        lore.add(GOLD + (getState().isEsCotizada() ? "Cotiza en bolsa" : "No cotiza en bolsa"));

        if (puedeEditarEmpleadoYEmpresa()) {
            lore.add(" ");
            lore.add(AQUA + "/empresas editar " +getState().getNombre()+ " nombre <nuevo nombre>");
            lore.add(AQUA + "/empresas editar " +getState().getNombre()+ " desc <nueva descripccion>");
            lore.add(AQUA + "/empresas logotipo " +getState().getNombre());
        }

        return ItemBuilder.of(Material.valueOf(getState().getLogotipo()))
                .title(GOLD + "" + BOLD + "" + getState().getNombre().toUpperCase())
                .lore(lore)
                .build();
    }

    private ItemStack buildItemInfo() {
        List<String> lore = switch (estadoEmpresaJugador) {
            case DIRECTOR_NO_COTIZADA -> buildLoreEmpresaDirectorNoCotizada();
            case DIRECTOR_COTIZADA -> buildLoreEmpresaCotizadaDirector();
            case NO_RELACION_O_ACCIONISTA_COTIZADA -> buildLoreEmpresaCotizada();
            case NO_RELACION_O_ACCIONISTA_NO_CORIZADA -> buildLoreEmpresaNoCotizada();
        };

        return ItemBuilder.of(Material.PAPER)
                .title(GOLD + "" + BOLD + "INFO")
                .lore(lore)
                .build();
    }

    private List<String> buildLoreEmpresaNoCotizada() {
        return List.of(
                GOLD + "Empresa no cotizada",
                "",
                AQUA + "/empresas pagar " + getState().getNombre()  + " <pixelcoins>",
                "",
                GOLD + "Para ver mas comandos "+AQUA+"/empresas ayuda"
        );
    }

    private List<String> buildLoreEmpresaCotizada() {
        return List.of(
                GOLD + "Eres el accionista de una empresa cotizada. En parte",
                GOLD + "eres propietario de la empresa. Puedes recibir dividendos",
                GOLD + "y decidir quien gestiona de la empresa a traves de las",
                GOLD + "votaciones donde participaran el resto de accionistas",
                AQUA + "/empresas votaciones " + getState().getNombre(),
                AQUA + "/empresas nuevodirector",
                "",
                GOLD + "Tambien puedes vender tus acciones en el mercado por PC:",
                AQUA + "/empresas misacciones",
                "",
                GOLD + "Para ver mas comandos "+AQUA+"/empresas ayuda"
        );
    }

    private List<String> buildLoreEmpresaCotizadaDirector() {
        return List.of(
                GOLD + "Eres el director de una empresa cotizada. Trabajas",
                GOLD + "para tus accionistas. Eres el encargado de gestionar",
                GOLD + "la empresa y de repartir dividendos",
                AQUA + "/empresas repartirdividendos "+getState().getNombre()+" <dividendo/accion>",
                AQUA + "/empresas emitiracciones "+getState().getNombre()+" <nºacciones nuevas> <precio/accion>",
                AQUA + "/empresas logotipo "+getState().getNombre(),
                AQUA + "/empresas contratar",
                AQUA + "/empresas despedir",
                "",
                GOLD + "La empresa al ser cotizada ya no puedes sacar",
                GOLD + "ni depositar pixelcoins por que ya no eres 100%",
                GOLD + "propietario",
                "",
                GOLD + "Para ver mas comandos "+AQUA+"/empresas ayuda"
        );
    }

    private List<String> buildLoreEmpresaDirectorNoCotizada() {
        return List.of(
                GOLD + "La empresa al no ser cotizada tienes todo el control:",
                AQUA + "/empresas depositar "+getState().getNombre()+ " <pixelcoins>",
                AQUA + "/empresas sacar "+getState().getNombre()+" <pixelcoins>",
                AQUA + "/empresas logotipo "+getState().getNombre(),
                AQUA + "/empresas contratar",
                AQUA + "/empresas despedir",
                "",
                GOLD + "Si la empresa necesita pixelcoins puedes vender acciones. Para ello:",
                AQUA + "/empresas ipo " + getState().getNombre() + " <numero accionones a vender> <precio/accion>",
                GOLD + "Por defecto una empresa tiene 100 acciones. La empresa al no ser",
                GOLD + "cotizada tienes las 100 acciones. "+AQUA+"/empresas misacciones",
                "",
                GOLD + "Para ver mas comandos "+AQUA+"/empresas ayuda"
        );
    }

    @Override
    public void beforeShow(Player player) {
        boolean esDirector = getState().getDirectorJugadorId().equals(player.getUniqueId());
        boolean esCotizada = getState().isEsCotizada();

        if (esDirector && esCotizada) {
            this.estadoEmpresaJugador = EstadoEmpresaJugador.DIRECTOR_COTIZADA;
        } else if (esDirector && !esCotizada) {
            this.estadoEmpresaJugador = EstadoEmpresaJugador.DIRECTOR_NO_COTIZADA;
        } else if (esCotizada) {
            this.estadoEmpresaJugador = EstadoEmpresaJugador.NO_RELACION_O_ACCIONISTA_COTIZADA;
        } else { //!esCotizada
            this.estadoEmpresaJugador = EstadoEmpresaJugador.NO_RELACION_O_ACCIONISTA_NO_CORIZADA;
        }
    }

    private boolean puedeVerTransaccionesEmpresa() {
        return switch (estadoEmpresaJugador) {
            case DIRECTOR_NO_COTIZADA, DIRECTOR_COTIZADA, NO_RELACION_O_ACCIONISTA_COTIZADA -> true;
            default -> false;
        };
    }

    private boolean puedeVerAccionistas() {
        return switch (estadoEmpresaJugador) {
            case DIRECTOR_COTIZADA, NO_RELACION_O_ACCIONISTA_COTIZADA -> true;
            default -> false;
        };
    }

    private boolean puedeEditarEmpleadoYEmpresa() {
        return switch (estadoEmpresaJugador) {
            case DIRECTOR_COTIZADA, DIRECTOR_NO_COTIZADA -> true;
            default -> false;
        };
    }

    private boolean puedeCerrarEmpresa() {
        return this.estadoEmpresaJugador == EstadoEmpresaJugador.DIRECTOR_NO_COTIZADA;
    }

    private boolean puedeRepartirDividendos() {
        return this.estadoEmpresaJugador == EstadoEmpresaJugador.DIRECTOR_COTIZADA;
    }

    private boolean puedeVerLasVotaciones() {
        return switch (estadoEmpresaJugador) {
            case NO_RELACION_O_ACCIONISTA_COTIZADA, DIRECTOR_COTIZADA -> true;
            default -> false;
        };
    }

    enum EstadoEmpresaJugador {
        DIRECTOR_COTIZADA,
        DIRECTOR_NO_COTIZADA,
        NO_RELACION_O_ACCIONISTA_COTIZADA,
        NO_RELACION_O_ACCIONISTA_NO_CORIZADA,
    }
}
