package es.serversurvival.objetos.solicitudes;

import es.serversurvival.main.Funciones;
import es.serversurvival.objetos.mySQL.Empleados;
import es.serversurvival.objetos.mySQL.Mensajes;
import es.serversurvival.objetos.mySQL.Transacciones;
import es.serversurvival.objetos.mySQL.tablasObjetos.Empleado;
import es.serversurvival.objetos.task.ScoreboardTaskManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class VenderSolicitud extends Solicitud {
    private static String titulo = ChatColor.DARK_RED + "" + ChatColor.BOLD + "solicitud compra empresa";
    private Inventory inventory;
    private String enviador;
    private String destinatario;
    private String empresa;
    private double precio;

    public VenderSolicitud(String enviador, String destinatario, String empresa, double precio) {
        this.enviador = enviador;
        this.destinatario = destinatario;
        this.empresa = empresa;
        this.precio = precio;
    }

    @Override
    public String getDestinatario() {
        return destinatario;
    }

    @Override
    public String getTitulo() {
        return titulo;
    }

    private Inventory contruirInventario() {
        inventory = Bukkit.createInventory(null, InventoryType.HOPPER, titulo);

        ItemStack aceptar = new ItemStack(Material.GREEN_WOOL);
        ItemMeta itemMetaAceptar = aceptar.getItemMeta();
        itemMetaAceptar.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "ACEPTAR");
        ArrayList<String> lore = new ArrayList<>();
        String desc = ChatColor.GOLD + "Solicitud para comprar a " + enviador + " su empresa " + empresa + " a " + ChatColor.GREEN + "" + precio + " PC";
        lore = Funciones.dividirDesc(lore, desc, 40);
        itemMetaAceptar.setLore(lore);
        aceptar.setItemMeta(itemMetaAceptar);

        ItemStack denegar = new ItemStack(Material.RED_WOOL);
        ItemMeta itemMetadenegar = denegar.getItemMeta();
        itemMetadenegar.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "DENEGAR");
        denegar.setItemMeta(itemMetadenegar);

        inventory.setItem(0, aceptar);
        inventory.setItem(4, denegar);

        return inventory;
    }

    @Override
    public void enviarSolicitud() {
        Player destinatario = Bukkit.getPlayer(this.destinatario);
        Player enviador = Bukkit.getPlayer(this.enviador);

        inventory = contruirInventario();
        destinatario.openInventory(inventory);
        enviador.sendMessage(ChatColor.GOLD + "Has enviado la solicitud");
        solicitudes.add(this);
    }

    @Override
    public void aceptar() {
        Empleados emp = new Empleados();
        Transacciones t = new Transacciones();
        Player enviador = Bukkit.getPlayer(this.enviador);
        Player destinatario = Bukkit.getPlayer(this.destinatario);

        emp.conectar();
        boolean trabaja = emp.trabajaEmpresa(destinatario.getName(), empresa);
        if (trabaja) {
            int id_empleado = emp.getId(destinatario.getName(), empresa);
            emp.borrarEmplado(id_empleado);
        }
        List<Empleado> empleadosEmpresa = emp.getEmpleadosEmrpesa(empresa);

        Mensajes men = new Mensajes();
        for (int i = 0; i < empleadosEmpresa.size(); i++) {
            men.nuevoMensaje(empleadosEmpresa.get(i).getEmpleado(), "La empresa en la que trabajas " + empresa + " ha cambiado de owner a " + destinatario.getName());
        }

        t.comprarEmpresa(this.enviador, this.destinatario, empresa, precio, enviador);
        t.nuevaTransaccion(this.destinatario, this.enviador, precio, empresa, Transacciones.TIPO.EMPRESA_VENTA);
        emp.desconectar();
        destinatario.sendMessage(ChatColor.GOLD + "Ahora eres dueño de " + ChatColor.DARK_AQUA + empresa + ChatColor.GOLD + " ,la has comprado por " + ChatColor.GREEN + precio + " PC");
        destinatario.playSound(destinatario.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);

        ScoreboardTaskManager sp = new ScoreboardTaskManager();
        if (enviador != null) {
            enviador.sendMessage(ChatColor.GOLD + destinatario.getName() + " te ha comprado " + ChatColor.DARK_AQUA + empresa + ChatColor.GOLD + " por " + ChatColor.GREEN + precio + " PC " + ChatColor.GOLD + ", ahora ya no eres dueño");
            enviador.playSound(enviador.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
            sp.updateScoreboard(enviador);
        }
        sp.updateScoreboard(destinatario);
        solicitudes.remove(this);
        destinatario.closeInventory();
    }

    @Override
    public void cancelar() {
        Player enviador = Bukkit.getPlayer(this.enviador);
        Player destinatario = Bukkit.getPlayer(this.destinatario);

        solicitudes.remove(this);
        destinatario.closeInventory();
        if (enviador != null) {
            enviador.sendMessage(ChatColor.GOLD + "" + this.destinatario + "te ha cancelado la solicitud");
        }

        destinatario.sendMessage(ChatColor.GOLD + "Has cancelado la solicitud");
    }
}