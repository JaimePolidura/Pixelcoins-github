package es.serversurvival.v1._shared.utils;

import es.serversurvival.v1.tienda._shared.domain.EncantamientoObjecto;
import es.serversurvival.v1.tienda._shared.domain.TiendaObjeto;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public final class ItemsUtils {
    private ItemsUtils () {}

    public static ItemStack getItemStakcByTiendaObjeto(TiendaObjeto tiendaObjeto){
        ItemStack itemToConvert = new ItemStack(Material.getMaterial(tiendaObjeto.getObjeto()), tiendaObjeto.getCantidad());
        itemToConvert.setDurability((short) tiendaObjeto.getDurabilidad());

        if(tiendaObjeto.getObjeto().equalsIgnoreCase("ENCHANTED_BOOK")){
            rellenarEncantamientoLibro(itemToConvert, tiendaObjeto.getEncantamientos());
        }else{
            rellenarEncantamiento(itemToConvert, tiendaObjeto.getEncantamientos());
        }

        return itemToConvert;
    }

    //TODO Fix duplicated code
    private static void rellenarEncantamientoLibro (ItemStack itemToConvert, List<EncantamientoObjecto> encantamientos) {
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) itemToConvert.getItemMeta();

        for(EncantamientoObjecto encantamiento : encantamientos){
            Enchantment enchantMentToPut = Enchantment.getByName(encantamiento.getNombre());
            int level = encantamiento.getNivel();

            meta.addStoredEnchant(enchantMentToPut, level, true);
        }

        itemToConvert.setItemMeta(meta);
    }

    private static void rellenarEncantamiento (ItemStack itemToConvert, List<EncantamientoObjecto> encantamientos) {
        ItemMeta itemMeta = itemToConvert.getItemMeta();
        for(EncantamientoObjecto encantamiento : encantamientos){
            Enchantment encantamientoAPoner = Enchantment.getByName(encantamiento.getNombre());
            int nivel = encantamiento.getNivel();

            itemMeta.addEnchant(encantamientoAPoner, nivel, true);
        }

        itemToConvert.setItemMeta(itemMeta);
    }
}
