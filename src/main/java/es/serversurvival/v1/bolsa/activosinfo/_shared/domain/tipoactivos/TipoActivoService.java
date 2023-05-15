package es.serversurvival.v1.bolsa.activosinfo._shared.domain.tipoactivos;

public abstract class TipoActivoService {
    public abstract Double getPrecio(String nombreActivo) throws Exception;
    public abstract String getNombreActivoLargo(String nombreActivo) throws Exception;
}
