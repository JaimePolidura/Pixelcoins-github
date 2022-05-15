package es.serversurvival.empresas.accionistasempresasserver._shared.domain;

import es.jaime.javaddd.domain.Aggregate;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class AccionEmpresaServer extends Aggregate {
    @Getter private final UUID accionEmpresaServerId;
    @Getter private final String nombreAccionista;
    @Getter private final TipoAccionista tipoAccionista;
    @Getter private final String empresa;
    @Getter private final int cantidad;
    @Getter private final double precioApertura;
    @Getter private final String fechaApertura;

    public AccionEmpresaServer withNombreAccionsta(String nombreAccionista){
        return new AccionEmpresaServer(accionEmpresaServerId, nombreAccionista, tipoAccionista, empresa,
                cantidad, precioApertura, fechaApertura);
    }

    public AccionEmpresaServer decreaseCantidad(int cantidad){
        return new AccionEmpresaServer(accionEmpresaServerId, nombreAccionista, tipoAccionista, empresa,
                this.cantidad - cantidad, precioApertura, fechaApertura);
    }
}
