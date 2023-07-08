package es.serversurvival.minecraftserver.empresas.mercado;

import es.bukkitbettermenus.MenuService;
import es.bukkitbettermenus.modules.sync.SyncMenuService;
import es.bukkitbettermenus.utils.ItemBuilder;
import es.serversurvival.minecraftserver._shared.menus.MenuItems;
import es.serversurvival.minecraftserver.empresas.vertodas.VerTodasEmpresasMenu;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseBus;
import es.serversurvival.pixelcoins.mensajes._shared.application.EnviadorMensajes;
import es.serversurvival.pixelcoins.mercado._shared.OfertasService;
import es.serversurvival.minecraftserver._shared.VerOfertasMercadoMenu;
import es.serversurvival.pixelcoins.empresas._shared.accionistas.OfertaAccionMercado;
import es.serversurvival.pixelcoins.empresas._shared.empresas.domain.Empresa;
import es.serversurvival.pixelcoins.empresas._shared.empresas.application.EmpresasService;
import es.serversurvival.pixelcoins.jugadores._shared.jugadores.Jugador;
import es.serversurvival.pixelcoins.jugadores._shared.jugadores.JugadoresService;
import es.serversurvival.pixelcoins.mercado._shared.TipoOferta;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Optional;

import static es.serversurvival._shared.utils.Funciones.formatPixelcoins;
import static es.serversurvival.minecraftserver._shared.menus.MenuItems.CLICKEABLE;
import static org.bukkit.ChatColor.*;

public final class MercadoAccionesEmpresasMenu extends VerOfertasMercadoMenu<OfertaAccionMercado> {
    private final JugadoresService jugadoresService;
    private final EmpresasService empresasService;

    public MercadoAccionesEmpresasMenu(EnviadorMensajes enviadorMensajes, SyncMenuService syncMenuService, OfertasService ofertasService,
                                       MenuService menuService, UseCaseBus useCaseBus, JugadoresService jugadoresService, EmpresasService empresasService) {
        super(enviadorMensajes, syncMenuService, ofertasService, menuService, useCaseBus);
        this.jugadoresService = jugadoresService;
        this.empresasService = empresasService;
    }


    @Override
    public TipoOferta[] tipoOfertas() {
        return new TipoOferta[]{TipoOferta.ACCIONES_SERVER_JUGADOR, TipoOferta.ACCIONES_SERVER_EMISION, TipoOferta.ACCIONES_SERVER_IPO};
    }

    @Override
    public Class<OfertaAccionMercado> ofertaClass() {
        return OfertaAccionMercado.class;
    }

    @Override
    public String titulo() {
        return DARK_RED + "" + BOLD + "MERCADO ACCIONES EMPRESA";
    }

    @Override
    public ItemStack buildItemFromOferta(OfertaAccionMercado oferta) {
        return ItemBuilder.of(Material.CREEPER_BANNER_PATTERN)
                .title(buildItemOfertaTitle(oferta))
                .lore(buildItemOfertaLore(oferta))
                .build();
    }

    private List<String> buildItemOfertaLore(OfertaAccionMercado oferta) {
        return List.of(
                GOLD + "Empresa: " + empresasService.getById(oferta.getEmpresaId()).getNombre(),
                GOLD + "Precio: " + formatPixelcoins(oferta.getPrecio()),
                GOLD + "Cantidad: " + oferta.getCantidad(),
                GOLD + "Vendedor: " + getVendedorNombre(oferta)
        );
    }

    private String getVendedorNombre(OfertaAccionMercado oferta) {
        Optional<Jugador> optionalJugador = jugadoresService.findById(oferta.getVendedorId());
        return oferta.getTipo() == TipoOferta.ACCIONES_SERVER_EMISION || optionalJugador.isEmpty() ?
                "La propia empresa" :
                optionalJugador.get().getNombre();
    }

    private String buildItemOfertaTitle(OfertaAccionMercado oferta) {
        Empresa empresa = empresasService.getById(oferta.getEmpresaId());

        return oferta.getVendedorId().equals(getPlayer().getUniqueId()) || empresa.getDirectorJugadorId().equals(getPlayer().getUniqueId()) ?
                VerOfertasMercadoMenu.PROPIETARIO_OFERTA_ITEM_DISPLAYNAME :
                VerOfertasMercadoMenu.NO_PROPIETARIO_OFERTA_DISPLAYNAME;
    }



    @Override
    public String mensajeCompraExsitosaAlComprador(OfertaAccionMercado oferta, ItemStack item) {
        return GOLD + "Has comprado 1 accion de " + empresasService.getById(oferta.getEmpresaId()).getNombre() + " por " +
                formatPixelcoins(oferta.getPrecio());
    }

    @Override
    public String mensajeCompraExsitosaAlVendedor(OfertaAccionMercado oferta, ItemStack item, String comprador) {
        return GOLD + comprador + " te ha comprado 1 accion de " + empresasService.getById(oferta.getEmpresaId()).getNombre() + " por " +
                formatPixelcoins(oferta.getPrecio());
    }

    @Override
    public String mensajeRetiradoExistoso(OfertaAccionMercado oferta, ItemStack item) {
        return GOLD + "Lo has retirado del mercado";
    }

    @Override
    public List<String> loreItemInfo() {
        return List.of(
                GOLD + "Puedes comprar y vender acciones de empresas del servidor",
                GOLD + "Esto te converte en parte propietario de la empresa. Por lo",
                GOLD + "Cual puedes recibir dividendes, decidir quien gestionala la",
                GOLD + "empresa etc..",
                GOLD + "",
                GOLD + "Para comprar/vender acciones:",
                AQUA + "/empresas misacciones",
                AQUA + "/empresas mercado",
                GOLD + "",
                GOLD + "Si quieres vender acciones de tu empresa: ",
                AQUA + "/empresas ipo <nombre empresa> <nº acciones a vender>",
                AQUA + "   <precio/accion>",
                GOLD + "",
                GOLD + "Para mas comandos: ",
                AQUA + "/empresas ayuda"
        );
    }

    @Override
    protected ItemStack buildOptionalItem1() {
        return ItemBuilder.of(Material.NETHERITE_SCRAP)
                .title(CLICKEABLE + "VER TODAS LAS EMPRESAS")
                .build();
    }

    @Override
    protected void onClickOptionalItem1(Player player, InventoryClickEvent event) {
        menuService.open(player, VerTodasEmpresasMenu.class);
    }

    @Override
    protected ItemStack buildOptionalItem2() {
        return ItemBuilder.of(Material.CHEST)
                .title(MenuItems.CLICKEABLE + "VER MERCADO DE EMPRESAS SERVER")
                .build();
    }

    @Override
    protected void onClickOptionalItem2(Player player, InventoryClickEvent event) {
        menuService.open(player, MercadoAccionesEmpresasMenu.class);
    }
}
