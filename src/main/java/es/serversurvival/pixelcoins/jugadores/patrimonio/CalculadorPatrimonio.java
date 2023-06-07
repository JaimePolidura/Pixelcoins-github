package es.serversurvival.pixelcoins.jugadores.patrimonio;

import java.util.UUID;

public interface CalculadorPatrimonio {
    double calcular(UUID jugadorId);

    TipoCuentaPatrimonio tipoCuenta();
}
