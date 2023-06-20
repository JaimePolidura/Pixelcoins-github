package es.serversurvival.pixelcoins.empresas.pagarsueldos;

import es.serversurvival.pixelcoins._shared.usecases.ParametrosUseCase;
import es.serversurvival.pixelcoins.empresas._shared.empresas.domain.Empresa;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class PagadorSueldosEmpresaParametros implements ParametrosUseCase {
    @Getter private final Empresa empresa;

    public static PagadorSueldosEmpresaParametros from(Empresa empresa) {
        return new PagadorSueldosEmpresaParametros(empresa);
    }
}
