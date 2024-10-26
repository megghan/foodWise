import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MySQLConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/foodwisebd";
    private static final String USERNAME = "yetiPequeno";
    private static final String PASSWORD = "27megghan!*";
    private Connection connection;

    public MySQLConnection() {
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Connected to the database successfully!");
        } catch (SQLException e) {
            System.out.println("Error connecting to the database: " + e.getMessage());
        }
    }

    // Close connection
    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("Connection closed successfully!");
            }
        } catch (SQLException e) {
            System.out.println("Error closing the connection: " + e.getMessage());
        }
    }
    //--------- Métodos necessários ------------
    public boolean userExists(String username) throws SQLException {
        String query = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1) > 0; // Retorna true se o usuário existe
            }
        }
        return false; // Retorna false se o usuário não existe
    }

    public void checkExpiringItems() throws SQLException {
        String query = "SELECT name, expiry_date FROM food";
        List<String> expiredItems = new ArrayList<>();
        List<String> aboutToExpireItems = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();

            // Data atual para comparação
            LocalDate today = LocalDate.now();

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                LocalDate expiryDate = resultSet.getDate("expiry_date").toLocalDate();

                // Checar se o item já venceu
                if (expiryDate.isBefore(today)) {
                    expiredItems.add(name + " (vencido)");
                }
                // Checar se o item está prestes a vencer (até 5 dias para vencer)
                else if (!expiryDate.isAfter(today.plusDays(5))) {
                    aboutToExpireItems.add(name + " (prestes a vencer)");
                }
            }
        }

        // Exibir resultados
        int totalItems = expiredItems.size() + aboutToExpireItems.size();

        if (totalItems > 0) {
            System.out.println("Você tem " +totalItems +" itens vencidos ou prestes a vencer" );
            System.out.println("Itens:");
            expiredItems.forEach(System.out::println);
            aboutToExpireItems.forEach(System.out::println);
        } else {
            System.out.println("Nenhum item vencido ou prestes a vencer.");
        }
    }

    public void listaComidas() throws SQLException{
        String query = "SELECT * FROM food";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            System.out.println("=== Lista de Alimentos ===");
            while (resultSet.next()) {
                System.out.println("Nome: " + resultSet.getString("name") +
                        ", Quantidade: " + resultSet.getInt("quantity") +
                        ", Data de Validade: " + resultSet.getDate("expiry_date"));
            }}
    }

    public void listaReceitas() throws SQLException {
        String query = "SELECT * FROM recipe";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            System.out.println("=== Lista de Receitas ===");
            while (resultSet.next()) {
                System.out.println("Nome: " + resultSet.getString("name") +
                        ", Ingredientes: " + resultSet.getString("ingredients"));
            }
        }}

    //----------- Métodos CRUD --------------

    // para a tabela food
    public void createFood(String name, int quantity, String expiryDate) throws SQLException {
        String query = "INSERT INTO food (name, quantity, expiry_date) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, quantity);
            pstmt.setDate(3, Date.valueOf(expiryDate));
            pstmt.executeUpdate();
            System.out.println("Item criado com sucesso");
        }
    }

    public void readFood() throws SQLException {
        String query = "SELECT * FROM food";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                System.out.printf("ID: %d, Nome: %s, Quantidade: %d, Data de Validade: %s%n",
                        rs.getInt("id"), rs.getString("name"), rs.getInt("quantity"), rs.getDate("expiry_date"));
            }
        }
    }

    public void updateFood(int id, String name, int quantity, String expiryDate) throws SQLException {
        String query = "UPDATE food SET name = ?, quantity = ?, expiry_date = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, quantity);
            pstmt.setDate(3, Date.valueOf(expiryDate));
            pstmt.setInt(4, id);
            pstmt.executeUpdate();
            System.out.println("Item atualizado com sucesso");
        }
    }

    public void deleteFood(int id) throws SQLException {
        String query = "DELETE FROM food WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Item excluido");
        }
    }

    // CRUD Methods for 'recipe' Table
    public void createRecipe(String name, String ingredients, String instructions) throws SQLException {
        String query = "INSERT INTO recipe (name, ingredients, instructions) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, name);
            pstmt.setString(2, ingredients);
            pstmt.setString(3, instructions);
            pstmt.executeUpdate();
            System.out.println("Receita Criada!");
        }
    }

    public void readRecipes() throws SQLException {
        String query = "SELECT * FROM recipe";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                System.out.printf("ID: %d, Name: %s, Ingredients: %s%n",
                        rs.getInt("id"), rs.getString("name"), rs.getString("ingredients"));
            }
        }
    }

    public void updateRecipe(int id, String name, String ingredients) throws SQLException {
        String query = "UPDATE recipe SET name = ?, ingredients = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, name);
            pstmt.setString(2, ingredients);
            pstmt.setInt(3, id);
            pstmt.executeUpdate();
            System.out.println("Receita atualizada");
        }
    }

    public void deleteRecipe(int id) throws SQLException {
        String query = "DELETE FROM recipe WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Receita excluida");
        }
    }

    // CRUD Methods for 'user' Table
    public void createUser(String username, String password, String diet) throws SQLException {
        //verificar se usuario existe antes de criar um novo.
        if(userExists(username)){
            System.out.println("Usuario já existe. Escolha outro nome");
            return;
        }


        String query = "INSERT INTO users (username, password, diet) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, diet);
            pstmt.executeUpdate();
            System.out.println("Usuario criado com sucesso");
        }
    }

    public void readUsers() throws SQLException {
        String query = "SELECT * FROM users";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                System.out.printf("ID: %d, Username: %s, Diet: %s%n",
                        rs.getInt("id"), rs.getString("username"), rs.getString("diet"));
            }
        }
    }

    public void updateUser(int id, String username, String password, String diet) throws SQLException {
        String query = "UPDATE users SET username = ?, password = ?, diet = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, diet);
            pstmt.setInt(4, id);
            pstmt.executeUpdate();
            System.out.println("Usuário atualizado com sucesso");
        }
    }

    public void deleteUser(int id) throws SQLException {
        String query = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Usuário excluido");
        }
    }
}