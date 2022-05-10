package es.serversurvival.web.verificacioncuentas._shared.domain;

import es.jaime.javaddd.domain.Aggregate;
import es.serversurvival._shared.mysql.TablaObjeto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class VerificacionCuenta extends Aggregate implements TablaObjeto {
    @Getter private final String jugador;
    @Getter private final int numero;
}
