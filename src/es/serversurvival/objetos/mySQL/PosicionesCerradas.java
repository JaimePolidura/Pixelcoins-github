package es.serversurvival.objetos.mySQL;

import es.serversurvival.main.Funciones;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PosicionesCerradas extends MySQL {
    public void nuevaPosicion(String jugador, String ticker, int nAcciones, double precioApertura, String fechaApertura, double precioCierre, int idPoscionAbierta) {
        Date dt = new Date();
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String fechaCierre = sdf.format(dt);
        ticker.toUpperCase();

        double rentabilidad = 0;
        rentabilidad = Funciones.redondeoDecimales(Funciones.diferenciaPorcntual(precioApertura, precioCierre), 3);

        try {
            String consulta = "INSERT INTO posicionescerradas (jugador, ticker, nAcciones, precioApertura, fechaApertura, precioCierre, fechaCierre, rentabilidad, idPosicionabierta) VALUES ('" + jugador + "','" + ticker + "','" + nAcciones + "','" + precioApertura + "', '" + fechaApertura + "','" + precioCierre + "','" + fechaCierre + "','" + rentabilidad + "','" + idPoscionAbierta + "')";
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

    public String getTicker(int id) {
        String ticker = "";
        try {
            String consulta = "SELECT ticker FROM posicionescerradas WHERE id = ?";
            PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                ticker = rs.getString("ticker");
                break;
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ticker;
    }

    public int getNAcciones(int id) {
        int nAcciones = 0;
        try {
            String consulta = "SELECT nAcciones FROM posicionescerradas WHERE id = ?";
            PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                nAcciones = rs.getInt("nAcciones");
                break;
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nAcciones;
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

    public int getIDPosicionAbierta(int id) {
        int idPosicionabierta = 0;
        try {
            String consulta = "SELECT idPosicionabierta FROM posicionescerradas WHERE id = ?";
            PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                idPosicionabierta = rs.getInt("idPosicionabierta");
                break;
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return idPosicionabierta;
    }

    public ArrayList<Integer> getTop3PersonalRentabilidadesID(String jugador) {
        ArrayList<Integer> ids = new ArrayList<>();
        try {
            String consulta = "SELECT id FROM posicionescerradas WHERE jugador = ? ORDER BY rentabilidad DESC LIMIT 3";
            PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
            preparedStatement.setString(1, jugador);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                ids.add(resultSet.getInt("id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ids;
    }

    public ArrayList<Integer> getTop3PersonalMenosRentabilidadesID(String jugador) {
        ArrayList<Integer> ids = new ArrayList<>();
        try {
            String consulta = "SELECT id FROM posicionescerradas WHERE jugador = ? ORDER BY rentabilidad ASC LIMIT 3";
            PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
            preparedStatement.setString(1, jugador);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                ids.add(resultSet.getInt("id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ids;
    }

    public ArrayList<Integer> getTop3GlobalRentabilidadesID() {
        ArrayList<Integer> ids = new ArrayList<>();
        try {
            String consulta = "SELECT id FROM posicionescerradas ORDER BY rentabilidad DESC LIMIT 3";
            Statement statement = conexion.createStatement();
            ResultSet resultSet = statement.executeQuery(consulta);

            while (resultSet.next()) {
                ids.add(resultSet.getInt("id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ids;
    }

    public ArrayList<Integer> getIdPosiciones(String name) {
        ArrayList<Integer> ids = new ArrayList<>();
        try {
            String consulta = "SELECT id FROM posicionescerradas WHERE jugador = ?";
            PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                ids.add(resultSet.getInt("id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ids;
    }
}