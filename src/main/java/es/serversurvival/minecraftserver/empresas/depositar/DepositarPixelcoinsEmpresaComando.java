package es.serversurvival.minecraftserver.empresas.depositar;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class DepositarPixelcoinsEmpresaComando {
    @Getter private String empresa;
    @Getter private double pixelcoins;
}
