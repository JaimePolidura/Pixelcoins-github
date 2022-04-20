package es.serversurvival.ayuda.verayudas;

import es.jaimetruman.commands.CommandRunner;
import es.jaimetruman.commands.CommandRunnerNonArgs;
import es.serversurvival.ayuda.*;
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

        Optional<String> tipoAyuda = getTipoAyuda(event.getCurrentItem().getItemMeta().getDisplayName());
        if(!tipoAyuda.isPresent()){
            CommandRunner ayudaSubCommand = getCommandOfAyuda(tipoAyuda.get());

            ((CommandRunnerNonArgs) ayudaSubCommand).execute(this.getPlayer());

            ((Player) event.getWhoClicked()).performCommand("/ayuda " + tipoAyuda.get().toLowerCase());
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

    private Optional<String> getTipoAyuda (String title) {
        List<String> ayudas = Arrays.asList("JUGAR", "PIXELCOINS", "NORMAS", "TIENDA", "DEUDA", "EMPRESARIO", "EMPLEO");

        return ayudas.stream()
                .filter(title::endsWith)
                .findAny();
    }
}
