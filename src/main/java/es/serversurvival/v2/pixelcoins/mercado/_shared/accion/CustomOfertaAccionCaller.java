package es.serversurvival.v2.pixelcoins.mercado._shared.accion;

import es.dependencyinjector.dependencies.DependenciesRepository;
import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.v2.pixelcoins.mercado._shared.Oferta;
import lombok.AllArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public final class CustomOfertaAccionCaller {
    private final DependenciesRepository dependenciesRepository;

    public void call(Class<? extends CustomOfertaAccionListener> customOfertaAccionListenerClazz,
                                        Oferta oferta, UUID jugadorId) {
        Optional<CustomOfertaAccionListener> customOfertaAccionListener = (Optional<CustomOfertaAccionListener>) dependenciesRepository.filterByImplementsInterfaceWithGeneric(
                customOfertaAccionListenerClazz,
                oferta.getTipoOferta().getOfertaClass()
        );

        if(customOfertaAccionListener.isPresent()){
            customOfertaAccionListener.get().on(oferta, jugadorId);
        }
    }
}
