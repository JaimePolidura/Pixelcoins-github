package es.serversurvival._shared.providers;

import es.dependencyinjector.annotations.Configuration;
import es.dependencyinjector.annotations.Provider;
import es.jaime.EventBus;
import es.jaime.impl.EventBusSynch;

@Configuration
public final class EventBusProvider {
    @Provider
    public EventBus eventBus() {
        return new EventBusSynch("es.serversurvival");
    }
}
