package es.serversurvival.main.Pixelcoins;

import java.util.*;
import java.util.concurrent.*;

import es.serversurvival.*;
import es.serversurvival.apiHttp.IEXCloud_API;

public class testMain {
    public static void main(String[] args) throws Exception {
        /* V1 */
        List<String> tickers = Arrays.asList("FB", "GOOG", "AMZN", "BABA", "SAN", "F", "NKLA", "TSLA", "AXP",
                "BRK.B", "GM", "GE", "MSFT", "AAPL", "KO", "MCD", "TSM", "TGT", "BRK.A", "GOOG", "AMZN", "FB", "BRK.B", "GM", "GE", "MSFT", "AAPL", "KO");

        double time1 = System.nanoTime();
        for(String ticker : tickers){
            System.out.println(IEXCloud_API.getOnlyPrice(ticker));
        }
        time1 = System.nanoTime() - time1;

        System.out.println("--------------");

        /* V2 */
        double time2 = System.nanoTime();
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<Future<Double>> futures = new ArrayList<>();
        for(String ticker : tickers){
            futures.add(executor.submit(() -> IEXCloud_API.getOnlyPrice(ticker)));
        }

        executor.shutdown();
        while (!executor.isTerminated());
        time2 = System.nanoTime() - time2;

        for (Future<Double> future : futures) {
            System.out.println(future.get());
        }

        System.out.println("No concurrente: " + time1);
        System.out.println("Concurrente: " + time2);
    }
}
