package es.serversurvival.v2.pixelcoins.empresas.ipo;

import es.serversurvival.v1._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.checkerframework.checker.fenum.qual.SwingHorizontalOrientation;

import java.util.UUID;

@AllArgsConstructor
public final class EmpresaIPORealizada extends PixelcoinsEvento {
    @Getter private final UUID ofertaId;
}
