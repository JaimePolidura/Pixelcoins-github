package es.serversurvival.v1.empresas.empresas._shared.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.Material;

import java.util.UUID;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
public final class Empresa {
    @Getter private final UUID empresaId;
    @Getter private final String nombre;
    @Getter private final String owner;
    @Getter private final double pixelcoins;
    @Getter private final double ingresos;
    @Getter private final double gastos;
    @Getter private final String icono;
    @Getter private final String descripcion;
    @Getter private final boolean cotizada;
    @Getter private final int accionesTotales;

    public Empresa withOwner(String owner){
        return new Empresa(empresaId, nombre, owner, pixelcoins, ingresos,
                gastos, icono, descripcion, cotizada, accionesTotales);
    }

    public Empresa incrementPixelcoinsBy(double pixelcoinsToIncrement){
        return new Empresa(empresaId, nombre, owner, this.pixelcoins + pixelcoinsToIncrement, ingresos,
                gastos, icono, descripcion, cotizada, accionesTotales);
    }

    public Empresa decrementPixelcoinsBy(double pixelcoinsToDecrement){
        return new Empresa(empresaId, nombre, owner, this.pixelcoins - pixelcoinsToDecrement, ingresos,
                gastos, icono, descripcion, cotizada, accionesTotales);
    }

    public Empresa setCotizadaToTrue(){
        return new Empresa(empresaId, nombre, owner, this.pixelcoins, this.ingresos,
                gastos, icono, descripcion, true, accionesTotales);
    }

    public Empresa withAccionesTotales(int accionesTotales){
        return new Empresa(empresaId, nombre, owner, this.pixelcoins, this.ingresos,
                gastos, icono, descripcion, cotizada, accionesTotales);
    }

    public Empresa withDescripccion(String desc){
        return new Empresa(empresaId, nombre, owner, this.pixelcoins, this.ingresos,
                gastos, icono, desc, cotizada, accionesTotales);
    }
    
    public Empresa withNombre(String nombre){
        return new Empresa(empresaId, nombre, owner, this.pixelcoins, this.ingresos,
                gastos, icono, descripcion, cotizada, accionesTotales);
    }

    public Empresa incrementIngresosBy(double ingresosToIncrement){
        return new Empresa(empresaId, nombre, owner, this.pixelcoins, this.ingresos + ingresosToIncrement,
                gastos, icono, descripcion, cotizada, accionesTotales);
    }

    public Empresa incrementGastosBy(double gastosToIncrement){
        return new Empresa(empresaId, nombre, owner, this.pixelcoins, this.ingresos,
                gastos + gastosToIncrement, icono, descripcion, cotizada, accionesTotales);
    }

    public Empresa withIcono(Material material){
        return new Empresa(empresaId, nombre, owner, this.pixelcoins, this.ingresos,
                gastos, material.toString(), descripcion, cotizada, accionesTotales);
    }
}
