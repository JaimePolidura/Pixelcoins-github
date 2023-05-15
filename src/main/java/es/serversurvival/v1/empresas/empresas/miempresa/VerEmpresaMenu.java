package es.serversurvival.v1.empresas.empresas.miempresa;

import es.bukkitbettermenus.Menu;
import es.bukkitbettermenus.MenuService;
import es.bukkitbettermenus.configuration.MenuConfiguration;
import es.bukkitbettermenus.menustate.AfterShow;
import es.bukkitbettermenus.modules.pagination.PaginationConfiguration;
import es.bukkitclassmapper._shared.utils.ItemBuilder;
import es.bukkitclassmapper._shared.utils.ItemUtils;
import es.serversurvival.v1.empresas.accionistasserver._shared.application.AccionistasServerService;
import es.serversurvival.v1.empresas.empleados._shared.application.EmpleadosService;
import es.serversurvival.v1.empresas.empleados._shared.domain.Empleado;
import es.serversurvival.v1.empresas.empleados.despedir.DespedirEmpleadoUseCase;
import es.serversurvival.v1.empresas.empresas._shared.domain.Empresa;
import es.serversurvival.v1.empresas.empresas.borrar.BorrarEmpresaConfirmacionMenu;
import es.serversurvival.v1.empresas.empresas.borrar.BorrarEmpresaConfirmacionMenuState;
import es.serversurvival.v1.empresas.empresas.pagardividendos.PagarDividendosConfirmacionMenu;
import es.serversurvival.v1.jugadores.perfil.PerfilMenu;
import es.serversurvival.v1.mensajes._shared.application.EnviadorMensajes;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static es.serversurvival.v1._shared.utils.Funciones.*;
import static es.serversurvival.v1._shared.utils.Funciones.FORMATEA;
import static java.lang.String.format;
import static org.bukkit.ChatColor.*;
import static org.bukkit.ChatColor.BOLD;
import static org.bukkit.ChatColor.DARK_RED;

@AllArgsConstructor
public final class VerEmpresaMenu extends Menu<Empresa> implements AfterShow {
    private final AccionistasServerService accionistasServerService;
    private final DespedirEmpleadoUseCase despedirEmpleadoUseCase;
    private final EnviadorMensajes enviadorMensajes;
    private final EmpleadosService empleadosService;
    private final MenuService menuService;

    @Override
    public int[][] items() {
        return new int[][] {
                {1, 2, 0, 0, 0, 0, 0, 0, 5},
                {6, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 7, 8, 9}
        };
    }

    @Override
    public MenuConfiguration configuration() {
        return MenuConfiguration.builder()
                .fixedItems()
                .title(format(DARK_RED + "" + BOLD + "        Tu empresa %s", this.getState().getNombre()))
                .item(1, buildItemInfo())
                .item(2, buildItemEmpresaStats())
                .item(3, buildItemAccionistas())
                .item(4, buildItemRepartirDividendo(), this::pagarDividendos)
                .item(5, buildItemBorrarEmpresa(), this::abrirBorrarEmpresaConfirmacion)
                .items(6, buildItemEmpleados(), this::despedirEmpleado)
                .breakpoint(7, Material.GREEN_BANNER, this::goBackToProfileMenu)
                .paginated(PaginationConfiguration.builder()
                        .forward(9, Material.GREEN_WOOL)
                        .backward(8, Material.RED_WOOL)
                        .build())
                .build();
    }

    private void pagarDividendos(Player player, InventoryClickEvent event) {
        this.menuService.open(player, PagarDividendosConfirmacionMenu.class, this.getState());
    }

    private void abrirBorrarEmpresaConfirmacion(Player player, InventoryClickEvent event) {
        this.menuService.open(player, BorrarEmpresaConfirmacionMenu.class, new BorrarEmpresaConfirmacionMenuState(this.getState().getNombre()));
    }

    private void despedirEmpleado(Player player, InventoryClickEvent event) {
        String empleadoNombre = ItemUtils.getLore(event.getCurrentItem(), 1).split(" ")[1];

        this.despedirEmpleadoUseCase.despedir(player.getName(), empleadoNombre, getState().getNombre(), "Despedido");
        player.sendMessage(ChatColor.GOLD + "Has despedido a: " + empleadoNombre);
        String mensajeOnline = ChatColor.RED + "Has sido despedido de " + getState().getNombre() + " razon: " + "Despedido";
        enviadorMensajes.enviarMensaje(empleadoNombre, mensajeOnline, mensajeOnline, Sound.BLOCK_ANVIL_LAND, 10, 1);

        super.deleteItem(event.getSlot(), super.getActualPageNumber());
    }

