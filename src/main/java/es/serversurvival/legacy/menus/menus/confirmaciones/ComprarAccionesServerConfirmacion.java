package es.serversurvival.legacy.menus.menus.confirmaciones;

import es.jaimetruman.ItemBuilder;
import es.serversurvival.legacy.menus.Menu;
import es.serversurvival.legacy.mySQL.AllMySQLTablesInstances;
import es.serversurvival.legacy.mySQL.tablasObjetos.OfertaMercadoServer;
import es.serversurvival.nfs.utils.Funciones;
import es.serversurvival.legacy.menus.inventoryFactory.InventoryCreator;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.ChatColor.*;
import static org.bukkit.ChatColor.GREEN;

public class ComprarAccionesServerConfirmacion extends Menu implements AumentoConfirmacion{
    private final String nombreEmpresa;
    private double precioTotal;
    private final Inventory inventory;
    private final Player player;
    private OfertaMercadoServer oferta;
    private double dineroJugador;

    private int cantidadAComprar;

    public ComprarAccionesServerConfirmacion(Player player, int id) {
        this.player = player;

        OfertaMercadoServer oferta = AllMySQLTablesInstances.ofertasMercadoServerMySQL.get(id);

        this.oferta = oferta;
        this.dineroJugador = AllMySQLTablesInstances.jugadoresMySQL.getJugador(player.getName()).getPixelcoins();
        this.nombreEmpresa = oferta.getEmpresa();
        this.cantidadAComprar = 1;

        String titulo = DARK_RED + "" + BOLD + "   SELECCIONA ACCIONES";
        String tituloAceptar = GREEN + "" + BOLD + "COMPRAR ACCIONES";
        String tituloCancelar = RED + "" + BOLD + "CANCELAR";
        List<String> lore = new ArrayList<>();
        lore.add(GOLD + "Comprar 1 acciones de " + nombreEmpresa + " a " + GREEN + AllMySQLTablesInstances.formatea.format(oferta.getPrecio()) + " PC");

        this.inventory = InventoryCreator.createConfirmacionAumento(titulo, tituloAceptar, lore, tituloCancelar);

        openMenu();
    }

    @Override
    public void onChangeAumento(int var) {
        int nuevaCantidad = var + this.cantidadAComprar;
        double nuevoPrecioTotal = oferta.getPrecio() * nuevaCantidad;

        if(nuevoPrecioTotal > dineroJugador || nuevoPrecioTotal <= 0 || nuevaCantidad > oferta.getCantidad() || nuevaCantidad <= 0){
            return;
        }

        this.cantidadAComprar = nuevaCantidad;
        this.precioTotal = nuevoPrecioTotal;

        String displayName = GREEN + "" + BOLD + "COMPRAR ACCIONES";
        List<String> lore = new ArrayList<>();
        lore.add(GOLD + "Comprar " + nuevaCantidad + " acciones " + nombreEmpresa + " a " + GREEN + oferta.getPrecio() + " PC -> total: " + AllMySQLTablesInstances.formatea.format(Funciones.redondeoDecimales(precioTotal, 3)) + " PC");

        ItemBuilder.of(Material.GREEN_WOOL).title(displayName).lore(lore).buildAddInventory(inventory, 14);
    }

    @Override
    public void confirmar() {
        if (dineroJugador < precioTotal) {
            player.sendMessage(DARK_RED + "No tienes el suficiente dinero");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
            closeMenu();
            return;
        }

        AllMySQLTablesInstances.transaccionesMySQL.comprarOfertaMercadoAccionServer(player, oferta.getId(), this.cantidadAComprar);

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
