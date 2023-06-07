package es.serversurvival.minecraftserver.empresas.mercado;

import es.bukkitbettermenus.MenuService;
import es.bukkitbettermenus.modules.sync.SyncMenuService;
import es.bukkitclassmapper._shared.utils.ItemBuilder;
import es.serversurvival.pixelcoins.mercado._shared.OfertasService;
import es.serversurvival.pixelcoins.mercado.comprar.ComprarOfertaUseCase;
import es.serversurvival.pixelcoins.mercado.retirar.RetirarOfertaUseCase;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.minecraftserver._shared.VerOfertasMercadoMenu;
import es.serversurvival.pixelcoins.empresas._shared.accionistas.OfertaAccionMercado;
import es.serversurvival.pixelcoins.empresas._shared.empresas.Empresa;
import es.serversurvival.pixelcoins.empresas._shared.empresas.EmpresasService;
import es.serversurvival.pixelcoins.jugadores._shared.jugadores.Jugador;
import es.serversurvival.pixelcoins.jugadores._shared.jugadores.JugadoresService;
import es.serversurvival.pixelcoins.mercado._shared.TipoOferta;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Optional;

import static org.bukkit.ChatColor.*;

public final class MercadoAccionesEmpresasMenu extends VerOfertasMercadoMenu<OfertaAccionMercado> {
    private final JugadoresService jugadoresService;
    private final EmpresasService empresasService;

    public MercadoAccionesEmpresasMenu(ComprarOfertaUseCase comprarOfertaUseCase, RetirarOfertaUseCase retirarOfertaUseCase,
                                       SyncMenuService syncMenuService, OfertasService ofertasService, MenuService menuService,
                                       JugadoresService jugadoresService, EmpresasService empresasService) {
        super(comprarOfertaUseCase, retirarOfertaUseCase, syncMenuService, ofertasService, menuService);
        this.jugadoresService = jugadoresService;
        this.empresasService = empresasService;
    }

    @Override
    public TipoOferta[] tipoOfertas() {
        return new TipoOferta[]{TipoOferta.ACCIONES_SERVER_JUGADOR, TipoOferta.ACCIONES_SERVER_EMISION};
    }

    @Override
    public Class<OfertaAccionMercado> ofertaClass() {
        return OfertaAccionMercado.class;
    }

    @Override
    public String titulo() {
        return DARK_RED + "" + BOLD + "ACCIONES EMPRESAS";
    }

    @Override
    public ItemStack buildItemFromOferta(OfertaAccionMercado oferta) {
        return ItemBuilder.of(Material.KNOWLEDGE_BOOK)
                .title(buildItemOfertaTitle(oferta))
                .lore(buildItemOfertaLore(oferta))
                .build();
    }

    private List<String> buildItemOfertaLore(OfertaAccionMercado oferta) {
        return List.of(
                GOLD + "Empresa: " + empresasService.getById(oferta.getEmpresaId()).getNombre(),
                GOLD + "Precio: " + GREEN + Funciones.FORMATEA.format(oferta.getPrecio()) + " PC",
                GOLD + "Cantidad: " + oferta.getCantidad(),
                GOLD + "Vendedor: " + getVendedorNombre(oferta)
        );
    }

    private String getVendedorNombre(OfertaAccionMercado oferta) {
        Optional<Jugador> optionalJugador = jugadoresService.findById(oferta.getVendedorId());
        return oferta.getTipoOferta() == TipoOferta.ACCIONES_SERVER_EMISION || optionalJugador.isEmpty() ?
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
                GREEN + Funciones.FORMATEA.format(oferta.getPrecio()) + " PC";
    }

    @Override
    public String mensajeCompraExsitosaAlVendedor(OfertaAccionMercado oferta, ItemStack item, String comprador) {
        return GOLD + comprador + " te ha comprado 1 accion de " + empresasService.getById(oferta.getEmpresaId()).getNombre() + " por " +
                GREEN + Funciones.FORMATEA.format(oferta.getPrecio()) + " PC";
    }

    @Override
    public List<String> loreItemInfo() {
        return null; //TODO
    }
}
