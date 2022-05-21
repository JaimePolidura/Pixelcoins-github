package es.serversurvival.empresas.empresas.miempresa;

import es.jaimetruman.ItemBuilder;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.empresas.accionistasserver._shared.application.AccionistasServerService;
import es.serversurvival.empresas.accionistasserver._shared.domain.AccionistaServer;
import es.serversurvival.empresas.empleados._shared.application.EmpleadosService;
import es.serversurvival.empresas.empleados._shared.domain.Empleado;
import es.serversurvival.empresas.empresas._shared.application.EmpresasService;
import es.serversurvival.empresas.empresas._shared.domain.Empresa;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival._shared.menus.inventory.InventoryFactory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static es.serversurvival._shared.utils.Funciones.FORMATEA;
import static org.bukkit.ChatColor.*;

public class VerEmpresaInventoryFactory extends InventoryFactory {
    private final Empresa empresa;
    private final EmpresasService empresasService;
    private final EmpleadosService empleadosService;
    private final AccionistasServerService accionistasEmpresasServerService;

    public VerEmpresaInventoryFactory (String empresa) {
        this.empresasService = DependecyContainer.get(EmpresasService.class);
        this.empleadosService = DependecyContainer.get(EmpleadosService.class);
        this.accionistasEmpresasServerService = DependecyContainer.get(AccionistasServerService.class);
        this.empresa = this.empresasService.getByNombre(empresa);
    }

    @Override
    protected Inventory buildInventory(String jugador) {
        Inventory inventory = Bukkit.createInventory(null, 54, DARK_RED + "" + BOLD + "      "  + this.empresa.getNombre().toUpperCase());

        inventory.setItem(0, buildItemInfo());
        inventory.setItem(1, buildItemsEmpresas());
        inventory.setItem(8, buildItemBorrarEmpresa());
        inventory.setItem(53, buildItemGoBack());
        if(empresa.isCotizada()){
            inventory.setItem(2, buildItemAccionistas());
            inventory.setItem(3, buildItemDividendo());
        }

        List<ItemStack> itemsEmpleados = buildItemsEmpleados();


        for(int i = 0; i < itemsEmpleados.size(); i++){
            if(i == 53) break;

            inventory.setItem(i + 9, itemsEmpleados.get(i));
        }

        return inventory;
    }

    private ItemStack buildItemsEmpresas () {
        String displayName = GOLD + "" + BOLD + "" + empresa.getNombre().toUpperCase();
        List<String> lore = new ArrayList<>();
        lore.add(GOLD + "Descripccion:");

        List<String> descripccion = Funciones.dividirDesc(empresa.getDescripcion(), 40);
        lore.addAll(1, descripccion);
        lore.add("  ");
        lore.add(GOLD + "Pixelcoins: " + GREEN + FORMATEA.format(empresa.getPixelcoins()) + " PC");
        lore.add(GOLD + "Ingresos: " + GREEN + FORMATEA.format(empresa.getIngresos()) + " PC");
        lore.add(GOLD + "Gastos: " + GREEN + FORMATEA.format(empresa.getGastos()) + " PC");
        double beneficiosPerdidas = empresa.getIngresos() - empresa.getGastos();
        if(beneficiosPerdidas >= 0){
            lore.add(GOLD + "Beneficios: "  +  GREEN + "+" +  FORMATEA.format(beneficiosPerdidas) + " PC");
            lore.add(GOLD + "Rentabilidad: " + GREEN + "+" + Funciones.redondeoDecimales(Funciones.rentabilidad(empresa.getIngresos(), beneficiosPerdidas),1) + "%");
        }else{
            lore.add(GOLD + "Perdidas: " + RED + FORMATEA.format(beneficiosPerdidas) + " PC");
            lore.add(GOLD + "Rentabilidad: " + RED + Funciones.redondeoDecimales(Funciones.rentabilidad(empresa.getIngresos(), beneficiosPerdidas),1) + "%");
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

        return ItemBuilder.of(Material.WRITABLE_BOOK).title(displayName).lore(lore).build();
    }

    private ItemStack buildItemBorrarEmpresa () {
        String displayName = RED + "" + BOLD + "" + UNDERLINE + "CLICK PARA BORRAR LA EMPRESA";

        return ItemBuilder.of(Material.BARRIER).title(displayName).build();
    }

    public ItemStack buildItemInfo () {
        String displayName = GOLD + "" + BOLD + "INFO";
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

        return ItemBuilder.of(Material.PAPER).title(displayName).build();
    }

    private List<ItemStack> buildItemsEmpleados () {
        List<ItemStack> itemsEmpleados = new ArrayList<>();
        List<Empleado> empleados = this.empleadosService.findByEmpresa(empresa.getNombre());

        empleados.forEach( (empleado) -> {
            List<String> lore = new ArrayList<>();
            lore.add("   ");
            lore.add(GOLD + "Empleado: " + empleado.getNombre());
            lore.add(GOLD + "Cargo: " + empleado.getCargo());
            lore.add(GOLD + "Sueldo: " + GREEN + FORMATEA.format(empleado.getSueldo()) + " PC/" + empleado.getTipoSueldo().nombre);
            lore.add(GOLD + "ID: " + empleado.getEmpleadoId());
            lore.add("   ");
            lore.add("/empresas despedir " + empresa + " " + empleado.getNombre());
            lore.add("  <razon>");
            lore.add("/empresas editarempleado " + empresa + " " + empleado.getNombre());
            lore.add("  sueldo <sueldo>");
            lore.add("/empresas editarempleado " + empresa + " " + empleado.getNombre());
            lore.add("  tiposueldo <tipo (ver en info)>");

            itemsEmpleados.add(ItemBuilder.of(Material.PLAYER_HEAD).lore(lore).title(RED + "" + BOLD + "" + UNDERLINE + "CLICK PARA DESPEDIR").build());
        });

        return itemsEmpleados;
    }

    private ItemStack buildItemAccionistas () {
        String displayName = GOLD + "" + BOLD + "ACCIONISTAS";
        List<String> lore = new ArrayList<>();
        //TODO XD
        var jugadoresAccionistas = accionistasEmpresasServerService.findByEmpresa(empresa.getNombre()).stream()
                .sorted(Comparator.comparingInt(AccionistaServer::getCantidad))
                .toList();

        for (AccionistaServer accionista : jugadoresAccionistas) {
            if(accionista.getNombreAccionista().equalsIgnoreCase(this.empresa.getNombre()))
                lore.add(GOLD + "" + "EMPRESA : " + GREEN + FORMATEA.format(accionista.getCantidad()
                        / this.empresa.getAccionesTotales()) + "%");
            else
                lore.add(GOLD + accionista.getNombreAccionista() + ": " + GREEN + FORMATEA.format(accionista.getCantidad()
                        / this.empresa.getAccionesTotales()) + "%");
        }

        return ItemBuilder.of(Material.NETHERITE_SCRAP).title(displayName).lore(lore).build();
    }

    private ItemStack buildItemDividendo () {
        String displayName = GOLD + "" + BOLD + "REPARTIR DIVIDENDO";
        List<String> lore = new ArrayList<>();
        lore.add(GOLD + "Repartir pixelcoins por cada accion");

        return ItemBuilder.of(Material.GOLD_INGOT).title(displayName).lore(lore).build();
    }
}
