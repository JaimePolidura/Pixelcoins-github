package es.serversurvival.menus.menus.confirmaciones;

import es.serversurvival.mySQL.MySQL;
import es.serversurvival.util.Funciones;
import es.serversurvival.menus.Menu;
import es.serversurvival.menus.inventoryFactory.InventoryCreator;
import es.serversurvival.util.MinecraftUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ComprarBolsaConfirmacion extends Menu implements Confirmacion {
    private String titulo;
    private String simbolo;
    private String destinatario;
    private double precioUnidad;
    private double precioTotal;
    private String tipo;
    private String alias;
    private Inventory inventory;
    private Player player;
    private int cantidad;
    private double dineroJugador;
    private String nombreValor;

    public ComprarBolsaConfirmacion(String simbolo, String nombreValor, String tipo, String alias, String destinatario, double precioUnidad) {
        this.nombreValor = nombreValor;
        this.alias = alias;
        this.tipo = tipo;
        this.simbolo = simbolo;
        this.destinatario = destinatario;
        this.precioUnidad = precioUnidad;
        this.player = Bukkit.getPlayer(destinatario);
        this.cantidad = 1;
        this.precioTotal = precioUnidad;

        this.inventory = InventoryCreator.createConfirmacionAumento(alias, simbolo, precioUnidad);

        MySQL.conectar();
        this.dineroJugador = jugadoresMySQL.getJugador(player.getName()).getPixelcoins();
        MySQL.desconectar();

        openMenu();
    }

    @Override
    public void onClick (InventoryClickEvent event) {
        if(event.getCurrentItem() == null) return;

        String nombreItem = event.getCurrentItem().getType().toString();

        switch (nombreItem){
            case "GREEN_WOOL":
                confirmar();
                break;
            case "RED_WOOL":
                cancelar();
                break;
            default:
                updateCantidad(event.getCurrentItem());
        }
    }

    private void updateCantidad(ItemStack itemStack) {
        if(itemStack == null || Funciones.noEsDeTipoItem(itemStack,"LIGHT_GRAY_BANNER" )){
            return;
        }

        String name = itemStack.getItemMeta().getDisplayName();
        StringBuilder stringBuild = new StringBuilder();
        for (int i = 0; i < name.length(); i++) {
            if(i == 2){
                if(name.charAt(i) == '-')
                    stringBuild.append(name.charAt(2));
            }else if (i >= 2){
                stringBuild.append(name.charAt(i));
            }
        }
        int nuevasAcciones = Integer.parseInt(stringBuild.toString());
        this.cantidad = cantidad + nuevasAcciones;

        precioTotal = precioUnidad * cantidad;
        if(precioTotal > dineroJugador){
            return;
        }

        if (precioTotal <= 0) {
            cantidad = cantidad - nuevasAcciones;
            precioTotal = precioUnidad * cantidad;
            return;
        }
        String displayName = ChatColor.GREEN + "" + ChatColor.BOLD + "COMPRAR " + alias.toUpperCase();
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GOLD + "Comprar " + cantidad + " " +  alias  + " " + simbolo + " a " + ChatColor.GREEN + precioUnidad + " PC -> total: " + formatea.format(Funciones.redondeoDecimales(precioTotal, 3)) + " PC");

        this.inventory.setItem(14, MinecraftUtils.loreDisplayName(Material.GREEN_WOOL, displayName, lore));
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
    public void confirmar() {
        if(dineroJugador < precioTotal){
            player.sendMessage(ChatColor.DARK_RED + "No tienes el suficiente dinero");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
            closeMenu();
            return;
        }

        MySQL.conectar();
        transaccionesMySQL.comprarUnidadBolsa(tipo.toUpperCase(), simbolo, nombreValor, alias, precioUnidad, cantidad, player);
        MySQL.desconectar();

        closeMenu();
    }

    @Override
    public void cancelar() {
        player.sendMessage(ChatColor.GOLD + "Has cancelado la compra");

        closeMenu();
    }
}
