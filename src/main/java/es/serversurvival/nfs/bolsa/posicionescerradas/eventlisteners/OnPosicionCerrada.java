package es.serversurvival.nfs.bolsa.posicionescerradas.eventlisteners;

import es.jaime.EventListener;
import es.serversurvival.nfs.shared.mysql.AllMySQLTablesInstances;
import es.serversurvival.nfs.shared.eventospixelcoins.PosicionCerradaEvento;
import es.serversurvival.nfs.bolsa.posicionescerradas.mysql.PosicionCerrada;

public final class OnPosicionCerrada implements AllMySQLTablesInstances {

    @EventListener
    public void onPosicionCerrada (PosicionCerradaEvento evento) {
        PosicionCerrada pos = evento.buildPosicionCerrada();

        posicionesCerradasMySQL.nuevaPosicion(pos.getJugador(), pos.getTipo_activo(), pos.getSimbolo(), pos.getCantidad(),
                pos.getPrecio_apertura(), pos.getFecha_apertura(), pos.getPrecio_cierre(), pos.getNombre_activo(), pos.getRentabilidad(), pos.getTipo_posicion());
    }
}
