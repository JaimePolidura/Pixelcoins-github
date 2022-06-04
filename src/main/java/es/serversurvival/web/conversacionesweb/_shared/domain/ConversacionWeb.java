package es.serversurvival.web.conversacionesweb._shared.domain;

import es.jaime.javaddd.domain.Aggregate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class ConversacionWeb {
    @Getter private final String webNombre;
    @Getter private final String serverNombre;
}
