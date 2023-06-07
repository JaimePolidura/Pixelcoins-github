package es.serversurvival.minecraftserver.empresas.crear;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class CrearEmpresaComando {
    @Getter private String empresa;
    @Getter private String descripccion;
}
