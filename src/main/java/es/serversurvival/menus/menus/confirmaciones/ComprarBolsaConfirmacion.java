package es.serversurvival.menus.menus.confirmaciones;

import es.serversurvival.mySQL.MySQL;
import es.serversurvival.mySQL.enums.TipoActivo;
import es.serversurvival.menus.Menu;
import es.serversurvival.menus.inventoryFactory.InventoryCreator;
import es.serversurvival.mySQL.tablasObjetos.OfertaMercadoServer;
import es.serversurvival.util.MinecraftUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static es.serversurvival.util.Funciones.*;

public class ComprarBolsaConfirmacion extends Menu implements Confirmacion {
    private final String simbolo;
    private double precioUnidad;
    private double precioTotal;
    private final TipoActivo tipoActivo;
    private final String alias;
    private final Inventory inventory;
    private final Player player;
    private int cantidadAComprar = 1;
    private double dineroJugador;
    private final String nombreValor;
    private int maxAccionesAComprar = 999999;
    private int id;

    public ComprarBolsaConfirmacion(String simbolo, String nombreValor, TipoActivo tipoActivo, String alias, Player player, double precioUnidad) {
        this.nombreValor = nombreValor;
        this.alias = alias;
        this.tipoActivo = tipoActivo;
        this.simbolo = simbolo;
        this.precioUnidad = precioUnidad;
        this.player = player;
        this.precioTotal = precioUnidad;

        this.inventory = InventoryCreator.createConfirmacionAumento(alias, simbolo, precioUnidad);

        MySQL.conectar();
        this.dineroJugador = jugadoresMySQL.getJugador(player.getName()).getPixelcoins();
        MySQL.desconectar();

        openMenu();
    }

    //Contructor para el tipo activo = acciones_empresa
    public ComprarBolsaConfirmacion(Player player, int id) {
        this.alias = "acciones";
        this.tipoActivo = TipoActivo.ACCIONES_SERVER;
        this.player = player;
        this.id = id;
        this.precioTotal = precioUnidad;

        MySQL.conectar();
        OfertaMercadoServer oferta = ofertasMercadoServerMySQL.get(id);

        this.maxAccionesAComprar = oferta.getCantidad();
        this.precioUnidad = oferta.getPrecio();
        this.dineroJugador = jugadoresMySQL.getJugador(player.getName()).getPixelcoins();
        this.nombreValor = oferta.getEmpresa();
        this.simbolo = oferta.getEmpresa();
        this.inventory = InventoryCreator.createConfirmacionAumento(alias, oferta.getEmpresa(), oferta.getPrecio());

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
        if(itemStack == null || noEsDeTipoItem(itemStack,"LIGHT_GRAY_BANNER" )){
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
        this.cantidadAComprar = cantidadAComprar + nuevasAcciones;

        precioTotal = precioUnidad * cantidadAComprar;
        if(precioTotal > dineroJugador || cantidadAComprar > maxAccionesAComprar){
            this.cantidadAComprar = cantidadAComprar - nuevasAcciones;
            return;
        }

        if (precioTotal <= 0) {
            cantidadAComprar = cantidadAComprar - nuevasAcciones;
            precioTotal = precioUnidad * cantidadAComprar;
            return;
        }
        String displayName = ChatColor.GREEN + "" + ChatColor.BOLD + "COMPRAR " + alias.toUpperCase();
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GOLD + "Comprar " + cantidadAComprar + " " +  alias  + " " + simbolo + " a " + ChatColor.GREEN + precioUnidad + " PC -> total: " + formatea.format(redondeoDecimales(precioTotal, 3)) + " PC");

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
        if (dineroJugador < precioTotal) {
            player.sendMessage(ChatColor.DARK_RED + "No tienes el suficiente dinero");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
            closeMenu();
            return;
        }

        MySQL.conectar();

        if(tipoActivo == TipoActivo.ACCIONES_SERVER){
            transaccionesMySQL.comprarOfertaMercadoAccionServer(player, id, cantidadAComprar);
        }else if(mercadoEstaAbierto()){
            transaccionesMySQL.comprarUnidadBolsa(tipoActivo, simbolo, nombreValor, alias, precioUnidad, cantidadAComprar, player.getName());
        }else{
            ordenesMySQL.abrirOrdenCompraLargo(player, simbolo, cantidadAComprar);
        }

        MySQL.desconectar();
        closeMenu();
    }

    @Override
    public void cancelar() {
        player.sendMessage(ChatColor.GOLD + "Has cancelado la compra");

        closeMenu();
    }
}
