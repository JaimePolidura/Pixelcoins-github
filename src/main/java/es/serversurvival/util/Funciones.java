package es.serversurvival.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

import es.serversurvival.mySQL.*;
import es.serversurvival.mySQL.tablasObjetos.*;
import es.serversurvival.mySQL.enums.POSICION;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import org.json.simple.parser.JSONParser;

public final class Funciones {
    public static final DecimalFormat FORMATEA = new DecimalFormat("###,###.##");
    public static final SimpleDateFormat DATE_FORMATER = new SimpleDateFormat("yyyy-MM-dd");

    private Funciones () {}

    public static int getEspaciosOcupados(Inventory inventory) {
        int espaciosLibres = 36;
        ItemStack[] items = inventory.getContents();

        for(int i = 0; i < 36; i++){
            if(items[i] == null || esDeTipoItem(items[i], "AIR")) {
                espaciosLibres--;
            }
        }
        
        return espaciosLibres;
    }

    public static int[] slotsItem(int n, int slotsLibres) {
        int[] arr = new int[slotsLibres];

        for (int i = 0; i < slotsLibres; i++) {
            if (n - 64 > 0) {
                arr[i] = 64;
                n = n - 64;
            } else {
                arr[i] = n;
                break;
            }
        }
        return arr;
    }

    public static int aumentarPorcentaje(double num, double porcentaje) {
        return (int) (num + Math.round((num / 100) * porcentaje));
    }

    public static int reducirPorcentaje (double numero, double porcentaje) {
        return aumentarPorcentaje(numero, -porcentaje);
    }

    public static List<String> dividirDesc (String sentence, Integer k){
        int totalLines = (sentence.length() / k);
        if(sentence.length() % k != 0){
            totalLines++;
        }

        int beginIndex = 0;
        int endIndex = k;
        List<String> toReturn = new ArrayList<>();

        for(int i = 0; i < totalLines; i++){
            if((i + 1) == totalLines){
                endIndex = sentence.substring(beginIndex).length() + beginIndex;
            }
            toReturn.add(sentence.substring(beginIndex, endIndex));

            beginIndex+=k;
            endIndex+=k;
        }

        return toReturn;
    }

    public static long diferenciaDias(Date d1, Date d2) {
        long difMil = Math.abs(d1.getTime() - d2.getTime());
        long dif = TimeUnit.DAYS.convert(difMil, TimeUnit.MILLISECONDS);

        return dif;
    }

    public static double rentabilidad(double ingresos, double beneficios) {
        return Math.round((beneficios / ingresos) * 100);
    }

    public static double diferenciaPorcntual(double base, double aComparar){
        return (aComparar - base) / (base / 100);
    }

    public static<K, V extends Comparable<V>> HashMap<K, V> sortMapByValueDecre(Map<K, V> hm) {
        List<Map.Entry<K, V>> list = new LinkedList<>(hm.entrySet());

        list.sort((o1, o2) -> (o2.getValue().compareTo(o1.getValue())));

        HashMap<K, V> temp = new LinkedHashMap<>();
        for (Map.Entry<K, V> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }

        return temp;
    }

    public static<K, V extends Comparable<V>> HashMap<K, V> sortMapByValueCrec(Map<K, V> hm) {
        List<Map.Entry<K, V>> list = new LinkedList<>(hm.entrySet());
        list.sort(Comparator.comparing(Map.Entry::getValue));

        HashMap<K, V> temp = new LinkedHashMap<>();
        for (Map.Entry<K, V> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }

        return temp;
    }

    public static double redondeoDecimales(double numero, int numeroDecimales) {
        BigDecimal redondeado = new BigDecimal(numero).setScale(numeroDecimales, RoundingMode.HALF_EVEN);
        return redondeado.doubleValue();
    }

    public static Map<String, Double> crearMapaTopPatrimonioPlayers(boolean creciente){
        Jugadores jugadoresMySQL = Jugadores.INSTANCE;
        MySQL.conectar();

        List<Jugador> jugadores = jugadoresMySQL.getAllJugadores();
        HashMap<String, Double> toReturn = new HashMap<>();

        Deudas deudas = Deudas.INSTANCE;
        PosicionesAbiertas posicionesAbiertas = PosicionesAbiertas.INSTANCE;
        Empresas empresas = Empresas.INSTANCE;
        LlamadasApi llamadasApi = LlamadasApi.INSTANCE;

        Map<String, LlamadaApi> mapAllLlamadas = llamadasApi.getMapOfAllLlamadasApi();

        jugadores.forEach((jugador) -> {
            double activosTotales = 0;

            //Liquidez
            activosTotales = jugador.getPixelcoin();

            //Deuas a cobrar
            activosTotales += deudas.getDeudasAcredor(jugador.getNombre()).stream()
                    .mapToDouble(Deuda::getPixelcoins)
                    .sum();

            //Deudas a pagar
            activosTotales -= deudas.getDeudasDeudor(jugador.getNombre()).stream()
                    .mapToInt(Deuda::getPixelcoins)
                    .sum();
            
            //Empresas
            activosTotales += empresas.getEmpresasOwner(jugador.getNombre()).stream()
                    .mapToDouble(Empresa::getPixelcoins)
                    .sum();

            //Bolsa
            List<PosicionAbierta> posicionAbiertasJugador = posicionesAbiertas.getPosicionesAbiertasJugador(jugador.getNombre());

            activosTotales += posicionAbiertasJugador.stream()
                    .filter(PosicionAbierta::esLargo)
                    .mapToDouble(pos -> (mapAllLlamadas.get(pos.getNombre()).getPrecio() * pos.getCantidad()))
                    .sum();

            activosTotales += posicionAbiertasJugador.stream()
                    .filter(PosicionAbierta::esLargo)
                    .mapToDouble(pos -> (pos.getPrecioApertura() - mapAllLlamadas.get(pos.getNombre()).getPrecio()) * pos.getCantidad())
                    .sum();

            toReturn.put(jugador.getNombre(), activosTotales);
        });

        if(creciente)
            return Funciones.sortMapByValueCrec(toReturn);
        else
            return Funciones.sortMapByValueDecre(toReturn);
    }

