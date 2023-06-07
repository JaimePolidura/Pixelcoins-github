package es.serversurvival.minecraftserver.empresas.sacar;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class SacarPixelcoinsEmpresaComando {
    @Getter private String empresa;
    @Getter private double pixelcoins;
}