    private void goBackToProfileMenu(Player player, InventoryClickEvent event) {
        this.menuService.open(player, PerfilMenu.class);
    }

    private List<ItemStack> buildItemEmpleados() {
        return this.empleadosService.findByEmpresa(this.getState().getNombre()).stream()
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
                        "/empresas editarempleado " + getState().getNombre() + " " + empleado.getNombre(),
                        "  sueldo <sueldo>",
                        "/empresas editarempleado " + getState().getNombre() + " " + empleado.getNombre(),
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
        List<String> loreAccionistas = accionistasServerService.findByEmpresa(getState().getNombre()).stream()
                .map(posicion -> new Accionista(posicion.getNombreAccionista(), posicion.getCantidad()))
                .collect(Collectors.toMap(Accionista::nombreAccionista, Accionista::cantidad, Integer::sum))
                .entrySet().stream()
                .map(accionista -> new Accionista(accionista.getKey(), accionista.getValue()))
                .sorted()
                .map(this::getLoreAccionista)
                .toList();

        return ItemBuilder.of(Material.NETHERITE_SCRAP)
                .title(GOLD + "" + BOLD + "ACCIONISTAS")
                .lore(loreAccionistas)
                .build();
    }

    private record Accionista(String nombreAccionista, int cantidad) implements  Comparable<Accionista>{
        @Override
        public int compareTo(Accionista o) {
            return o.cantidad - cantidad;
        }
    }

    private String getLoreAccionista(Accionista accionista) {
        double ownership = (double)accionista.cantidad() / (double)getState().getAccionesTotales();
        double percentajeOwnership = redondeoDecimales(ownership * 100, 1);

        return GOLD + accionista.nombreAccionista() + ": " + GREEN + FORMATEA.format(percentajeOwnership) + "%";
    }

    private ItemStack buildItemEmpresaStats() {
        List<String> lore = new ArrayList<>();
        lore.add(GOLD + "Descripccion:");
        lore.add(1, GOLD + "" + getState().getDescripcion());
        lore.add("  ");
        lore.add(GOLD + "Pixelcoins: " + GREEN + FORMATEA.format(getState().getPixelcoins()) + " PC");
        lore.add(GOLD + "Ingresos: " + GREEN + FORMATEA.format(getState().getIngresos()) + " PC");
        lore.add(GOLD + "Gastos: " + GREEN + FORMATEA.format(getState().getGastos()) + " PC");
        double beneficiosPerdidas = getState().getIngresos() - getState().getGastos();
        if(beneficiosPerdidas >= 0){
            lore.add(GOLD + "Beneficios: "  +  GREEN + "+" +  FORMATEA.format(beneficiosPerdidas) + " PC");
            lore.add(GOLD + "Rentabilidad: " + GREEN + "+" + redondeoDecimales(rentabilidad(getState().getIngresos(), beneficiosPerdidas),1) + "%");
        }else{
            lore.add(GOLD + "Perdidas: " + RED + FORMATEA.format(beneficiosPerdidas) + " PC");
            lore.add(GOLD + "Rentabilidad: " + RED + redondeoDecimales(rentabilidad(getState().getIngresos(), beneficiosPerdidas),1) + "%");
        }
        lore.add("   ");
        lore.add("/empresas depositar " + getState().getNombre() + " <pixelcoins>");
        lore.add("/empresas sacar " + getState().getNombre() + " <pixelcoins>");
        lore.add("/empresas logotipo " + getState().getNombre() + "");
        lore.add("/empresas editardescripccion " + getState().getNombre());
        lore.add("  <nueva desc>");
        lore.add("/empresas editarnombre " + getState().getNombre() + " <nombre>");
        lore.add("Mas info en /ayuda empresario o:");
        lore.add("http://serversurvival.ddns.net/perfil");

        return ItemBuilder.of(Material.WRITABLE_BOOK)
                .title(GOLD + "" + BOLD + "" + getState().getNombre().toUpperCase())
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
                        "/empresas contratar <nombreAccionista>",
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
    public void afterShow(Player player) {
        if(getState().isCotizada()){
            super.getActualPage().setItem(2, buildItemAccionistas(), 3);
            super.getActualPage().setItem(3, buildItemRepartirDividendo(), 4);
        }

        for (ItemStack itemEmpleado : super.getItemsByItemNum(6)) {
            SkullMeta currentItemMeta = (SkullMeta) itemEmpleado.getItemMeta();
            String empleadoNombre = ItemUtils.getLore(itemEmpleado, 1).split(" ")[1];
            currentItemMeta.setOwningPlayer(Bukkit.getOfflinePlayer(empleadoNombre));

            itemEmpleado.setItemMeta(currentItemMeta);
        }
    }
}
