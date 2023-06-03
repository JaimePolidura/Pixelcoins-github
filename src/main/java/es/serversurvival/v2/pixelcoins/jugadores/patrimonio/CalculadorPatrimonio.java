package es.serversurvival.v2.pixelcoins.jugadores.patrimonio;

import java.util.UUID;

public interface CalculadorPatrimonio {
    double calcular(UUID jugadorId);

    TipoCuentaPatrimonio tipoCuenta();
}
