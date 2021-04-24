package es.serversurvival.nfs.empresas.vertodas;

import es.jaimetruman.ItemBuilder;
import es.serversurvival.legacy.mySQL.tablasObjetos.Empleado;
import es.serversurvival.legacy.mySQL.tablasObjetos.Empresa;
import es.serversurvival.legacy.util.Funciones;
import es.serversurvival.legacy.menus.inventoryFactory.InventoryFactory;
import es.serversurvival.nfs.empresas.vertodas.EmpresasVerTodasMenu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.ChatColor.*;

public class EmpresasVerTodasInventoryFactory extends InventoryFactory {

    @Override
    protected Inventory buildInventory(String jugadorNombre) {
        Inventory inventory = Bukkit.createInventory(null, 54, EmpresasVerTodasMenu.titulo);

        List<Empresa> todasLasEmpresas = empresasMySQL.getTodasEmpresas();
        ItemStack back = buildItemGoBack();

        todasLasEmpresas.forEach( (empresa) -> {
            String displayName;
            if(empresa.getOwner().equalsIgnoreCase(jugadorNombre)){
                displayName = GOLD + "" + BOLD + "" + UNDERLINE + "CLICK PARA VER TU EMPRESA";
            }else{
                displayName = GOLD + "" + BOLD + "" + UNDERLINE + "CLICK PARA SOLICITAR UN SERVICIO";
            }

            Material icono = Material.getMaterial(empresa.getIcono());

            List<String> lore = new ArrayList<>();
            lore = insertarDatosEmpresa(empresa, lore);
            lore.add("      ");
            lore = insertarEmpleados(empresa.getNombre(), lore);

            ItemBuilder.of(icono).title(displayName).lore(lore).buildAddInventory(inventory);
        });

        inventory.setItem(53, back);

        return inventory;
    }

    private List<String> insertarDatosEmpresa (Empresa empresa, List<String> lore) {
        lore.add("   ");
        lore.add(GOLD + "Empresa: " + BOLD + empresa.getNombre());
        lore.add(GOLD + "Owner: " + GOLD + empresa.getOwner());
        if(empresa.isCotizada()) lore.add(GOLD + "Cotiza en bolsa");
        lore.add("     ");
        lore.add(GOLD + "Pixelcoins: " + GREEN + formatea.format(empresa.getPixelcoins()) + " PC");
        lore.add(GOLD + "Ingresos: " + GREEN + formatea.format(empresa.getIngresos()) + " PC");
        lore.add(GOLD + "Gastos: " + GREEN + formatea.format(empresa.getGastos()) + " PC");
        lore.add(GOLD + "Beneficios: " + GREEN + formatea.format(empresa.getIngresos() - empresa.getGastos()) + " PC");
        double margen = Funciones.rentabilidad(empresa.getIngresos(), empresa.getIngresos() - empresa.getGastos());
        lore.add(GOLD + "Rentabilidad: " + GREEN + ((int) margen) + "%");

        lore.add("      ");
        lore.add(GOLD + "Empleados:");

        return lore;
    }

    private List<String> insertarEmpleados (String nombreEmpresa, List<String> lore) {
        List<Empleado> empleados = empleadosMySQL.getEmpleadosEmrpesa(nombreEmpresa);

        lore.add(GOLD + "Empleados:");
        if(empleados.size() != 0){
            for (Empleado empleado : empleados) {
                lore.add(GOLD + "-" + empleado.getJugador());
            }
        }else{
            lore.add(GOLD + "Sin trabajadores");
        }

        return lore;
    }
}
