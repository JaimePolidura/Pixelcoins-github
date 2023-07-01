package es.serversurvival.minecraftserver.tienda.vertienda;

import es.bukkitbettermenus.MenuService;
import es.bukkitbettermenus.modules.sync.SyncMenuService;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.minecraftserver._shared.MinecraftUtils;
import es.serversurvival.minecraftserver._shared.VerOfertasMercadoMenu;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseBus;
import es.serversurvival.pixelcoins.jugadores._shared.jugadores.JugadoresService;
import es.serversurvival.pixelcoins.mercado._shared.OfertasService;
import es.serversurvival.pixelcoins.mercado._shared.TipoOferta;
import es.serversurvival.pixelcoins.tienda._shared.OfertaTiendaItemMinecraft;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static es.serversurvival._shared.utils.Funciones.formatPixelcoins;
import static org.bukkit.ChatColor.*;

public final class TiendaMenu extends VerOfertasMercadoMenu<OfertaTiendaItemMinecraft> {
    private final JugadoresService jugadoresService;

    public TiendaMenu(SyncMenuService syncMenuService, OfertasService ofertasService, MenuService menuService,
                      UseCaseBus useCaseBus, JugadoresService jugadoresService) {
        super(syncMenuService, ofertasService, menuService, useCaseBus);
        this.jugadoresService = jugadoresService;
    }

    @Override
    public TipoOferta[] tipoOfertas() {
        return new TipoOferta[]{TipoOferta.TIENDA_ITEM_MINECRAFT};
    }

    @Override
    public Class<OfertaTiendaItemMinecraft> ofertaClass() {
        return OfertaTiendaItemMinecraft.class;
    }

    @Override
    public String titulo() {
        return ChatColor.DARK_RED + "" + ChatColor.BOLD + "            Tienda";
    }

    @Override
    public List<String> loreItemInfo() {
        return List.of(
                GOLD + "Puedes vender objetos de minecraft en la tienda. Para ello:",
                GOLD + "    1º Selecciona un item en la mano",
                GOLD + "    2º Pon el comando " + AQUA + "/tienda vender <precio/objeto>",
                "",
                GOLD + "Para mas ayuda: " + AQUA + "/tienda ayuda"
        );
    }

    @Override
    public ItemStack buildItemFromOferta(OfertaTiendaItemMinecraft oferta) {
        return MinecraftUtils.setLore(oferta.toItemStack(), List.of(
                GOLD + "Vendedor: " + jugadoresService.getNombreById(oferta.getVendedorId()),
                GOLD + "Precio: " + formatPixelcoins(oferta.getPrecio()) + "/ Unidad",
                GOLD + "Fecha subida: " + Funciones.toString(oferta.getFechaSubida())
        ));
    }

    @Override
    public String mensajeCompraExsitosaAlComprador(OfertaTiendaItemMinecraft oferta, ItemStack item) {
        return GOLD + "Has comprado " + item.getType() + " por " + formatPixelcoins(oferta.getPrecio());
    }

    @Override
    public String mensajeCompraExsitosaAlVendedor(OfertaTiendaItemMinecraft oferta, ItemStack item, String comprador) {
        return GOLD + comprador + " te ha comprado 1 de " + item.getType() + " por " + formatPixelcoins(oferta.getPrecio());
    }

    @Override
    public String mensajeRetiradoExistoso(OfertaTiendaItemMinecraft oferta, ItemStack item) {
        return GOLD + "Has retirado " + item.getAmount() + " de " + item.getType().name();
    }
}
