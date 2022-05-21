package es.serversurvival._shared.utils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;

public final class CollectionUtils {
    private CollectionUtils () {}

    public static<K, V extends Comparable<V>> HashMap<K, V> sortMapByValueDecre(Map<K, V> hm) {
        List<Map.Entry<K, V>> list = new LinkedList<>(hm.entrySet());

        list.sort((o1, o2) -> (o2.getValue().compareTo(o1.getValue())));

        HashMap<K, V> temp = new LinkedHashMap<>();
        for (Map.Entry<K, V> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }

        return temp;
    }

    public static <K, V> int getPoisitionOfKeyInMap(Map<K, V> map, Predicate<K> keyMatcher){
        int position = 0;

        for(var entry : map.entrySet()){
            position++;

            if(keyMatcher.test(entry.getKey()))
                return position;
        }

        return -1;
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

    public static<K, V extends Comparable<V>> HashMap<K, V> sortMapByValueCrec(Map<K, V> hm) {
        List<Map.Entry<K, V>> list = new LinkedList<>(hm.entrySet());
        list.sort(Map.Entry.comparingByValue());

        HashMap<K, V> temp = new LinkedHashMap<>();
        for (Map.Entry<K, V> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }

        return temp;
    }

    public static<K, V> Map<K, List<V>> mergeMapList (List<V> toIteratem, Function<V, K> keyMapper) {
        Map<K, List<V>> acumulator = new HashMap<>();

        for(V element : toIteratem){
            K mappedKey = keyMapper.apply(element);

            if(acumulator.get(mappedKey) == null){
                acumulator.put(mappedKey, listOf(element));
            }else{
                List<V> listOfValues = acumulator.get(mappedKey);
                listOfValues.add(element);

                acumulator.put(mappedKey, listOfValues);
            }
        }

        return acumulator;
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

    public static <T> Predicate<T> distinctBy(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();

        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    @SafeVarargs
    public static<E> List<E> listOf (E... elements) {
        return new ArrayList<>(Arrays.asList(elements));
    }
}
