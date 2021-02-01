package es.serversurvival.menus;

import es.serversurvival.mySQL.*;
import es.serversurvival.util.Funciones;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.text.DecimalFormat;

public abstract class Menu {
    protected DecimalFormat formatea = Funciones.FORMATEA;
    protected static Cuentas cuentasMySQL = Cuentas.INSTANCE;
    protected static Empleados empleadosMySQL = Empleados.INSTANCE;
    protected static Empresas empresasMySQL = Empresas.INSTANCE;
    protected static Encantamientos encantamientosMySQL = Encantamientos.INSTANCE;
    protected static Jugadores jugadoresMySQL = Jugadores.INSTANCE;
    protected static LlamadasApi llamadasApiMySQL = LlamadasApi.INSTANCE;
    protected static Mensajes mensajesMySQL = Mensajes.INSTANCE;
    protected static Ofertas ofertasMySQL = Ofertas.INSTANCE;
    protected static PosicionesAbiertas posicionesAbiertasMySQL = PosicionesAbiertas.INSTANCE;
    protected static PosicionesCerradas posicionesCerradasMySQL = PosicionesCerradas.INSTANCE;
    protected static Transacciones transaccionesMySQL = Transacciones.INSTANCE;
    protected static Deudas deudasMySQL = Deudas.INSTANCE;
    protected static OrdenesPreMarket ordenesMySQL = OrdenesPreMarket.INSTANCE;

    public abstract Inventory getInventory();
    public abstract Player getPlayer();

    public void openMenu(){
        getPlayer().openInventory(getInventory());
        MenuManager.nuevoMenu(getPlayer().getName(), this);
    }

    public void closeMenu(){
        MenuManager.borrarMenu(getPlayer().getName());
        getPlayer().closeInventory();
    }
}
