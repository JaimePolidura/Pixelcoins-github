package es.serversurvival.ayuda.verayudas;

import es.serversurvival._shared.menus.Menu;
import es.serversurvival._shared.menus.inventory.InventoryCreator;
import es.serversurvival._shared.menus.Clickable;
import es.serversurvival._shared.utils.Funciones;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class AyudaMenu extends Menu implements Clickable {
    private final Inventory inventory;
    private final Player player;

    public AyudaMenu (Player player) {
        this.player = player;
        this.inventory = InventoryCreator.createInventoryMenu(new AyudaMenuInventoryFactory(), player.getName());
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
        if(event.getCurrentItem() == null || Funciones.noEsDeTipoItem(event.getCurrentItem(), "WRITABLE_BOOK")){
            return;
        }

        Optional<String> tipoAyuda = getTipoAyuda(event.getCurrentItem().getItemMeta().getDisplayName());
        if(!tipoAyuda.isPresent()){
            player.performCommand(getCommandOfAyuda(tipoAyuda.get()));

            event.getWhoClicked().closeInventory();
        }
    }

    private String getCommandOfAyuda (String nombreComando) {
        switch ((nombreComando)) {
            case "JUGAR":
                return "jugar";
            case "PIXELCOINS":
                //TODO return ayuda help
                return "ayuda";
            case "NORMAS":
                return "normas";
            case "TIENDA":
                //TODO return tienda help
                return "tienda help";
            case "DEUDA":
                return "deudas help";
            case "EMPRESARIO":
                return "empresas help";
            case "EMPLEO":
                return "empleados help";
            case "BOLSA":
                return "bolsa help";
            default:
                return "ayuda";
        }
    }

    private Optional<String> getTipoAyuda (String title) {
        List<String> ayudas = Arrays.asList("JUGAR", "PIXELCOINS", "NORMAS", "TIENDA", "DEUDA", "EMPRESARIO", "EMPLEO");

        return ayudas.stream()
                .filter(title::endsWith)
                .findAny();
    }
}
