package es.serversurvival._shared.providers;

import es.dependencyinjector.dependencies.annotations.Configuration;
import es.dependencyinjector.providers.Provider;
import es.jaime.EventBus;
import es.jaime.impl.EventBusSynch;

@Configuration
public final class EventBusProvider {
    @Provider
    public EventBus eventBus() {
        return new EventBusSynch("es.serversurvival");
    }
}
