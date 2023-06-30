package es.serversurvival._shared.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import org.json.simple.parser.JSONParser;

public final class Funciones {
    public static final UUID NULL_ID = new UUID(0L, 0L);
    public static final ObjectMapper MAPPER = new ObjectMapper();
    public static final LocalDateTime NULL_LOCALDATETIME = LocalDateTime.of(0, 1, 1, 0, 0, 0);

    private static final DecimalFormat FORMATEA = new DecimalFormat("###,###.##");

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

    public static String formatPixelcoins(double pixelcoins) {
        return pixelcoins >= 0 ?
                ChatColor.GREEN + formatNumero(pixelcoins) + " PC " + ChatColor.GOLD :
                ChatColor.RED +   formatNumero(pixelcoins) + " PC " + ChatColor.GOLD;
    }

    public static String formatNumero(double numero) {
        return Funciones.FORMATEA.format(Funciones.redondeoDecimales(numero, 2));
    }

    public static String formatRentabilidad(double rentabilidad) {
        String rentabildiadString = formatPorcentaje(rentabilidad);

        return (rentabilidad >= 0) ?
                (ChatColor.GREEN + "+" + rentabildiadString + " % " + ChatColor.RESET) :
                (ChatColor.RED + rentabildiadString + " % " + ChatColor.RESET);
    }

    public static String formatPorcentaje(double porcentaje) {
        return Funciones.FORMATEA.format(Funciones.redondeoDecimales(porcentaje * 100, 1));
    }

    public static double rentabilidad(double ingresos, double beneficios) {
        return Math.round((beneficios / ingresos) * 100);
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

    public static String toString(LocalDateTime localDateTime) {
        return localDateTime.toString().split("T")[0];
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

    public static boolean cuincideNombre (String nombre, String... items){
        List<String> bannedNamesList = Arrays.asList(items);

        return bannedNamesList.stream()
                .anyMatch( (name) -> name.equalsIgnoreCase(nombre));
    }

    public static long diasToMillis(int dias) {
        return (long) dias * 24 * 60 * 60 * 1000;
    }

    public static int millisToDias(long millis) {
        return (int) (millis / (24 * 60 * 60 * 1000));
    }
}
