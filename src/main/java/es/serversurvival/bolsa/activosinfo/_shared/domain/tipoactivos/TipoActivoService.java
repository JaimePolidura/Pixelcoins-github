package es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos;

public abstract class TipoActivoService {
    public abstract double getPrecio(String nombreActivo);
    public abstract String getNombreActivoLargo(String nombreActivo);
}
