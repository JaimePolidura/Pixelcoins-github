package es.serversurvival._shared.eventospixelcoins;

import es.dependencyinjector.dependencies.annotations.Service;
import es.jaime.Event;
import es.jaime.EventBus;
import es.jaime.impl.EventBusSync;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.concurrent.Executor;

@RequiredArgsConstructor
public final class EventBusWrapperAsync implements EventBus {
    private final EventBusSync syncEventBus;
    private final Executor executor;

    @Override
    public void publish(@NonNull Collection<? extends Event> events) {
        events.forEach(this::publish);
    }

    @Override
    public void publish(@NonNull Event event) {
        this.executor.execute(() -> syncEventBus.publish(event));
    }
}
