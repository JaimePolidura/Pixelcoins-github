package es.serversurvival.deudas;

import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.deudas._shared.domain.Deuda;

import java.util.UUID;

public final class DeudasTestMother {
    public static Deuda createDeuda(String acredor, String deudor){
        return new Deuda(UUID.randomUUID(), deudor, acredor, 10, 5, 0, 2, Funciones.hoy());
    }
}
