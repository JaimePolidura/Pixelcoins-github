package es.serversurvival.v2.pixelcoins.tienda._shared;

import es.serversurvival.v2.pixelcoins.mercado._shared.Oferta;
import es.serversurvival.v2.pixelcoins.mercado._shared.TipoOferta;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class OfertaTiendaItemMinecraftMercado extends Oferta {
    @Getter private final List<TiendaItemMinecraftEncantamientos> encantamientos;
    @Getter private final int durabilidad;
    @Getter private final boolean tieneNombre;
    @Getter private final String nombre;

    public OfertaTiendaItemMinecraftMercado(UUID ofertaId, UUID vendedorId, LocalDateTime fechaSubida, int cantidad, double precio, String objeto,
                                            TipoOferta tipoOferta, List<TiendaItemMinecraftEncantamientos> encantamientos, int durabilidad, boolean tieneNombre, String nombre) {
        super(ofertaId, vendedorId, fechaSubida, cantidad, precio, objeto, tipoOferta);
        this.encantamientos = encantamientos;
        this.durabilidad = durabilidad;
        this.tieneNombre = tieneNombre;
        this.nombre = nombre;
    }

    public static OfertaItemTiendaMercadoBuilder builder() {
        return new OfertaItemTiendaMercadoBuilder();
    }

    public static class OfertaItemTiendaMercadoBuilder extends Oferta.AbstractOfertaBuilder<OfertaItemTiendaMercadoBuilder> {
        private List<TiendaItemMinecraftEncantamientos> encantamientos;
        private boolean tieneNombre;
        private int durabilidad;
        private String nombre;

        public OfertaItemTiendaMercadoBuilder item(ItemStack itemStack) {
            this.tieneNombre = itemStack.getItemMeta().hasDisplayName();
            this.nombre = itemStack.getItemMeta().getDisplayName();
            this.durabilidad = itemStack.getDurability();
            this.encantamientos = getEncantamientosDeItem(itemStack);
            this.objeto = itemStack.getType().toString();
            this.cantidad = itemStack.getAmount();

            return this;
        }

        private List<TiendaItemMinecraftEncantamientos> getEncantamientosDeItem (ItemStack item) {
            Map<Enchantment, Integer> enchantmentsBukkitTypes = item.getType() == Material.ENCHANTED_BOOK ?
                    ((EnchantmentStorageMeta) item.getItemMeta()).getStoredEnchants() :
                    item.getEnchantments();

            return enchantmentsBukkitTypes.keySet().stream()
                    .map(e -> new TiendaItemMinecraftEncantamientos(e.getName(), enchantmentsBukkitTypes.get(e)))
                    .toList();
        }

        @Override
        public OfertaTiendaItemMinecraftMercado build() {
            return new OfertaTiendaItemMinecraftMercado(ofertaId, vendedorId, fechaSubida, cantidad, precio, objeto, tipoOferta,
                    encantamientos, durabilidad, tieneNombre, nombre);
        }
    }
}
