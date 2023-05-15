package es.serversurvival.v1.empresas.accionistasserver._shared.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public final class AccionistaServer {
    @Getter private final UUID accionistaServerId;
    @Getter private final String nombreAccionista;
    @Getter private final TipoAccionista tipoAccionista;
    @Getter private final String empresa;
    @Getter private final int cantidad;
    @Getter private final double precioApertura;
    @Getter private final String fechaApertura;

    public AccionistaServer withNombreAccionsta(String nombreAccionista){
        return new AccionistaServer(accionistaServerId, nombreAccionista, tipoAccionista, empresa,
                cantidad, precioApertura, fechaApertura);
    }

    public AccionistaServer decreaseCantidad(int cantidad){
        return new AccionistaServer(accionistaServerId, nombreAccionista, tipoAccionista, empresa,
                this.cantidad - cantidad, precioApertura, fechaApertura);
    }

    public boolean esJugador(){
        return this.tipoAccionista == TipoAccionista.JUGADOR;
    }

    public boolean esEmpresa(){
        return this.tipoAccionista == TipoAccionista.EMPRESA;
    }
}
