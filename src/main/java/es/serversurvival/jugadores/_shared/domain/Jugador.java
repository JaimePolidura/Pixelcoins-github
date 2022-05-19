package es.serversurvival.jugadores._shared.domain;

import es.jaime.javaddd.domain.Aggregate;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false)
public final class Jugador extends Aggregate {
    @Getter private final UUID jugadorId;
    @Getter private final String nombre;
    @Getter private final double pixelcoins;
    @Getter private final int nVentas;
    @Getter private final double ingresos;
    @Getter private final double gastos;
    @Getter private final int nInpagosDeuda;
    @Getter private final int nPagosDeuda;
    @Getter private final int numeroVerificacionCuenta;

    public Jugador withNombre(String nombre){
        return new Jugador(jugadorId, nombre, pixelcoins, nVentas, ingresos, gastos,
                nInpagosDeuda, nPagosDeuda, numeroVerificacionCuenta);
    }

    public Jugador withNumeroCuenta(int numeroVerificacionCuenta){
        return new Jugador(jugadorId, nombre, pixelcoins, nVentas, ingresos, gastos,
                nInpagosDeuda, nPagosDeuda, numeroVerificacionCuenta);
    }

    public Jugador incrementGastosBy(double gastos){
        return new Jugador(jugadorId, nombre, pixelcoins, nVentas, ingresos, this.gastos + gastos,
                nInpagosDeuda, nPagosDeuda, numeroVerificacionCuenta);
    }

    public Jugador incrementIngresosBy(double ingresos){
        return new Jugador(jugadorId, nombre, pixelcoins, nVentas, this.ingresos + ingresos, gastos,
                nInpagosDeuda, nPagosDeuda, numeroVerificacionCuenta);
    }

    public Jugador incrementNVentas(){
        return new Jugador(jugadorId, nombre, pixelcoins, nVentas + 1, ingresos, gastos,
                nInpagosDeuda, nPagosDeuda, numeroVerificacionCuenta);
    }

    public Jugador incrementNInpago(){
        return new Jugador(jugadorId, nombre, pixelcoins, nVentas, ingresos, gastos,
                nInpagosDeuda + 1, nPagosDeuda, numeroVerificacionCuenta);
    }

    public Jugador incrementNPagos(){
        return new Jugador(jugadorId, nombre, pixelcoins, nVentas, ingresos, gastos,
                nInpagosDeuda, nPagosDeuda + 1, numeroVerificacionCuenta);
    }

    public Jugador incrementPixelcoinsBy(double pixelcoins){
        return new Jugador(jugadorId, nombre, this.pixelcoins + pixelcoins, nVentas, ingresos, gastos,
                nInpagosDeuda, nPagosDeuda, numeroVerificacionCuenta);
    }

    public Jugador decrementPixelcoinsBy(double pixelcoins){
        return new Jugador(jugadorId, nombre, this.pixelcoins - pixelcoins, nVentas, ingresos, gastos,
                nInpagosDeuda, nPagosDeuda, numeroVerificacionCuenta);
    }
}
