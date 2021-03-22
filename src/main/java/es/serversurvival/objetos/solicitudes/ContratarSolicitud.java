package es.serversurvival.objetos.solicitudes;

import es.serversurvival.main.Funciones;
import es.serversurvival.objetos.mySQL.Empleados;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class ContratarSolicitud extends Solicitud {
    private Empleados empleadosMySQL = new Empleados();
    private static String titulo = ChatColor.DARK_RED + "" + ChatColor.BOLD + "    Solicitud Contrato";
    private Inventory inventory;
    private String enviador;
    private String destinatario;
    private String empresa;
    private double sueldo;
    private String tipoSueldo;
    private String cargo = "trabajador";

    public ContratarSolicitud(String enviador, String destinatario, String empresa, double sueldo, String tipoSueldo, String cargo) {
        this.enviador = enviador;
        this.destinatario = destinatario;
        this.empresa = empresa;
        this.sueldo = sueldo;
        this.tipoSueldo = tipoSueldo;
        this.cargo = cargo;
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
        String nombreTipoSueldo = Empleados.toStringTipoSueldo(tipoSueldo);
        String desc = ChatColor.GOLD + "Solicitud de contrato de la empresa " + this.empresa + " para trabajar como " + this.cargo + " , con un sueldo de " + ChatColor.GREEN + this.sueldo + " PC" + ChatColor.GOLD + "/" + nombreTipoSueldo;
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
        Player p = Bukkit.getPlayer(enviador);
        Player tp = Bukkit.getPlayer(destinatario);

        empleadosMySQL.conectar();
        empleadosMySQL.nuevoEmpleado(destinatario, empresa, sueldo, tipoSueldo, cargo);
        empleadosMySQL.desconectar();

        solicitudes.remove(this);
        tp.closeInventory();

        if (p != null) {
            p.sendMessage(ChatColor.GOLD + "Has contratado a " + destinatario + ChatColor.AQUA + " /empresas despedir /empresas editarempleado");
        }
        tp.sendMessage(ChatColor.GOLD + "Ahora trabajas para " + enviador + ChatColor.AQUA + " /empleos irse /empleos misempleos");
    }

    @Override
    public void cancelar() {
        Player p = Bukkit.getPlayer(enviador);
        Player tp = Bukkit.getPlayer(destinatario);

        solicitudes.remove(this);
        tp.closeInventory();

        tp.sendMessage(ChatColor.GOLD + "Has cancelado la solicitud");
        if (p != null) {
            p.sendMessage(ChatColor.GOLD + tp.getName() + " te ha cancelado la solicitud");
        }
    }
}