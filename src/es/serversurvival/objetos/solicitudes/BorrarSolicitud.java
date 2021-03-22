package es.serversurvival.objetos.solicitudes;

import es.serversurvival.main.Funciones;
import es.serversurvival.objetos.mySQL.*;
import es.serversurvival.objetos.task.ScoreboardPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

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
        Empresas empr = new Empresas();
        Jugador j = new Jugador();
        Transacciones t = new Transacciones();
        Empleados empl = new Empleados();
        empl.conectar();

        ArrayList<String> empleados = empl.getNombreEmpleadosEmpresa(empresa);
        ArrayList<Integer> idEmpleados = empl.getidEmpleadosEmpresa(empresa);
        for (int i = 0; i < idEmpleados.size(); i++) {
            empl.borrarEmplado(idEmpleados.get(i));
        }

        double liquidez = empr.getPixelcoins(empresa);
        empr.borrarEmpresa(empresa);

        j.setPixelcoin(destinatario.getName(), j.getDinero(destinatario.getName()) + liquidez);
        t.nuevaTransaccion(this.destinatario, this.destinatario, liquidez, empresa, Transacciones.TIPO.EMPRESA_BORRAR);

        destinatario.sendMessage(ChatColor.GOLD + "Has borrado tu empresa: " + empresa + ", se ha retirado a tu cuenta un total de " + ChatColor.GREEN + liquidez + " PC");

        Player tp = null;
        Mensajes men = new Mensajes();

        for (int i = 0; i < empleados.size(); i++) {
            tp = destinatario.getServer().getPlayer(empleados.get(i));

            if (tp != null) {
                tp.sendMessage(ChatColor.GOLD + destinatario.getName() + " ha borrado su empresa donde trabajabas: " + empresa);
            } else {
                men.nuevoMensaje(empleados.get(i), "El owner de la empresa en la que trabajas: " + empresa + " la ha borrado, ya no existe");
            }
        }
        empl.desconectar();
        ScoreboardPlayer sp = new ScoreboardPlayer();
        sp.updateScoreboard(destinatario);

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