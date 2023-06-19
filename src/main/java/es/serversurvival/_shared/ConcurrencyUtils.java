package es.serversurvival._shared;

import es.jaime.javaddd.application.utils.Utils;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Lock;

public final class ConcurrencyUtils {
    public static void atomicAcquire(Lock... locks) {
        Queue<Lock> acquired = new LinkedList<>();

        while (acquired.size() != locks.length) {
            Lock nextLock = locks[acquired.size()];

            if(nextLock.tryLock()){
                acquired.add(nextLock);
            }else{
                releaseAcquired(acquired);

                Utils.sleep((long) (Math.random() * 2));
            }
        }
    }

    private static void releaseAcquired(Queue<Lock> acquired) {
        while (!acquired.isEmpty()) {
            acquired.poll().unlock();
        }
    }

    public static void releaseAll(Lock... locks) {
        for (int i = 0; i < locks.length; i++) {
            locks[i].unlock();
        }
    }
}
