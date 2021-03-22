package es.serversurvival.objetos.menus;
import es.serversurvival.main.Funciones;
import es.serversurvival.objetos.mySQL.Empleados;
import es.serversurvival.objetos.mySQL.Empresas;
import es.serversurvival.objetos.mySQL.tablasObjetos.Empleado;
import es.serversurvival.objetos.mySQL.tablasObjetos.Empresa;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class EmpresasMenu extends Menu{
    private Empleados empleadosMySQL = new Empleados();
    public DecimalFormat formatea = new DecimalFormat("###,###.##");
    private Empresas empresasMySQL = new Empresas();
    private Player player;
    private String titulo = ChatColor.DARK_RED + "" + ChatColor.BOLD + "         Empresas";

    public EmpresasMenu(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public String titulo() {
        return titulo;
    }

    @Override
    public void openMenu() {
        activeMenus.add(this);
        player.openInventory(buildInv());
    }

    private Inventory buildInv(){
        Inventory inventory = Bukkit.createInventory(null, 54, titulo);
        empresasMySQL.conectar();

        List<Empresa> todasLasEmpresas = empresasMySQL.getTodasEmpresas();

        todasLasEmpresas.forEach( (empresa) -> {
            ItemStack iconoEmpresa = new ItemStack(Material.getMaterial(empresa.getIcono()), 1);
            ItemMeta itemMeta = iconoEmpresa.getItemMeta();

            List<String> lore = new ArrayList<>();
            lore = insertarDatosEmpresa(empresa, lore);
            lore.add("      ");
            lore = insertarEmpleados(empresa.getNombre(), lore);

            itemMeta.setLore(lore);
            itemMeta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + empresa.getNombre());
            iconoEmpresa.setItemMeta(itemMeta);

            inventory.addItem(iconoEmpresa);
        });

        return inventory;
    }

    private List<String> insertarDatosEmpresa (Empresa empresa, List<String> lore) {
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
                lore.add(ChatColor.GOLD + "-" + empleados.get(i).getEmpleado());
            }
        }else{
            lore.add(ChatColor.GOLD + "Sin trabajadores");
        }

        return lore;
    }

    /*private Inventory buildInv(){
        Inventory inventory = Bukkit.createInventory(null, 54, titulo);
        empresasMySQL.conectar();

        List<Empresa> empresas = empresasMySQL.getTodasEmpresas();

        String owner;
        double pixelcoins;
        double ingresos;
        double gastos;
        double beneficios;
        double margen;

        int nRecorridos = 0;
        String icono;
        String descripcion;

        ItemStack item = null;
        ItemMeta im = null;

        Empleados em = new Empleados();

        List<Empleado> empleadosList = new ArrayList<>();
        ArrayList<String> lore = new ArrayList<String>();
        for(Empresa empresa : empresas) {
            im = null;

            owner = empresa.getOwner();
            pixelcoins = empresa.getPixelcoins();
            ingresos = empresa.getIngresos();
            gastos = empresa.getGastos();
            beneficios = ingresos - gastos;
            icono = empresa.getIcono();
            descripcion = empresa.getDescripcion();

            lore.clear();

            margen = Funciones.rentabilidad(ingresos, beneficios);

            item = new ItemStack(Material.getMaterial(icono), 1);
            im = item.getItemMeta();

            lore.add(ChatColor.GOLD + "Owner: " + ChatColor.GOLD + owner);

            lore = Funciones.dividirDesc(lore, descripcion, 47);

            lore.add("     ");
            lore.add(ChatColor.GOLD + "Pixelcoins: " + ChatColor.GREEN + formatea.format(pixelcoins) + " PC");
            lore.add(ChatColor.GOLD + "Ingresos: " + ChatColor.GREEN + formatea.format(ingresos) + " PC");
            lore.add(ChatColor.GOLD + "Gastos: " + ChatColor.GREEN + formatea.format(gastos) + " PC");
            lore.add(ChatColor.GOLD + "Beneficios: " + ChatColor.GREEN + formatea.format(beneficios) + " PC");
            lore.add(ChatColor.GOLD + "Rentabilidad: " + ChatColor.GREEN + ((int) margen) + "%");
            lore.add("      ");
            lore.add(ChatColor.GOLD + "Empleados:");

            empleadosList = em.getEmpleadosEmrpesa(empresa.getNombre());

            for (int i = 0; i < empleadosList.size(); i++) {
                if (empleadosList.get(0) == null) {
                    break;
                } else {
                    nRecorridos++;
                    lore.add(ChatColor.GOLD + "-" + empleadosList.get(i).getEmpleado());
                }
            }

            if (nRecorridos == 0) {
                lore.add("  ");
                lore.add("Sin trabajadores");
            }
            im.setLore(lore);
            im.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + empresa.getNombre());
            item.setItemMeta(im);

            inventory.addItem(item);
        }
        return inventory;
    }*/

    @Override
    public void closeMenu() {
        activeMenus.remove(this);
    }
}
