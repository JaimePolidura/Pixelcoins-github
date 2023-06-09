package es.serversurvival.pixelcoins.mercado._shared.accion;

import es.dependencyinjector.dependencies.DependenciesRepository;
import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.pixelcoins.mercado._shared.Oferta;
import lombok.AllArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public final class OfertaAccionCaller {
    private final DependenciesRepository dependencies;

    public void call(Class<? extends OfertaAccionListener> customOfertaAccionListenerClazz,
                                        Oferta oferta, UUID jugadorId) {
        Optional<OfertaAccionListener> customOfertaAccionListener = (Optional<OfertaAccionListener>) dependencies.filterByImplementsInterfaceWithGeneric(
                customOfertaAccionListenerClazz,
                oferta.getTipo().getOfertaClass()
        );

        if(customOfertaAccionListener.isPresent()){
            customOfertaAccionListener.get().on(oferta, jugadorId);
        }
    }
}
