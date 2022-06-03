package es.serversurvival.transacciones._shared.domain;

import es.jaime.javaddd.domain.Aggregate;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class Transaccion extends Aggregate {
    @Getter private final UUID transaccionId;
    @Getter private final String fecha;
    @Getter private final String comprador;
    @Getter private final String vendedor;
    @Getter private final int cantidad;
    @Getter private final String objeto;
    @Getter private final TipoTransaccion tipo;

    public Transaccion withComprador(String comprador){
        return new Transaccion(transaccionId, fecha, comprador, vendedor, cantidad, objeto, tipo);
    }

    public Transaccion withVendedor(String vendedor){
        return new Transaccion(transaccionId, fecha, comprador, vendedor, cantidad, objeto, tipo);
    }
}
