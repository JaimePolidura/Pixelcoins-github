package es.serversurvival.v2.pixelcoins.empresas._shared.empresas;

import es.serversurvival.v2.pixelcoins.empresas.crear.CrearEmpresaParametros;
import es.serversurvival.v2.pixelcoins.empresas.editarempresa.EditarEmpresaParametros;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@AllArgsConstructor
public final class Empresa {
    public static final int N_ACCIONES_INICIAL = 1000;

    @Getter private final UUID empresaId;
    @Getter private final String nombre;
    @Getter private final UUID fundadorJugadorId;
    @Getter private final UUID directorJugadorId;
    @Getter private final String descripccion;
    @Getter private final String icono;
    @Getter private final int nTotalAcciones;
    @Getter private final LocalDateTime fechaCreacion;
    @Getter private final boolean esCotizada;
    @Getter private final boolean estaCerrado;
    @Getter private final LocalDateTime fechaCerrado;

    public Empresa nuevoDirector(UUID directorJugadorId) {
        return new Empresa(empresaId, nombre, fundadorJugadorId, directorJugadorId, descripccion, icono, nTotalAcciones,
                fechaCreacion, esCotizada, estaCerrado, fechaCerrado);
    }

    public Empresa incrementNTotalAccionesEn(int nTotalAccionesAIncrementar){
        return new Empresa(empresaId, nombre, fundadorJugadorId, directorJugadorId, descripccion, icono, nTotalAcciones + nTotalAccionesAIncrementar,
                fechaCreacion, esCotizada, estaCerrado, fechaCerrado);
    }

    public Empresa marcarComoCotizada() {
        return new Empresa(empresaId, nombre, fundadorJugadorId, directorJugadorId, descripccion, icono, nTotalAcciones,
                fechaCreacion, true, estaCerrado, fechaCerrado);
    }

    public Empresa cerrar() {
        return new Empresa(empresaId, nombre, fundadorJugadorId, directorJugadorId, descripccion, icono, nTotalAcciones,
                fechaCreacion, esCotizada, true, LocalDateTime.now());
    }

    public Empresa editar(EditarEmpresaParametros parametros) {
        return new Empresa(empresaId, parametros.getNuevoNombre(), fundadorJugadorId, directorJugadorId, parametros.getNuevaDescripccion(),
                parametros.getNuevoIcono(), nTotalAcciones, fechaCreacion, esCotizada, estaCerrado, fechaCerrado);
    }

    public static Empresa fromParametrosCrearEmpresa(CrearEmpresaParametros comando) {
        return Empresa.builder()
                .empresaId(UUID.randomUUID())
                .nombre(comando.getNombre())
                .fundadorJugadorId(comando.getJugadorCreadorId())
                .directorJugadorId(comando.getJugadorCreadorId())
                .descripccion(comando.getDescripccion())
                .icono(comando.getIcono())
                .nTotalAcciones(N_ACCIONES_INICIAL)
                .fechaCreacion(LocalDateTime.now())
                .esCotizada(false)
                .estaCerrado(false)
                .fechaCerrado(null)
                .build();
    }
}
