package es.serversurvival.pixelcoins.empresas._shared.empresas.domain;

import es.serversurvival._shared.ConfigurationVariables;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.pixelcoins.empresas.editarempresa.EditarEmpresaParametros;
import es.serversurvival.pixelcoins.empresas.crear.CrearEmpresaParametros;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class Empresa {
    @Getter private UUID empresaId;
    @Getter private String nombre;
    @Getter private UUID fundadorJugadorId;
    @Getter private UUID directorJugadorId;
    @Getter private String descripcion;
    @Getter private String logotipo;
    @Getter private int nTotalAcciones;
    @Getter private LocalDateTime fechaCreacion;
    @Getter private boolean esCotizada;
    @Getter private boolean estaCerrado;
    @Getter private LocalDateTime fechaCerrado;

    public Empresa nuevoDirector(UUID directorJugadorId) {
        return new Empresa(empresaId, nombre, fundadorJugadorId, directorJugadorId, descripcion, logotipo, nTotalAcciones,
                fechaCreacion, esCotizada, estaCerrado, fechaCerrado);
    }

    public Empresa incrementNTotalAccionesEn(int nTotalAccionesAIncrementar){
        return new Empresa(empresaId, nombre, fundadorJugadorId, directorJugadorId, descripcion, logotipo, nTotalAcciones + nTotalAccionesAIncrementar,
                fechaCreacion, esCotizada, estaCerrado, fechaCerrado);
    }

    public Empresa marcarComoCotizada() {
        return new Empresa(empresaId, nombre, fundadorJugadorId, directorJugadorId, descripcion, logotipo, nTotalAcciones,
                fechaCreacion, true, estaCerrado, fechaCerrado);
    }

    public Empresa cerrar() {
        return new Empresa(empresaId, nombre, fundadorJugadorId, directorJugadorId, descripcion, logotipo, nTotalAcciones,
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
                .fechaCerrado(Funciones.NULL_LOCALDATETIME)
                .fundadorJugadorId(comando.getJugadorCreadorId())
                .directorJugadorId(comando.getJugadorCreadorId())
                .descripcion(comando.getDescripccion())
                .logotipo(comando.getIcono())
                .nTotalAcciones(ConfigurationVariables.EMPRESAS_N_ACCIONES_INICIAL)
                .fechaCreacion(LocalDateTime.now())
                .esCotizada(false)
                .estaCerrado(false)
                .build();
    }
}
