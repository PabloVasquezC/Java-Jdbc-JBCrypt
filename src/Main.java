import java.util.Objects;
import java.util.Scanner;
import lib.org.mindrot.jbcrypt.BCrypt;
import java.sql.*;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);


        typing("Welcome to the login system\n");
        typing("Please choose an option:\n");
        typing("1. Login\n");
        typing("2. Register\n");
        typing("Choose: ");
        String choose = scanner.nextLine();

        if(Objects.equals(choose, "1")) {
            typing("Please enter your username: ");
            String username = scanner.nextLine();

            typing("Ok, now enter your password: ");
            String password = scanner.nextLine();

            // Intentar obtener el usuario y su contraseña de la base de datos
            try {
                String hashedPassword = "";
                String url = "jdbc:mysql://localhost:/exampleconnectionjavajdbc";
                String dbUsername = "root";
                String dbPassword = "CATA.7531";

                try (Connection con = DriverManager.getConnection(url, dbUsername, dbPassword)) {
                    String sql = "SELECT HASHED_PASSWORD FROM USERS WHERE USERNAME = ?";

                    try (PreparedStatement pst = con.prepareStatement(sql)) {
                        pst.setString(1, username);

                        try (ResultSet rs = pst.executeQuery()) {
                            if (rs.next()) {
                                hashedPassword = rs.getString("HASHED_PASSWORD");
                            }
                        }
                    }
                }

                // Verificar si la contraseña ingresada coincide con la contraseña almacenada
                if (BCrypt.checkpw(password, hashedPassword)) {
                    typing("Login successful");
                } else {
                    typing("Invalid username or password");
                }
            } catch (SQLException e) {
                typing("Error connecting to database: " + e.getMessage());
            }
        } else

        if(Objects.equals(choose, "2")) {
            typing("Please enter your username: ");
            String username = scanner.nextLine();

            typing("Ok, now enter your password: ");
            String password = scanner.nextLine();

            // Validar la fortaleza de la contraseña antes de almacenarla
            if (isValidPassword(password)) {
                // Hashing password before storing
                String hashedPassword = hashPassword(password);

                // Intentar insertar el usuario y su contraseña en la base de datos
                try {
                    insertUserToDatabase(username, hashedPassword);
                    typing("User successfully registered\n");
                    typing("Username: " + username + "\n");
                } catch (SQLException e) {
                    System.out.println(hashedPassword);
                    typing("Error connecting to database: " + e.getMessage());
                }
            } else {
                  typing("Password is not strong enough");
            }
        }


    }

    public static String typing(String message) {
        for (int i = 0; i < message.length(); i++) {
            System.out.print(message.charAt(i));

            try {
                // Pausar la ejecución durante 300 ms
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return message;
    }

    // Hashing password methods
    public static String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    // Método para validar la fortaleza de la contraseña
    public static boolean isValidPassword(String password) {
        // Agrega lógica para validar la fortaleza de la contraseña (longitud, caracteres, etc.)
        return password.length() >= 8; // Por ejemplo, aquí se verifica la longitud mínima de 8 caracteres
    }

    // Método para insertar usuario y contraseña en la base de datos
    public static void insertUserToDatabase(String username, String hashedPassword) throws SQLException {
        String url = "jdbc:mysql://localhost:/exampleconnectionjavajdbc";
        String dbUsername = "root";
        String dbPassword = "CATA.7531";

        try (Connection con = DriverManager.getConnection(url, dbUsername, dbPassword)) {
            String sql = "INSERT INTO USERS (USERNAME, HASHED_PASSWORD) VALUES (?, ?)";

            try (PreparedStatement pst = con.prepareStatement(sql)) {
                pst.setString(1, username);
                pst.setString(2, hashedPassword);
                pst.executeUpdate();
            }
        }
    }
}
