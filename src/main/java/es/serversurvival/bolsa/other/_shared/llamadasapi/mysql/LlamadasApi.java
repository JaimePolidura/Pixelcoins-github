package es.serversurvival.bolsa.other._shared.llamadasapi.mysql;

import es.jaimetruman.delete.Delete;
import es.jaimetruman.insert.Insert;
import es.jaimetruman.select.Select;
import es.jaimetruman.select.SelectOptionInitial;
import es.jaimetruman.update.Update;
import es.jaimetruman.update.UpdateOptionInitial;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival._shared.mysql.MySQLRepository;
import es.serversurvival._shared.utils.apiHttp.IEXCloud_API;
import es.serversurvival.bolsa.posicionesabiertas._shared.application.PosicionesAbiertasSerivce;
import main.Pair;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * II 156 -> 121
 */
public final class LlamadasApi extends MySQLRepository {
    public final static LlamadasApi INSTANCE = new LlamadasApi();
    private final PosicionesAbiertasSerivce posicionesAbiertasSerivce;

    private final SelectOptionInitial select;
    private final UpdateOptionInitial update;

    private LlamadasApi () {
        this.posicionesAbiertasSerivce = DependecyContainer.get(PosicionesAbiertasSerivce.class);
        this.select = Select.from("llamadasapi");
        this.update = Update.table("llamadasapi");
    }

    public void nuevaLlamada(String simbolo, double precio, TipoActivo tipo, String nombreValor){
        String insertQuery = Insert.table("llamadasapi")
                .fields("simbolo", "precio", "tipo_activo", "nombre_activo")
                .values(simbolo, precio, tipo.toString(), nombreValor);

        executeUpdate(insertQuery);
    }

    public LlamadaApi getLlamadaAPI(String simbolo) {
        return (LlamadaApi) buildObjectFromQuery(select.where("simbolo").equal(simbolo));
    }

    public List<LlamadaApi> getTodasLlamadasApi(){
        return buildListFromQuery(select);
    }

    public List<LlamadaApi> getTodasLlamadasApiCondicion (Predicate<? super LlamadaApi> condicion) {
        return getTodasLlamadasApi().stream()
                .filter(condicion)
                .collect(Collectors.toList());
    }

    public Map<String, LlamadaApi> getMapOfAllLlamadasApi () {
        Map<String, LlamadaApi> mapLlamadas = new HashMap<>();

        List<LlamadaApi> llamadaApiList = getTodasLlamadasApi();
        llamadaApiList.forEach(llamada -> mapLlamadas.put(llamada.getSimbolo(), llamada) );

        return mapLlamadas;
    }

    public void borrarLlamada(String simbolo){
        executeUpdate(Delete.from("llamadasapi").where("simbolo").equal(simbolo));
    }

    public void setPrecio(String simbolo, double precio){
        executeUpdate(update.set("precio", precio).where("simbolo").equal(simbolo));
    }

    public void setNombreValor(String simbolo, String nombreValor){
        executeUpdate(update.set("nombre_activo", nombreValor).where("simbolo").equal(simbolo));
    }

    public boolean estaReg (String simbolo) {
        return getLlamadaAPI(simbolo) != null;
    }

    public void borrarLlamadaSiNoEsUsada (String ticker) {
        if(this.posicionesAbiertasSerivce.existsNombreActivo(ticker))
            borrarLlamada(ticker);
    }

    public void nuevaLlamadaSiNoEstaReg(String ticker, double precio, TipoActivo tipoactivo, String nombrevalor) {
        if(!estaReg(ticker))
            nuevaLlamada(ticker, precio, tipoactivo, nombrevalor);
    }

    public Optional<Pair<String, Double>> getPairNombreValorPrecio (String ticker) {
        try{
            double precio;
            String nombreValor;

            if(estaReg(ticker)){
                LlamadaApi llamadaApi = this.getLlamadaAPI(ticker);
                nombreValor = llamadaApi.getNombre_activo();
                precio = llamadaApi.getPrecio();
            }else{
                nombreValor = IEXCloud_API.getNombreEmpresa(ticker);
                precio = IEXCloud_API.getOnlyPrice(ticker);
            }

            return Optional.of(new Pair<>(nombreValor, precio));
        }catch (Exception e){
            return Optional.empty();
        }
    }

    @Override
    protected LlamadaApi buildObjectFromResultSet(ResultSet rs) throws SQLException {
        return new LlamadaApi(rs.getString("simbolo"),
                rs.getDouble("precio"),
                TipoActivo.valueOf(rs.getString("tipo_activo")),
                rs.getString("nombre_activo"));
    }
}
