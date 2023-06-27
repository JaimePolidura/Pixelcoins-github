package es.serversurvival.minecraftserver.deudas.verdeudasmercado;

import es.bukkitbettermenus.MenuService;
import es.bukkitbettermenus.modules.sync.SyncMenuService;
import es.bukkitclassmapper._shared.utils.ItemBuilder;
import es.serversurvival.minecraftserver._shared.MinecraftUtils;
import es.serversurvival.minecraftserver._shared.VerOfertasMercadoMenu;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseBus;
import es.serversurvival.pixelcoins.deudas._shared.*;
import es.serversurvival.pixelcoins.deudas._shared.application.DeudasService;
import es.serversurvival.pixelcoins.deudas._shared.domain.Deuda;
import es.serversurvival.pixelcoins.mercado._shared.OfertasService;
import es.serversurvival.pixelcoins.mercado._shared.TipoOferta;
import es.serversurvival.minecraftserver.deudas._shared.DeudaItemLore;
import es.serversurvival.pixelcoins.jugadores._shared.jugadores.JugadoresService;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.*;

import static es.serversurvival._shared.utils.Funciones.FORMATEA;
import static org.bukkit.ChatColor.*;

public final class MercadoDeudaMenu extends VerOfertasMercadoMenu<OfertaDeudaMercado> {
    private final DeudaItemLore deudaItemMercadoLore;
    private final JugadoresService jugadoresService;
    private final DeudasService deudasService;

    public MercadoDeudaMenu(SyncMenuService syncMenuService, OfertasService ofertasService, MenuService menuService,
                            UseCaseBus useCaseBus, DeudaItemLore deudaItemMercadoLore, JugadoresService jugadoresService,
                            DeudasService deudasService) {
        super(syncMenuService, ofertasService, menuService, useCaseBus);
        this.deudaItemMercadoLore = deudaItemMercadoLore;
        this.jugadoresService = jugadoresService;
        this.deudasService = deudasService;
    }


    @Override
    public TipoOferta[] tipoOfertas() {
        return new TipoOferta[]{TipoOferta.DEUDA_MERCADO_PRIMARIO, TipoOferta.DEUDA_MERCADO_SECUNDARIO};
    }

    @Override
    public Class<OfertaDeudaMercado> ofertaClass() {
        return OfertaDeudaMercado.class;
    }

    @Override
    public String titulo() {
        return ChatColor.DARK_RED + "" + ChatColor.BOLD + "     MERCADO DE DEUDA";
    }

    @Override
    public List<String> loreItemInfo() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public ItemStack buildItemFromOferta(OfertaDeudaMercado oferta) {
        ItemStack itemStack = ItemBuilder.of(oferta.getVendedorId().equals(getPlayer().getUniqueId()) ?
                        Material.GREEN_BANNER :
                        Material.BLUE_BANNER)
                .build();
        String vendedor = jugadoresService.getNombreById(oferta.getVendedorId());
        List<String> lore =  oferta.esMercadoPrimario() ?
                buildLoreMercadoPrimario(vendedor, (OfertaDeudaMercadoPrimario) oferta) :
                buildLoreMercadoSecundario(vendedor, (OfertaDeudaMercadoSecundario) oferta);
        MinecraftUtils.setLore(itemStack, lore);

        return itemStack;
    }

    private List<String> buildLoreMercadoSecundario(String vendedor, OfertaDeudaMercadoSecundario oferta) {
        Deuda deuda = deudasService.getById(oferta.getObjetoToUUID());
        return deudaItemMercadoLore.buildOfertaDeudaMercado(oferta.getPrecio(), vendedor, deuda.getInteres(), deuda.getNominal(), deuda.getPeriodoPagoCuotaMs(),
                deuda.getNCuotasTotales() - deuda.getNCuotasPagadas(), deuda.getNCuotasImpagadas(), deuda.getPixelcoinsRestantesDePagar(), false,
                jugadoresService.getNombreById(deuda.getDeudorJugadorId()));
    }

    private List<String> buildLoreMercadoPrimario(String vendedor, OfertaDeudaMercadoPrimario oferta) {
        double cuota = oferta.getInteres() * oferta.getPrecio();

        return deudaItemMercadoLore.buildOfertaDeudaMercado(oferta.getPrecio(), vendedor, oferta.getInteres(), oferta.getPrecio(), oferta.getPeriodoPagoCuotaMs(),
                oferta.getNCuotasTotales(), 0, oferta.getNCuotasTotales() * cuota + oferta.getPrecio(), true,
                vendedor);
    }

    @Override
    public String mensajeCompraExsitosaAlComprador(OfertaDeudaMercado oferta, ItemStack item) {
        String vendedor = jugadoresService.getNombreById(oferta.getVendedorId());

        return GOLD + "Has comprado la deuda de " + vendedor + " por " + GREEN + FORMATEA.format(oferta.getPrecio()) + " PC";
    }

    @Override
    public String mensajeCompraExsitosaAlVendedor(OfertaDeudaMercado oferta, ItemStack item, String comprador) {
        return GOLD + comprador + " ha comprado la deuda por " + GREEN + FORMATEA.format(oferta.getPrecio()) + " PC";
    }

    @Override
    public String mensajeRetiradoExistoso(OfertaDeudaMercado oferta, ItemStack item) {
        return GOLD + "Has retirado la deuda del mercado";
    }
}
