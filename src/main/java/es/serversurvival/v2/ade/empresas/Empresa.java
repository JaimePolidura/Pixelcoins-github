package es.serversurvival.v2.ade.empresas;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
public final class Empresa {
    private final UUID empresaId;
    private final String nombre;
    private final String descripccion;
    private final UUID fundadorJugadorId;
    private final UUID directorJugadorId;
    private final String icono;
    private final int numeroAcciones;
    private final boolean esPublica;
    private final LocalDateTime fechaCreacion;
}
