package es.serversurvival.objetos.apiHttp;

public class borrame {
    public static void main(String[] args) {
        try {
            double precio = IEXCloud_API.getOnlyPrice("cacxdxda");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
