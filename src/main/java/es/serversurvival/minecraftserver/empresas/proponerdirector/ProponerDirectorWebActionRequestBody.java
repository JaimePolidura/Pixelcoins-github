package es.serversurvival.minecraftserver.empresas.proponerdirector;

import es.serversurvival.minecraftserver.webaction.messages.WebActionFormParam;
import es.serversurvival.minecraftserver.webaction.messages.WebActionRequestBody;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class ProponerDirectorWebActionRequestBody extends WebActionRequestBody {
    @Getter
    @WebActionFormParam(showPriory = 10, desc = "Nombre de la empresa")
    private String nombreDeLaEmpresa;

    @Getter
    @WebActionFormParam(showPriory = 9, desc = "Nombre del nuevo director")
    private String nombreDelNuevoDirector;

    @Getter
    @WebActionFormParam(showPriory = 8, desc = "Sueldo en PC que va a cobrar")
    private double sueldo;

    @Getter
    @WebActionFormParam(showPriory = 7, desc = "Cada cuantos dias va a cobrar el sueldo")
    private int periodoPagoEnDias;

    @Getter
    @WebActionFormParam(showPriory = 6, desc = "Razon por la que se va a cambiar el director")
    private String descripccion;
}
