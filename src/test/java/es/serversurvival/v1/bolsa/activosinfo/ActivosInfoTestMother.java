package es.serversurvival.v1.bolsa.activosinfo;

import es.serversurvival.v1.bolsa.activosinfo._shared.domain.ActivoInfo;
import es.serversurvival.v1.bolsa.activosinfo._shared.domain.tipoactivos.TipoActivo;

public final class ActivosInfoTestMother {
    public static ActivoInfo createActivoInfoAcciones(String nombreActivo){
        return new ActivoInfo(nombreActivo, 1, TipoActivo.ACCIONES, nombreActivo);
    }

    public static ActivoInfo createActivoInfoCriptos(String nombreActivo){
        return new ActivoInfo(nombreActivo, 1, TipoActivo.CRIPTOMONEDAS, nombreActivo);
    }

    public static ActivoInfo createActivoInfoMateriasPrimas(String nombreActivo){
        return new ActivoInfo(nombreActivo, 1, TipoActivo.MATERIAS_PRIMAS, nombreActivo);
    }
}
