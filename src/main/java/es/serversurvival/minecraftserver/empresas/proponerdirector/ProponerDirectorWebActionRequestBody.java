package es.serversurvival.minecraftserver.empresas.proponerdirector;

import es.serversurvival.minecraftserver.webaction.messages.WebActionRequestBody;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class ProponerDirectorWebActionRequestBody extends WebActionRequestBody {
    @Getter private String nombreDeLaEmpresa;
    @Getter private String nombreDelNuevoDirector;
    @Getter private String descripccion;
    @Getter private double sueldo;
    @Getter private long periodoPagoEnSegundos;
}
