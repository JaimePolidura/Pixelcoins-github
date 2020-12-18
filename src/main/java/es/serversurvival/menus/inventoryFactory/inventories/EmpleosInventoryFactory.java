package es.serversurvival.menus.inventoryFactory.inventories;

import es.serversurvival.menus.inventoryFactory.InventoryFactory;
import es.serversurvival.mySQL.Empleados;
import es.serversurvival.mySQL.tablasObjetos.Empleado;
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
        ItemStack info = new ItemStack(Material.PAPER);
        ItemMeta infoMeta = info.getItemMeta();

        infoMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "INFO");

        List<String> lore = new ArrayList<>();
        lore.add("Puedes estar contratado en");
        lore.add("una empresa y ganar dinero.");
        lore.add("Mas info en /ayuda empleo o en:");
        lore.add("http://serversurvival.ddns.net");

        infoMeta.setLore(lore);
        info.setItemMeta(infoMeta);

        return info;
    }

    private List<ItemStack> buildItemsEmpleos (String jugador) {
        empleadosMySQL.conectar();

        List<Empleado> empleaosJugador = empleadosMySQL.getTrabajosJugador(jugador);

        List<ItemStack> itemsEmpleos = new ArrayList<>();
        empleaosJugador.forEach( (empl) -> {
            String icono = empresasMySQL.getEmpresa(empl.getEmpresa()).getIcono();

            ItemStack empleo = new ItemStack(Material.getMaterial(icono));
            ItemMeta empleoMeta = empleo.getItemMeta();
            empleoMeta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "CLICK PARA IRTE");

            List<String> lore = new ArrayList<>();
            lore.add("   ");
            lore.add(ChatColor.GOLD + "Empresa: " + empl.getEmpresa());
            lore.add(ChatColor.GOLD + "Cargo: " + empl.getCargo());
            lore.add(ChatColor.GOLD + "Sueldo: " + ChatColor.GREEN + empl.getSueldoFormateado() + "/" + Empleados.toStringTipoSueldo(empl.getTipo_sueldo()));
            lore.add(ChatColor.GOLD + "Ultima vez que te pagaron: " + empl.getFecha_ultimapaga());
            lore.add("   ");
            lore.add(ChatColor.GOLD + "ID: " + empl.getId());

            empleoMeta.setLore(lore);
            empleo.setItemMeta(empleoMeta);

            itemsEmpleos.add(empleo);
        });
        empleadosMySQL.desconectar();

        return itemsEmpleos;
    }
}
