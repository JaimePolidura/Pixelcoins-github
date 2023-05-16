package es.serversurvival.v1._shared.utils;

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
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import org.json.simple.parser.JSONParser;

public final class Funciones {
    public static final UUID NULL_ID = new UUID(0L, 0L);
    public static final DecimalFormat FORMATEA = new DecimalFormat("###,###.##");
    public static final SimpleDateFormat DATE_FORMATER_LEGACY = new SimpleDateFormat("yyyy-MM-dd");
    public static final DateTimeFormatter DATE_FORMATER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final ExecutorService POOL = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    public static final ObjectMapper MAPPER = new ObjectMapper();

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

    public static long toMillis(LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);

        return zonedDateTime.toInstant().toEpochMilli();
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

    public static double diferenciaPorcntual(double cInicial, double cFinal){
        return (cFinal / cInicial) * 100 - 100;
    }

    public static double redondeoDecimales(double numero, int numeroDecimales) {
        BigDecimal redondeado = new BigDecimal(numero).setScale(numeroDecimales, RoundingMode.HALF_EVEN);
        return redondeado.doubleValue();
    }

    public synchronized static Object peticionHttp(String link) throws Exception {
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

    public static boolean esDeTipo (ItemStack item, Material... tipos) {
        return Arrays.stream(tipos)
                .anyMatch(mat -> mat == item.getType());
    }

    public static boolean esDeTipoItem(ItemStack item, String...tipos) {
        return cuincideNombre(item.getType().toString(), tipos);
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

    public static String hoy(){
        return DATE_FORMATER_LEGACY.format(new Date());
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

    public static boolean cuincideNombre (String nombre, String... items){
        List<String> bannedNamesList = Arrays.asList(items);

        return bannedNamesList.stream()
                .anyMatch( (name) -> name.equalsIgnoreCase(nombre));
    }
}
