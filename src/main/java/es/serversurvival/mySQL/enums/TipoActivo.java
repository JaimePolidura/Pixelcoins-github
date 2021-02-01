package es.serversurvival.mySQL.enums;

import es.serversurvival.apiHttp.IEXCloud_API;
import lombok.SneakyThrows;

public enum TipoActivo {
    ACCIONES {
        @Override
        public double getPrecio(String simbolo) {
            try {
                return IEXCloud_API.getOnlyPrice(simbolo);
            } catch (Exception e) {
                return -1;
            }
        }
    },
    CRIPTOMONEDAS{
        @Override
        public double getPrecio(String simbolo) {
            try {
                return IEXCloud_API.getPrecioCriptomoneda(simbolo);
            } catch (Exception e) {
                return -1;
            }
        }
    },
    MATERIAS_PRIMAS{
        @Override
        public double getPrecio(String simbolo) {
            try {
                return IEXCloud_API.getPrecioMateriaPrima(simbolo);
            } catch (Exception e) {
                return -1;
            }
        }
    },
    ACCIONES_SERVER {
        @Override
        public double getPrecio(String simbolo) {
            return 0;
        }
    };

    public abstract double getPrecio (String simbolo);
}
