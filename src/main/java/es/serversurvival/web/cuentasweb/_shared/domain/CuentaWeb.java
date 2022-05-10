package es.serversurvival.web.cuentasweb._shared.domain;

import es.jaime.javaddd.domain.Aggregate;
import es.serversurvival._shared.mysql.TablaObjeto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class CuentaWeb extends Aggregate implements TablaObjeto {
    @Getter private final UUID cuentaWebId;
    @Getter private final String username;
    @Getter private final String password;
    @Getter private final boolean active = true;
    @Getter private final String roles = "USER";

    public CuentaWeb withPassword(String password){
        return new CuentaWeb(cuentaWebId, username, password);
    }

    public CuentaWeb withUsername(String username){
        return new CuentaWeb(cuentaWebId, username, password);
    }
}
