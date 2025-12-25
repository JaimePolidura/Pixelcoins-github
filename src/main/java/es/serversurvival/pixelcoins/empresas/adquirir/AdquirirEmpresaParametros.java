package es.serversurvival.pixelcoins.empresas.adquirir;

import es.serversurvival.pixelcoins._shared.usecases.ParametrosUseCase;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class AdquirirEmpresaParametros implements ParametrosUseCase {
    private final UUID jugadorId;
    private final UUID empresaIdCompradora;
    private final UUID empresaIdAdquirida;
    private final double precioTotal;
    private final Optional<UUID> votacionId;
}
