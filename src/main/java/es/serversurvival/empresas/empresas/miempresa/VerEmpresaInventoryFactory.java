package es.serversurvival.empresas.empresas.miempresa;

import es.jaimetruman.ItemBuilder;
import es.serversurvival._shared.DependecyContainer;
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
import java.util.List;
import java.util.Map;

import static org.bukkit.ChatColor.*;

public class VerEmpresaInventoryFactory extends InventoryFactory {
    private final String empresaNombre;
    private final EmpresasService empresasService;

    public VerEmpresaInventoryFactory (String empresa) {
        this.empresaNombre = empresa;
        this.empresasService = DependecyContainer.get(EmpresasService.class);
    }

    @Override
    protected Inventory buildInventory(String jugador) {
        Inventory inventory = Bukkit.createInventory(null, 54, DARK_RED + "" + BOLD + "      "  + empresaNombre.toUpperCase());

        inventory.setItem(0, buildItemInfo());
        inventory.setItem(1, buildItemsEmpresas());
        inventory.setItem(8, buildItemBorrarEmpresa());
        inventory.setItem(53, buildItemGoBack());
        if(empresasService.isCotizada(empresaNombre)){
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
        Empresa empresaAVer = empresasService.getEmpresaByNombre(empresaNombre);

        String displayName = GOLD + "" + BOLD + "" + empresaNombre.toUpperCase();
        List<String> lore = new ArrayList<>();
        lore.add(GOLD + "Descripccion:");

        List<String> descripccion = Funciones.dividirDesc(empresaAVer.getDescripcion(), 40);
        lore.addAll(1, descripccion);
        lore.add("  ");
        lore.add(GOLD + "Pixelcoins: " + GREEN + formatea.format(empresaAVer.getPixelcoins()) + " PC");
        lore.add(GOLD + "Ingresos: " + GREEN + formatea.format(empresaAVer.getIngresos()) + " PC");
        lore.add(GOLD + "Gastos: " + GREEN + formatea.format(empresaAVer.getGastos()) + " PC");
        double beneficiosPerdidas = empresaAVer.getIngresos() - empresaAVer.getGastos();
        if(beneficiosPerdidas >= 0){
            lore.add(GOLD + "Beneficios: "  +  GREEN + "+" +  formatea.format(beneficiosPerdidas) + " PC");
            lore.add(GOLD + "Rentabilidad: " + GREEN + "+" + Funciones.redondeoDecimales(Funciones.rentabilidad(empresaAVer.getIngresos(), beneficiosPerdidas),1) + "%");
        }else{
            lore.add(GOLD + "Perdidas: " + RED + formatea.format(beneficiosPerdidas) + " PC");
            lore.add(GOLD + "Rentabilidad: " + RED + Funciones.redondeoDecimales(Funciones.rentabilidad(empresaAVer.getIngresos(), beneficiosPerdidas),1) + "%");
        }
        lore.add("   ");
        lore.add("/empresas depositar " + empresaNombre + " <pixelcoins>");
        lore.add("/empresas sacar " + empresaNombre + " <pixelcoins>");
        lore.add("/empresas logotipo " + empresaNombre + "");
        lore.add("/empresas editardescripccion " + empresaNombre);
        lore.add("  <nueva desc>");
        lore.add("/empresas editarnombre " + empresaNombre + " <nombre>");
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
        List<Empleado> empleados = empleadosMySQL.getEmpleadosEmrpesa(empresaNombre);

        empleados.forEach( (empleado) -> {
            List<String> lore = new ArrayList<>();
            lore.add("   ");
            lore.add(GOLD + "Empleado: " + empleado.getNombre());
            lore.add(GOLD + "Cargo: " + empleado.getCargo());
            lore.add(GOLD + "Sueldo: " + GREEN + formatea.format(empleado.getSueldo()) + " PC/" + empleado.getTipoSueldo().nombre);
            lore.add(GOLD + "ID: " + empleado.getEmpleadoId());
            lore.add("   ");
            lore.add("/empresas despedir " + empresaNombre + " " + empleado.getNombre());
            lore.add("  <razon>");
            lore.add("/empresas editarempleado " + empresaNombre + " " + empleado.getNombre());
            lore.add("  sueldo <sueldo>");
            lore.add("/empresas editarempleado " + empresaNombre + " " + empleado.getNombre());
            lore.add("  tiposueldo <tipo (ver en info)>");

            itemsEmpleados.add(ItemBuilder.of(Material.PLAYER_HEAD).lore(lore).title(RED + "" + BOLD + "" + UNDERLINE + "CLICK PARA DESPEDIR").build());
        });

        return itemsEmpleados;
    }

    private ItemStack buildItemAccionistas () {
        String displayName = GOLD + "" + BOLD + "ACCIONISTAS";
        List<String> lore = new ArrayList<>();
        Map<String, Integer> jugadoresAccionistas = ofertasMercadoServerMySQL.getAccionistasEmpresaServer(empresaNombre);

        jugadoresAccionistas.forEach((jugador, peso) -> {
            if(jugador.equalsIgnoreCase(empresaNombre)){
                lore.add(GOLD + "" + "EMPRESA : " + GREEN + formatea.format(peso) + "%");
            }else{
                lore.add(GOLD + jugador + ": " + GREEN + formatea.format(peso) + "%");
            }

        });

        return ItemBuilder.of(Material.NETHERITE_SCRAP).title(displayName).lore(lore).build();
    }

    private ItemStack buildItemDividendo () {
        String displayName = GOLD + "" + BOLD + "REPARTIR DIVIDENDO";
        List<String> lore = new ArrayList<>();
        lore.add(GOLD + "Repartir pixelcoins por cada accion");

        return ItemBuilder.of(Material.GOLD_INGOT).title(displayName).lore(lore).build();
    }
}
