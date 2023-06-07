package es.serversurvival.pixelcoins.empresas._shared.accionistas;

import es.serversurvival.pixelcoins.empresas._shared.empresas.Empresa;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

//TODO AccionistaEmpresa y posiciones son muy similares
@Builder
@AllArgsConstructor
public final class AccionistaEmpresa {
    @Getter private final UUID accionistaId;
    @Getter private final UUID empresaId;
    @Getter private final UUID accionisaJugadorId;
    @Getter private final int nAcciones;

    public AccionistaEmpresa decrementarNAccionesPorUno() {
        return new AccionistaEmpresa(accionistaId, empresaId, accionistaId, nAcciones - 1);
    }

    public AccionistaEmpresa incrementarNAccionesEnUno() {
        return new AccionistaEmpresa(accionistaId, empresaId, accionistaId, nAcciones + 1);
    }

    public boolean noTieneMasAcciones() {
        return this.nAcciones <= 0;
    }

    public static AccionistaEmpresa fromFundadorEmpresa(Empresa empresa) {
        return AccionistaEmpresa.builder()
                .accionistaId(UUID.randomUUID())
                .empresaId(empresa.getEmpresaId())
                .accionisaJugadorId(empresa.getFundadorJugadorId())
                .nAcciones(empresa.getNTotalAcciones())
                .build();
    }
}
