package es.serversurvival.deudas._shared.domain;


import es.jaime.javaddd.domain.Aggregate;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
public final class Deuda extends Aggregate {
    @Getter private final UUID deudaId;
    @Getter private final String deudor;
    @Getter private final String acredor;
    @Getter private final double pixelcoinsRestantes;
    @Getter private final int tiempoRestante;
    @Getter private final int interes;
    @Getter private final double cuota;
    @Getter private final String fechaUltimapaga;

    public Deuda decrementPixelcoinsRestantes(double pixelcoinsRestantes){
        return new Deuda(deudaId, deudor, acredor, this.pixelcoinsRestantes - pixelcoinsRestantes,
                tiempoRestante, interes, cuota, fechaUltimapaga);
    }

    public Deuda decrementTiempoRestanteByOne(){
        return new Deuda(deudaId, deudor, acredor, pixelcoinsRestantes,
                this.tiempoRestante - 1, interes, cuota, fechaUltimapaga);
    }

    public Deuda withFechaUltimoPago(String fecha){
        return new Deuda(deudaId, deudor, acredor, pixelcoinsRestantes,
                tiempoRestante, interes, cuota, fecha);
    }

    public Deuda withAcredor(String acredor){
        return new Deuda(deudaId, deudor, acredor, pixelcoinsRestantes,
                tiempoRestante, interes, cuota, fechaUltimapaga);
    }

    public Deuda withDeudor(String deudor){
        return new Deuda(deudaId, deudor, acredor, pixelcoinsRestantes,
                tiempoRestante, interes, cuota, fechaUltimapaga);
    }
}
