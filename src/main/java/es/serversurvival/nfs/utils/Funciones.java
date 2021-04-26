package es.serversurvival.nfs.utils;

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

import es.serversurvival.nfs.jugadores.mySQL.Jugador;
import es.serversurvival.nfs.deudas.mysql.Deuda;
import es.serversurvival.nfs.deudas.mysql.Deudas;
import es.serversurvival.nfs.empresas.mysql.Empresa;
import es.serversurvival.nfs.empresas.mysql.Empresas;
import es.serversurvival.nfs.jugadores.mySQL.Jugadores;
import es.serversurvival.nfs.bolsa.llamadasapi.LlamadaApi;
import es.serversurvival.nfs.bolsa.llamadasapi.LlamadasApi;
import es.serversurvival.nfs.mensajes.mysql.Mensajes;
import es.serversurvival.nfs.bolsa.posicionesabiertas.mysql.PosicionAbierta;
import es.serversurvival.nfs.bolsa.posicionesabiertas.mysql.PosicionesAbiertas;
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

    public static List<java.lang.String> dividirDesc (java.lang.String sentence, Integer k){
        int totalLines = (sentence.length() / k);
        if(sentence.length() % k != 0){
            totalLines++;
        }

        int beginIndex = 0;
        int endIndex = k;
        List<java.lang.String> toReturn = new ArrayList<>();

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

    public static Map<java.lang.String, Double> crearMapaTopPatrimonioPlayers (boolean creciente) {
        Deudas deudasMySQL = Deudas.INSTANCE;
        Jugadores jugadoresMySQL = Jugadores.INSTANCE;
        PosicionesAbiertas posicionesAbiertasMySQL = PosicionesAbiertas.INSTANCE;
        Empresas empresasMySQL = Empresas.INSTANCE;
        LlamadasApi llamadasApiMySQL = LlamadasApi.INSTANCE;

        List<Jugador> allJugadordes = jugadoresMySQL.getAllJugadores();
        Map<java.lang.String, LlamadaApi> mapAllLlamadas = llamadasApiMySQL.getMapOfAllLlamadasApi();
        Map<java.lang.String, List<Deuda>> mapDeudasAcredor = deudasMySQL.getAllDeudasAcredorMap();
        Map<java.lang.String, List<Deuda>> mapDeudasDeudor = deudasMySQL.getAllDeudasDeudorMap();
        Map<java.lang.String, List<Empresa>> mapEmpresasJugador = empresasMySQL.getAllEmpresasJugadorMap();
        Map<java.lang.String, List<PosicionAbierta>> mapPosicionesLargo = posicionesAbiertasMySQL.getAllPosicionesAbiertasMap(PosicionAbierta::noEsTipoAccionServerYLargo);
        Map<java.lang.String, List<PosicionAbierta>> mapPosicionesCorto = posicionesAbiertasMySQL.getAllPosicionesAbiertasMap(PosicionAbierta::esCorto);

        HashMap<java.lang.String, Double> toReturn = new HashMap<>();

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

    public static double getPatrimonioJugador(java.lang.String nombreJugador){
        Jugadores jugadoresMySQL = Jugadores.INSTANCE;
        Deudas deudas = Deudas.INSTANCE;
        PosicionesAbiertas posicionesAbiertas = PosicionesAbiertas.INSTANCE;
        Empresas empresas = Empresas.INSTANCE;
        LlamadasApi llamadasApi = LlamadasApi.INSTANCE;

        Map<java.lang.String, LlamadaApi> mapAllLlamadas = llamadasApi.getMapOfAllLlamadasApi();

        Jugador jugador = jugadoresMySQL.getJugador(nombreJugador);

        double patrimonio = 0;

        //Liquidez
        patrimonio = jugador.getPixelcoins();

        //Deuas a cobrar
        patrimonio += deudas.getDeudasAcredor(jugador.getNombre()).stream()
                .mapToDouble(Deuda::getPixelcoins_restantes)
                .sum();

        //Deudas a pagar
        patrimonio -= deudas.getDeudasDeudor(jugador.getNombre()).stream()
                .mapToInt(Deuda::getPixelcoins_restantes)
                .sum();

        //Empresas
        patrimonio += empresas.getEmpresasOwner(jugador.getNombre()).stream()
                .mapToDouble(Empresa::getPixelcoins)
                .sum();

        //Bolsa
        List<PosicionAbierta> posicionAbiertasJugador = posicionesAbiertas.getPosicionesAbiertasJugadorCondicion(jugador.getNombre(), PosicionAbierta::noEsTipoAccionServer);

        patrimonio += posicionAbiertasJugador.stream()
                .filter(PosicionAbierta::esLargo)
                .mapToDouble(pos -> (mapAllLlamadas.get(pos.getNombre_activo()).getPrecio() * pos.getCantidad()))
                .sum();

        patrimonio += posicionAbiertasJugador.stream()
                .filter(PosicionAbierta::esCorto)
                .mapToDouble(pos -> (pos.getPrecio_apertura() - mapAllLlamadas.get(pos.getNombre_activo()).getPrecio()) * pos.getCantidad())
                .sum();


        return patrimonio;
    }

    public static Object peticionHttp(java.lang.String link) throws Exception {
        URL url = new URL(link);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        JSONParser parser = new JSONParser();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();

        java.lang.String responseLine;
        while ((responseLine = bufferedReader.readLine()) != null) {
            response.append(responseLine.trim());
        }

        return parser.parse(response.toString());
    }

    public static boolean cuincideNombre (java.lang.String nombre, java.lang.String... items){
        List<java.lang.String> bannedNamesList = Arrays.asList(items);

        return bannedNamesList.stream()
                .anyMatch( (name) -> name.equalsIgnoreCase(nombre));
    }

    public static boolean noCuincideNombre (java.lang.String nombre, java.lang.String... items){
        return !cuincideNombre(nombre, items);
    }

    public static boolean esDeTipo (ItemStack item, Material... tipos) {
        return Arrays.stream(tipos)
                .anyMatch(mat -> mat == item.getType());
    }

    public static boolean esDeTipoItem(ItemStack item, java.lang.String...tipos) {
        return cuincideNombre(item.getType().toString(), tipos);
    }

    public static boolean noEsDeTipoItem(ItemStack item, java.lang.String...tipos) {
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

    public static java.lang.String quitarCaracteres (java.lang.String palabra, char... caracteres) {
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

    public static java.lang.String quitarPalabrasEntreEspacios (java.lang.String palabra, java.lang.String... palabrasAQuitar) {
        java.lang.String[] palabraDivididaEspacios = palabra.split(" ");
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

    public static java.lang.String buildStringFromArray (java.lang.String[] array, int startIndex) {
        StringBuilder builder = new StringBuilder();

        for(int i = startIndex; i < array.length; i++){
            builder.append(array[i]);

            if((array.length - 1) != i) {
                builder.append(" ");
            }
        }

        return builder.toString();
    }

    public static<E> List<E> listOf (E... elements) {
        List<E> list = new ArrayList<>(Arrays.asList(elements));

        return list;
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
        //return !Funciones.esHoyDiaSemana(7, 1) && Funciones.esHoyHora(15, 30, 22, 30);
        return true;
    }

    public static boolean mercadoNoEstaAbierto() {
        return !mercadoEstaAbierto();
    }

    public static java.lang.String buildStringFromArray (java.lang.String[] array) {
        return buildStringFromArray(array, 0);
    }

    public static void enviarMensajeYSonidoSiOnline(java.lang.String jugador, java.lang.String mensaje, Sound sound) {
        Player player = Bukkit.getPlayer(jugador);
        if(player != null){
            player.sendMessage(mensaje);
            player.playSound(player.getLocation(), sound, 10, 1);
        }
    }

    public static void enviarMensajeYSonido (Player player, java.lang.String mensaje, Sound sound) {
        player.sendMessage(mensaje);
        player.playSound(player.getLocation(), sound, 10, 1);
    }

    public static void enviarMensaje (java.lang.String nombreJugador, java.lang.String mensajeOnline, java.lang.String mensajeOffline) {
        Player player = Bukkit.getPlayer(nombreJugador);
        if(player != null){
            player.sendMessage(mensajeOnline);
        }else{
            Mensajes.INSTANCE.nuevoMensaje("", nombreJugador, mensajeOffline);
        }
    }

    public static void enviarMensaje (java.lang.String nombreJugador, java.lang.String mensajeOnline, java.lang.String mensajeOffline, Sound sound, int v1, int v2) {
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
