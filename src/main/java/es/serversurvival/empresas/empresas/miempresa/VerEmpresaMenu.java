package es.serversurvival.empresas.empresas.miempresa;

import es.jaimetruman.ItemBuilder;
import es.jaimetruman.ItemUtils;
import es.jaimetruman.menus.Menu;
import es.jaimetruman.menus.configuration.MenuConfiguration;
import es.jaimetruman.menus.menustate.AfterShow;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.empresas.accionistasserver._shared.application.AccionistasServerService;
import es.serversurvival.empresas.accionistasserver._shared.domain.AccionistaServer;
import es.serversurvival.empresas.empleados._shared.application.EmpleadosService;
import es.serversurvival.empresas.empleados._shared.domain.Empleado;
import es.serversurvival.empresas.empleados.despedir.DespedirEmpleadoUseCase;
import es.serversurvival.empresas.empresas._shared.application.EmpresasService;
import es.serversurvival.empresas.empresas._shared.domain.Empresa;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static es.serversurvival._shared.utils.Funciones.*;
import static es.serversurvival._shared.utils.Funciones.FORMATEA;
import static java.lang.String.format;
import static org.bukkit.ChatColor.*;
import static org.bukkit.ChatColor.BOLD;
import static org.bukkit.ChatColor.DARK_RED;

public final class VerEmpresaMenu extends Menu implements AfterShow {
    public static final String TITULO = DARK_RED + "" + BOLD + "       %s";

    private final Empresa empresa;
    private final EmpleadosService empleadosService;
    private final EmpresasService empresasService;
    private final AccionistasServerService accionistasServerService;

    public VerEmpresaMenu(String empresaNombre) {
        this.empleadosService = DependecyContainer.get(EmpleadosService.class);
        this.empresasService = DependecyContainer.get(EmpresasService.class);
        this.empresa = this.empresasService.getByNombre(empresaNombre);
        this.accionistasServerService = DependecyContainer.get(AccionistasServerService.class);
    }

    @Override
    public int[][] items() {
        int accionistas = empresa.isCotizada() ? 3 : 0;
        int dividendo = empresa.isCotizada() ? 4 : 0;

        return new int[][] {
                {1, 2, accionistas, dividendo, 0, 0, 0, 0, 5},
                {6, 0,           0,         0, 0, 0, 0, 0, 0},
                {0, 0,           0,         0, 0, 0, 0, 0, 0},
                {0, 0,           0,         0, 0, 0, 0, 0, 0},
                {0, 0,           0,         0, 0, 0, 0, 0, 0},
                {0, 0,           0,         0, 0, 0, 7, 0, 0}
        };
    }

    @Override
    public MenuConfiguration configuration() {
        return MenuConfiguration.builder()
                .fixedItems()
                .title(format(TITULO, this.empresa.getNombre()))
                .item(1, buildItemInfo())
                .item(2, buildItemEmpresaStats())
                .item(3, buildItemAccionistas())
                .item(4, buildItemRepartirDividendo(), this::pagarDividendos)
                .item(5, buildItemBorrarEmpresa(), this::abrirBorrarEmpresaConfirmacion)
                .items(6, buildItemEmpleados(), this::despedirEmpleado)
                .item(7, Material.GREEN_BANNER, this::goBackToProfileMenu)
                .build();
    }

    private void pagarDividendos(Player player, InventoryClickEvent event) {
        //TODO
    }

    private void abrirBorrarEmpresaConfirmacion(Player player, InventoryClickEvent event) {
        //TODO
    }

    private void despedirEmpleado(Player player, InventoryClickEvent event) {
        String empleadoNombre = ItemUtils.getLore(event.getCurrentItem(), 1).split(" ")[1];

        (new DespedirEmpleadoUseCase()).despedir(player.getName(), empleadoNombre, empresa.getNombre(), "Despedido desde el menu");
        player.sendMessage(ChatColor.GOLD + "Has despedido a: " + empleadoNombre);
        String mensajeOnline = ChatColor.RED + "Has sido despedido de " + empresa + " razon: " + "Despedido";
        enviarMensaje(empleadoNombre, mensajeOnline, mensajeOnline, Sound.BLOCK_ANVIL_LAND, 10, 1);

        super.deleteItem(event.getSlot(), super.getActualPageNumber());
    }


    private void goBackToProfileMenu(Player player, InventoryClickEvent event) {
        //TODO
    }

    private List<ItemStack> buildItemEmpleados() {
        return this.empleadosService.findByEmpresa(this.empresa.getNombre()).stream()
                .map(this::buildEmpleadoItem)
                .toList();
    }

