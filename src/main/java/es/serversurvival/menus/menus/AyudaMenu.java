package es.serversurvival.menus.menus;

import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.comandos.subComandos.ayuda.*;
import es.serversurvival.menus.Menu;
import es.serversurvival.menus.inventoryFactory.InventoryCreator;
import es.serversurvival.menus.inventoryFactory.inventories.AyudaMenuInventoryFactory;
import es.serversurvival.util.Funciones;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;
import java.util.List;

public class AyudaMenu extends Menu implements Clickable {
    private Inventory inventory;
    private Player player;

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

        String tipoAyuda = getTipoAyuda(event.getCurrentItem().getItemMeta().getDisplayName());
        if(tipoAyuda != null){
            CommandRunner ayudaSubCommand = getCommandOfAyuda(tipoAyuda);
            ayudaSubCommand.execute(event.getWhoClicked(), null);

            ((Player) event.getWhoClicked()).performCommand("/ayuda " + tipoAyuda.toLowerCase());
            event.getWhoClicked().closeInventory();
        }
    }

    private CommandRunner getCommandOfAyuda (String nombreComando) {
        switch ((nombreComando)) {
            case "JUGAR":
                return new JugarAyuda();
            case "PIXELCOINS":
                return new DineroAyuda();
            case "NORMAS":
                return new NormasAyuda();
            case "TIENDA":
                return new TiendaAyuda();
            case "DEUDA":
                return new DeudaAyuda();
            case "EMPRESARIO":
                return new EmpresarioAyuda();
            case "EMPLEO":
                return new EmpleoAyuda();
            case "BOLSA":
                return new BolsaAyuda();
            default:
                return null;
        }
    }

    private String getTipoAyuda (String title) {
        List<String> ayudas = Arrays.asList("JUGAR", "PIXELCOINS", "NORMAS", "TIENDA", "DEUDA", "EMPRESARIO", "EMPLEO");

        for(String ayuda : ayudas)
            if (title.endsWith(ayuda))
                return ayuda;

        return null;
    }
}
