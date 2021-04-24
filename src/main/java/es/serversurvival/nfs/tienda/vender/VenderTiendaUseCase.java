package es.serversurvival.nfs.tienda.vender;

import es.serversurvival.legacy.mySQL.AllMySQLTablesInstances;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.util.Map;


public final class VenderTiendaUseCase implements AllMySQLTablesInstances {

    public void crearOferta(ItemStack itemAVender, Player player, double precio) {
        Inventory inventarioJugador = player.getInventory();
        String nombreJugador = player.getName();

        int idOfetaNueva = ofertasMySQL.nuevaOferta(nombreJugador, itemAVender.getType().toString(), itemAVender.getAmount(), precio, itemAVender.getDurability());

        Map<Enchantment, Integer> encantamientos = getEncantamientosDeItem(itemAVender);
        encantamientosMySQL.insertarEncantamientosDeItem(encantamientos, idOfetaNueva);
    }

    private Map<Enchantment, Integer> getEncantamientosDeItem (ItemStack item) {
        if (item.getType() == Material.ENCHANTED_BOOK) {
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
            return meta.getStoredEnchants();
        } else {
            return item.getEnchantments();
        }
    }
}
