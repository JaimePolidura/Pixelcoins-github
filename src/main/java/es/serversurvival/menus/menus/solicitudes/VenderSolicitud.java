package es.serversurvival.menus.menus.solicitudes;

import es.serversurvival.mySQL.enums.TRANSACCIONES;
import es.serversurvival.util.Funciones;
import es.serversurvival.menus.Menu;
import es.serversurvival.menus.inventoryFactory.InventoryCreator;
import es.serversurvival.mySQL.Empleados;
import es.serversurvival.mySQL.Mensajes;
import es.serversurvival.mySQL.Transacciones;
import es.serversurvival.mySQL.tablasObjetos.Empleado;
import es.serversurvival.task.ScoreBoardManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.Collections;
import java.util.List;

public class VenderSolicitud extends Menu implements Solicitud {
    private Inventory inventory;
    private Player player;
    private String enviador;
    private String destinatario;
    private String empresa;
    private double precio;
    private boolean isClicked = false;

    public VenderSolicitud(String enviador, String destinatario, String empresa, double precio) {
        this.enviador = enviador;
        this.destinatario = destinatario;
        this.empresa = empresa;
        this.precio = precio;
        this.player = Bukkit.getPlayer(destinatario);

        String titulo = ChatColor.DARK_RED + "" + ChatColor.BOLD + "solicitud compra empresa";
        String nombreAceptar = ChatColor.GREEN + "" + ChatColor.BOLD + "ACEPTAR";
        String desc = ChatColor.GOLD + "Solicitud para comprar a " + enviador + " su empresa " + empresa + " a " + ChatColor.GREEN + "" + precio + " PC";
        List<String> loreAceptar = Funciones.dividirDesc(desc, 40);
        String cancelarNombre = ChatColor.RED + "" + ChatColor.BOLD + "DENEGAR";

        this.inventory = InventoryCreator.createSolicitud(titulo, nombreAceptar, loreAceptar, cancelarNombre, Collections.emptyList());
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
    public String getDestinatario() {
        return destinatario;
    }

    @Override
    public void enviarSolicitud() {
        openMenu();

        Player enviadorPlayer = Bukkit.getPlayer(this.enviador);
        enviadorPlayer.sendMessage(ChatColor.GOLD + "Has enviado la solicitud");
    }

    @Override
    public void aceptar() {
        isClicked = true;

        Player enviador = Bukkit.getPlayer(this.enviador);

        empleadosMySQL.conectar();
        boolean trabaja = empleadosMySQL.trabajaEmpresa(player.getName(), empresa);
        if (trabaja) {
            int id_empleado = empleadosMySQL.getEmpleado(player.getName(), empresa).getId();
            empleadosMySQL.borrarEmplado(id_empleado);
        }
        List<Empleado> empleadosEmpresa = empleadosMySQL.getEmpleadosEmrpesa(empresa);

        for (int i = 0; i < empleadosEmpresa.size(); i++) {
            mensajesMySQL.nuevoMensaje(empleadosEmpresa.get(i).getEmpleado(), "La empresa en la que trabajas " + empresa + " ha cambiado de owner a " + player.getName());
        }

        transaccionesMySQL.comprarEmpresa(this.enviador, this.destinatario, empresa, precio, enviador);
        transaccionesMySQL.nuevaTransaccion(this.destinatario, this.enviador, precio, empresa, TRANSACCIONES.EMPRESA_VENTA);
        empleadosMySQL.desconectar();
        player.sendMessage(ChatColor.GOLD + "Ahora eres dueño de " + ChatColor.DARK_AQUA + empresa + ChatColor.GOLD + " ,la has comprado por " + ChatColor.GREEN + precio + " PC");
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);

        if (enviador != null) {
            enviador.sendMessage(ChatColor.GOLD + player.getName() + " te ha comprado " + ChatColor.DARK_AQUA + empresa + ChatColor.GOLD + " por " + ChatColor.GREEN + precio + " PC " + ChatColor.GOLD + ", ahora ya no eres dueño");
            enviador.playSound(enviador.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
            ScoreBoardManager.updateScoreboard(enviador);
        }
        ScoreBoardManager.updateScoreboard(player);

        closeMenu();
    }

    @Override
    public void cancelar() {
        isClicked = true;

        Player enviador = Bukkit.getPlayer(this.enviador);

        if (enviador != null) {
            enviador.sendMessage(ChatColor.GOLD + "" + this.destinatario + "te ha cancelado la solicitud");
        }

        player.sendMessage(ChatColor.GOLD + "Has cancelado la solicitud");

        closeMenu();
    }

    @Override
    public void onCloseInventory(InventoryCloseEvent event) {
        if(!isClicked && Bukkit.getPlayer(enviador) != null){
            Bukkit.getPlayer(enviador).sendMessage(ChatColor.GOLD + destinatario + " ha cancelado la solicitud");
        }
    }
}