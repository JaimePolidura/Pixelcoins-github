package es.serversurvival.v1._shared;

@FunctionalInterface
public interface CuatriConsumer<A, B, C, D> {
    void consume(A a, B b, C c, D d);
}
