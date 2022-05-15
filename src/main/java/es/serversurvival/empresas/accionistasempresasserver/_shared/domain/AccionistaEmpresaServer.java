package es.serversurvival.empresas.accionistasempresasserver._shared.domain;

import es.jaime.javaddd.domain.Aggregate;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class AccionistaEmpresaServer extends Aggregate {
    @Getter private final UUID accionistaEmpresaServerId;
    @Getter private final String nombreAccionista;
    @Getter private final TipoAccionista tipoAccionista;
    @Getter private final String empresa;
    @Getter private final int cantidad;
    @Getter private final double precioApertura;
    @Getter private final String fechaApertura;

    public AccionistaEmpresaServer withNombreAccionsta(String nombreAccionista){
        return new AccionistaEmpresaServer(accionistaEmpresaServerId, nombreAccionista, tipoAccionista, empresa,
                cantidad, precioApertura, fechaApertura);
    }

    public AccionistaEmpresaServer withCantidad(int cantidad){
        return new AccionistaEmpresaServer(accionistaEmpresaServerId, nombreAccionista, tipoAccionista, empresa,
                cantidad, precioApertura, fechaApertura);
    }
}
