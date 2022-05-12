package es.serversurvival.bolsa.other._shared.llamadasapi.mysql;

import es.serversurvival.bolsa._shared.domain.Criptomonedas;
import es.serversurvival.bolsa._shared.domain.MateriasPrimas;
import es.serversurvival.bolsa.posicionesabiertas._shared.domain.PosicionAbierta;
import es.serversurvival.bolsa.other._shared.posicionescerradas.mysql.TipoPosicion;
import es.serversurvival._shared.utils.apiHttp.IEXCloud_API;
import org.bukkit.Material;


public enum TipoActivo {
    ACCIONES(Material.NAME_TAG, "acciones") {
        @Override
        public double getPrecio(String simbolo) {
            try {
                return IEXCloud_API.getOnlyPrice(simbolo);
            } catch (Exception e) {
                return -1;
            }
        }
    },
    CRIPTOMONEDAS(Material.GOLD_BLOCK, "monedas"){
        @Override
        public double getPrecio(String simbolo) {
            try {
                return IEXCloud_API.getPrecioCriptomoneda(simbolo);
            } catch (Exception e) {
                return -1;
            }
        }
    },
    MATERIAS_PRIMAS(Material.COAL, "unidades"){
        @Override
        public double getPrecio(String simbolo) {
            try {
                return IEXCloud_API.getPrecioMateriaPrima(simbolo);
            } catch (Exception e) {
                return -1;
            }
        }
    },
    ACCIONES_SERVER(Material.GREEN_BANNER, "acciones"){
        @Override
        public double getPrecio(String simbolo) {
            return 0;
        }
    };

    public abstract double getPrecio (String simbolo);

    private final Material materialDisplay;
    private final String alias;

    TipoActivo(Material materialDisplay, String alias) {
        this.materialDisplay = materialDisplay;
        this.alias = alias;
    }

    public Material getMaterialDisplay() {
        return materialDisplay;
    }

    public String getAlias() {
        return alias;
    }

    public String getAliasUpperFirst() {
        return alias.substring(0, 1).toUpperCase() + alias.substring(1);
    }

    public static String getNombreActivo(String valor){
        String nombreSimbolo = MateriasPrimas.getNombreActivo(valor);

        if(nombreSimbolo.equalsIgnoreCase(valor))
            nombreSimbolo = Criptomonedas.getNombreActivo(valor);

        return nombreSimbolo;

    }

    public static Material getMaterialFor (PosicionAbierta posicion) {
        return posicion.getTipoPosicion() == TipoPosicion.CORTO ?
                Material.REDSTONE_TORCH :
                posicion.getTipoActivo().materialDisplay;
    }
}
