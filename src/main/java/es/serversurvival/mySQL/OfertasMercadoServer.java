package es.serversurvival.mySQL;

import es.serversurvival.mySQL.enums.TipoOfertante;
import es.serversurvival.mySQL.tablasObjetos.OfertaMercadoServer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public final class OfertasMercadoServer extends MySQL{
    private OfertasMercadoServer() {}
    public static final OfertasMercadoServer INSTANCE = new OfertasMercadoServer();

    public void nueva (String jugador, String empresa, double precio, int cantidad, TipoOfertante tipoOfertante) {
        String fecha = dateFormater.format(new Date());

        executeUpdate("INSERT INTO ofertasbolsaserver (jugador, empresa, precio, cantidad, fecha, tipo_ofertante) VALUES ('"+jugador+"', '"+empresa+"', '"+precio+"', '"+cantidad+"', '"+fecha+"', '"+tipoOfertante.toString()+"')");
    }

    public List<OfertaMercadoServer> getAll () {
        return buildListFromQuery("SELECT * FROM ofertasbolsaserver");
    }

    @Override
    protected OfertaMercadoServer buildObjectFromResultSet(ResultSet rs) throws SQLException {
        return new OfertaMercadoServer(
                rs.getInt("id"),
                rs.getString("jugador"),
                rs.getString("empresa"),
                rs.getDouble("precio"),
                rs.getInt("cantidad"),
                rs.getString("fecha"),
                TipoOfertante.valueOf(rs.getString("tipo_ofertante"))
        );
    }
}
