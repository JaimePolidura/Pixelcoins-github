package es.serversurvival.bolsa.vervalores.materiasprimas;

import es.serversurvival.Pixelcoin;
import es.serversurvival.bolsa._shared.llamadasapi.mysql.TipoActivo;
import es.serversurvival.bolsa.vervalores.ComprarBolsaConfirmacion;
import es.serversurvival._shared.menus.Clickable;
import es.serversurvival._shared.menus.PostLoading;
import es.serversurvival._shared.utils.apiHttp.IEXCloud_API;
import es.serversurvival._shared.menus.Menu;
import es.serversurvival._shared.menus.inventory.InventoryCreator;
import es.serversurvival._shared.utils.MinecraftUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class MateriasPrimasMenu extends Menu implements Clickable, PostLoading {
    public final static String titulo = ChatColor.DARK_RED + "" + ChatColor.BOLD + "   Escoge para invertir";
    private Inventory inventory;
    private Player player;

    public MateriasPrimasMenu (Player player) {
        this.player = player;
        this.inventory = InventoryCreator.createInventoryMenu(new MateriasPrimasInventoryFactory(), player.getName());

        postLoad();
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
        ItemStack itemStack = event.getCurrentItem();
        if(itemStack == null || !itemStack.getType().toString().equalsIgnoreCase("COAL")){
            return;
        }

        String nombreValor = itemStack.getItemMeta().getDisplayName().substring(4);
        List<String> lore = itemStack.getItemMeta().getLore();
        String precioLore = lore.get(1);
        if (precioLore.equalsIgnoreCase(ChatColor.RED + "Cargando...")) {
            return;
        }

        double precio = Double.parseDouble(lore.get(1).split(" ")[1]);
        String simbolo = lore.get(0).split(" ")[1];

        String alias;
        if(simbolo.equalsIgnoreCase("DCOILBRENTEU")){
            alias = "barriles";
        }else if(simbolo.equalsIgnoreCase("DHHNGSP")){
            alias = "galones";
        }else{
            alias = "galones";
        }

        ComprarBolsaConfirmacion confirmacion = new ComprarBolsaConfirmacion(simbolo, nombreValor, TipoActivo.MATERIAS_PRIMAS, alias, player, precio);
    }

    @Override
    public void postLoad() {
        Bukkit.getScheduler().scheduleAsyncDelayedTask(Pixelcoin.getInstance(), () -> {
            int pos = 0;

            for (ItemStack actual : inventory.getContents()) {
                if (pos == 4 || actual == null || actual.getType().toString().equalsIgnoreCase("AIR")) {
                    break;
                }
                pos++;

                List<String> precioLore = actual.getItemMeta().getLore();
                precioLore.remove(1);

                String ticker = precioLore.get(0).split(" ")[1];
                try {
                    double preioMat = IEXCloud_API.getPrecioMateriaPrima(ticker);

                    precioLore.add(1, ChatColor.GOLD + "Precio/Unidad:" + ChatColor.GREEN + " " + preioMat + " $");

                    MinecraftUtils.setLore(actual, precioLore);
                } catch (Exception e) {
                    player.sendMessage(ChatColor.DARK_RED + "No hagas spam del comando");
                }
            }
        }, 0L);
    }
}
