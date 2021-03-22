package es.serversurvival.objetos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


import es.serversurvival.config.Funciones;
import net.md_5.bungee.api.ChatColor;

public class Empresas extends MySQL {
    public DecimalFormat formatea = new DecimalFormat("###,###.##");

    private void nuevaEmpresa(String nombreEmpresa, String owner, int pixelcoins, int ingresos, int gastos, String icono, String descripcion) {
        try {
            String consulta = "INSERT INTO empresas (nombre, owner, pixelcoins, ingresos, gastos, icono, descripcion) VALUES ('" + nombreEmpresa + "','" + owner + "','" + pixelcoins + "','" + ingresos + "','" + gastos + "','" + icono + "','" + descripcion + "')";
            Statement st2 = (Statement) conexion.createStatement();
            st2.executeUpdate(consulta);
        } catch (SQLException e) {

        }
    }

    private int getNEmpresas(String owner) {
        int nempresas = 0;
        try {
            String consulta = "SELECT * FROM empresas";
            Statement st = conexion.createStatement();
            ResultSet rs;
            rs = st.executeQuery(consulta);

            while (rs.next()) {
                if (rs.getString("owner").equalsIgnoreCase(owner)) {
                    nempresas = nempresas + 1;
                }
            }
        } catch (SQLException e) {

        }
        return nempresas;
    }

    public String getOwner(String nombreEmpresa) {
        String owner = "";
        try {
            String consulta = "SELECT * FROM empresas";
            Statement st = conexion.createStatement();
            ResultSet rs;
            rs = st.executeQuery(consulta);

            while (rs.next()) {
                if (rs.getString("nombre").equalsIgnoreCase(nombreEmpresa)) {
                    owner = rs.getString("owner");
                    break;
                }
            }
        } catch (SQLException e) {

        }
        return owner;
    }

    public int getPixelcoins(String nombreEmpresa) {
        int pixelcoins = 0;
        try {
            String consulta = "SELECT * FROM empresas";
            Statement st = conexion.createStatement();
            ResultSet rs;
            rs = st.executeQuery(consulta);

            while (rs.next()) {
                if (rs.getString("nombre").equalsIgnoreCase(nombreEmpresa)) {
                    pixelcoins = rs.getInt("pixelcoins");
                    break;
                }
            }
        } catch (SQLException e) {

        }
        return pixelcoins;
    }

    public int getIngresos(String nombreEmpresa) {
        int ingresos = 0;
        try {
            String consulta = "SELECT * FROM empresas";
            Statement st = conexion.createStatement();
            ResultSet rs;
            rs = st.executeQuery(consulta);

            while (rs.next()) {
                if (rs.getString("nombre").equalsIgnoreCase(nombreEmpresa)) {
                    ingresos = rs.getInt("ingresos");
                    break;
                }
            }
        } catch (SQLException e) {

        }
        return ingresos;
    }

    public int getGastos(String nombreEmpresa) {
        int gastos = 0;
        try {
            String consulta = "SELECT * FROM empresas";
            Statement st = conexion.createStatement();
            ResultSet rs;
            rs = st.executeQuery(consulta);

            while (rs.next()) {
                if (rs.getString("nombre").equalsIgnoreCase(nombreEmpresa)) {
                    gastos = rs.getInt("gastos");
                    break;
                }
            }
        } catch (SQLException e) {

        }
        return gastos;
    }

    public String getIcono(String nombreEmpresa) {
        String icono = "";
        try {
            String consulta = "SELECT * FROM empresas";
            Statement st = conexion.createStatement();
            ResultSet rs;
            rs = st.executeQuery(consulta);

            while (rs.next()) {
                if (rs.getString("nombre").equalsIgnoreCase(nombreEmpresa)) {
                    icono = rs.getString("icono");
                    break;
                }
            }
        } catch (SQLException e) {

        }
        return icono;
    }

    public String getDescripcion(String nombreEmpresa) {
        String descripcion = "";
        try {
            String consulta = "SELECT * FROM empresas";
            Statement st = conexion.createStatement();
            ResultSet rs;
            rs = st.executeQuery(consulta);

            while (rs.next()) {
                if (rs.getString("nombre").equalsIgnoreCase(nombreEmpresa)) {
                    descripcion = rs.getString("descripcion");
                    break;
                }
            }
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
            String consulta = "SELECT * FROM empresas";
            Statement st = conexion.createStatement();
            ResultSet rs;
            rs = st.executeQuery(consulta);

            while (rs.next()) {
                if (rs.getString("nombre").equalsIgnoreCase(nombreEmpresa) && rs.getString("owner").equalsIgnoreCase(jugador)) {
                    es = true;
                    break;
                }
            }
        } catch (SQLException e) {

        }
        return es;
    }

    public boolean estaRegistradoNombre(String nombre) {
        boolean registrado = false;

        try {
            String consulta = "SELECT * FROM empresas";
            Statement st = conexion.createStatement();
            ResultSet rs;
            rs = st.executeQuery(consulta);

            while (rs.next()) {
                if (rs.getString("nombre").equalsIgnoreCase(nombre)) {
                    registrado = true;
                    break;
                }
            }
        } catch (SQLException e) {

        }
        return registrado;
    }

