package es.serversurvival.menus.menus;

import es.serversurvival.menus.Menu;
import es.serversurvival.menus.inventoryFactory.InventoryCreator;
import es.serversurvival.menus.inventoryFactory.inventories.EmpresasMercadoInventoryFactory;
import es.serversurvival.menus.menus.confirmaciones.ComprarAccionesServerConfirmacion;
import es.serversurvival.menus.menus.confirmaciones.ComprarBolsaConfirmacion;
import es.serversurvival.mySQL.MySQL;
import es.serversurvival.mySQL.enums.TipoActivo;
import es.serversurvival.util.Funciones;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class EmpresasMercadoMenu extends Menu implements Clickable, Paginated{
    private EmpresasMercadoInventoryFactory inventoryFactory = new EmpresasMercadoInventoryFactory();
    private Inventory inventory;
    private final Player player;
    private int currentIndex = 0;
    private List<Page> pages = new ArrayList<>();

    public EmpresasMercadoMenu(Player player) {
        MySQL.conectar();

        this.player = player;
        this.inventory = InventoryCreator.createInventoryMenu(inventoryFactory, player.getName());
        this.pages.add(new Page(0, inventory));

        MySQL.desconectar();

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
            MySQL.conectar();
            ofertasMercadoServerMySQL.cancelarOferta(player, id);
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
            menu.openMenu();
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
