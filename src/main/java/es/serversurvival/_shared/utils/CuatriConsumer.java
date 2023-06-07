package es.serversurvival._shared.utils;

@FunctionalInterface
public interface CuatriConsumer<A, B, C, D> {
    void consume(A a, B b, C c, D d);
}
