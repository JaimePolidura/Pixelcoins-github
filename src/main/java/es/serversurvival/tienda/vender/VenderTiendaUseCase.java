package es.serversurvival.tienda.vender;

import es.bukkitbettermenus.MenuService;
import es.bukkitbettermenus.modules.sync.SyncMenuService;
import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.jaime.javaddd.domain.exceptions.IllegalQuantity;
import es.jaime.javaddd.domain.exceptions.IllegalType;
import es.serversurvival.Pixelcoin;
import es.serversurvival.tienda._shared.application.TiendaService;
import es.serversurvival.tienda._shared.domain.EncantamientoObjecto;
import es.serversurvival.tienda._shared.domain.TiendaObjeto;
import es.serversurvival.tienda.vertienda.TiendaMenu;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@UseCase
@AllArgsConstructor
public final class VenderTiendaUseCase {
    private static final List<String> bannedItems = Arrays.asList("POTION", "BANNER", "SPLASH_POTION", "LINGERING_POTION", "AIR");
    private static final int MAX_ITEMS_PER_PLAYER = 5;

    private final SyncMenuService syncMenuService;
    private final TiendaService tiendaService;
    private final MenuService menuService;
    private final EventBus eventBus;

    public TiendaObjeto crearOferta(String nombreJugador, ItemStack itemAVender, double precio) {
        this.ensureCorrectFormatPixelcoins(precio);
        this.ensureItemNotBanned(itemAVender);
        this.ensureHasItemsToUpload(nombreJugador);

        var tiendaObjeto = this.tiendaService.save(
                nombreJugador, itemAVender.getType().toString(), itemAVender.getAmount(), precio,
                itemAVender.getDurability(), getEncantamientosDeItem(itemAVender)
        );

        var newMenu = this.menuService.buildMenu(Bukkit.getPlayer(nombreJugador), TiendaMenu.class);
        this.syncMenuService.sync(newMenu);

        this.eventBus.publish(new NuevoItemTienda(tiendaObjeto.getTiendaObjetoId(), nombreJugador, itemAVender));

        return tiendaObjeto;
    }

    private void ensureCorrectFormatPixelcoins(double precio){
        if(precio <= 0)
            throw new IllegalQuantity("Las pixelcoin deben ser numeros naturales");
    }

    private void ensureItemNotBanned(ItemStack itemStack){
        if(bannedItems.contains(itemStack.getType().toString()))
            throw new IllegalType("Los items " + bannedItems + " no se pueden subir a la tienda");
    }

    private void ensureHasItemsToUpload(String nombreJugador){
        if(tiendaService.findByJugador(nombreJugador).size() + 1 >= MAX_ITEMS_PER_PLAYER)
            throw new IllegalQuantity("No puedes subir mas items a la tienda");
    }

    private List<EncantamientoObjecto> getEncantamientosDeItem (ItemStack item) {
        Map<Enchantment, Integer> enchantmentsBukkitTypes = item.getType() == Material.ENCHANTED_BOOK ?
                ((EnchantmentStorageMeta) item.getItemMeta()).getStoredEnchants() :
                item.getEnchantments();

        return enchantmentsBukkitTypes.keySet().stream()
                .map(e -> new EncantamientoObjecto(e.getName(), enchantmentsBukkitTypes.get(e)))
                .toList();
    }
}
