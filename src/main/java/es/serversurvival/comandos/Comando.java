package es.serversurvival.comandos;

import es.serversurvival.mySQL.*;
import es.serversurvival.util.Funciones;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

public abstract class Comando {
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

    public DecimalFormat formatea = Funciones.FORMATEA;

    public abstract String getCNombre();

    public abstract String getSintaxis();

    public abstract String getAyuda();

    public abstract void execute(Player player, String[] args);
}
