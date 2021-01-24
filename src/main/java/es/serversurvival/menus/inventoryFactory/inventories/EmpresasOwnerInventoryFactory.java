package es.serversurvival.menus.inventoryFactory.inventories;

import es.serversurvival.util.Funciones;
import es.serversurvival.menus.inventoryFactory.InventoryFactory;
import es.serversurvival.menus.menus.EmpresasOwnerMenu;
import es.serversurvival.mySQL.Empresas;
import es.serversurvival.mySQL.tablasObjetos.Empresa;
import es.serversurvival.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class EmpresasOwnerInventoryFactory extends InventoryFactory {
    @Override
    protected Inventory buildInventory(String jugador) {
        Inventory inventory = Bukkit.createInventory(null, 54, EmpresasOwnerMenu.titulo);

        ItemStack info = buildItemInfo();
        ItemStack verempresas = buildItemVertodas();
        ItemStack back = buildItemGoBack();
        List<ItemStack> items = buildItemsEmpresas(jugador);

        inventory.setItem(0, info);
        inventory.setItem(1, verempresas);
        for(int i = 0; i < items.size(); i++){
            if(i == 53) break;

            inventory.setItem(i + 9, items.get(i));
        }
        inventory.setItem(53, back);

        return inventory;
    }

    private List<ItemStack> buildItemsEmpresas (String jugador) {
        List<ItemStack> itemsEmpresas = new ArrayList<>();
        empresasMySQL.conectar();

        List<Empresa> empresasOwner = empresasMySQL.getEmpresasOwner(jugador);
        empresasOwner.forEach( (empr) -> {
            Material icono = Material.valueOf(empr.getIcono());
            String displayName = ChatColor.GOLD + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "CLICK PARA VER " + empr.getNombre();

            itemsEmpresas.add(ItemBuilder.displayname(icono, displayName));
        });
        empresasMySQL.desconectar();

        return itemsEmpresas;
    }

    private ItemStack buildItemInfo(){
        String displayName = ChatColor.GOLD + "" + ChatColor.BOLD + "INFO";
        List<String> lore = new ArrayList<>();
        lore.add("Puedes crear tus propias empresas,");
        lore.add("contratar a gente, despedir etc.");
        lore.add("Para hacer todo lo relacionado con");
        lore.add("las empresas recomiendo hacerlo desde");
        lore.add("la web: http://serversurvival.ddns.net");
        lore.add("Se puede hacer con comandos.");
        lore.add("En la descripccion de las empresa");
        lore.add("se vel el uso de estos");

        return ItemBuilder.loreDisplayName(Material.PAPER, displayName, lore);
    }

    private ItemStack buildItemVertodas (){
        String displayName = ChatColor.GOLD + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "CLICK PARA VER EL RESTO DE LAS EMPRESAS";

        return ItemBuilder.displayname(Material.BOOK, displayName);
    }
}
