package es.serversurvival.objetos;

import java.sql.*;
import java.text.DecimalFormat;
import java.util.*;

import es.serversurvival.task.ScoreboardPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


import es.serversurvival.main.Funciones;
import net.md_5.bungee.api.ChatColor;

public class Empresas extends MySQL {
    public final static int nMaxEmpresas = 5;
    public DecimalFormat formatea = new DecimalFormat("###,###.##");

    private void nuevaEmpresa(String nombreEmpresa, String owner, int pixelcoins, int ingresos, int gastos, String icono, String descripcion) {
        try {
            String consulta = "INSERT INTO empresas (nombre, owner, pixelcoins, ingresos, gastos, icono, descripcion) VALUES ('" + nombreEmpresa + "','" + owner + "','" + pixelcoins + "','" + ingresos + "','" + gastos + "','" + icono + "','" + descripcion + "')";
            Statement st2 = (Statement) conexion.createStatement();
            st2.executeUpdate(consulta);
        } catch (SQLException e) {

        }
    }

    public void borrarEmpresa(String nombre) {
        try {
            String consulta = "DELETE FROM empresas WHERE nombre = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta);
            pst.setString(1, nombre);
            pst.executeUpdate();
        } catch (Exception e) {

        }
    }

    private int getNEmpresas(String owner) {
        int nempresas = 0;
        try {
            String consulta = "SELECT nombre FROM empresas WHERE owner = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta);
            pst.setString(1, owner);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                nempresas = nempresas + 1;
            }
            rs.close();
        } catch (SQLException e) {

        }
        return nempresas;
    }

    public String getOwner(String nombreEmpresa) {
        String owner = "";
        try {
            String consulta = "SELECT owner FROM empresas WHERE nombre = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta);
            pst.setString(1, nombreEmpresa);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                owner = rs.getString("owner");
                break;
            }
            rs.close();
        } catch (SQLException e) {

        }
        return owner;
    }

    public int getPixelcoins(String nombreEmpresa) {
        int pixelcoins = 0;
        try {
            String consulta = "SELECT pixelcoins FROM empresas WHERE nombre = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta);
            pst.setString(1, nombreEmpresa);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                pixelcoins = rs.getInt("pixelcoins");
                break;
            }
            rs.close();
        } catch (SQLException e) {

        }
        return pixelcoins;
    }

    public int getIngresos(String nombreEmpresa) {
        int ingresos = 0;
        try {
            String consulta = "SELECT ingresos FROM empresas WHERE nombre = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta);
            pst.setString(1, nombreEmpresa);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                ingresos = rs.getInt("ingresos");
                break;
            }
            rs.close();
        } catch (SQLException e) {

        }
        return ingresos;
    }

    public int getGastos(String nombreEmpresa) {
        int gastos = 0;
        try {
            String consulta = "SELECT gastos FROM empresas WHERE nombre = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta);
            pst.setString(1, nombreEmpresa);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                gastos = rs.getInt("gastos");
                break;
            }
            rs.close();
        } catch (SQLException e) {

        }
        return gastos;
    }

    public String getIcono(String nombreEmpresa) {
        String icono = "";
        try {
            String consulta = "SELECT icono FROM empresas WHERE nombre = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta);
            pst.setString(1, nombreEmpresa);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                icono = rs.getString("icono");
                break;
            }
            rs.close();
        } catch (SQLException e) {

        }
        return icono;
    }

    public String getDescripcion(String nombreEmpresa) {
        String descripcion = "";
        try {
            String consulta = "SELECT descripcion FROM empresas WHERE nombre = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta);
            pst.setString(1, nombreEmpresa);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                descripcion = rs.getString("descripcion");
                break;
            }
            rs.close();
        } catch (SQLException e) {

        }
        return descripcion;
    }

    public void setOwner(String nombreEmpresa, String nuevoOwner) {
        try {
            String consulta2 = "UPDATE empresas SET owner = ? WHERE nombre = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta2);

            pst.setString(1, nuevoOwner);
            pst.setString(2, nombreEmpresa);
            pst.executeUpdate();
        } catch (SQLException e) {

        }
    }

    public void setPixelcoins(String nombreEmpresa, int pixelcoins) {
        try {
            String consulta2 = "UPDATE empresas SET pixelcoins = ? WHERE nombre = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta2);

            pst.setInt(1, pixelcoins);
            pst.setString(2, nombreEmpresa);
            pst.executeUpdate();
        } catch (SQLException e) {

        }
    }

    public void setIngresos(String nombreEmpresa, int ingresos) {
        try {
            String consulta2 = "UPDATE empresas SET ingresos = ? WHERE nombre = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta2);

            pst.setInt(1, ingresos);
            pst.setString(2, nombreEmpresa);
            pst.executeUpdate();
        } catch (SQLException e) {

        }
    }

    public void setGastos(String nombreEmpresa, int gastos) {
        try {
            String consulta2 = "UPDATE empresas SET gastos = ? WHERE nombre = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta2);

            pst.setInt(1, gastos);
            pst.setString(2, nombreEmpresa);
            pst.executeUpdate();
        } catch (SQLException e) {

        }
    }

    public void setIcono(String nombreEmpresa, String icono) {
        try {
            String consulta2 = "UPDATE empresas SET icono = ? WHERE nombre = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta2);

            pst.setString(1, icono);
            pst.setString(2, nombreEmpresa);
            pst.executeUpdate();
        } catch (SQLException e) {

        }
    }

    public void setNombre(String nombreEmpresa, String nuevoNombre) {
        try {
            String consulta2 = "UPDATE empresas SET nombre = ? WHERE nombre = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta2);

            pst.setString(1, nuevoNombre);
            pst.setString(2, nombreEmpresa);
            pst.executeUpdate();
        } catch (SQLException e) {

        }
    }

    public void setDescripcion(String nombreEmpresa, String descripcion) {
        try {
            String consulta2 = "UPDATE empresas SET descripcion = ? WHERE nombre = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta2);

            pst.setString(1, descripcion);
            pst.setString(2, nombreEmpresa);
            pst.executeUpdate();
        } catch (SQLException e) {

        }
    }

    public boolean esOwner(String jugador, String nombreEmpresa) {
        boolean es = false;
        try {
            String consulta = "SELECT nombre FROM empresas WHERE nombre = ? AND owner = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta);
            pst.setString(1, nombreEmpresa);
            pst.setString(2, jugador);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                es = true;
            }
            rs.close();
        } catch (SQLException e) {

        }
        return es;
    }

    public ArrayList<String> getNombreEmpresasOwner(String owner) {
        ArrayList<String> empresas = new ArrayList<>();
        try {
            String consulta = "SELECT nombre FROM empresas WHERE owner = ?";
            PreparedStatement pst = conexion.prepareStatement(consulta);
            pst.setString(1, owner);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                empresas.add(rs.getString("nombre"));
            }
            rs.close();
        } catch (Exception e) {

        }
        return empresas;
    }

    public boolean estaRegistradoNombre(String nombre) {
        boolean reg = false;
        try {
            String consulta = "SELECT nombre FROM empresas WHERE nombre = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta);
            pst.setString(1, nombre);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                reg = true;
            }

            rs.close();
        } catch (SQLException e) {

        }
        return reg;
    }

    public void crearEmpresa(String nombreEmpresa, Player p, String descripcion) {
        String owner = p.getName();
        int nempresas = this.getNEmpresas(owner) + 1;
        boolean reg = this.estaRegistradoNombre(nombreEmpresa);
        if (nempresas <= nMaxEmpresas) {
            if (!reg) {
                this.nuevaEmpresa(nombreEmpresa, owner, 0, 0, 0, "DIAMOND_PICKAXE", descripcion);

                p.sendMessage(ChatColor.GOLD + "Has creado una empresa! " + ChatColor.GOLD + "comandos utiles: " + ChatColor.AQUA + "/depositar, /contratar, /logotipo, /empresas, /miempresa /editarnombre m?s: /ayuda empresas");
                p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
                p.getServer().broadcastMessage(ChatColor.GOLD + owner + " ha creado una nueva empresa: " + ChatColor.DARK_AQUA + nombreEmpresa);
            } else {
                p.sendMessage(ChatColor.DARK_RED + "El nombre de esa empresa ya esta registrada");
            }
        } else {
            p.sendMessage(ChatColor.DARK_RED + "Solo puedes tener como maximo " + nMaxEmpresas + " empresas a la vez, al menos por ahora xd");
        }
        ScoreboardPlayer sp = new ScoreboardPlayer();
        sp.updateScoreboard(p);
    }

    public void cambiarIcono(String nombreEmpresa, Player p, String icono) {
        boolean existe = this.estaRegistradoNombre(nombreEmpresa);

        if (!existe) {
            p.sendMessage(ChatColor.DARK_RED + "Esa empresa no existe");
            return;
        }
        boolean esowner = this.esOwner(p.getName(), nombreEmpresa);

        if (!esowner) {
            p.sendMessage(ChatColor.DARK_RED + "No eres el due?o de esa empresa");
            return;
        }

        this.setIcono(nombreEmpresa, icono);
        p.sendMessage(ChatColor.GOLD + "Has cambiado el logotipo a: " + icono);
        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
    }

    public void cambiarNombre(Player p, String empresa, String nuevoNombre) {
        if (!this.estaRegistradoNombre(empresa)) {
            p.sendMessage(ChatColor.DARK_RED + "Esa empresa no existe");
            return;
        }
        if (this.estaRegistradoNombre(nuevoNombre)) {
            p.sendMessage(ChatColor.DARK_RED + "Esa nombre ya esta cogido");
            return;
        }
        if (!this.esOwner(p.getName(), empresa)) {
            p.sendMessage(ChatColor.DARK_RED + "No eres el due?o de esa empresa");
            return;
        }
        ArrayList<String> empleados;
        Empleados em = new Empleados();

        empleados = em.getNombreEmpleadosEmpresa(empresa);
        em.cambiarEmpresaNombre(empresa, nuevoNombre);

        this.setNombre(empresa, nuevoNombre);
        p.sendMessage(ChatColor.GOLD + "Has cambiado de nombre a tu empresa!");
        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);

        Mensajes men = new Mensajes();
        for (int i = 0; i < empleados.size(); i++) {
            men.nuevoMensaje(empleados.get(i), "La empresa en la que trabajas: " + empresa + " ha cambiado a de nombre a " + nuevoNombre);
        }
        ScoreboardPlayer sp = new ScoreboardPlayer();
        sp.updateScoreboard(p);
    }

    public void mostrarEmpresas(Player p) {
        Inventory empresas = Bukkit.createInventory(null, 54, ChatColor.DARK_RED + "" + ChatColor.BOLD + "          Empresas");
        String nombreEmpresa;
        String owner;
        int pixelcoins;
        int ingresos;
        int gastos;
        int beneficios;
        double beneficiosD;
        double ingresosD;
        double margen;

        int nRecorridos = 0;
        ArrayList<String> empleados = null;
        String icono;
        String descripcion;

        ItemStack item = null;
        ItemMeta im = null;

        Empleados em = new Empleados();
        Funciones f = new Funciones();

        ArrayList<String> lore = new ArrayList<String>();
        try {
            String consulta = "SELECT * FROM empresas";
            Statement st = conexion.createStatement();
            ResultSet rs;
            rs = st.executeQuery(consulta);

            while (rs.next()) {
                im = null;

                nombreEmpresa = rs.getString("nombre");
                owner = rs.getString("owner");
                pixelcoins = rs.getInt("pixelcoins");
                ingresos = rs.getInt("ingresos");
                gastos = rs.getInt("gastos");
                beneficios = ingresos - gastos;
                icono = rs.getString("icono");
                descripcion = rs.getString("descripcion");

                lore.clear();

                beneficiosD = (double) beneficios;
                ingresosD = (double) ingresos;

                margen = f.rentabilidad(ingresosD, beneficiosD);

                item = new ItemStack(Material.getMaterial(icono), 1);
                im = item.getItemMeta();

                lore.add(ChatColor.GOLD + "Owner: " + ChatColor.GOLD + owner);

                lore = f.dividirDesc(lore, descripcion, 47);

                lore.add("     ");
                lore.add(ChatColor.GOLD + "Pixelcoins: " + ChatColor.GREEN + formatea.format(pixelcoins) + " PC");
                lore.add(ChatColor.GOLD + "Ingresos: " + ChatColor.GREEN + formatea.format(ingresos) + " PC");
                lore.add(ChatColor.GOLD + "Gastos: " + ChatColor.GREEN + formatea.format(gastos) + " PC");
                lore.add(ChatColor.GOLD + "Beneficios: " + ChatColor.GREEN + formatea.format(beneficios) + " PC");
                lore.add(ChatColor.GOLD + "Rentabilidad: " + ChatColor.GREEN + margen + "%");
                lore.add("      ");
                lore.add(ChatColor.GOLD + "Empleados:");

                try {
                    em.conectar();
                    empleados = em.getNombreEmpleadosEmpresa(nombreEmpresa);
                    em.desconectar();
                } catch (Exception e) {

                }

                for (int i = 0; i < empleados.size(); i++) {
                    if (empleados.get(0) == null) {
                        break;
                    } else {
                        nRecorridos++;
                        lore.add(ChatColor.GOLD + "-" + empleados.get(i));
                    }
                }

                if (nRecorridos == 0) {
                    lore.add("  ");
                    lore.add("Sin trabajadores");
                }

                im.setLore(lore);

                im.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + nombreEmpresa);

                item.setItemMeta(im);

                empresas.addItem(item);

            }
            p.openInventory(empresas);
        } catch (SQLException e) {
            p.sendMessage("wtf");
        }
    }

    public void verEmpresa(Player p, String empresa) {
        boolean reg = this.estaRegistradoNombre(empresa);
        if (!reg) {
            p.sendMessage(ChatColor.DARK_RED + "Esa empresa no existe");
            return;
        }
        boolean ow = this.esOwner(p.getName(), empresa);
        if (!ow) {
            p.sendMessage(ChatColor.DARK_RED + "No eres el dueño de la empresa");
            return;
        }
        Funciones f = new Funciones();

        String logitipo = this.getIcono(empresa);
        String descripcion = this.getDescripcion(empresa);
        int pixelcoins = this.getPixelcoins(empresa);
        double ingresos = this.getIngresos(empresa);
        double gastos = this.getGastos(empresa);
        double beneficios = ingresos - gastos;
        double rentabilidad = f.rentabilidad(ingresos, beneficios);
        ArrayList<String> empleados = new ArrayList<String>();

        p.sendMessage(ChatColor.GOLD + "        Estadisticas para " + ChatColor.DARK_AQUA + empresa);
        p.sendMessage(ChatColor.GOLD + "Pixelcoins en la empresa: " + ChatColor.GREEN + formatea.format(pixelcoins) + " PC");
        p.sendMessage(ChatColor.GOLD + "Ingresos: " + ChatColor.GREEN + formatea.format(ingresos) + " PC");
        p.sendMessage(ChatColor.GOLD + "gastos: " + ChatColor.GREEN + formatea.format(gastos) + " PC");
        if (beneficios < 0) {
            p.sendMessage(ChatColor.GOLD + "beneficios: " + ChatColor.RED + formatea.format(beneficios) + ChatColor.GREEN + " PC");
        } else {
            p.sendMessage(ChatColor.GOLD + "beneficios: " + ChatColor.GREEN + formatea.format(beneficios) + " PC");
        }
        p.sendMessage(ChatColor.GOLD + "logotipo: " + logitipo);
        p.sendMessage(ChatColor.GOLD + "descripcion: " + descripcion);

        if (rentabilidad < 0) {
            p.sendMessage(ChatColor.GOLD + "rentabilidad: " + ChatColor.RED + rentabilidad + "%");
        } else {
            p.sendMessage(ChatColor.GOLD + "rentabilidad: " + rentabilidad + "%");
        }

        p.sendMessage(ChatColor.GOLD + "Empleados:");
        try {
            Empleados em = new Empleados();
            em.conectar();
            empleados = em.getNombreEmpleadosEmpresa(empresa);
            String nombre = "";
            String cargo = "";
            String tipoSueldo = "";
            String nombreTipoSueldo = "";
            int sueldo = 0;
            int id_empleado = 0;
            boolean enc = false;

            for (int i = 0; i < empleados.size(); i++) {
                enc = true;
                id_empleado = em.getId(empleados.get(i), empresa);

                nombre = em.getEmpleado(id_empleado);
                cargo = em.getCargo(id_empleado);
                tipoSueldo = em.getTipo(id_empleado);
                sueldo = em.getSueldo(id_empleado);

                switch (tipoSueldo) {
                    case "d":
                        nombreTipoSueldo = "dia";
                        break;
                    case "s":
                        nombreTipoSueldo = "semana";
                        break;
                    case "2s":
                        nombreTipoSueldo = "2 semanas";
                        break;
                    case "m":
                        nombreTipoSueldo = "mes";
                        break;
                }

                p.sendMessage(ChatColor.GOLD + " -" + nombre + " cargo: " + cargo + " sueldo: " + ChatColor.GREEN + sueldo + " PC/ " + nombreTipoSueldo);
            }

            if (!enc) {
                p.sendMessage(ChatColor.GOLD + "       Sin trabajadores");
            }
            em.desconectar();
        } catch (Exception e) {

        }
    }

    public void cambiarDescripciom(String empresa, String nuevaDescripcion, Player p) {
        boolean reg = this.estaRegistradoNombre(empresa);
        if (!reg) {
            p.sendMessage(ChatColor.DARK_RED + "Esa empresa no existe");
            return;
        }
        boolean ow = this.esOwner(p.getName(), empresa);
        if (!ow) {
            p.sendMessage(ChatColor.DARK_RED + "No eres el dueño de esa empresa");
            return;
        }

        this.setDescripcion(empresa, nuevaDescripcion);

        p.sendMessage(ChatColor.GOLD + "Has cambiado la descripccion de tu empresa: " + ChatColor.DARK_AQUA + empresa + ChatColor.GOLD + " a ver en " + ChatColor.AQUA + "/empresas");
    }
}