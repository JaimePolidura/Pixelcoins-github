package es.serversurvival.web.conversacionesweb._shared.domain;

import java.util.Optional;

public interface ConversacionesWebRepostiory {
    void save(ConversacionWeb conversacionWeb);

    Optional<ConversacionWeb> findByWebNombre(String webNombre);

    Optional<ConversacionWeb> findByServerNombre(String serverNombre);

    void deleteByWebNombre(String webNombre);

    void deleteByServerNombre(String serverNombre);
}
