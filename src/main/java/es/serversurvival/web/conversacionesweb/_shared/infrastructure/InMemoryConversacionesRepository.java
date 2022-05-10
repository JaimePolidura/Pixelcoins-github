package es.serversurvival.web.conversacionesweb._shared.infrastructure;

import es.serversurvival.web.conversacionesweb._shared.domain.ConversacionWeb;
import es.serversurvival.web.conversacionesweb._shared.domain.ConversacionesWebRepostiory;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public final class InMemoryConversacionesRepository implements ConversacionesWebRepostiory {
    private final Set<ConversacionWeb> conversacionesWeb;

    public InMemoryConversacionesRepository(){
        this.conversacionesWeb = new HashSet<>();
    }

    @Override
    public synchronized void save(ConversacionWeb conversacionWeb) {
        this.conversacionesWeb.add(conversacionWeb);
    }

    @Override
    public synchronized Optional<ConversacionWeb> findByWebNombre(String webNombre) {
        return this.conversacionesWeb.stream()
                .filter(conversacionWeb -> conversacionWeb.getWebNombre().equalsIgnoreCase(webNombre))
                .findFirst();
    }

    @Override
    public synchronized Optional<ConversacionWeb> findByServerNombre(String serverNombre) {
        return this.conversacionesWeb.stream()
                .filter(conversacionWeb -> conversacionWeb.getServerNombre().equalsIgnoreCase(serverNombre))
                .findFirst();
    }

    @Override
    public synchronized void deleteByWebNombre(String webNombre) {
        this.conversacionesWeb.removeIf(conversacionWeb -> conversacionWeb.getWebNombre().equalsIgnoreCase(webNombre));
    }

    @Override
    public synchronized void deleteByServerNombre(String serverNombre) {
        this.conversacionesWeb.removeIf(conversacionWeb -> conversacionWeb.getServerNombre().equalsIgnoreCase(serverNombre));
    }
}
