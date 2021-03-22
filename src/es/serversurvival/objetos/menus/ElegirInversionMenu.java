package es.serversurvival.objetos.menus;


import es.serversurvival.comandos.subComandos.bolsa.ValoresBolsaSubComando;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ElegirInversionMenu extends Menu implements Clickleable{
    private ItemStack acciones;
    private ItemStack criptomonedas;
    private ItemStack materiasPrimas;

    private Player player;
    private final String titulo = ChatColor.DARK_RED + "" + ChatColor.BOLD + " ¿En que quieres invertir?";
    private Inventory inventory = Bukkit.createInventory(null, InventoryType.HOPPER, titulo);

    public ElegirInversionMenu(Player player){
        this.player = player;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public String titulo() {
        return titulo;
    }

    @Override
    public void openMenu() {
        buildInv();
        player.openInventory(inventory);
        activeMenus.add(this);
    }

    @Override
    public void closeMenu() {
        activeMenus.remove(this);
    }

    private void buildInv(){
        acciones = new ItemStack(Material.PAPER);
        ItemMeta itemMetaAcciones = acciones.getItemMeta();
        itemMetaAcciones.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Acciones");
        acciones.setItemMeta(itemMetaAcciones);
        inventory.setItem(0, acciones);

        criptomonedas = new ItemStack(Material.GOLD_INGOT);
        ItemMeta itemMetaCripto = criptomonedas.getItemMeta();
        itemMetaCripto.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Criptomonedas");
        criptomonedas.setItemMeta(itemMetaCripto);
        inventory.setItem(2, criptomonedas);

        materiasPrimas = new ItemStack(Material.CHARCOAL);
        ItemMeta itemMeta = materiasPrimas.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Materias primas");
        materiasPrimas.setItemMeta(itemMeta);
        inventory.setItem(4, materiasPrimas);
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        ItemStack itemStack = event.getCurrentItem();
        if(itemStack == null || itemStack.getType().toString().equalsIgnoreCase("AIR") && !(itemStack.getType().toString().equalsIgnoreCase("PAPER")) && !(itemStack.getType().toString().equalsIgnoreCase("CHARCOAL")) && !(itemStack.getType().toString().equalsIgnoreCase("GOLD_INGOT")) ){
            return;
        }

        if(itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(acciones.getItemMeta().getDisplayName())){
            closeMenu();
            AccionesValoresMenu accionesValoresMenu = new AccionesValoresMenu(player);
            accionesValoresMenu.openMenu();
        }else if(itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(criptomonedas.getItemMeta().getDisplayName())){
            CriptomonedasMenu criptomonedasMenu = new CriptomonedasMenu(player);
            criptomonedasMenu.openMenu();
        }else {
            MateriasPrimasMenu materiasPrimasMenu = new MateriasPrimasMenu(player);
            materiasPrimasMenu.openMenu();
        }
    }
}
