package es.serversurvival.objetos.mySQL;

import es.serversurvival.main.Funciones;
import es.serversurvival.objetos.apiHttp.IEXCloud_API;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.plugin.Plugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PosicionesAbiertas extends MySQL {
    private static String titulo = ChatColor.DARK_RED + "SELECCIONA Nº DE ACCIONES";

    public void nuevaPosicion(String jugador, String ticker, int nAcciones, double precioApertura) {
        Date dt = new Date();
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String fecha = sdf.format(dt);

        try {
            String consulta = "INSERT INTO posicionesabiertas (jugador, ticker, nAcciones, precioApertura, fechaApertura) VALUES ('" + jugador + "','" + ticker + "','" + nAcciones + "','" + precioApertura + "', '" + fecha + "')";
            Statement statement = conexion.createStatement();
            statement.executeUpdate(consulta);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void borrarPosicion(int id) {
        try {
            String consulta = "DELETE FROM posicionesabiertas WHERE id = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta);
            pst.setInt(1, id);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getJugador(int id) {
        String nombre = "";
        try {
            String consulta = "SELECT jugador FROM posicionesabiertas WHERE id = ?";
            PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                nombre = rs.getString("jugador");
                break;
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nombre;
    }

    public String getTicker(int id) {
        String ticker = "";
        try {
            String consulta = "SELECT ticker FROM posicionesabiertas WHERE id = ?";
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
        int acciones = 0;
        try {
            String consulta = "SELECT nAcciones FROM posicionesabiertas WHERE id = ?";
            PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                acciones = rs.getInt("nAcciones");
                break;
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return acciones;
    }

    public double getPreciopApertura(int id) {
        double apertura = 0;
        try {
            String consulta = "SELECT precioApertura FROM posicionesabiertas WHERE id = ?";
            PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                apertura = rs.getDouble("precioApertura");
                break;
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apertura;
    }

    public String getFechaApertura(int id) {
        String fecha = "";
        try {
            String consulta = "SELECT fechaApertura FROM posicionesabiertas WHERE id = ?";
            PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                fecha = rs.getString("fechaApertura");
                break;
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fecha;
    }

    public ArrayList<Integer> getIdsPosicionesJugador(String jugador) {
        ArrayList<Integer> toReturn = new ArrayList<>();
        try {
            String consuta = "SELECT id FROM posicionesabiertas WHERE jugador = ?";
            PreparedStatement preparedStatement = conexion.prepareStatement(consuta);
            preparedStatement.setString(1, jugador);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                toReturn.add(resultSet.getInt("id"));
            }
            resultSet.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    public boolean existe(int id) {
        boolean toReturn = false;
        try {
            String consulta = "SELECT id FROM posicionesabiertas WHERE id = ?";
            PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                toReturn = true;
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    public void setNAcciones(int id, int NAcciones) {
        try {
            String consulta = "UPDATE posicionesabiertas SET nAcciones = ? WHERE id = ?";
            PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
            preparedStatement.setInt(1, NAcciones);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pagarDividendos(Plugin plugin) {
        try {
            String consulta = "SELECT ticker, nAcciones, jugador, id FROM posicionesabiertas";
            Statement statement = conexion.createStatement();
            ResultSet resultSet = statement.executeQuery(consulta);

            String ticker;
            String jugador;
            int id;
            int nAcciones;

            JSONArray jsonArray;
            double diviendo = 0;
            Date fechaHoy = new Date();
            Date fechaPagoDividendo = null;
            Transacciones t = new Transacciones();

            while (resultSet.next()) {
                ticker = resultSet.getString("ticker");
                jugador = resultSet.getString("jugador");
                id = resultSet.getInt("id");
                nAcciones = resultSet.getInt("nAcciones");

                try {
                    jsonArray = IEXCloud_API.getDividendo(ticker, "week");
                    JSONObject jsonObject = (JSONObject) jsonArray.get(0);
                    diviendo = (double) jsonObject.get("amount");
                    fechaPagoDividendo = new Date((String) jsonObject.get("paymentDate"));
                } catch (Exception e) {
                    continue;
                }

                if (Funciones.diferenciaDias(fechaHoy, fechaPagoDividendo) != 0) {
                    continue;
                }
                t.pagaDividendo(ticker, jugador, diviendo, nAcciones);
                plugin.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Se ha pagado " + diviendo + " PC de dividendo a " + jugador + " en la empresa: " + ticker);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}