package es.serversurvival.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.util.*;
import java.util.concurrent.TimeUnit;

import es.serversurvival.objetos.mySQL.*;
import es.serversurvival.objetos.mySQL.tablasObjetos.Deuda;
import es.serversurvival.objetos.mySQL.tablasObjetos.Empresa;
import es.serversurvival.objetos.mySQL.tablasObjetos.Jugador;
import org.bukkit.Location;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public final class Funciones {

    private Funciones () {}

    public static int espaciosLibres(Inventory inventory) {
        int el = 36;
        for (ItemStack is : inventory.getContents()) {
            if (is == null || is.getType().toString().equalsIgnoreCase("AIR")) {
                continue;
            } else {
                el--;
            }
        }
        return el;
    }

    public static boolean comparar(Location loc1, Location loc2) {
        if (loc1.getBlockX() == loc2.getBlockX() && loc1.getBlockY() == loc2.getBlockY() && loc1.getBlockZ() == loc2.getBlockZ()) {
            return true;
        } else {
            return false;
        }
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

    public static int interes(int num, int interes) {
        double n = (double) num;
        double i = (double) interes;

        double r = Math.round((n / 100) * i);
        int resultado = num + (int) r;

        return resultado;
    }

    public static ArrayList<String> dividirDesc(ArrayList<String> lore, String descripcion, int k) {
        char[] descripcionChar = descripcion.toCharArray();
        String a = "";
        int pos = 0;
        int longitud = descripcionChar.length;
        int posRestantes = longitud;

        if (posRestantes >= k) {
            for (int i = 0; i < longitud; i++) {
                if (posRestantes >= k) {
                    for (int j = 0; j < k; j++) {
                        posRestantes--;
                        a = a + descripcionChar[pos];
                        pos++;
                    }
                    lore.add(ChatColor.GOLD + a);
                    a = "";
                } else {
                    for (int j = 0; j < posRestantes; j++) {
                        a = a + descripcionChar[pos];
                        pos++;
                    }
                    lore.add(ChatColor.GOLD + a);
                    a = "";
                    break;
                }
            }
        } else {
            for (int i = 0; i < longitud; i++) {
                a = a + descripcionChar[pos];
                pos++;
            }
            lore.add(ChatColor.GOLD + a);
        }
        return lore;
    }

    public static long diferenciaDias(Date d1, Date d2) {
        long difMil = Math.abs(d1.getTime() - d2.getTime());
        long dif = TimeUnit.DAYS.convert(difMil, TimeUnit.MILLISECONDS);

        return dif;
    }

    public static double rentabilidad(double ingresos, double beneficios) {
        return Math.round((beneficios / ingresos) * 100);
    }

    public static double diferenciaPorcntual(double a, double b) {
        return (b - a) / a * 100;
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

    public static Map<String, Double> crearMapaTopPlayers(boolean creciente){
        Jugadores j = new Jugadores();
        j.conectar();

        List<Jugador> jugadores = j.getAllJugadores();
        HashMap<String, Double> toReturn = new HashMap<>();

        Deudas deudas = new Deudas();
        PosicionesAbiertas posicionesAbiertas = new PosicionesAbiertas();
        Empresas empresas = new Empresas();
        LlamadasApi llamadasApi = new LlamadasApi();

        jugadores.forEach((jugador) -> {
            double activosTotales = 0;

            //Liquidez
            activosTotales = jugador.getPixelcoin();

            //Deuas a cobrar
            activosTotales += deudas.getDeudasAcredor(jugador.getNombre()).stream()
                    .mapToDouble(Deuda::getPixelcoins)
                    .sum();

            //Empresas
            activosTotales += empresas.getEmpresasOwner(jugador.getNombre()).stream()
                    .mapToDouble(Empresa::getPixelcoins)
                    .sum();

            activosTotales += posicionesAbiertas.getPosicionesAbiertasJugador(jugador.getNombre()).stream()
                    .mapToDouble(posicion -> llamadasApi.getLlamadaAPI(posicion.getNombre()).getPrecio() * posicion.getCantidad())
                    .sum();

            toReturn.put(jugador.getNombre(), activosTotales);
        });

        if(creciente)
            return Funciones.sortMapByValueCrec(toReturn);
        else
            return Funciones.sortMapByValueDecre(toReturn);
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

    public static Object httpResponseToObject (HttpURLConnection connection) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();

        String responseLine = null;
        while ((responseLine = bufferedReader.readLine()) != null) {
            response.append(responseLine.trim());
        }

        return parser.parse(response.toString());
    }
}