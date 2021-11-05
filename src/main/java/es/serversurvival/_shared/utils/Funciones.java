package es.serversurvival._shared.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;

import es.serversurvival.bolsa._shared.llamadasapi.mysql.LlamadaApi;
import es.serversurvival.bolsa._shared.llamadasapi.mysql.LlamadasApi;
import es.serversurvival.bolsa._shared.posicionesabiertas.mysql.PosicionAbierta;
import es.serversurvival.bolsa._shared.posicionesabiertas.mysql.PosicionesAbiertas;
import es.serversurvival.deudas._shared.mysql.Deuda;
import es.serversurvival.deudas._shared.mysql.Deudas;
import es.serversurvival.empresas._shared.mysql.Empresa;
import es.serversurvival.empresas._shared.mysql.Empresas;
import es.serversurvival.jugadores._shared.mySQL.Jugador;
import es.serversurvival.jugadores._shared.mySQL.Jugadores;
import es.serversurvival.mensajes._shared.mysql.Mensajes;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import org.json.simple.parser.JSONParser;

public final class Funciones {
    public static final DecimalFormat FORMATEA = new DecimalFormat("###,###.##");
    public static final SimpleDateFormat DATE_FORMATER_LEGACY = new SimpleDateFormat("yyyy-MM-dd");
    public static final DateTimeFormatter DATE_FORMATER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final ExecutorService POOL = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

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

        return TimeUnit.DAYS.convert(difMil, TimeUnit.MILLISECONDS);
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
        list.sort(Map.Entry.comparingByValue());

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

    public static Map<String, Double> crearMapaTopPatrimonioPlayers (boolean creciente) {
        Deudas deudasMySQL = Deudas.INSTANCE;
        Jugadores jugadoresMySQL = Jugadores.INSTANCE;
        PosicionesAbiertas posicionesAbiertasMySQL = PosicionesAbiertas.INSTANCE;
        Empresas empresasMySQL = Empresas.INSTANCE;
        LlamadasApi llamadasApiMySQL = LlamadasApi.INSTANCE;

        List<Jugador> allJugadordes = jugadoresMySQL.getAllJugadores();
        Map<String, LlamadaApi> mapAllLlamadas = llamadasApiMySQL.getMapOfAllLlamadasApi();
        Map<String, List<Deuda>> mapDeudasAcredor = deudasMySQL.getAllDeudasAcredorMap();
        Map<String, List<Deuda>> mapDeudasDeudor = deudasMySQL.getAllDeudasDeudorMap();
        Map<String, List<Empresa>> mapEmpresasJugador = empresasMySQL.getAllEmpresasJugadorMap();
        Map<String, List<PosicionAbierta>> mapPosicionesLargo = posicionesAbiertasMySQL.getAllPosicionesAbiertasMap(PosicionAbierta::noEsTipoAccionServerYLargo);
        Map<String, List<PosicionAbierta>> mapPosicionesCorto = posicionesAbiertasMySQL.getAllPosicionesAbiertasMap(PosicionAbierta::esCorto);

        HashMap<String, Double> toReturn = new HashMap<>();

        allJugadordes.forEach((jugador) -> {
            double activosTotales = 0;

            //Liquidez
            activosTotales = jugador.getPixelcoins();

            // Deudas acredor
            if(mapDeudasAcredor.get(jugador.getNombre()) != null){
                activosTotales += mapDeudasAcredor.get(jugador.getNombre()).stream()
                        .mapToInt(Deuda::getPixelcoins_restantes)
                        .sum();
            }

            //Deudas deudor
            if(mapDeudasDeudor.get(jugador.getNombre()) != null){
                activosTotales -= mapDeudasDeudor.get(jugador.getNombre()).stream()
                        .mapToInt(Deuda::getPixelcoins_restantes)
                        .sum();
            }

            //Emrpesas
            if(mapEmpresasJugador.get(jugador.getNombre()) != null){
                activosTotales += mapEmpresasJugador.get(jugador.getNombre()).stream()
                        .mapToDouble(Empresa::getPixelcoins)
                        .sum();
            }

            ///Posiciones abiertas largas
            if(mapPosicionesLargo.get(jugador.getNombre()) != null){
                activosTotales += mapPosicionesLargo.get(jugador.getNombre()).stream()
                        .mapToDouble(pos -> (mapAllLlamadas.get(pos.getNombre_activo()).getPrecio() * pos.getCantidad()))
                        .sum();
            }

            //Posicioenes abiertas cortas
            if(mapPosicionesCorto.get(jugador.getNombre()) != null){
                activosTotales += mapPosicionesCorto.get(jugador.getNombre()).stream()
                        .mapToDouble(pos -> (pos.getPrecio_apertura() - mapAllLlamadas.get(pos.getNombre_activo()).getPrecio()) * pos.getCantidad())
                        .sum();
            }


            toReturn.put(jugador.getNombre(), activosTotales);
        });

        if(creciente)
            return Funciones.sortMapByValueCrec(toReturn);
        else
            return Funciones.sortMapByValueDecre(toReturn);
    }

