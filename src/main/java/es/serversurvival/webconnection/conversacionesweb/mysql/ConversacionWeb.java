package es.serversurvival.webconnection.conversacionesweb.mysql;

import es.serversurvival.shared.mysql.TablaObjeto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class ConversacionWeb implements TablaObjeto {
    @Getter private final String web_nombre;
    @Getter private final String server_nombre;
}
