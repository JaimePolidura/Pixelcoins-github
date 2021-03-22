package es.serversurvival.mobs;

import es.serversurvival.mySQL.*;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public abstract class InteractuableMob {
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

    public abstract void onClick (PlayerInteractEntityEvent event);
    public abstract Location getLocation();
}
