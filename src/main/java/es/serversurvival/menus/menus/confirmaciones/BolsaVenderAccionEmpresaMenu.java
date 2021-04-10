package es.serversurvival.menus.menus.confirmaciones;

import es.jaimetruman.ItemBuilder;
import es.serversurvival.menus.Menu;
import es.serversurvival.menus.inventoryFactory.InventoryCreator;
import es.serversurvival.mySQL.tablasObjetos.PosicionAbierta;
import es.serversurvival.util.MinecraftUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static es.serversurvival.util.Funciones.noEsDeTipoItem;
import static org.bukkit.ChatColor.*;
import static org.bukkit.ChatColor.BOLD;

public class BolsaVenderAccionEmpresaMenu extends Menu implements Confirmacion {
    private final Inventory inventory;
    private final Player player;
    private final PosicionAbierta posicionAVender;
    private double precioVenta;
    private String empresa;
    private double precioApertura;

    public BolsaVenderAccionEmpresaMenu(Player player, PosicionAbierta posicionAVender) {
        this.player = player;
        this.posicionAVender = posicionAVender;

        String empresa = posicionAVender.getNombre_activo();
        this.precioVenta = (int) posicionAVender.getPrecio_apertura();
        this.empresa = posicionAVender.getNombre_activo();
        this.precioApertura = posicionAVender.getPrecio_apertura();

        String titulo = DARK_RED + "" + BOLD + "   VENDER ACCION ";
        String tituloAceptar = GREEN + "" + BOLD + "VENDER " + empresa;
        String tituloCancelar = RED + "" + BOLD + "CANCELAR";
        List<String> lore = new ArrayList<>();
        lore.add(GOLD + "Vender 1 accion de " + empresa + " a " + GREEN + formatea.format(precioVenta) + " PC");
        lore.add(GOLD + "Compraste estas acciones a " + GREEN + formatea.format(precioApertura) + " PC");

        this.inventory = InventoryCreator.createConfirmacionAumento(titulo, tituloAceptar, lore, tituloCancelar);

        openMenu();
    }

    @Override
    public void onOtherClick(InventoryClickEvent event) {
        ItemStack itemStack = event.getCurrentItem();

        if(itemStack == null || noEsDeTipoItem(itemStack,"LIGHT_GRAY_BANNER")){
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
        double nuevoPrecio = Integer.parseInt(stringBuild.toString()) + this.precioVenta;

        if(nuevoPrecio <= 0 || nuevoPrecio > 999999){
            return;
        }
        this.precioVenta = nuevoPrecio;

        String displayName = ChatColor.GREEN + "" + ChatColor.BOLD + "VENDER " + empresa;
        List<String> lore = new ArrayList<>();
        lore.add(GOLD + "Vender 1 accion de " + empresa + " a " + GREEN + formatea.format(precioVenta) + " PC");
        lore.add(GOLD + "Compraste estas acciones a " + GREEN + formatea.format(precioApertura) + " PC");

        ItemBuilder.of(Material.GREEN_WOOL).title(displayName).lore(lore).buildAddInventory(inventory, 14);
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
        ofertasMercadoServerMySQL.venderOfertaDesdeBolsaCartera(player, posicionAVender, precioVenta);
        closeMenu();
    }

    @Override
    public void cancelar() {
        player.sendMessage(ChatColor.GOLD + "Has cancelado la venta");

        closeMenu();
    }
}
