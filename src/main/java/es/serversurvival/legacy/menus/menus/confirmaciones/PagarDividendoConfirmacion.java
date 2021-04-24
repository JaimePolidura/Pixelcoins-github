package es.serversurvival.legacy.menus.menus.confirmaciones;

import es.jaimetruman.ItemBuilder;
import es.serversurvival.legacy.menus.Menu;
import es.serversurvival.legacy.mySQL.AllMySQLTablesInstances;
import es.serversurvival.legacy.mySQL.tablasObjetos.Empresa;
import es.serversurvival.legacy.util.Funciones;
import es.serversurvival.legacy.menus.inventoryFactory.InventoryCreator;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.ChatColor.*;

public class PagarDividendoConfirmacion extends Menu implements AumentoConfirmacion{
    private final Inventory inventory;
    private final Player player;
    private int accionesTotales;
    private Empresa empresa;
    private int dividendoPorAccion;

    public PagarDividendoConfirmacion (Player player, String nombreEmpresa) {

        this.accionesTotales = AllMySQLTablesInstances.ofertasMercadoServerMySQL.getAccionesTotalesParaPagarDividendo(nombreEmpresa);
        this.empresa = AllMySQLTablesInstances.empresasMySQL.getEmpresa(nombreEmpresa);

        this.player = player;
        this.dividendoPorAccion = 1;

        String titulo = DARK_RED + "" + BOLD + "   REPARTIR DIVIDENDOS ";
        List<String> lore = new ArrayList<>();
        lore.add(GOLD + "Repartir " + GREEN + AllMySQLTablesInstances.formatea.format(dividendoPorAccion) + " PC" + GOLD + " por accion");
        lore.add(GOLD + "Que seria un gasto total de " + GREEN + AllMySQLTablesInstances.formatea.format(accionesTotales * dividendoPorAccion) + " PC");
        lore.add(GOLD + "La empresa tiene " + GREEN + AllMySQLTablesInstances.formatea.format(empresa.getPixelcoins()) + " PC " + GOLD + "en caja");
        String nombreItemAceptar = GREEN + "" + BOLD + "REPARTIR";
        String itemCancelar = RED + "" + BOLD + "CANCELAR";

        this.inventory = InventoryCreator.createConfirmacionAumento(titulo, nombreItemAceptar, lore, itemCancelar);

        openMenu();
    }

    @Override
    public void onChangeAumento(int var) {
        int nuevoDividendo = var + dividendoPorAccion;

        if(nuevoDividendo * accionesTotales >= empresa.getPixelcoins() || nuevoDividendo <= 0){
            return;
        }

        this.dividendoPorAccion = nuevoDividendo;

        String nombreItemAceptar =  GREEN + "" + BOLD + "   REPARTIR";
        List<String> loreAcepatr = new ArrayList<>();
        loreAcepatr.add(GOLD + "Repartir " + GREEN + AllMySQLTablesInstances.formatea.format(dividendoPorAccion) + " PC" + GOLD + " por accion");
        loreAcepatr.add(GOLD + "Que seria un gasto de " + GREEN + AllMySQLTablesInstances.formatea.format(accionesTotales * dividendoPorAccion) + " PC");
        loreAcepatr.add(GOLD + "La empresa tiene " + GREEN + AllMySQLTablesInstances.formatea.format(empresa.getPixelcoins()) + " PC " + GOLD + "en caja");

        ItemBuilder.of(Material.GREEN_WOOL).title(nombreItemAceptar).lore(loreAcepatr).buildAddInventory(inventory, 14);
    }

    @Override
    public void confirmar() {
        AllMySQLTablesInstances.transaccionesMySQL.pagarDividendoAccionServer(player, empresa.getNombre(), dividendoPorAccion, accionesTotales * dividendoPorAccion);

        Funciones.enviarMensajeYSonido(player, GOLD + "Se han pagado todos los dividendos", Sound.ENTITY_PLAYER_LEVELUP);

        closeMenu();
    }

    @Override
    public void cancelar() {
        player.sendMessage(GOLD + "Has cancelado la compra");

        closeMenu();
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public Player getPlayer() {
        return player;
    }
}
