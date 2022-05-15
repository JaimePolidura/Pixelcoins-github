package es.serversurvival.web.verificacioncuentas._shared.domain;

import es.jaime.javaddd.domain.Aggregate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class VerificacionCuenta extends Aggregate {
    @Getter private final String jugador;
    @Getter private final int numero;
}
