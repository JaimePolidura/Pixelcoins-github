package es.serversurvival.pixelcoins.mercado._shared.accion;

import es.dependencyinjector.dependencies.DependenciesRepository;
import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.pixelcoins.mercado._shared.Oferta;
import es.serversurvival.pixelcoins.mercado._shared.OfertaCustomComprarValidator;
import lombok.AllArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public final class OfertaCustomCaller {
    private final DependenciesRepository dependencies;

    public void callCustomComprarValidator(Oferta oferta, UUID compradorId) {
        this.dependencies.filterByImplementsInterfaceWithGeneric(OfertaCustomComprarValidator.class, oferta.getClass()).ifPresent(validator -> {
            validator.validate(oferta, compradorId);
        });
    }

    public void callAccion(Class<? extends OfertaAccionListener> customOfertaAccionListenerClass,
                           Oferta oferta, UUID jugadorId) {
        Optional<OfertaAccionListener> customOfertaAccionListener = (Optional<OfertaAccionListener>) dependencies.filterByImplementsInterfaceWithGeneric(
                customOfertaAccionListenerClass,
                oferta.getTipo().getOfertaClass()
        );

        if(customOfertaAccionListener.isPresent()){
            customOfertaAccionListener.get().on(oferta, jugadorId);
        }
    }
}
