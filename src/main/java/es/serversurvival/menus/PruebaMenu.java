package es.serversurvival.menus;

import es.serversurvival.menus.menus.Paginated;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class PruebaMenu extends Menu implements Paginated{
    private final String nameItemGoForward = ChatColor.GREEN + "" + ChatColor.BOLD + "-->";
    private final String nameItemGoBack = ChatColor.RED + "" + ChatColor.BOLD + "<--";

    private Player player;
    private Inventory inventory;
    private List<Page> pages;
    private int index;

    public PruebaMenu(Player player) {
        this.index = 0;
        this.pages = new ArrayList<>();
        this.inventory = Bukkit.createInventory(null, 27, "PAG " + index);
        addBackFordwardItems(inventory);

        this.player = player;
    }

    @Override
    public void goFordward() {
        if(!(index == 0 && pages.size() == 0) && index + 1 > pages.size()){
            return;
        }

        index++;

        if(pages.size() != 0){
            Inventory newInventory = pages.get(index).inventory;
            this.inventory = newInventory;
            player.openInventory(newInventory);
        }else {
            this.inventory = Bukkit.createInventory(null, 27, "PAG " + index);
            addBackFordwardItems(inventory);
            pages.add(new Page(index, inventory));
            player.openInventory(inventory);

        }

        MenuManager.nuevoMenu(player.getName(), this);
    }

    @Override //Size : index + 1
    public void goBack() {
        if(index - 1 < 0) return;

        index--;
        Inventory newInventory = pages.get(index - 1).inventory;
        this.inventory = newInventory;
        player.openInventory(newInventory);
        MenuManager.nuevoMenu(player.getName(), this);
    }

    private void addBackFordwardItems (Inventory inventory) {
        ItemStack goFordward = new ItemStack(Material.GREEN_WOOL);
        ItemMeta goFordwardMeta = goFordward.getItemMeta();
        goFordwardMeta.setDisplayName(nameItemGoForward);
        goFordward.setItemMeta(goFordwardMeta);

        ItemStack goBack = new ItemStack(Material.RED_WOOL);
        ItemMeta goBackMeta = goBack.getItemMeta();
        goBackMeta.setDisplayName(nameItemGoBack);
        goBack.setItemMeta(goBackMeta);

        inventory.addItem(goFordward);
        inventory.addItem(goBack);
    }

    @Override
    public int getCurrentIndex() {
        return index;
    }

    @Override
    public List<Page> getPages() {
        return pages;
    }

    @Override
    public String getNameItemGoBack() {
        return nameItemGoBack;
    }

    @Override
    public String getNameItemGoFordward() {
        return nameItemGoForward;
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