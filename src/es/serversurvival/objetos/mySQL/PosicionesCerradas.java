package es.serversurvival.objetos.mySQL;

import es.serversurvival.main.Funciones;
import es.serversurvival.objetos.mySQL.tablasObjetos.PosicionCerrada;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PosicionesCerradas extends MySQL {
    public void nuevaPosicion(String jugador, String tipo, String nombre,  int cantidad, double precioApertura, String fechaApertura, double precioCierre) {
        Date dt = new Date();
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String fechaCierre = sdf.format(dt);

        double rentabilidad = 0;
        rentabilidad = Funciones.redondeoDecimales(Funciones.diferenciaPorcntual(precioApertura, precioCierre), 3);

        try {
            String consulta = "INSERT INTO posicionescerradas (jugador, tipo, nombre, cantidad, precioApertura, fechaApertura, precioCierre, fechaCierre, rentabilidad) VALUES ('" + jugador + "','"+tipo+"','" + nombre + "','" + cantidad + "','" + precioApertura + "', '" + fechaApertura + "','" + precioCierre + "','" + fechaCierre + "','" + rentabilidad + "')";
            Statement statement = conexion.createStatement();
            statement.executeUpdate(consulta);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getJugador(int id) {
        String jugador = "";
        try {
            String consulta = "SELECT jugador FROM posicionescerradas WHERE id = ?";
            PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                jugador = rs.getString("jugador");
                break;
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jugador;
    }

    public String getNombre(int id) {
        String nombre = "";
        try {
            String consulta = "SELECT nombre FROM posicionescerradas WHERE id = ?";
            PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                nombre = rs.getString("nombre");
                break;
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nombre;
    }

    public String getTipo(int id) {
        String tipo = "";
        try {
            String consulta = "SELECT tipo FROM posicionescerradas WHERE id = ?";
            PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                tipo = rs.getString("tipo");
                break;
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tipo;
    }

    public int getCantidad(int id) {
        int cantidad = 0;
        try {
            String consulta = "SELECT cantidad FROM posicionescerradas WHERE id = ?";
            PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                cantidad = rs.getInt("cantidad");
                break;
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cantidad;
    }

    public double getPrecioApertura(int id) {
        double precio = 0;
        try {
            String consulta = "SELECT precioApertura FROM posicionescerradas WHERE id = ?";
            PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                precio = rs.getDouble("precioApertura");
                break;
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return precio;
    }

    public String getFechaApertura(int id) {
        String fechaApertura = "";
        try {
            String consulta = "SELECT fechaApertura FROM posicionescerradas WHERE id = ?";
            PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                fechaApertura = rs.getString("fechaApertura");
                break;
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fechaApertura;
    }

    public double getPrecioCierre(int id) {
        double precioCierre = 0;
        try {
            String consulta = "SELECT precioCierre FROM posicionescerradas WHERE id = ?";
            PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                precioCierre = rs.getDouble("precioCierre");
                break;
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return precioCierre;
    }

    public String getFechaCierre(int id) {
        String getFechaCierre = "";
        try {
            String consulta = "SELECT fechaCierre FROM posicionescerradas WHERE id = ?";
            PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                getFechaCierre = rs.getString("fechaCierre");
                break;
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getFechaCierre;
    }

    public double getRentabilidad(int id) {
        double rentabilidad = 0;
        try {
            String consulta = "SELECT rentabilidad FROM posicionescerradas WHERE id = ?";
            PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                rentabilidad = rs.getDouble("rentabilidad");
                break;
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rentabilidad;
    }

    public List<PosicionCerrada> getTop3PersonalRentabilidadesID(String jugador) {
        ArrayList<PosicionCerrada> posicionCerradas = new ArrayList<>();
        try {
            String consulta = "SELECT * FROM posicionescerradas WHERE jugador = ? ORDER BY rentabilidad DESC LIMIT 3";
            PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
            preparedStatement.setString(1, jugador);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                posicionCerradas.add(new PosicionCerrada(
                        resultSet.getInt("id"),
                        resultSet.getString("jugador"),
                        resultSet.getString("tipo"),
                        resultSet.getString("nombre"),
                        resultSet.getInt("cantidad"),
                        resultSet.getDouble("precioApertura"),
                        resultSet.getString("fechaApertura"),
                        resultSet.getDouble("precioCierre"),
                        resultSet.getString("fechaCierre"),
                        resultSet.getDouble("rentabilidad")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return posicionCerradas;
    }

    public List<PosicionCerrada> getTop3PersonalMenosRentabilidadesID(String jugador) {
        ArrayList<PosicionCerrada> posicionCerradas = new ArrayList<>();
        try {
            String consulta = "SELECT * FROM posicionescerradas WHERE jugador = ? ORDER BY rentabilidad ASC LIMIT 3";
            PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
            preparedStatement.setString(1, jugador);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                posicionCerradas.add(new PosicionCerrada(
                        resultSet.getInt("id"),
                        resultSet.getString("jugador"),
                        resultSet.getString("tipo"),
                        resultSet.getString("nombre"),
                        resultSet.getInt("cantidad"),
                        resultSet.getDouble("precioApertura"),
                        resultSet.getString("fechaApertura"),
                        resultSet.getDouble("precioCierre"),
                        resultSet.getString("fechaCierre"),
                        resultSet.getDouble("rentabilidad")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return posicionCerradas;
    }

    public List<PosicionCerrada> getTop5GlobalRentabilidadesID() {
        ArrayList<PosicionCerrada> posicionCerradas = new ArrayList<>();
        try {
            String consulta = "SELECT * FROM posicionescerradas ORDER BY rentabilidad DESC LIMIT 5";
            Statement statement = conexion.createStatement();
            ResultSet resultSet = statement.executeQuery(consulta);

            while (resultSet.next()) {
                posicionCerradas.add(new PosicionCerrada(
                        resultSet.getInt("id"),
                        resultSet.getString("jugador"),
                        resultSet.getString("tipo"),
                        resultSet.getString("nombre"),
                        resultSet.getInt("cantidad"),
                        resultSet.getDouble("precioApertura"),
                        resultSet.getString("fechaApertura"),
                        resultSet.getDouble("precioCierre"),
                        resultSet.getString("fechaCierre"),
                        resultSet.getDouble("rentabilidad")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return posicionCerradas;
    }

    public List<PosicionCerrada> getIdPosiciones(String name) {
        ArrayList<PosicionCerrada> posicionCerradas = new ArrayList<>();
        try {
            String consulta = "SELECT * FROM posicionescerradas WHERE jugador = ?";
            PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                posicionCerradas.add(new PosicionCerrada(
                        resultSet.getInt("id"),
                        resultSet.getString("jugador"),
                        resultSet.getString("tipo"),
                        resultSet.getString("nombre"),
                        resultSet.getInt("cantidad"),
                        resultSet.getDouble("precioApertura"),
                        resultSet.getString("fechaApertura"),
                        resultSet.getDouble("precioCierre"),
                        resultSet.getString("fechaCierre"),
                        resultSet.getDouble("rentabilidad")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return posicionCerradas;
    }
}