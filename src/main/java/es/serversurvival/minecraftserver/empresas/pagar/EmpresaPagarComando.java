package es.serversurvival.minecraftserver.empresas.pagar;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class EmpresaPagarComando {
    @Getter private String empresa;
    @Getter private double precio;
}
