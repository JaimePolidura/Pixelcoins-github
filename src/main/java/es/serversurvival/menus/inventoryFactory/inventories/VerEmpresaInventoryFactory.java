package es.serversurvival.menus.inventoryFactory.inventories;

import es.serversurvival.menus.inventoryFactory.InventoryFactory;
import es.serversurvival.mySQL.Empleados;
import es.serversurvival.mySQL.Empresas;
import es.serversurvival.mySQL.tablasObjetos.Empleado;
import es.serversurvival.mySQL.tablasObjetos.Empresa;
import es.serversurvival.util.Funciones;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VerEmpresaInventoryFactory extends InventoryFactory {
    private String empresa;

    public VerEmpresaInventoryFactory (String empresa) {
        this.empresa = empresa;
    }

    @Override
    protected Inventory buildInventory(String jugador) {
        Inventory inventory = Bukkit.createInventory(null, 54, ChatColor.DARK_RED + "" + ChatColor.BOLD + "      "  + empresa.toUpperCase());

        empresasMySQL.conectar();
        ItemStack info = buildItemInfo();
        ItemStack empresa = buildItemsEmpresas();
        ItemStack borrar = buildItemBorrarEmpresa();
        ItemStack back = buildItemGoBack();
        List<ItemStack> itemsEmpleados = buildItemsEmpleados();
        empresasMySQL.desconectar();

        inventory.setItem(0, info);
        inventory.setItem(1, empresa);
        inventory.setItem(2, borrar);
        inventory.setItem(53, back);
        for(int i = 0; i < itemsEmpleados.size(); i++){
            if(i == 53) break;

            inventory.setItem(i + 9, itemsEmpleados.get(i));
        }

        return inventory;
    }

    private ItemStack buildItemsEmpresas () {
        ItemStack empresaItem = new ItemStack(Material.WRITABLE_BOOK);
        Empresa empresaAVer = empresasMySQL.getEmpresa(empresa);

        ItemMeta empresaMeta = empresaItem.getItemMeta();

        empresaMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "" + empresa.toUpperCase());
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GOLD + "Descripccion:");

        List<String> descripccion = Funciones.dividirDesc(empresa, 40);
        lore.addAll(1, descripccion);
        lore.add("  ");
        lore.add(ChatColor.GOLD + "Pixelcoins: " + ChatColor.GREEN + formatea.format(empresaAVer.getPixelcoins()) + " PC");
        lore.add(ChatColor.GOLD + "Ingresos: " + ChatColor.GREEN + formatea.format(empresaAVer.getIngresos()) + " PC");
        lore.add(ChatColor.GOLD + "Gastos: " + ChatColor.GREEN + formatea.format(empresaAVer.getGastos()) + " PC");
        double beneficiosPerdidas = empresaAVer.getIngresos() - empresaAVer.getGastos();
        if(beneficiosPerdidas >= 0){
            lore.add(ChatColor.GOLD + "Beneficios: "  +  ChatColor.GREEN + "+" +  formatea.format(beneficiosPerdidas) + " PC");
            lore.add(ChatColor.GOLD + "Rentabilidad: " + ChatColor.GREEN + "+" + Funciones.redondeoDecimales(Funciones.rentabilidad(empresaAVer.getIngresos(), beneficiosPerdidas),1) + "%");
        }else{
            lore.add(ChatColor.GOLD + "Perdidas: " + ChatColor.RED + formatea.format(beneficiosPerdidas) + " PC");
            lore.add(ChatColor.GOLD + "Rentabilidad: " + ChatColor.RED + Funciones.redondeoDecimales(Funciones.rentabilidad(empresaAVer.getIngresos(), beneficiosPerdidas),1) + "%");
        }

        lore.add("   ");
        lore.add("/empresas depositar " + empresa + " <pixelcoins>");
        lore.add("/empresas sacar " + empresa + " <pixelcoins>");
        lore.add("/empresas logotipo " + empresa + "");
        lore.add("/empresas editardescripccion " + empresa);
        lore.add("  <nueva desc>");
        lore.add("/empresas editarnombre " + empresa + " <nombre>");
        lore.add("Mas info en /ayuda empresario o:");
        lore.add("http://serversurvival.ddns.net/perfil");

        empresaMeta.setLore(lore);
        empresaItem.setItemMeta(empresaMeta);


        return empresaItem;
    }

    private ItemStack buildItemBorrarEmpresa () {
        ItemStack borrar = new ItemStack(Material.BARRIER);
        ItemMeta borrarMeta = borrar.getItemMeta();

        borrarMeta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "CLICK PARA BORRAR LA EMPRESA");
        borrarMeta.setLore(Arrays.asList("   "));

        borrar.setItemMeta(borrarMeta);

        return borrar;
    }

    public ItemStack buildItemInfo () {
        ItemStack info = new ItemStack(Material.PAPER);
        ItemMeta infoMeta = info.getItemMeta();

        infoMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "INFO");

        List<String> lore = new ArrayList<>();
        lore.add("Puedes contratar empleados, ");
        lore.add("despedirlos etc. Comandos:");
        lore.add("                              ");
        lore.add("/empresas contratar <jugador>");
        lore.add(" <empresa> <sueldo> <tipo sueldo>");
        lore.add("El tipo sueldo es la frequencia con");
        lore.add("la que el sueldo se va a pagar: ");
        lore.add("  d: cada dia");
        lore.add("  s: cada semana");
        lore.add("  2s cada dos semanas");
        lore.add("  m: cada mes");
        lore.add("   ");
        lore.add("Para mas info: /ayuda empresario o");
        lore.add("http://serversurvival.ddns.net/perfil");

        infoMeta.setLore(lore);
        info.setItemMeta(infoMeta);

        return info;
    }

    private List<ItemStack> buildItemsEmpleados () {
        List<ItemStack> itemsEmpleados = new ArrayList<>();
        List<Empleado> empleados = empleadosMySQL.getEmpleadosEmrpesa(empresa);

        empleados.forEach( (empleado) -> {
            ItemStack empleadoItem = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta empleadoMeta = (SkullMeta) empleadoItem.getItemMeta();

            empleadoMeta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "CLICK PARA DESPEDIR");

            List<String> lore = new ArrayList<>();
            lore.add("   ");
            lore.add(ChatColor.GOLD + "Empleado: " + empleado.getEmpleado());
            lore.add(ChatColor.GOLD + "Cargo: " + empleado.getCargo());
            lore.add(ChatColor.GOLD + "Sueldo: " + ChatColor.GREEN + empleado.getSueldoFormateado() + " PC/" + Empleados.toStringTipoSueldo(empleado.getTipo()));
            lore.add(ChatColor.GOLD + "ID: " + empleado.getId());
            lore.add("   ");
            lore.add("/empresas despedir " + empresa + " " + empleado.getEmpleado());
            lore.add("  <razon>");
            lore.add("/empresas editarempleado " + empresa + " " + empleado.getEmpleado());
            lore.add("  sueldo <sueldo>");
            lore.add("/empresas editarempleado " + empresa + " " + empleado.getEmpleado());
            lore.add("  tiposueldo <tipo (ver en info)>");


            empleadoMeta.setLore(lore);
            empleadoItem.setItemMeta(empleadoMeta);

            itemsEmpleados.add(empleadoItem);
        });

        return itemsEmpleados;
    }
}