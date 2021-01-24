package es.serversurvival.menus.inventoryFactory.inventories;

import es.serversurvival.menus.inventoryFactory.InventoryFactory;
import es.serversurvival.mySQL.Empleados;
import es.serversurvival.mySQL.tablasObjetos.Empleado;
import es.serversurvival.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class EmpleosInventoryFactory extends InventoryFactory {
    @Override
    protected Inventory buildInventory(String jugador) {
        Inventory inventory = Bukkit.createInventory(null, 54, ChatColor.DARK_RED + "" + ChatColor.BOLD + "        TUS EMPLEOS");

        ItemStack info = buildInfo();
        ItemStack back = buildItemGoBack();
        List<ItemStack> itemsEmpleos = buildItemsEmpleos(jugador);

        inventory.setItem(0, info);
        for(int i = 0; i < itemsEmpleos.size(); i++){
            if(i == 53) break;

            inventory.setItem(i + 9, itemsEmpleos.get(i));
        }
        inventory.setItem(53, back);

        return inventory;
    }

    public ItemStack buildInfo(){
        List<String> lore = new ArrayList<>();
        lore.add("Puedes estar contratado en");
        lore.add("una empresa y ganar dinero.");
        lore.add("Mas info en /ayuda empleo o en:");
        lore.add("http://serversurvival.ddns.net");

        return ItemBuilder.loreDisplayName(Material.PAPER, ChatColor.GOLD + "" + ChatColor.BOLD + "INFO", lore);
    }

    private List<ItemStack> buildItemsEmpleos (String jugador) {
        empleadosMySQL.conectar();

        List<Empleado> empleaosJugador = empleadosMySQL.getTrabajosJugador(jugador);

        List<ItemStack> itemsEmpleos = new ArrayList<>();
        empleaosJugador.forEach( (empl) -> {
            String icono = empresasMySQL.getEmpresa(empl.getEmpresa()).getIcono();
            String displayName = ChatColor.RED + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "CLICK PARA IRTE";

            List<String> lore = new ArrayList<>();
            lore.add("   ");
            lore.add(ChatColor.GOLD + "Empresa: " + empl.getEmpresa());
            lore.add(ChatColor.GOLD + "Cargo: " + empl.getCargo());
            lore.add(ChatColor.GOLD + "Sueldo: " + ChatColor.GREEN + empl.getSueldoFormateado() + "/" + Empleados.toStringTipoSueldo(empl.getTipo_sueldo()));
            lore.add(ChatColor.GOLD + "Ultima vez que te pagaron: " + empl.getFecha_ultimapaga());
            lore.add("   ");
            lore.add(ChatColor.GOLD + "ID: " + empl.getId());

            itemsEmpleos.add(ItemBuilder.loreDisplayName(Material.valueOf(icono), displayName, lore));
        });
        empleadosMySQL.desconectar();

        return itemsEmpleos;
    }
}
