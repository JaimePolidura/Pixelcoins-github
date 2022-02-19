package es.serversurvival.bolsa.verofertasmercadoserver;

import es.jaimetruman.ItemBuilder;
import es.serversurvival._shared.menus.inventory.InventoryFactory;
import es.serversurvival.bolsa._shared.ofertasmercadoserver.mysql.OfertaMercadoServer;
import es.serversurvival.bolsa._shared.ofertasmercadoserver.mysql.TipoOfertante;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.ChatColor.*;

public class EmpresasMercadoInventoryFactory extends InventoryFactory {
    private final String titulo = DARK_RED + "" + BOLD + "    MERCADO DE ACCIONES";
    private List<ItemStack> itemExcessInventory = new ArrayList<>();

    @Override
    protected Inventory buildInventory(String jugador) {
        Inventory inventory = Bukkit.createInventory(null, 54, titulo);

        ItemStack info = buildItemInfo();
        List<ItemStack> itemsOfertas = buildItemsOfertas(jugador);

        inventory.setItem(0, info);

        for(int i = 0; i < itemsOfertas.size(); i++){
            if(i < 43){
                inventory.setItem(i + 9, itemsOfertas.get(i));
            }else{
                itemExcessInventory.add(itemsOfertas.get(i));
            }
        }

        if(itemExcessInventory.size() > 0){
            inventory.setItem(52, buildItemGoBack());
            inventory.setItem(53, buildItemFordward());
        }else{
            inventory.setItem(53, buildItemGoBack());
        }

        return inventory;
    }

    public Inventory buildInventoryExecess () {
        Inventory inventory = Bukkit.createInventory(null, 54, titulo);

        List<ItemStack> copyOfItemExcessList = new ArrayList<>(itemExcessInventory);
        itemExcessInventory.clear();

        boolean addFordwardItem = false;

        for(int i = 0; i < copyOfItemExcessList.size(); i++){
            if(i < 51){
                inventory.addItem(copyOfItemExcessList.get(i));
            }else{
                itemExcessInventory.add(copyOfItemExcessList.get(i));
                addFordwardItem = true;
            }
        }

        inventory.setItem(53, buildItemInfo());
        if(addFordwardItem){
            inventory.setItem(51, buildItemGoBack());
            inventory.setItem(52, buildItemFordward());
        }else{
            inventory.setItem(52, buildItemGoBack());
        }

        return inventory;
    }


    private List<ItemStack> buildItemsOfertas (String jugador) {
        List<OfertaMercadoServer> mercadoOferta = ofertasMercadoServerMySQL.getAll();
        List<ItemStack> items = new ArrayList<>();

        mercadoOferta.forEach(oferta -> items.add(buildItemFromOferta(oferta, jugador)));

        return items;
    }

    private ItemStack buildItemFromOferta (OfertaMercadoServer ofertaAccion, String jugador) {
        String displayname = ofertaAccion.getJugador().equalsIgnoreCase(jugador) ?
                RED + "" + UNDERLINE + "" + BOLD + "CLICK PARA CANCELAR" :
                GOLD + "" + UNDERLINE + "" + BOLD + "CLICK PARA COMPRAR";
        Material material;
        if(ofertaAccion.getJugador().equals(jugador) && ofertaAccion.getTipo_ofertante() == TipoOfertante.JUGADOR){
            material = Material.RED_BANNER;
        }else if (ofertaAccion.getTipo_ofertante() == TipoOfertante.JUGADOR) {
            material = Material.GREEN_BANNER;
        }else{
            material = Material.BLUE_BANNER;
        }

        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(GOLD + "Empresa: " + ofertaAccion.getEmpresa());
        lore.add(GOLD + "Ofertante: " + ofertaAccion.getJugador() + " ("+ofertaAccion.getTipo_ofertante().toString().toLowerCase()+")");
        lore.add(GOLD + "Acciones: " + ofertaAccion.getCantidad());
        lore.add(GOLD + "Precio/Accion: " + GREEN + formatea.format(ofertaAccion.getPrecio()) + " PC");
        lore.add(GOLD + "Precio total: " + GREEN + formatea.format(ofertaAccion.getPrecio() * ofertaAccion.getCantidad()) + " PC");
        lore.add("   ");
        lore.add(GOLD + "Fecha: " + ofertaAccion.getFecha());
        lore.add("  ");
        lore.add("" + ofertaAccion.getId());

        return ItemBuilder.of(material).title(displayname).lore(lore).build();
    }

    private ItemStack buildItemInfo () {
        String displayName = GOLD + "" + BOLD + "INFO";
        List<String> lore = new ArrayList<>();
        lore.add("Las empresas del servidor pueden salir");
        lore.add("a bosla. /empresas sacarbolsa");
        lore.add("Aqui es donde se pueden comprar y");
        lore.add("vender las acciones de las empresas");

        return ItemBuilder.of(Material.PAPER).title(displayName).lore(lore).build();
    }
}
