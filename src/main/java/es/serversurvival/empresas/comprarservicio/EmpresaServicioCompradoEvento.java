package es.serversurvival.empresas.comprarservicio;

import es.serversurvival._shared.eventospixelcoins.EventoTipoTransaccion;
import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.empresas._shared.mysql.Empresa;
import es.serversurvival.transacciones.mySQL.Transaccion;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static es.serversurvival.transacciones.mySQL.TipoTransaccion.*;

@AllArgsConstructor
public final class EmpresaServicioCompradoEvento extends PixelcoinsEvento implements EventoTipoTransaccion {
    @Getter private final String comprador;
    @Getter private final Empresa empresa;
    @Getter private final double precio;

    @Override
    public Transaccion buildTransaccion() {
        return new Transaccion(-1, formatFecha(), comprador, empresa.getNombre(), (int) precio, "", EMPRESA_COMPRAR_SERVICIO);
    }
}