    private ItemStack buildEmpleadoItem(Empleado empleado) {
        return ItemBuilder.of(Material.PLAYER_HEAD)
                .title(RED + "" + BOLD + "" + UNDERLINE + "CLICK PARA DESPEDIR")
                .lore(List.of(
                        "   ",
                        GOLD + "Empleado: " + empleado.getNombre(),
                        GOLD + "Cargo: " + empleado.getCargo(),
                        GOLD + "Sueldo: " + GREEN + FORMATEA.format(empleado.getSueldo()) + " PC/" + empleado.getTipoSueldo().nombre,
                        GOLD + "ID: " + empleado.getEmpleadoId(),
                        "   ",
                        "/empresas editarempleado " + empresa + " " + empleado.getNombre(),
                        "  sueldo <sueldo>",
                        "/empresas editarempleado " + empresa + " " + empleado.getNombre(),
                        "  tiposueldo <tipo (ver en info)>"
                ))
                .build();
    }

    private ItemStack buildItemBorrarEmpresa() {
        return ItemBuilder.of(Material.BARRIER).title(RED + "" + BOLD + "" + UNDERLINE + "CLICK PARA BORRAR LA EMPRESA").build();
    }

    private ItemStack buildItemRepartirDividendo() {
        return ItemBuilder.of(Material.GOLD_INGOT)
                .title(GOLD + "" + BOLD + "REPARTIR DIVIDENDO")
                .lore(GOLD + "Repartir pixelcoins por cada accion")
                .build();
    }

    private ItemStack buildItemAccionistas() {
        return ItemBuilder.of(Material.NETHERITE_SCRAP)
                .title(GOLD + "" + BOLD + "ACCIONISTAS")
                .lore(accionistasServerService.findByEmpresa(empresa.getNombre()).stream()
                        .sorted(Comparator.comparingInt(AccionistaServer::getCantidad))
                        .map(accionista -> GOLD + "EMPRESA : " + GREEN + FORMATEA.format(accionista.getCantidad()
                                / this.empresa.getAccionesTotales()) + "%")
                        .toList())
                .build();
    }

    private ItemStack buildItemEmpresaStats() {
        List<String> lore = new ArrayList<>();
        lore.add(GOLD + "Descripccion:");
        lore.addAll(1, dividirDesc(empresa.getDescripcion(), 40));
        lore.add("  ");
        lore.add(GOLD + "Pixelcoins: " + GREEN + FORMATEA.format(empresa.getPixelcoins()) + " PC");
        lore.add(GOLD + "Ingresos: " + GREEN + FORMATEA.format(empresa.getIngresos()) + " PC");
        lore.add(GOLD + "Gastos: " + GREEN + FORMATEA.format(empresa.getGastos()) + " PC");
        double beneficiosPerdidas = empresa.getIngresos() - empresa.getGastos();
        if(beneficiosPerdidas >= 0){
            lore.add(GOLD + "Beneficios: "  +  GREEN + "+" +  FORMATEA.format(beneficiosPerdidas) + " PC");
            lore.add(GOLD + "Rentabilidad: " + GREEN + "+" + redondeoDecimales(rentabilidad(empresa.getIngresos(), beneficiosPerdidas),1) + "%");
        }else{
            lore.add(GOLD + "Perdidas: " + RED + FORMATEA.format(beneficiosPerdidas) + " PC");
            lore.add(GOLD + "Rentabilidad: " + RED + redondeoDecimales(rentabilidad(empresa.getIngresos(), beneficiosPerdidas),1) + "%");
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

        return ItemBuilder.of(Material.WRITABLE_BOOK)
                .title(GOLD + "" + BOLD + "" + empresa.getNombre().toUpperCase())
                .lore(lore)
                .build();
    }

    private ItemStack buildItemInfo() {
        return ItemBuilder.of(Material.PAPER)
                .title(GOLD + "" + BOLD + "INFO")
                .lore(List.of(
                        "Puedes contratar empleados, ",
                        "despedirlos etc. Comandos:",
                        "                              ",
                        "/empresas contratar <jugador>",
                        " <empresa> <sueldo> <tipo sueldo>",
                        "El tipo sueldo es la frequencia con",
                        "la que el sueldo se va a pagar: ",
                        "  d: cada dia",
                        "  s: cada semana",
                        "  2s cada dos semanas",
                        "  m: cada mes",
                        "   ",
                        "Para mas info: /ayuda empresario o",
                        "http://serversurvival.ddns.net/perfil"
                ))
                .build();
    }

    @Override
    public void afterShow() {
        for (ItemStack itemEmpleado : super.getItemsByItemNum(6)) {
            SkullMeta currentItemMeta = (SkullMeta) itemEmpleado.getItemMeta();
            String empleadoNombre = ItemUtils.getLore(itemEmpleado, 1).split(" ")[1];
            currentItemMeta.setOwningPlayer(Bukkit.getOfflinePlayer(empleadoNombre));

            itemEmpleado.setItemMeta(currentItemMeta);
        }
    }
}
