package es.serversurvival.minecraftserver.empresas.proponerdirector;

import es.serversurvival.minecraftserver.webaction.messages.WebActionRequestBody;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class ProponerDirectorWebActionRequestBody extends WebActionRequestBody {
    @Getter private String nombreDelNuevoDirector;
    @Getter private String nombreDeLaEmpresa;
    @Getter private long periodoPagoEnDias;
    @Getter private String descripccion;
    @Getter private double sueldo;
}
