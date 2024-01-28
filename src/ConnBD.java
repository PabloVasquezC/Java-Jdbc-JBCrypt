

import java.sql.*;

public class ConnBD {

    public static void main(String[] args) throws Exception {

        String sql = "SELECT * FROM USERS";
        String url = "jdbc:mysql://localhost:/exampleconnectionjavajdbc";
        String username = "root";
        String password = "CATA.7531";

        try (Connection con = DriverManager.getConnection(url, username, password);
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                // Obtener valores de las columnas por nombre o índice
                String id = rs.getString("ID");
                String user = rs.getString("USERNAME");
                String hashedpassword = rs.getString("HASHED_PASSWORD");
                // Imprimir los valores de las columnas
                System.out.println("ID: " + id + ", Username: " + user + ", Hashed password: " + hashedpassword);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
