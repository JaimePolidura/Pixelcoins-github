package es.serversurvival.minecraftserver.deudas.verdeudasmercado;

import es.bukkitbettermenus.MenuService;
import es.bukkitbettermenus.modules.sync.SyncMenuService;
import es.bukkitclassmapper._shared.utils.ItemBuilder;
import es.serversurvival.minecraftserver._shared.MinecraftUtils;
import es.serversurvival.minecraftserver._shared.VerOfertasMercadoMenu;
import es.serversurvival.pixelcoins.deudas._shared.*;
import es.serversurvival.pixelcoins.mercado._shared.OfertasService;
import es.serversurvival.pixelcoins.mercado._shared.TipoOferta;
import es.serversurvival.pixelcoins.mercado.comprar.ComprarOfertaUseCase;
import es.serversurvival.pixelcoins.mercado.retirar.RetirarOfertaUseCase;
import es.serversurvival.minecraftserver.deudas._shared.DeudaItemMercadoLore;
import es.serversurvival.v2.pixelcoins.deudas._shared.*;
import es.serversurvival.pixelcoins.jugadores._shared.jugadores.JugadoresService;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.*;

import static es.serversurvival._shared.utils.Funciones.FORMATEA;
import static org.bukkit.ChatColor.*;

public final class MercadoDeudaMenu extends VerOfertasMercadoMenu<OfertaDeudaMercado> {
    private final DeudaItemMercadoLore deudaItemMercadoLore;
    private final JugadoresService jugadoresService;
    private final DeudasService deudasService;

    public MercadoDeudaMenu(ComprarOfertaUseCase comprarOfertaUseCase, RetirarOfertaUseCase retirarOfertaUseCase, SyncMenuService syncMenuService,
                            OfertasService ofertasService, MenuService menuService, DeudaItemMercadoLore deudaItemMercadoLore, JugadoresService jugadoresService,
                            DeudasService deudasService) {
        super(comprarOfertaUseCase, retirarOfertaUseCase, syncMenuService, ofertasService, menuService);
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
        return ChatColor.DARK_RED + "" + ChatColor.BOLD + "            Tienda";
    }

    @Override
    public List<String> loreItemInfo() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public ItemStack buildItemFromOferta(OfertaDeudaMercado oferta) {
        return oferta.esMercadoPrimario() ?
                buildItemMercadoPrimario((OfertaDeudaMercadoPrimario) oferta) :
                buildItemMercadoSecundario((OfertaDeudaMercadoSecundario) oferta);
    }

    @Override
    public String mensajeCompraExsitosaAlComprador(OfertaDeudaMercado oferta, ItemStack item) {
        String vendedor = jugadoresService.getNombreById(oferta.getVendedorId());

        return GOLD + "Has comprado la deuda de " + vendedor + " por " + GREEN + FORMATEA.format(oferta.getPrecio()) +  "PC";
    }

    @Override
    public String mensajeCompraExsitosaAlVendedor(OfertaDeudaMercado oferta, ItemStack item, String comprador) {
        return GOLD + comprador + " ha comprado la deuda por " + GREEN + FORMATEA.format(oferta.getPrecio()) +  "PC";
    }

    //TODO Quitar codigo dueplicado
    private ItemStack buildItemMercadoSecundario(OfertaDeudaMercadoSecundario oferta) {
        ItemStack itemStack = ItemBuilder.of(Material.WRITTEN_BOOK).build();
        String vendedor = jugadoresService.getNombreById(oferta.getVendedorId());
        Deuda deuda = deudasService.getById(oferta.getObjetoToUUID());

        List<String> lore =  deudaItemMercadoLore.buildOfertaDeudaMercado(oferta.getPrecio(), vendedor, deuda.getInteres(), deuda.getNominal(), deuda.getPeriodoPagoCuotaMs(),
                deuda.getNCuotasTotales() - deuda.getNCuotasPagadas(), deuda.getNCuotasImpagadas(), deuda.getPixelcoinsRestantesDePagar());

        MinecraftUtils.setLore(itemStack, lore);

        return itemStack;
    }

    private ItemStack buildItemMercadoPrimario(OfertaDeudaMercadoPrimario oferta) {
        ItemStack itemStack = ItemBuilder.of(Material.WRITTEN_BOOK).build();
        String vendedor = jugadoresService.getNombreById(oferta.getVendedorId());
        double cuota = oferta.getInteres() * oferta.getPrecio();

        List<String> lore = deudaItemMercadoLore.buildOfertaDeudaMercado(oferta.getPrecio(), vendedor, oferta.getInteres(), oferta.getPrecio(), oferta.getPeriodoPagoCuotaMs(),
                oferta.getNumeroCuotasTotales(), 0, oferta.getNumeroCuotasTotales() * cuota + oferta.getPrecio());

        MinecraftUtils.setLore(itemStack, lore);

        return itemStack;
    }
}
