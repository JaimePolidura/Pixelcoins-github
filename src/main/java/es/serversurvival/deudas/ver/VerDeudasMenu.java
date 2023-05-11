package es.serversurvival.deudas.ver;

import es.bukkitbettermenus.Menu;
import es.bukkitbettermenus.MenuService;
import es.bukkitbettermenus.configuration.MenuConfiguration;
import es.bukkitbettermenus.modules.pagination.PaginationConfiguration;
import es.bukkitclassmapper._shared.utils.ItemBuilder;
import es.bukkitclassmapper._shared.utils.ItemUtils;
import es.serversurvival.deudas._shared.application.DeudasService;
import es.serversurvival.deudas._shared.domain.Deuda;
import es.serversurvival.deudas.cancelar.CancelarDeudaUseCase;
import es.serversurvival.deudas.pagarTodo.PagarDeudaCompletaUseCase;
import es.serversurvival.jugadores.perfil.PerfilMenu;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

import static es.serversurvival._shared.utils.Funciones.FORMATEA;
import static org.bukkit.ChatColor.*;

@AllArgsConstructor
public final class VerDeudasMenu extends Menu {
    private static final String TITULO = DARK_RED + "" + BOLD + "        TUS DEUDAS";

    private final PagarDeudaCompletaUseCase pagarDeudaUseCase;
    private final CancelarDeudaUseCase cancelarDeudaUseCase;
    private final DeudasService deudasService;
    private final MenuService menuService;

    @Override
    public int[][] items() {
        return new int[][] {
                {1, 0, 0, 0, 0, 0, 0, 0, 0  },
                {2, 0, 0, 0, 0, 0, 0, 0, 0  },
                {0, 0, 0, 0, 0, 0, 7, 8, 9  }
        };
    }

    @Override
    public MenuConfiguration configuration() {
        return MenuConfiguration.builder()
                .fixedItems()
                .title(TITULO)
                .item(1, buildItemInfo())
                .items(2, this::buildItemsDeudas, this::onDeudaClicked)
                .breakpoint(7, buildItemGoBackToProfile(), this::goBackToProfile)
                .paginated(PaginationConfiguration.builder()
                        .backward(8, Material.RED_WOOL)
                        .forward(9, Material.GREEN_WOOL)
                        .build())

                .build();
    }

    private void onDeudaClicked(Player player, InventoryClickEvent event) {
        UUID idDeuda = UUID.fromString(ItemUtils.getLore(event.getCurrentItem(), 6));
        boolean esAcredor = event.getCurrentItem().getType() == Material.GREEN_BANNER;

        if(esAcredor){
            cancelarDeuda(player, idDeuda);
        } else {
            pagarDeudaComleto(player, idDeuda);
        }
    }

    private void pagarDeudaComleto(Player player, UUID idDeuda) {
        this.pagarDeudaUseCase.pagarDeuda(idDeuda, player.getName());
        player.sendMessage(GOLD + "Has pagado toda la deuda");
        player.closeInventory();
    }

    private void cancelarDeuda(Player player, UUID idDeuda) {
        this.cancelarDeudaUseCase.cancelarDeuda(player.getName(), idDeuda);
        player.sendMessage(GOLD + "Has pagado cancelado la deuda");
        player.closeInventory();
    }
    
    private void goBackToProfile(Player player, InventoryClickEvent event) {
        this.menuService.open(player, new PerfilMenu(player.getName()));
    }

    private List<ItemStack> buildItemsDeudas(Player player) {
        return this.deudasService.findByJugador(player.getName()).stream()
                .map(deuda -> this.buildItemDeuda(deuda, player))
                .toList();
    }

    private ItemStack buildItemDeuda(Deuda deuda, Player player) {
        boolean esAcredor = deuda.getAcredor().equalsIgnoreCase(player.getName());

        return ItemBuilder.of(esAcredor ? Material.GREEN_BANNER : Material.RED_BANNER)
                .title(GOLD + "" + BOLD + "CLICK PARA " + (esAcredor ? "CANCELAR" : "PAGAR"))
                .lore(List.of(
                        GOLD + (esAcredor ? "Te debe" : "Debes"),
                        GOLD + "Acredor: " + deuda.getAcredor(),
                        GOLD + "Deudor: " + deuda.getDeudor(),
                        GOLD + "Pixelcoins: " + GREEN + FORMATEA.format(deuda.getPixelcoinsRestantes()) + " PC",
                        GOLD + "Dias restantes: " + deuda.getTiempoRestante(),
                        "   ",
                        String.valueOf(deuda.getDeudaId())
                ))
                .build();
    }

    private ItemStack buildItemGoBackToProfile() {
        return ItemBuilder.of(Material.RED_BANNER).title(RED + "" + BOLD + "Go to profile").build();
    }

    private ItemStack buildItemInfo() {
        return ItemBuilder.of(Material.PAPER)
                .title(ChatColor.GOLD + "" + ChatColor.BOLD + "INFO")
                .lore(List.of(
                        "Para condecer prestamos has de hacer",
                        "/deudas prestar <nombreAccionista> <dinero> <dias> [interes]",
                        "<nombreAccionista> -> al nombreAccionista que vas a endeudar",
                        "<dinero> -> total de pixelcoins  a prestar",
                        "<dias> -> dias en el que vencera el prestamo",
                        "   cuando acabe de pagar la deuda el nombreAccionista",
                        "[interes] -> porcentaje añadido a la cantidad",
                        "de pixelcoins prestadas que te tendra que devolver",
                        "en un plazo de tiempo puesto en <dias>.",
                        "No es obligatorio, por defecto es 0",
                        "   ",
                        "Mas info en /deudas ayuda o en la web",
                        "http://serversurvival.ddns.net/perfil"
                ))
                .build();
    }
}
