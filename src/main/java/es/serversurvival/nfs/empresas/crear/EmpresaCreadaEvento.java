package es.serversurvival.nfs.empresas.crear;

import es.serversurvival.legacy.mySQL.eventos.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class EmpresaCreadaEvento extends PixelcoinsEvento {
    @Getter private final String jugador;
    @Getter private final String empresa;
}