    public static double getPatrimonioJugador(String nombreJugador){
        Jugadores jugadoresMySQL = Jugadores.INSTANCE;
        Deudas deudas = Deudas.INSTANCE;
        PosicionesAbiertas posicionesAbiertas = PosicionesAbiertas.INSTANCE;
        Empresas empresas = Empresas.INSTANCE;
        LlamadasApi llamadasApi = LlamadasApi.INSTANCE;

        Map<String, LlamadaApi> mapAllLlamadas = llamadasApi.getMapOfAllLlamadasApi();
        Jugador jugador = jugadoresMySQL.getJugador(nombreJugador);

        double patrimonio = 0;

        //Liquidez
        patrimonio = jugador.getPixelcoin();

        //Deuas a cobrar
        patrimonio += deudas.getDeudasAcredor(jugador.getNombre()).stream()
                .mapToDouble(Deuda::getPixelcoins)
                .sum();

        //Deudas a pagar
        patrimonio -= deudas.getDeudasDeudor(jugador.getNombre()).stream()
                .mapToInt(Deuda::getPixelcoins)
                .sum();

        //Empresas
        patrimonio += empresas.getEmpresasOwner(jugador.getNombre()).stream()
                .mapToDouble(Empresa::getPixelcoins)
                .sum();

        //Bolsa
        List<PosicionAbierta> posicionAbiertasJugador = posicionesAbiertas.getPosicionesAbiertasJugador(jugador.getNombre());

        patrimonio += posicionAbiertasJugador.stream()
                .filter(PosicionAbierta::esLargo)
                .mapToDouble(pos -> (mapAllLlamadas.get(pos.getNombre()).getPrecio() * pos.getCantidad()))
                .sum();

        patrimonio += posicionAbiertasJugador.stream()
                .filter(PosicionAbierta::esLargo)
                .mapToDouble(pos -> (pos.getPrecioApertura() - mapAllLlamadas.get(pos.getNombre()).getPrecio()) * pos.getCantidad())
                .sum();


        return patrimonio;
    }

    public static boolean esDouble(String supuestoDouble) {
        try{
            Double.parseDouble(supuestoDouble);
            return true;
        }catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean esInteger (String supuestoInteger) {
        try{
            Integer.parseInt(supuestoInteger);
            return true;
        }catch (NumberFormatException e) {
            return false;
        }
    }

    public static Object peticionHttp(String link) throws Exception {
        URL url = new URL(link);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        BufferedReader bfr = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        JSONParser parser = new JSONParser();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();

        String responseLine;
        while ((responseLine = bufferedReader.readLine()) != null) {
            response.append(responseLine.trim());
        }

        return parser.parse(response.toString());
    }

    public static boolean cuincideNombre (String nombre, String... items){
        List<String> bannedNamesList = Arrays.asList(items);

        return bannedNamesList.stream()
                .anyMatch( (name) -> name.equalsIgnoreCase(nombre));
    }

    public static boolean noCuincideNombre (String nombre, String... items){
        return !cuincideNombre(nombre, items);
    }

    public static boolean esDeTipoItem(ItemStack item, String...tipos) {
        return cuincideNombre(item.getType().toString(), tipos);
    }

    public static boolean noEsDeTipoItem(ItemStack item, String...tipos) {
        return !cuincideNombre(item.getType().toString(), tipos);
    }

    public static<E> int getSumaTotalListInteger (List<E> list, ToIntFunction<E> whatToSum) {
        return list.stream()
                .mapToInt(whatToSum)
                .sum();
    }

    public static<E> double getSumaTotalListDouble (List<E> list, ToDoubleFunction<E> whatToSum) {
        return list.stream()
                .mapToDouble(whatToSum)
                .sum();
    }

    public static int generateRandomNumber (int from, int to) {
        return (int)(Math.random() * (to - from + 1) + from);
    }

    public static String quitarCaracteres (String palabra, char... caracteres) {
        StringBuilder stringBuilder = new StringBuilder();

        for(int i = 0; i < palabra.length(); i++) {
            boolean cuincide = false;

            for (int j = 0; j < caracteres.length; j++) {
                if (palabra.charAt(i) == caracteres[j]) {
                    cuincide = true;
                    break;
                }
            }

            if(!cuincide){
                stringBuilder.append(palabra.charAt(i));
            }
        }
        return stringBuilder.toString();
    }

    public static String quitarPalabrasEntreEspacios (String palabra, String... palabrasAQuitar) {
        String[] palabraDivididaEspacios = palabra.split(" ");
        StringBuilder stringBuilder = new StringBuilder();

        for(int i = 0; i < palabraDivididaEspacios.length; i++){
            boolean haCuincidido = false;
            
            for(int j = 0; j < palabrasAQuitar.length; j++){

                if(palabraDivididaEspacios[i].equalsIgnoreCase(palabrasAQuitar[j])){
                    haCuincidido = true;
                    break;
                }
            }

            if(!haCuincidido){
                stringBuilder.append(palabraDivididaEspacios[i]).append(" ");
            }
        }

        return stringBuilder.toString();
    }

    public static <T> Predicate<T> distinctBy(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();

        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
