package es.serversurvival.main;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.TimeUnit;

import es.serversurvival.objetos.mySQL.*;
import es.serversurvival.objetos.mySQL.tablasObjetos.Deuda;
import es.serversurvival.objetos.mySQL.tablasObjetos.Empresa;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;


public final class Funciones {

    //Conseguir los espacios libres de un jugador
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

    //Comparar dos locations
    public static boolean comparar(Location loc1, Location loc2) {
        if (loc1.getBlockX() == loc2.getBlockX() && loc1.getBlockY() == loc2.getBlockY() && loc1.getBlockZ() == loc2.getBlockZ()) {
            return true;
        } else {
            return false;
        }
    }

    //Delvolver con array todos los slots libres que ocupara un numero de bloques
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

    //Aumentar cantidad en un %
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

    public static HashMap<String, Double> sortByValueCre(HashMap<String, Double> hm) {
        // Create a list from elements of HashMap
        List<Map.Entry<String, Double>> list = new LinkedList<Map.Entry<String, Double>>(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        // put data from sorted list to hashmap
        HashMap<String, Double> temp = new LinkedHashMap<String, Double>();
        for (Map.Entry<String, Double> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    public static HashMap<String, Double> sortByValueDecre(HashMap<String, Double> hm) {
        // Create a list from elements of HashMap
        List<Map.Entry<String, Double>> list = new LinkedList<Map.Entry<String, Double>>(hm.entrySet());
        Collections.reverseOrder();

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        // put data from sorted list to hashmap
        HashMap<String, Double> temp = new LinkedHashMap<String, Double>();
        for (Map.Entry<String, Double> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    public static HashMap<Integer, Double> sortByValueDecreIntDou(HashMap<Integer, Double> hm) {
        // Create a list from elements of HashMap
        List<Map.Entry<Integer, Double>> list = new LinkedList<Map.Entry<Integer, Double>>(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<Integer, Double>>() {
            public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        // put data from sorted list to hashmap
        HashMap<Integer, Double> temp = new LinkedHashMap<Integer, Double>();
        for (Map.Entry<Integer, Double> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    public static HashMap<String, Integer> sortByValueDecreInt(HashMap<String, Integer> hm) {
        // Create a list from elements of HashMap
        List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        // put data from sorted list to hashmap
        HashMap<String, Integer> temp = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    public static HashMap<String, Integer> sortByValueCreInt(HashMap<String, Integer> hm) {
        // Create a list from elements of HashMap
        List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        // put data from sorted list to hashmap
        HashMap<String, Integer> temp = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> aa : list) {
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

        HashMap<String, Double> jugadores = (HashMap<String, Double>) j.getAllJugadoresDinero();
        HashMap<String, Double> toReturn = new HashMap<>();

        Deudas deudas = new Deudas();
        PosicionesAbiertas posicionesAbiertas = new PosicionesAbiertas();
        Empresas empresas = new Empresas();
        LlamadasApi llamadasApi = new LlamadasApi();

        jugadores.forEach((nombre, dinero) -> {
            double activosTotales = 0;

            //Liquidez
            activosTotales = j.getDinero(nombre);

            //Deuas a cobrar
            activosTotales += deudas.getDeudasAcredor(nombre).stream()
                    .mapToDouble(Deuda::getPixelcoins)
                    .sum();

            //Empresas
            activosTotales += empresas.getEmpresasOwner(nombre).stream()
                    .mapToDouble(Empresa::getPixelcoins)
                    .sum();

            //Inversiones
            activosTotales += posicionesAbiertas.getPosicionesJugador(nombre).stream()
                    .mapToDouble(posicion -> llamadasApi.getPrecio(posicion.getNombre()) * posicion.getCantidad())
                    .sum();

            toReturn.put(nombre, activosTotales);
        });

        if(creciente)
            return Funciones.sortByValueCre(toReturn);
        else
            return Funciones.sortByValueDecre(toReturn);
    }
}