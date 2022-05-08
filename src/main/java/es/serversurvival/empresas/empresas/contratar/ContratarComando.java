package es.serversurvival.empresas.empresas.contratar;

import lombok.Getter;

public class ContratarComando {
    @Getter private String jugador;
    @Getter private String empresa;
    @Getter private double sueldo;
    @Getter private String tipoSueldo;
    @Getter private String cargo;
}
