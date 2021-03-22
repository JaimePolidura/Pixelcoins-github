package es.serversurvival.objetos.solicitudes;

import es.serversurvival.main.Funciones;
import es.serversurvival.objetos.mySQL.*;
import es.serversurvival.objetos.mySQL.tablasObjetos.Empleado;
import es.serversurvival.objetos.mySQL.tablasObjetos.Empresa;
import es.serversurvival.objetos.task.ScoreboardTaskManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class BorrarSolicitud extends Solicitud {
    private static String titulo = ChatColor.DARK_RED + "" + ChatColor.BOLD + "     Confirmar borrar";
    private Inventory inventory;
    private String destinatario;
    private String empresa;

    public BorrarSolicitud(String destinatario, String empresa) {
        this.destinatario = destinatario;
        this.empresa = empresa;
    }

    @Override
    public String getDestinatario() {
        return destinatario;
    }

    @Override
    public String getTitulo() {
        return titulo;
    }

    @Override
    public void enviarSolicitud() {
        inventory = this.construirInventario();
        Player destinatario = Bukkit.getPlayer(this.destinatario);

        destinatario.openInventory(inventory);
        solicitudes.add(this);
    }

    private Inventory construirInventario() {
        inventory = Bukkit.createInventory(null, InventoryType.HOPPER, titulo);

        ItemStack aceptar = new ItemStack(Material.GREEN_WOOL);
        ItemMeta itemMetaAceptar = aceptar.getItemMeta();
        itemMetaAceptar.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "CONFIRMAR");
        ArrayList<String> lore = new ArrayList<>();
        String desc = ChatColor.GOLD + "Confirmar para borrar la emprea: " + this.empresa + ", se te devolveran las pixel coins de la empresa a tu cuenta";
        lore = Funciones.dividirDesc(lore, desc, 40);
        itemMetaAceptar.setLore(lore);
        aceptar.setItemMeta(itemMetaAceptar);

        ItemStack denegar = new ItemStack(Material.RED_WOOL);
        ItemMeta itemMetadenegar = denegar.getItemMeta();
        itemMetadenegar.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "CANCELAR");
        denegar.setItemMeta(itemMetadenegar);

        inventory.setItem(0, aceptar);
        inventory.setItem(4, denegar);

        return inventory;
    }

    @Override
    public void aceptar() {
        Player destinatario = Bukkit.getPlayer(this.destinatario);
        Empresas empresasMySQL = new Empresas();

        empresasMySQL.conectar();
        empresasMySQL.borrarEmpresaManual(empresa);
        empresasMySQL.desconectar();

        solicitudes.remove(this);
        destinatario.closeInventory();
    }

    @Override
    public void cancelar() {
        Player destinatario = Bukkit.getPlayer(this.destinatario);

        solicitudes.remove(this);
        destinatario.sendMessage(ChatColor.GOLD + "Has cancelado");
        destinatario.closeInventory();
    }
}