package es.serversurvival.deudas._shared.newformat.domain;


import es.serversurvival._shared.mysql.TablaObjeto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class Deuda implements TablaObjeto {
    @Getter private final UUID deudaId;
    @Getter private final String deudor;
    @Getter private final String acredor;
    @Getter private final int pixelcoins_restantes;
    @Getter private final int tiempo_restante;
    @Getter private final int interes;
    @Getter private final int couta;
    @Getter private final String fecha_ultimapaga;

    public Deuda decrementPixelcoinsRestantes(int pixelcoinsRestantes){
        return new Deuda(deudaId, deudor, acredor, this.pixelcoins_restantes - pixelcoinsRestantes,
                tiempo_restante, interes, couta, fecha_ultimapaga);
    }

    public Deuda decrementTiempoRestanteByOne(){
        return new Deuda(deudaId, deudor, acredor, pixelcoins_restantes,
                this.tiempo_restante - 1, interes, couta, fecha_ultimapaga);
    }

    public Deuda withFechaUltimoPago(String fecha){
        return new Deuda(deudaId, deudor, acredor, pixelcoins_restantes,
                tiempo_restante, interes, couta, fecha);
    }

    public Deuda withAcredor(String acredor){
        return new Deuda(deudaId, deudor, acredor, pixelcoins_restantes,
                tiempo_restante, interes, couta, fecha_ultimapaga);
    }

    public Deuda withDeudor(String deudor){
        return new Deuda(deudaId, deudor, acredor, pixelcoins_restantes,
                tiempo_restante, interes, couta, fecha_ultimapaga);
    }
}
