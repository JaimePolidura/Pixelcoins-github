package es.serversurvival.pixelcoins.deudas.comprar;

import es.dependencyinjector.dependencies.annotations.Service;
import es.jaime.javaddd.domain.exceptions.CannotBeYourself;
import es.serversurvival.pixelcoins.deudas._shared.OfertaDeudaMercadoSecundario;
import es.serversurvival.pixelcoins.deudas._shared.application.DeudasService;
import es.serversurvival.pixelcoins.deudas._shared.domain.Deuda;
import es.serversurvival.pixelcoins.mercado._shared.OfertaCustomComprarValidator;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Service
@AllArgsConstructor
public final class ComprarDeudaMercadoSecundarioValidator implements OfertaCustomComprarValidator<OfertaDeudaMercadoSecundario> {
    private final DeudasService deudasService;

    @Override
    public void validate(OfertaDeudaMercadoSecundario oferta, UUID compradorId) {
        Deuda deuda = deudasService.getById(oferta.getObjetoToUUID());
        if(deuda.getDeudorJugadorId().equals(compradorId)){
            throw new CannotBeYourself("No puedes comprar una deuda donde eres ya deudor");
        }
    }
}
