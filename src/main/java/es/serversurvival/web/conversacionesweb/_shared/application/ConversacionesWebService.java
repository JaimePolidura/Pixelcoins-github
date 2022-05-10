package es.serversurvival.web.conversacionesweb._shared.application;

import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.web.conversacionesweb._shared.domain.ConversacionWeb;
import es.serversurvival.web.conversacionesweb._shared.domain.ConversacionesWebRepostiory;

import java.util.Optional;

public final class ConversacionesWebService {
    private final ConversacionesWebRepostiory repositoryDb;

    public ConversacionesWebService() {
        this.repositoryDb = DependecyContainer.get(ConversacionesWebRepostiory.class);
    }

    public void save(ConversacionWeb conversacionWeb) {
        this.repositoryDb.save(conversacionWeb);
    }

    public boolean hayConversacionEntre (String webNombre, String serverNombre) {
        var conversacionWebOptional = this.repositoryDb.findByWebNombre(webNombre);

        return conversacionWebOptional.isPresent() && conversacionWebOptional.get().getServerNombre().equalsIgnoreCase(serverNombre);
    }

    public Optional<ConversacionWeb> findByServerNombre(String serverNombre) {
        return this.repositoryDb.findByServerNombre(serverNombre);
    }

    public ConversacionWeb getByWebNombre(String webNombre) {
        return this.repositoryDb.findByWebNombre(webNombre)
                .orElseThrow(() -> new ResourceNotFound("Conversacion no encontrada"));
    }

    public void deleteByWebNombre(String webNombre) {
        this.repositoryDb.deleteByWebNombre(webNombre);
    }

    public void deleteByServerNombre(String serverNombre) {
        this.repositoryDb.deleteByServerNombre(serverNombre);
    }
}
