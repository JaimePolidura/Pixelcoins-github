package es.serversurvival.nfs.deudas.cancelar;

import es.serversurvival.legacy.mySQL.eventos.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class DeudaCanceladaEvento extends PixelcoinsEvento {
    @Getter private final String acredor;
    @Getter private final String deudor;
    @Getter private final int pixelcoins_restantes;
}
