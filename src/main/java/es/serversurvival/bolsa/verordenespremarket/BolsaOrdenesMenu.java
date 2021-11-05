package es.serversurvival.bolsa.verordenespremarket;

import es.serversurvival.bolsa.cancelarorderpremarket.CancelarOrdenUseCase;
import es.serversurvival._shared.menus.Menu;
import es.serversurvival._shared.menus.inventory.InventoryCreator;
import es.serversurvival._shared.menus.Clickable;
import es.serversurvival._shared.utils.Funciones;
import io.vavr.control.Try;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class BolsaOrdenesMenu extends Menu implements Clickable {
    private final CancelarOrdenUseCase cancelarOrdenUseCase = CancelarOrdenUseCase.INSTANCE;

    private final Inventory inventory;
    private final Player player;

    public BolsaOrdenesMenu(Player player) {
        this.player = player;
        this.inventory = InventoryCreator.createInventoryMenu(new BolsaOrdenesInventoryFactory(), player.getName());

        openMenu();
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public void onOherClick(InventoryClickEvent event) {
        ItemStack itemClicked = event.getCurrentItem();

        if(Funciones.noEsDeTipoItem(itemClicked, "NAME_TAG", "REDSTONE_TORCH")){
            return;
        }

        Try<Integer> idTry = getIdOrdenFromItem(itemClicked);
        if(idTry.isSuccess()){
            cancelarOrdenUseCase.cancelar(idTry.get());
            player.sendMessage(ChatColor.RED + "Has cancelado la orden");

            closeMenu();
        }
    }

    private Try<Integer> getIdOrdenFromItem(ItemStack item) {
        return Try.of(() -> Integer.parseInt(item.getItemMeta().getLore().get(5)));
    }
}
