package es.serversurvival.bolsa.activosinfo;

import es.serversurvival.bolsa.activosinfo._shared.domain.ActivoInfo;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.SupportedTipoActivo;

public final class ActivosInfoTestMother {
    public static ActivoInfo createActivoInfoAcciones(String nombreActivo){
        return new ActivoInfo(nombreActivo, 1, SupportedTipoActivo.ACCIONES, nombreActivo);
    }

    public static ActivoInfo createActivoInfoCriptos(String nombreActivo){
        return new ActivoInfo(nombreActivo, 1, SupportedTipoActivo.CRIPTOMONEDAS, nombreActivo);
    }

    public static ActivoInfo createActivoInfoMateriasPrimas(String nombreActivo){
        return new ActivoInfo(nombreActivo, 1, SupportedTipoActivo.MATERIAS_PRIMAS, nombreActivo);
    }
}