    public void crearEmpresa(String nombreEmpresa, Player p, String descripcion) {
        String owner = p.getName();
        int nempresas = this.getNEmpresas(owner) + 1;
        boolean reg = this.estaRegistradoNombre(nombreEmpresa);
        if (nempresas <= 3) {
            if (reg == false) {
                this.nuevaEmpresa(nombreEmpresa, owner, 0, 0, 0, "DIAMOND_PICKAXE", descripcion);

                p.sendMessage(ChatColor.GOLD + "Has creado una empresa! " + ChatColor.GOLD + "comandos utiles: " + ChatColor.AQUA + "/depositar, /contratar, /logotipo, /empresas, /miempresa /editarnombre m?s: /ayuda empresas");
                p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
                p.getServer().broadcastMessage(ChatColor.GOLD + owner + " ha creado una nueva empresa: " + ChatColor.DARK_AQUA + nombreEmpresa);
            } else {
                p.sendMessage(ChatColor.DARK_RED + "El nombre de esa empresa ya esta registrada");
            }
        } else {
            p.sendMessage(ChatColor.DARK_RED + "Solo puedes tener como maximo 3 empresas a la vez, al menos por ahora xd");
        }
    }

    public void cambiarIcono(String nombreEmpresa, Player p, String icono) {
        boolean existe = this.estaRegistradoNombre(nombreEmpresa);

        if (existe == false) {
            p.sendMessage(ChatColor.DARK_RED + "Esa empresa no existe");
            return;
        }
        boolean esowner = this.esOwner(p.getName(), nombreEmpresa);

        if (esowner == false) {
            p.sendMessage(ChatColor.DARK_RED + "No eres el due?o de esa empresa");
            return;
        }

        this.setIcono(nombreEmpresa, icono);
        p.sendMessage(ChatColor.GOLD + "Has cambiado el logotipo a: " + icono);
        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
    }

    public void cambiarNombre(Player p, String empresa, String nuevoNombre) {
        if (this.estaRegistradoNombre(empresa) == false) {
            p.sendMessage(ChatColor.DARK_RED + "Esa empresa no existe");
            return;
        }
        if (this.estaRegistradoNombre(nuevoNombre) == true) {
            p.sendMessage(ChatColor.DARK_RED + "Esa nombre ya esta cogido");
            return;
        }
        if (this.esOwner(p.getName(), empresa) == false) {
            p.sendMessage(ChatColor.DARK_RED + "No eres el due?o de esa empresa");
            return;
        }
        ArrayList<String> empleados = new ArrayList<String>();
        try {
            Empleados em = new Empleados();
            em.conectar("root", "", "pixelcoins");
            empleados = em.getNombreEmpleadosEmpresa(empresa);
            em.cambiarEmpresaNombre(empresa, nuevoNombre);
            em.desconectar();
        } catch (Exception e) {

        }
        this.setNombre(empresa, nuevoNombre);
        p.sendMessage(ChatColor.GOLD + "Has cambiado de nombre a tu empresa!");
        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);

        Mensajes men = new Mensajes();
        for (int i = 0; i < empleados.size(); i++) {
            men.nuevoMensaje(empleados.get(i), "La empresa en la que trabajas: " + empresa + " ha cambiado a de nombre a " + nuevoNombre);
        }
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
        int margen;

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

                margen = (int) Math.round((beneficiosD / ingresosD) * 100);

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
                    em.conectar("root", "", "pixelcoins");
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
        if (reg == false) {
            p.sendMessage(ChatColor.DARK_RED + "Esa empresa no existe");
            return;
        }
        boolean ow = this.esOwner(p.getName(), empresa);
        if (ow == false) {
            p.sendMessage(ChatColor.DARK_RED + "No eres el due?o de la empresa");
            return;
        }

        String logitipo = this.getIcono(empresa);
        String descripcion = this.getDescripcion(empresa);
        int pixelcoins = this.getPixelcoins(empresa);
        int ingresos = this.getIngresos(empresa);
        int gastos = this.getGastos(empresa);
        int beneficios = ingresos - gastos;
        ArrayList<String> empleados = new ArrayList<String>();

        p.sendMessage(ChatColor.GOLD + "        Estadisticas para " + ChatColor.DARK_AQUA + empresa);
        p.sendMessage(ChatColor.GOLD + "Pixelcoins en la empresa: " + ChatColor.GREEN + pixelcoins + " PC");
        p.sendMessage(ChatColor.GOLD + "Ingresos: " + ChatColor.GREEN + ingresos + " PC");
        p.sendMessage(ChatColor.GOLD + "gastos: " + ChatColor.GREEN + gastos + " PC");
        if (beneficios < 0) {
            p.sendMessage(ChatColor.GOLD + "beneficios: " + ChatColor.RED + beneficios + ChatColor.GREEN + " PC");
        } else {
            p.sendMessage(ChatColor.GOLD + "beneficios: " + ChatColor.GREEN + beneficios + " PC");
        }
        p.sendMessage(ChatColor.GOLD + "logotipo: " + logitipo);
        p.sendMessage(ChatColor.GOLD + "descripcion: " + descripcion);

        p.sendMessage("   ");
        p.sendMessage(ChatColor.GOLD + "Empleados:");
        try {
            Empleados em = new Empleados();
            em.conectar("root", "", "pixelcoins");
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

            if (enc == false) {
                p.sendMessage(ChatColor.GOLD + "       Sin trabajadores");
            }
            em.desconectar();
        } catch (Exception e) {

        }
    }

}