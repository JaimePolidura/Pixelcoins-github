package es.serversurvival.nfs.webconnection.verificacioncuentas.mysql;

import es.serversurvival.legacy.mySQL.tablasObjetos.TablaObjeto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class VerificacionCuenta implements TablaObjeto {
    @Getter private final String jugador;
    @Getter private final int numero;
}
