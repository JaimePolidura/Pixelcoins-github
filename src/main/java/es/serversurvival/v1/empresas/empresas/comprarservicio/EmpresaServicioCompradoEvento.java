package es.serversurvival.v1.empresas.empresas.comprarservicio;

import es.serversurvival.v1.transacciones._shared.domain.EventoTipoTransaccion;
import es.serversurvival.v1._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.v1.transacciones._shared.domain.Transaccion;
import es.serversurvival.v1.transacciones._shared.domain.TipoTransaccion;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public final class EmpresaServicioCompradoEvento extends PixelcoinsEvento implements EventoTipoTransaccion {
    @Getter private final String comprador;
    @Getter private final String empresaNombre;
    @Getter private final double precio;

    @Override
    public Transaccion buildTransaccion() {
        return new Transaccion(UUID.randomUUID(), formatFecha(), comprador, comprador, (int) precio, "", TipoTransaccion.EMPRESA_COMPRAR_SERVICIO);
    }

    public static EmpresaServicioCompradoEvento of(String comprador, String empresaNombre, double precio){
        return new EmpresaServicioCompradoEvento(comprador, empresaNombre, precio);
    }
}
