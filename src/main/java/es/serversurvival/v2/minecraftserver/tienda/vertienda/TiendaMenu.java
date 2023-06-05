package es.serversurvival.v2.minecraftserver.tienda.vertienda;

import es.bukkitbettermenus.MenuService;
import es.bukkitbettermenus.modules.sync.SyncMenuService;
import es.serversurvival.v2.minecraftserver._shared.VerOfertasMercadoMenu;
import es.serversurvival.v2.pixelcoins.mercado._shared.OfertasService;
import es.serversurvival.v2.pixelcoins.mercado._shared.TipoOferta;
import es.serversurvival.v2.pixelcoins.mercado.comprar.ComprarOfertaUseCase;
import es.serversurvival.v2.pixelcoins.mercado.retirar.RetirarOfertaUseCase;
import es.serversurvival.v2.pixelcoins.tienda._shared.OfertaTiendaItemMinecraft;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static es.serversurvival.v1._shared.utils.Funciones.FORMATEA;
import static org.bukkit.ChatColor.*;

public final class TiendaMenu extends VerOfertasMercadoMenu<OfertaTiendaItemMinecraft> {
    public TiendaMenu(ComprarOfertaUseCase comprarOfertaUseCase, RetirarOfertaUseCase retirarOfertaUseCase,
                      SyncMenuService syncMenuService, OfertasService ofertasService, MenuService menuService) {
        super(comprarOfertaUseCase, retirarOfertaUseCase, syncMenuService, ofertasService, menuService);
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
                "Para vender un item: 1) selecciona",
                "el item con la mano y 2) pon el comando:",
                "/tienda vender <precio>"
        );
    }

    @Override
    public ItemStack buildItemFromOferta(OfertaTiendaItemMinecraft oferta) {
        return oferta.toItemStack();
    }

    @Override
    public String mensajeCompraExsitosaAlComprador(OfertaTiendaItemMinecraft oferta, ItemStack item) {
        return GOLD + "Has comprado " + item.getType() + " por " + GREEN + "" +
                FORMATEA.format(oferta.getPrecio()) + "PC";
    }

    @Override
    public String mensajeCompraExsitosaAlVendedor(OfertaTiendaItemMinecraft oferta, ItemStack item, String comprador) {
        return GOLD + comprador + " te ha comprado 1 de " + item.getType() +
                " por " + GREEN + FORMATEA.format(oferta.getPrecio()) + " PC";
    }
}
