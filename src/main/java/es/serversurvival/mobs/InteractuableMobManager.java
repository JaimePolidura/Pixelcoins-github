package es.serversurvival.mobs;

import es.serversurvival.mobs.mobs.*;
import es.serversurvival.mobs.mobs.withers.*;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

public final class InteractuableMobManager {
    private static Map<Location, InteractuableMob> mobs = new HashMap<>();

    private InteractuableMobManager() {}

    static {
        /** COMANDOS*/
        MobWarps mobWarps = new MobWarps();
        mobs.put(mobWarps.getLocation(), mobWarps);

        MobHome mobHome = new MobHome();
        mobs.put(mobHome.getLocation(), mobHome);

        /**  MENUS */
        MobAyuda mobAyuda = new MobAyuda();
        mobs.put(mobAyuda.getLocation(), mobAyuda);

        MobTienda mobTienda = new MobTienda();
        mobs.put(mobTienda.getLocation(), mobTienda);

        MobPerfil mobPerfil = new MobPerfil();
        mobs.put(mobPerfil.getLocation(), mobPerfil);

        MobBolsa mobBolsa = new MobBolsa();
        mobs.put(mobBolsa.getLocation(), mobBolsa);

        MobEmpresas mobEmpresas = new MobEmpresas();
        mobs.put(mobEmpresas.getLocation(), mobEmpresas);

        MobTopJugadores mobTopJugadores = new MobTopJugadores();
        mobs.put(mobTopJugadores.getLocation(), mobTopJugadores);

        /** WITHERS */
        DiamantesPixelcoinsMob diamantesPixelcoinsMob = new DiamantesPixelcoinsMob();
        mobs.put(diamantesPixelcoinsMob.getLocation(), diamantesPixelcoinsMob);

        PixelcoinsItemMob pixelcoinsDiamantesMob = new PixelcoinsItemMob();
        mobs.put(pixelcoinsDiamantesMob.getLocation(), pixelcoinsDiamantesMob);

        PixelcoinsMaxItemsMob pixelcoinsMaxItemsMob = new PixelcoinsMaxItemsMob();
        mobs.put(pixelcoinsMaxItemsMob.getLocation(), pixelcoinsMaxItemsMob);

        CuarzoPixelcoinsMob cuarzoPixelcooinsMob = new CuarzoPixelcoinsMob();
        mobs.put(cuarzoPixelcooinsMob.getLocation(), cuarzoPixelcooinsMob);

        LapisPixelcoinsMob lapisPixelcoinsMob = new LapisPixelcoinsMob();
        mobs.put(lapisPixelcoinsMob.getLocation(), lapisPixelcoinsMob);
    }

    public static InteractuableMob getInterectuableMob (Location location) {
        return mobs.get(location);
    }
}