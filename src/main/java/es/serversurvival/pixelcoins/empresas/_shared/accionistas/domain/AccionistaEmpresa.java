package es.serversurvival.pixelcoins.empresas._shared.accionistas.domain;

import es.serversurvival.pixelcoins.empresas._shared.empresas.domain.Empresa;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class AccionistaEmpresa {
    @Getter private UUID accionistaId;
    @Getter private UUID empresaId;
    @Getter private UUID accionisaJugadorId;
    @Getter private int nAcciones;

    public AccionistaEmpresa decrementarNAccionesPorUno() {
        return new AccionistaEmpresa(accionistaId, empresaId, accionisaJugadorId, nAcciones - 1);
    }

    public AccionistaEmpresa incrementarNAccionesEnUno() {
        return new AccionistaEmpresa(accionistaId, empresaId, accionisaJugadorId, nAcciones + 1);
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
