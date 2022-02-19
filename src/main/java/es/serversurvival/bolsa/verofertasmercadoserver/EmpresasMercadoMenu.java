package es.serversurvival.bolsa.verofertasmercadoserver;

import es.serversurvival.bolsa.cancelarofertamercadoserver.CancelarOfertaAccionServerUseCase;
import es.serversurvival.bolsa.comprarofertasmercadoserver.ComprarAccionesServerConfirmacion;
import es.serversurvival.jugadores.perfil.PerfilMenu;
import es.serversurvival._shared.menus.Menu;
import es.serversurvival._shared.menus.inventory.InventoryCreator;
import es.serversurvival._shared.menus.Clickable;
import es.serversurvival._shared.menus.Paginated;
import es.serversurvival._shared.utils.Funciones;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static es.serversurvival._shared.utils.Funciones.enviarMensajeYSonido;
import static org.bukkit.ChatColor.AQUA;
import static org.bukkit.ChatColor.GOLD;

public class EmpresasMercadoMenu extends Menu implements Clickable, Paginated {
    private final CancelarOfertaAccionServerUseCase cancelarOfertaUseaCase = CancelarOfertaAccionServerUseCase.INSTANCE;

    private EmpresasMercadoInventoryFactory inventoryFactory = new EmpresasMercadoInventoryFactory();
    private Inventory inventory;
    private final Player player;
    private int currentIndex = 0;
    private List<Page> pages = new ArrayList<>();

    public EmpresasMercadoMenu(Player player) {

        this.player = player;
        this.inventory = InventoryCreator.createInventoryMenu(inventoryFactory, player.getName());
        this.pages.add(new Page(0, inventory));

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
        ItemStack clikedItem = event.getCurrentItem();

        boolean notValidClick = clikedItem == null || !Funciones.cuincideNombre(clikedItem.getType().toString(), "RED_BANNER", "BLUE_BANNER", "GREEN_BANNER") || clikedItem.getItemMeta().getLore().get(1) == null;
        if(notValidClick){
            return;
        }

        List<String> loreItemClicked = clikedItem.getItemMeta().getLore();
        int id = Integer.parseInt(loreItemClicked.get(loreItemClicked.size() - 1));
        String empresa = loreItemClicked.get(1).split(" ")[1];

        if(clikedItem.getType() == Material.RED_BANNER){ //Red banner -> la oferta es del jugador
            cancelarOfertaUseaCase.cancelar(player.getName(), id);

            enviarMensajeYSonido(player, GOLD + "Has cancelado tu oferta en el mercado. Ahora vuelves a tener esas acciones en tu cartera: " + AQUA + "/bolsa cartera",
                    Sound.ENTITY_PLAYER_LEVELUP);

            closeMenu();
        }else if (clikedItem.getType() == Material.BLUE_BANNER || clikedItem.getType() == Material.GREEN_BANNER) {
            ComprarAccionesServerConfirmacion conrimracin = new ComprarAccionesServerConfirmacion(player, id);
        }
    }

    @Override
    public void goFordward() {
        if(weAreInTheLasPage()){
            Inventory newInventory = inventoryFactory.buildInventoryExecess();
            this.inventory = newInventory;
            pages.add(new Page(currentIndex + 1, newInventory));
        }else{
            Page newPage = pages.get(currentIndex + 1);
            this.inventory = newPage.inventory;
        }

        currentIndex++;
        openMenu();
    }

    private boolean weAreInTheLasPage() {
        return this.currentIndex + 1 >= this.pages.size();
    }

    @Override
    public void goBack() {
        if(currentIndex == 0){
            PerfilMenu menu = new PerfilMenu(player);
        }else{
            this.inventory = pages.get(currentIndex - 1).inventory;
            currentIndex--;

            openMenu();
        }
    }

    @Override
    public int getCurrentIndex() {
        return currentIndex;
    }

    @Override
    public List<Page> getPages() {
        return pages;
    }

    @Override
    public String getNameItemGoBack() {
        return ITEM_NAME_GOBACK;
    }

    @Override
    public String getNameItemGoFordward() {
        return ITEM_NAME_GOFORDWARD;
    }
}
