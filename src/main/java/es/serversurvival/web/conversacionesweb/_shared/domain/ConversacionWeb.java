package es.serversurvival.web.conversacionesweb._shared.domain;

import es.jaime.javaddd.domain.Aggregate;
import es.serversurvival._shared.mysql.TablaObjeto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class ConversacionWeb extends Aggregate implements TablaObjeto {
    @Getter private final String webNombre;
    @Getter private final String serverNombre;
}
