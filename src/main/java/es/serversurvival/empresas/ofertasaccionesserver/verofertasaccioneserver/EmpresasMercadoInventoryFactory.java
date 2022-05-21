package es.serversurvival.empresas.ofertasaccionesserver.verofertasaccioneserver;

import es.jaimetruman.ItemBuilder;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival._shared.menus.inventory.InventoryFactory;
import es.serversurvival.empresas.ofertasaccionesserver._shared.application.OfertasAccionesServerService;
import es.serversurvival.empresas.ofertasaccionesserver._shared.domain.OfertaAccionServer;
import es.serversurvival.empresas.accionistasserver._shared.domain.TipoAccionista;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static es.serversurvival._shared.utils.Funciones.FORMATEA;
import static org.bukkit.ChatColor.*;

public class EmpresasMercadoInventoryFactory extends InventoryFactory {
    private final String titulo = DARK_RED + "" + BOLD + "    MERCADO DE ACCIONES";
    private final OfertasAccionesServerService ofertasAccionesServerService;
    private final List<ItemStack> itemExcessInventory = new ArrayList<>();

    public EmpresasMercadoInventoryFactory() {
        this.ofertasAccionesServerService = DependecyContainer.get(OfertasAccionesServerService.class);
    }

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
        List<OfertaAccionServer> mercadoOferta = ofertasAccionesServerService.findAll();
        List<ItemStack> items = new ArrayList<>();

        mercadoOferta.forEach(oferta -> items.add(buildItemFromOferta(oferta, jugador)));

        return items;
    }

    private ItemStack buildItemFromOferta (OfertaAccionServer ofertaAccion, String jugador) {
        String displayname = ofertaAccion.getNombreOfertante().equalsIgnoreCase(jugador) ?
                RED + "" + UNDERLINE + "" + BOLD + "CLICK PARA CANCELAR" :
                GOLD + "" + UNDERLINE + "" + BOLD + "CLICK PARA COMPRAR";
        Material material;
        if(ofertaAccion.getNombreOfertante().equals(jugador) && ofertaAccion.getTipoOfertante() == TipoAccionista.JUGADOR){
            material = Material.RED_BANNER;
        }else if (ofertaAccion.getTipoOfertante() == TipoAccionista.JUGADOR) {
            material = Material.GREEN_BANNER;
        }else{
            material = Material.BLUE_BANNER;
        }

        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(GOLD + "Empresa: " + ofertaAccion.getEmpresa());
        lore.add(GOLD + "Ofertante: " + ofertaAccion.getNombreOfertante() + " ("+ofertaAccion.getTipoOfertante().toString().toLowerCase()+")");
        lore.add(GOLD + "Acciones: " + ofertaAccion.getCantidad());
        lore.add(GOLD + "Precio/Accion: " + GREEN + FORMATEA.format(ofertaAccion.getPrecio()) + " PC");
        lore.add(GOLD + "Precio total: " + GREEN + FORMATEA.format(ofertaAccion.getPrecio() * ofertaAccion.getCantidad()) + " PC");
        lore.add("   ");
        lore.add(GOLD + "Fecha: " + ofertaAccion.getFecha());
        lore.add("  ");
        lore.add("" + ofertaAccion.getNombreOfertante());

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
