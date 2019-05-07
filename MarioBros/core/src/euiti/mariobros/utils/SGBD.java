package euiti.mariobros.utils;

import java.io.File;
import java.io.ObjectOutputStream.PutField;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SGBD {
	private static SGBD miSGBD;

    private SGBD() {
        File f = new File("mario.db");
        if (!f.exists()) {
            this.crearBD();
            this.crearTablas();
            //this.pruebasRanking();
            //this.pruebasPersonalizar();
            //this.pruebasUsuarios();

        }
    }

    public static SGBD getMiSGBD() {
        if (miSGBD == null) {
            miSGBD = new SGBD();
        }
        return miSGBD;
    }


    private void crearBD() {
        try (Connection con = DriverManager.getConnection("jdbc:sqlite:mario.db")) {
            if (con != null) {
                System.out.println("La bd ha sido creada.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Connection conectarBD() {
        String url = "jdbc:sqlite:mario.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    private void crearTablas() {

        String ranking = "CREATE TABLE Ranking" +
                "(IdRanking INTEGER AUTOINCREMENT, "+
                "Puntuacion INT(11) NOT NULL, " +
                "PRIMARY KEY(IdRanking)) ";

        try (Connection con = this.conectarBD();
             Statement stmt = con.createStatement()) {
            stmt.execute(ranking);

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Tablas creadas");
    }
    
    public void addPuntuacion(int puntuacion) {
    	try (Connection con = this.conectarBD();
               Statement stmt = con.createStatement()) {
               stmt.executeUpdate("INSERT INTO Ranking(Puntuacion)" +
                       " VALUES("+ puntuacion + ")");
       
           } catch (Exception e) {
               System.err.println(e.getClass().getName() + ": " + e.getMessage());
               System.exit(0);
           }
           System.out.println("Insertados datos ranking");
    }
    
    public ArrayList obtenerPuntuaciones() {
    	ArrayList<Integer> puntuaciones = new ArrayList<>();

    	// Sentencia sql que obtiene las mejores partidas
        String sql = "SELECT Puntuacion FROM Ranking ORDER BY Puntuacion DESC LIMIT 5;";

        // Conexión con la base de datos
        try (Connection conn = SGBD.getMiSGBD().conectarBD();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int puntuacion = rs.getInt("Puntuacion");
                puntuaciones.add(puntuacion);
            }

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Consulta obtenerMejoresPartidas terminada");
        return puntuaciones;
    }
}
