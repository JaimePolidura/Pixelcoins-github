package es.serversurvival.objetos.solicitudes;

import es.serversurvival.main.Funciones;
import es.serversurvival.objetos.mySQL.Transacciones;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;;

public class ComprarAccionSolicitud extends Solicitud {
    private static String titulo = ChatColor.DARK_RED + "" + ChatColor.BOLD + "   SELECCIONA ACCIONES";
    private String ticker;
    private String destinatario;
    private double precioAccion;
    private double precioTotal;
    private Inventory inventory;
    private int nAcciones = 1;

    public ComprarAccionSolicitud(String ticker, String destinatario, double precioAccion) {
        this.ticker = ticker;
        this.destinatario = destinatario;
        this.precioAccion = precioAccion;
    }

    private Inventory contruirInventario() {
        Inventory inventory = Bukkit.createInventory(null, 27, titulo);

        ItemStack comprar = new ItemStack(Material.GREEN_WOOL);
        ItemMeta comprarm = comprar.getItemMeta();
        comprarm.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "COMPRAR ACCIONES");
        ArrayList<String> lista = new ArrayList<>();
        lista.add(ChatColor.GOLD + "Comprar 1 acccion de " + ticker + " a " + ChatColor.GREEN + formatea.format(precioAccion) + " PC");
        comprarm.setLore(lista);
        comprar.setItemMeta(comprarm);

        ItemStack cancelar = new ItemStack(Material.RED_WOOL);
        ItemMeta cancelarm = cancelar.getItemMeta();
        cancelarm.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "CANCELAR");
        cancelar.setItemMeta(cancelarm);

        ItemStack mas1 = new ItemStack(Material.LIGHT_GRAY_BANNER);
        ItemMeta mas1m = mas1.getItemMeta();
        mas1m.setDisplayName(ChatColor.GREEN + "+1");
        mas1.setItemMeta(mas1m);

        ItemStack mas5 = new ItemStack(Material.LIGHT_GRAY_BANNER);
        ItemMeta mas5m = mas5.getItemMeta();
        mas5m.setDisplayName(ChatColor.GREEN + "+5");
        mas5.setItemMeta(mas5m);

        ItemStack mas10 = new ItemStack(Material.LIGHT_GRAY_BANNER);
        ItemMeta mas10m = mas10.getItemMeta();
        mas10m.setDisplayName(ChatColor.GREEN + "+10");
        mas10.setItemMeta(mas10m);


        ItemStack menos1 = new ItemStack(Material.LIGHT_GRAY_BANNER);
        ItemMeta menos1m = menos1.getItemMeta();
        menos1m.setDisplayName(ChatColor.RED + "-1");
        menos1.setItemMeta(menos1m);

        ItemStack menos5 = new ItemStack(Material.LIGHT_GRAY_BANNER);
        ItemMeta menos5m = menos5.getItemMeta();
        menos5m.setDisplayName(ChatColor.RED + "-5");
        menos5.setItemMeta(menos5m);

        ItemStack menos10 = new ItemStack(Material.LIGHT_GRAY_BANNER);
        ItemMeta menos10m = menos10.getItemMeta();
        menos10m.setDisplayName(ChatColor.RED + "-10");
        menos10.setItemMeta(menos10m);

        inventory.setItem(9, menos10);
        inventory.setItem(10, menos5);
        inventory.setItem(11, menos1);

        inventory.setItem(12, cancelar);
        inventory.setItem(14, comprar);

        inventory.setItem(15, mas1);
        inventory.setItem(16, mas5);
        inventory.setItem(17, mas10);

        return inventory;
    }

    public void updateNAcciones(ItemStack itemStack) {
        String name = itemStack.getItemMeta().getDisplayName();
        StringBuilder stringBuild = new StringBuilder();
        for (int i = 0; i < name.length(); i++) {
            if (i > 1) {
                stringBuild.append(name.charAt(i));
            }
        }
        int nuevasAcciones = Integer.parseInt(stringBuild.toString());
        nAcciones = nAcciones + nuevasAcciones;

        precioTotal = precioAccion * nAcciones;

        if (precioTotal <= 0) {
            nAcciones = nAcciones - nuevasAcciones;
            precioTotal = precioAccion * nAcciones;
            return;
        }
        ItemStack comprar = new ItemStack(Material.GREEN_WOOL);
        ItemMeta itemMeta = comprar.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "COMPRAR ACCIONES");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GOLD + "Comprar " + nAcciones + " accciones de " + ticker + " a " + ChatColor.GREEN + precioAccion + " PC -> total: " + formatea.format(Funciones.redondeoDecimales(precioTotal, 3)) + " PC");
        itemMeta.setLore(lore);
        comprar.setItemMeta(itemMeta);
        inventory.setItem(14, comprar);
    }

    @Override
    public void enviarSolicitud() {
        Player player = Bukkit.getPlayer(destinatario);

        inventory = this.contruirInventario();
        player.openInventory(inventory);
        solicitudes.add(this);
    }

    @Override
    public void aceptar() {
        Player player = Bukkit.getPlayer(destinatario);
        Transacciones t = new Transacciones();
        t.conectar();
        t.comprarAccion(ticker, precioAccion, nAcciones, player);
        t.desconectar();
        player.closeInventory();
        solicitudes.remove(this);
    }

    @Override
    public void cancelar() {
        Player player = Bukkit.getPlayer(destinatario);
        player.sendMessage(ChatColor.GOLD + "Has cancelado la compra");
        solicitudes.remove(this);
        player.closeInventory();
    }

    @Override
    public String getDestinatario() {
        return destinatario;
    }

    @Override
    public String getTitulo() {
        return titulo;
    }
}