package es.serversurvival.v2.minecraftserver.empresas.editarempresa;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class EditarEmpresaComando {
    @Getter private final String empresa;
    @Getter private final String queSeEdita;
    @Getter private final String nuevoValor;
}
