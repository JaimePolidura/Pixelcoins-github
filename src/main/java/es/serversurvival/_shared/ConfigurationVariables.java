package es.serversurvival._shared;

public final class ConfigurationVariables {
    public static final String  WEB_ACTIONS_SERVER_IP = "localhost";
    public static final int     WEB_ACTIONS_SERVER_PORT= 8080;
    public static final String  DB_NAME = "pixelcoins_test";
    public static final String  DB_USER = "root";
    public static final String  DB_PASSWORD = "";
    public static final String  DB_HOST = "localhost";
    public static final int     DB_PORT = 3306;

    public static final String FINHUB_API_KEY = "bqaod1nrh5r8t7qnb7cg";
    public static final int BOLSA_PRECIOS_CACHE_N_VECES_LECTURA_SIN_ACTUALIZAR = 2;
    public static final long BOLSA_PRECIOS_CACHE_TIEMPO_MS_VALIDOS = 5 * 60 * 1000; //5 minutos
}
