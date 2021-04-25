package es.serversurvival.nfs.cuentaweb;

import es.serversurvival.legacy.mySQL.tablasObjetos.TablaObjeto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class Cuenta implements TablaObjeto {
    @Getter private final int id;
    @Getter private final String username;
    @Getter private final String password;
    @Getter private final boolean active = true;
    @Getter private final String roles = "USER";
}
