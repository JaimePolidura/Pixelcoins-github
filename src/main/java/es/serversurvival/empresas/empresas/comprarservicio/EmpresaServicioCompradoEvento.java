package es.serversurvival.empresas.empresas.comprarservicio;

import es.serversurvival.transacciones._shared.domain.EventoTipoTransaccion;
import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.empresas.empresas._shared.domain.Empresa;
import es.serversurvival.transacciones._shared.domain.Transaccion;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

import static es.serversurvival.transacciones._shared.domain.TipoTransaccion.*;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public final class EmpresaServicioCompradoEvento extends PixelcoinsEvento implements EventoTipoTransaccion {
    @Getter private final String comprador;
    @Getter private final String empresaNombre;
    @Getter private final double precio;

    @Override
    public Transaccion buildTransaccion() {
        return new Transaccion(UUID.randomUUID(), formatFecha(), comprador, comprador, (int) precio, "", EMPRESA_COMPRAR_SERVICIO);
    }

    public static EmpresaServicioCompradoEvento of(String comprador, String empresaNombre, double precio){
        return new EmpresaServicioCompradoEvento(comprador, empresaNombre, precio);
    }
}
