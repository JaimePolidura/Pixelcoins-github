package es.serversurvival.pixelcoins.empresas.editarempresa;

import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.pixelcoins.empresas._shared.empresas.domain.Empresa;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class EmpresaEditada extends PixelcoinsEvento {
    @Getter private final Empresa empresaAntigua;
    @Getter private final Empresa empresaAhora;
    @Getter private final String nuevoNombre;
    @Getter private final String nuevaDescripccion;
    @Getter private final String nuevoIcono;

    public UUID getEmpresaId() {
        return this.empresaAhora.getEmpresaId();
    }

    public UUID getDirectorId() {
        return this.empresaAhora.getDirectorJugadorId();
    }
}
