package es.serversurvival.v2.ade.empresas;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
public final class AccionistasEmpresa {
    private final UUID accionId;
    private final UUID empresaId;
    private final UUID accionistaJugadorId;
    private final int numeroAcciones;
    private final double precioCompra;
    private final LocalDateTime fechaCompra;
}
