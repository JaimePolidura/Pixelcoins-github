package es.serversurvival.menus.inventoryFactory.inventories;

import es.serversurvival.util.Funciones;
import es.serversurvival.menus.inventoryFactory.InventoryFactory;
import es.serversurvival.menus.menus.EmpresasVerTodasMenu;
import es.serversurvival.mySQL.tablasObjetos.Empleado;
import es.serversurvival.mySQL.tablasObjetos.Empresa;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class EmpresasVerTodasInventoryFactory extends InventoryFactory {

    @Override
    protected Inventory buildInventory(String player) {
        return buildInv();
    }

    private Inventory buildInv(){
        Inventory inventory = Bukkit.createInventory(null, 54, EmpresasVerTodasMenu.titulo);
        empresasMySQL.conectar();

        List<Empresa> todasLasEmpresas = empresasMySQL.getTodasEmpresas();
        ItemStack back = buildItemGoBack();

        todasLasEmpresas.forEach( (empresa) -> {
            ItemStack iconoEmpresa = new ItemStack(Material.getMaterial(empresa.getIcono()), 1);
            ItemMeta itemMeta = iconoEmpresa.getItemMeta();

            List<String> lore = new ArrayList<>();
            lore = insertarDatosEmpresa(empresa, lore);
            lore.add("      ");
            lore = insertarEmpleados(empresa.getNombre(), lore);

            itemMeta.setLore(lore);
            itemMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "CLICK PARA SOLICITAR UN SERVICIO");
            iconoEmpresa.setItemMeta(itemMeta);

            inventory.addItem(iconoEmpresa);
        });

        inventory.setItem(53, back);

        return inventory;
    }

    private List<String> insertarDatosEmpresa (Empresa empresa, List<String> lore) {
        lore.add("   ");
        lore.add(ChatColor.GOLD + "Empresa: " + ChatColor.BOLD + empresa.getNombre());
        lore.add(ChatColor.GOLD + "Owner: " + ChatColor.GOLD + empresa.getOwner());
        lore.add("     ");
        lore.add(ChatColor.GOLD + "Pixelcoins: " + ChatColor.GREEN + formatea.format(empresa.getPixelcoins()) + " PC");
        lore.add(ChatColor.GOLD + "Ingresos: " + ChatColor.GREEN + formatea.format(empresa.getIngresos()) + " PC");
        lore.add(ChatColor.GOLD + "Gastos: " + ChatColor.GREEN + formatea.format(empresa.getGastos()) + " PC");
        lore.add(ChatColor.GOLD + "Beneficios: " + ChatColor.GREEN + empresa.getBeneficiosFormateado() + " PC");
        double margen = Funciones.rentabilidad(empresa.getIngresos(), empresa.getIngresos() - empresa.getGastos());
        lore.add(ChatColor.GOLD + "Rentabilidad: " + ChatColor.GREEN + ((int) margen) + "%");
        lore.add("      ");
        lore.add(ChatColor.GOLD + "Empleados:");

        return lore;
    }

    private List<String> insertarEmpleados (String nombreEmpresa, List<String> lore) {
        List<Empleado> empleados = empleadosMySQL.getEmpleadosEmrpesa(nombreEmpresa);

        lore.add(ChatColor.GOLD + "Empleados:");
        if(empleados.size() != 0){
            for(int i = 0; i < empleados.size(); i++){
                lore.add(ChatColor.GOLD + "-" + empleados.get(i).getJugador());
            }
        }else{
            lore.add(ChatColor.GOLD + "Sin trabajadores");
        }

        return lore;
    }
}