    public static double getPatrimonioJugador(String nombreJugador){
        return crearMapaTopPatrimonioPlayers(false).get(nombreJugador);
    }

    public static Object peticionHttp(String link) throws Exception {
        URL url = new URL(link);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

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

    public static boolean esDeTipo (ItemStack item, Material... tipos) {
        return Arrays.stream(tipos)
                .anyMatch(mat -> mat == item.getType());
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
        return (int) (Math.random() * (to - from + 1) + from);
    }

    public static String quitarCaracteres (String palabra, char... caracteres) {
        StringBuilder stringBuilder = new StringBuilder();

        for(int i = 0; i < palabra.length(); i++) {
            boolean cuincide = false;

            for (int j = caracteres.length - 1; j >= 0; j--) {
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

    public static String buildStringFromArray (String[] array, int startIndex) {
        StringBuilder builder = new StringBuilder();

        for(int i = startIndex; i < array.length; i++){
            builder.append(array[i]);

            if((array.length - 1) != i) {
                builder.append(" ");
            }
        }

        return builder.toString();
    }

    @SafeVarargs
    public static<E> List<E> listOf (E... elements) {
        return new ArrayList<>(Arrays.asList(elements));
    }
    
    public static boolean esHoyDiaSemana (int... diaSemanas) {
        LocalDate localDate = LocalDate.now();

        return Arrays.stream(diaSemanas).anyMatch(dia -> localDate.getDayOfWeek().getValue() == dia);
    }

    public static boolean esHoyHora (int horaMin, int minMin, int horaMax, int minMax) {
        LocalDateTime ahora = LocalDateTime.now();

        return ahora.getHour() > horaMin && ahora.getHour() < horaMax || ahora.getHour() == horaMin &&
                ahora.getMinute() >= minMin || (ahora.getHour() == horaMax && ahora.getMinute() <= minMax);
    }

    public static boolean mercadoEstaAbierto() {
        return !Funciones.esHoyDiaSemana(7, 1) && Funciones.esHoyHora(15, 30, 22, 30);
    }

    public static boolean mercadoNoEstaAbierto() {
        return !mercadoEstaAbierto();
    }

    public static String buildStringFromArray (String[] array) {
        return buildStringFromArray(array, 0);
    }

    public static void enviarMensajeYSonidoSiOnline(String jugador, String mensaje, Sound sound) {
        Player player = Bukkit.getPlayer(jugador);
        if(player != null){
            player.sendMessage(mensaje);
            player.playSound(player.getLocation(), sound, 10, 1);
        }
    }

    public static void enviarMensajeYSonido (Player player, String mensaje, Sound sound) {
        player.sendMessage(mensaje);
        player.playSound(player.getLocation(), sound, 10, 1);
    }

    public static void enviarMensaje (String nombreJugador, String mensajeOnline, String mensajeOffline) {
        Player player = Bukkit.getPlayer(nombreJugador);
        if(player != null){
            player.sendMessage(mensajeOnline);
        }else{
            Mensajes.INSTANCE.nuevoMensaje("", nombreJugador, mensajeOffline);
        }
    }

    public static void enviarMensaje (String nombreJugador, String mensajeOnline, String mensajeOffline, Sound sound, int v1, int v2) {
        Player player = Bukkit.getPlayer(nombreJugador);
        if (player != null) {
            player.sendMessage(mensajeOnline);
            player.playSound(player.getLocation(), sound, v1, v2);
        } else {
            Mensajes.INSTANCE.nuevoMensaje("", nombreJugador, mensajeOffline);
        }
    }

    public static<K, V> Map<K, List<V>> mergeMapList (List<V> toIteratem, Function<V, K> keyMapper) {
        Map<K, List<V>> acumulator = new HashMap<>();
        
        for(V element : toIteratem){
            K mappedKey = keyMapper.apply(element);

            if(acumulator.get(element) == null){
                acumulator.put(mappedKey, listOf(element));
            }else{
                List<V> listOfValues = acumulator.get(mappedKey);
                listOfValues.add(element);

                acumulator.put(mappedKey, listOfValues);
            }
        }

        return acumulator;
    }
}
