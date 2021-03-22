package es.serversurvival.objetos.mySQL;

import java.sql.*;
import java.text.DecimalFormat;
import java.util.*;

import es.serversurvival.objetos.mySQL.tablasObjetos.Empleado;
import es.serversurvival.objetos.mySQL.tablasObjetos.Empresa;
import es.serversurvival.objetos.task.ScoreboardTaskManager;
import org.bukkit.Sound;
import org.bukkit.entity.Player;


import es.serversurvival.main.Funciones;
import net.md_5.bungee.api.ChatColor;

public class Empresas extends MySQL {
    public static final int CrearEmpresaNombreLonMax = 16;
    public static final int nMaxEmpresas = 5;
    public static final int CrearEmpresaDescLonMax = 200;
    public DecimalFormat formatea = new DecimalFormat("###,###.##");

    public Empresa nuevaEmpresa(String nombreEmpresa, String owner, double pixelcoins, double ingresos, double gastos, String icono, String descripcion) {
        try {
            String consulta = "INSERT INTO empresas (nombre, owner, pixelcoins, ingresos, gastos, icono, descripcion) VALUES ('" + nombreEmpresa + "','" + owner + "','" + pixelcoins + "','" + ingresos + "','" + gastos + "','" + icono + "','" + descripcion + "')";
            Statement st2 = (Statement) conexion.createStatement();
            st2.executeUpdate(consulta);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return getEmpresa(getMaxId());
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

    private int getMaxId(){
        try{
            String consulta = "SELECT id_empresa FROM empresas ORDER BY id_empresa DESC LIMIT 1";
            ResultSet rs = conexion.prepareStatement(consulta).executeQuery();

            while (rs.next()){
                return rs.getInt("id_empresa");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        return -1;
    }

    private int getNEmpresas(String owner) {
        int nempresas = 0;
        try {
            String consulta = "SELECT nombre FROM empresas WHERE owner = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta);
            pst.setString(1, owner);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                nempresas++;
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

    public double getPixelcoins(String nombreEmpresa) {
        double pixelcoins = 0;
        try {
            String consulta = "SELECT pixelcoins FROM empresas WHERE nombre = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta);
            pst.setString(1, nombreEmpresa);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                pixelcoins = rs.getDouble("pixelcoins");
                break;
            }
            rs.close();
        } catch (SQLException e) {

        }
        return pixelcoins;
    }

    public double getIngresos(String nombreEmpresa) {
        double ingresos = 0;
        try {
            String consulta = "SELECT ingresos FROM empresas WHERE nombre = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta);
            pst.setString(1, nombreEmpresa);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                ingresos = rs.getDouble("ingresos");
                break;
            }
            rs.close();
        } catch (SQLException e) {

        }
        return ingresos;
    }

    public double getGastos(String nombreEmpresa) {
        double gastos = 0;
        try {
            String consulta = "SELECT gastos FROM empresas WHERE nombre = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta);
            pst.setString(1, nombreEmpresa);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                gastos = rs.getDouble("gastos");
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

    public String getNombre(int id){
        String nombre = null;
        try {
            String consulta = "SELECT nombre FROM empresas WHERE id_empresa = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                nombre = rs.getString("nombre");
                break;
            }
            rs.close();
        } catch (SQLException e) {

        }
        return nombre;
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

    public void setPixelcoins(String nombreEmpresa, double pixelcoins) {
        try {
            String consulta2 = "UPDATE empresas SET pixelcoins = ? WHERE nombre = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta2);

            pst.setDouble(1, pixelcoins);
            pst.setString(2, nombreEmpresa);
            pst.executeUpdate();
        } catch (SQLException e) {

        }
    }

    public void setIngresos(String nombreEmpresa, double ingresos) {
        try {
            String consulta2 = "UPDATE empresas SET ingresos = ? WHERE nombre = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta2);

            pst.setDouble(1, ingresos);
            pst.setString(2, nombreEmpresa);
            pst.executeUpdate();
        } catch (SQLException e) {

        }
    }

    public void setGastos(String nombreEmpresa, double gastos) {
        try {
            String consulta2 = "UPDATE empresas SET gastos = ? WHERE nombre = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta2);

            pst.setDouble(1, gastos);
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

    public List<Empresa> getEmpresasOwner(String owner) {
        List<Empresa> empresas = new ArrayList<>();
        try {
            String consulta = "SELECT * FROM empresas WHERE owner = ?";
            PreparedStatement pst = conexion.prepareStatement(consulta);
            pst.setString(1, owner);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                empresas.add(new Empresa(
                        rs.getInt("id_empresa"),
                        rs.getString("nombre"),
                        rs.getString("owner"),
                        rs.getDouble("pixelcoins"),
                        rs.getDouble("ingresos"),
                        rs.getDouble("gastos"),
                        rs.getString("icono"),
                        rs.getString("descripcion")
                ));
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
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

                p.sendMessage(ChatColor.GOLD + "Has creado una empresa! " + ChatColor.GOLD + "comandos utiles: " + ChatColor.AQUA + "/empresas depositar, /empresas contratar, /empresas logotipo, /empresas vertodas, /empresas miempresa /empresas editarnombre m?s: /ayuda empresario o /empresas ayuda");
                p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
                p.getServer().broadcastMessage(ChatColor.GOLD + owner + " ha creado una nueva empresa: " + ChatColor.DARK_AQUA + nombreEmpresa);
            } else {
                p.sendMessage(ChatColor.DARK_RED + "El nombre de esa empresa ya esta registrada");
            }
        } else {
            p.sendMessage(ChatColor.DARK_RED + "Solo puedes tener como maximo " + nMaxEmpresas + " empresas a la vez, al menos por ahora xd");
        }
        ScoreboardTaskManager sp = new ScoreboardTaskManager();
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
            p.sendMessage(ChatColor.DARK_RED + "No eres el dueño de esa empresa");
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
        Empleados em = new Empleados();

        List<Empleado> empleados = em.getEmpleadosEmrpesa(empresa);
        em.cambiarEmpresaNombre(empresa, nuevoNombre);

        this.setNombre(empresa, nuevoNombre);
        p.sendMessage(ChatColor.GOLD + "Has cambiado de nombre a tu empresa!");
        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);

        Mensajes men = new Mensajes();
        for (int i = 0; i < empleados.size(); i++) {
            men.nuevoMensaje(empleados.get(i).getEmpleado(), "La empresa en la que trabajas: " + empresa + " ha cambiado a de nombre a " + nuevoNombre);
        }
        ScoreboardTaskManager sp = new ScoreboardTaskManager();
        sp.updateScoreboard(p);
    }

    public List<Empresa> getTodasEmpresas() {
        List<Empresa> empresas = new ArrayList<>();
        try {
            String consulta = "SELECT * FROM empresas";
            ResultSet rs = conexion.createStatement().executeQuery(consulta);

            while (rs.next()) {
                empresas.add(new Empresa(
                        rs.getInt("id_empresa"),
                        rs.getString("nombre"),
                        rs.getString("owner"),
                        rs.getDouble("pixelcoins"),
                        rs.getDouble("ingresos"),
                        rs.getDouble("gastos"),
                        rs.getString("icono"),
                        rs.getString("descripcion")
                ));
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return empresas;
    }

    public Empresa getEmpresa(String empresa){
        Empresa toReturn = null;
        try{
            String consulta = "SELECT * FROM empresas WHERE nombre = ?";
            PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
            preparedStatement.setString(1, empresa);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()){
                toReturn = new Empresa(
                        rs.getInt("id_empresa"),
                        rs.getString("nombre"),
                        rs.getString("owner"),
                        rs.getDouble("pixelcoins"),
                        rs.getDouble("ingresos"),
                        rs.getDouble("gastos"),
                        rs.getString("icono"),
                        rs.getString("descripcion")
                );
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return toReturn;
    }

    public Empresa getEmpresa(int id_empresa){
        Empresa toReturn = null;
        try{
            String consulta = "SELECT * FROM empresas WHERE id_empresa = ?";
            PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
            preparedStatement.setInt(1, id_empresa);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()){
                toReturn = new Empresa(
                        rs.getInt("id_empresa"),
                        rs.getString("nombre"),
                        rs.getString("owner"),
                        rs.getDouble("pixelcoins"),
                        rs.getDouble("ingresos"),
                        rs.getDouble("gastos"),
                        rs.getString("icono"),
                        rs.getString("descripcion")
                );
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return toReturn;
    }

    public void verEmpresa(Player p, String nombreEmpresa) {
        Empresa empresa = this.getEmpresa(nombreEmpresa);

        if (empresa == null) {
            p.sendMessage(ChatColor.DARK_RED + "Esa empresa no existe");
            return;
        }
        boolean ow = this.esOwner(p.getName(), nombreEmpresa);
        if (!empresa.getOwner().equalsIgnoreCase(p.getName())) {
            p.sendMessage(ChatColor.DARK_RED + "No eres el dueño de la empresa");
            return;
        }
        Funciones f = new Funciones();

        String logitipo = empresa.getIcono();
        String descripcion = empresa.getDescripcion();
        double pixelcoins = empresa.getPixelcoins();
        double ingresos = empresa.getIngresos();
        double gastos = empresa.getGastos();
        double beneficios = ingresos - gastos;
        double rentabilidad = f.rentabilidad(ingresos, beneficios);

        p.sendMessage(ChatColor.GOLD + "        Estadisticas para " + ChatColor.DARK_AQUA + nombreEmpresa);
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
            p.sendMessage(ChatColor.GOLD + "rentabilidad: " + ChatColor.RED + ((int) rentabilidad) + "%");
        } else {
            p.sendMessage(ChatColor.GOLD + "rentabilidad: " + ((int) rentabilidad) + "%");
        }

        p.sendMessage(ChatColor.GOLD + "Empleados:");
        try {
            Empleados em = new Empleados();
            em.conectar();
            List<Empleado> empleados = em.getEmpleadosEmrpesa(nombreEmpresa);
            String nombre = "";
            String cargo = "";
            String tipoSueldo = "";
            String nombreTipoSueldo = "";
            double sueldo = 0;
            int id_empleado = 0;
            boolean enc = false;

            for (Empleado empleado : empleados) {
                enc = true;
                id_empleado = empleado.getId();

                nombre = empleado.getEmpleado();
                cargo = empleado.getCargo();
                tipoSueldo = empleado.getTipo();
                sueldo = empleado.getSueldo();

                nombreTipoSueldo = toStringTipoSueldo(tipoSueldo);

                p.sendMessage(ChatColor.GOLD + " -" + nombre + " cargo: " + cargo + " sueldo: " + ChatColor.GREEN + sueldo + " PC/ " + nombreTipoSueldo);
            }

            if (!enc) {
                p.sendMessage(ChatColor.GOLD + "       Sin trabajadores");
            }
            em.desconectar();
        } catch (Exception e) {

        }
    }

    public void cambiarDescripciom(String nombreEmpresa, String nuevaDescripcion, Player p) {
        Empresa empresa = this.getEmpresa(nombreEmpresa);

        if (empresa != null) {
            p.sendMessage(ChatColor.DARK_RED + "Esa empresa no existe");
            return;
        }
        if (!empresa.getOwner().equalsIgnoreCase(p.getName())) {
            p.sendMessage(ChatColor.DARK_RED + "No eres el dueño de esa empresa");
            return;
        }

        this.setDescripcion(nombreEmpresa, nuevaDescripcion);

        p.sendMessage(ChatColor.GOLD + "Has cambiado la descripccion de tu empresa: " + ChatColor.DARK_AQUA + nombreEmpresa + ChatColor.GOLD + " a ver en " + ChatColor.AQUA + "/empresas vertodas");
    }

    public static String toStringTipoSueldo(String tipoSueldo) {
        String nombreTipoSueldo = null;
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
        return nombreTipoSueldo;
    }
}