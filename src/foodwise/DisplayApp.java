package foodwise;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DisplayApp extends Application {
    private MySQLConnection db = new MySQLConnection();
    private int userId;
    private String username;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("FoodWise - Gerenciamento de Alimentos");


        GridPane loginPane = createLoginPane(primaryStage);

        Scene loginScene = new Scene(loginPane, 400, 800);

        // Adicionando CSS (opcional)
        loginScene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showMainScreen(Stage primaryStage, String username) {
        VBox mainLayout = new VBox(10);
        mainLayout.setPadding(new Insets(10));
        mainLayout.setId("mainLayout");

        Label expiringItemsLabel = new Label();

        try {
            int expiringItemsCount = db.checkExpiringItems();
            expiringItemsLabel.setText("Você tem " + expiringItemsCount + " itens vencidos ou prestes a vencer.");
        } catch (SQLException e) {
            expiringItemsLabel.setText("Erro ao carregar itens prestes a vencer.");
            e.printStackTrace();
        }

        Button createRecipeButton = new Button("Criar Receita");
        createRecipeButton.setOnAction(event -> createRecipePane(primaryStage));

        Button viewRecipesButton = new Button("Ver Receitas");
        viewRecipesButton.setOnAction(event -> showRecipesScreen(primaryStage,userId));

        Button viewFoodButton = new Button("Ver Lista de Comidas");
        viewFoodButton.setOnAction(event -> showFoodsScreen(primaryStage, userId));

        Button addItemButton = new Button("Adicionar Comida");
        addItemButton.setOnAction(event -> {
            GridPane foodPane = createFoodPane(primaryStage);
            Scene foodScene = new Scene(foodPane, 600, 600);
            foodScene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            primaryStage.setScene(foodScene);
        });

        mainLayout.getChildren().addAll(expiringItemsLabel, createRecipeButton, viewRecipesButton, viewFoodButton, addItemButton);
        Scene mainScene = new Scene(mainLayout, 600, 600);
        mainScene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        primaryStage.setScene(mainScene);
        primaryStage.setTitle("FoodWise - Bem-vindo, " + username);
    }

//----------------TELAS--------------------//


    private GridPane createLoginPane(Stage primaryStage) {
        GridPane loginPane = new GridPane();
        loginPane.setPadding(new Insets(10));
        loginPane.setVgap(10);
        loginPane.setHgap(10);

        loginPane.setAlignment(Pos.CENTER);

        // Título
        Label titleLabel = new Label("Food Wise");
        titleLabel.getStyleClass().add("title-label");
        titleLabel.setAlignment(Pos.CENTER); // Centraliza o título

        Label userLabel = new Label("Usuário:");
        TextField userField = new TextField();
        Label passLabel = new Label("Senha:");
        PasswordField passField = new PasswordField();

        Button loginButton = new Button("Entrar");
        loginButton.setOnAction(event -> {
            String username = userField.getText();
            String password = passField.getText();
            try {
                boolean isAuthenticated = db.authenticateUser(username, password);
                if (isAuthenticated) {
                    userId = db.getUserId(username, password); // Obtenha o userId aqui
                    this.username = username;
                    System.out.println(userId);
                    showMainScreen(primaryStage, username);
                    showAlert("Sucesso", "Login bem-sucedido!");
                } else {
                    showAlert("Falha na Autenticação", "Usuário ou senha inválidos.");
                }
            } catch (SQLException e) {
                showAlert("Erro de Autenticação", e.getMessage());
            }
        });

        Button newUserButton = new Button("Novo Usuário");
        newUserButton.setOnAction(event -> primaryStage.setScene(new Scene(createRegistrationPane(primaryStage), 600, 600)));

        loginPane.add(titleLabel, 0, 0, 2, 1);
        loginPane.add(userLabel, 0, 1);
        loginPane.add(userField, 1, 1);
        loginPane.add(passLabel, 0, 2);
        loginPane.add(passField, 1, 2);
        loginPane.add(loginButton, 1, 3);
        loginPane.add(newUserButton, 1, 4);

        return loginPane;
    }

    private GridPane createRegistrationPane(Stage primaryStage) {
        GridPane registrationPane = new GridPane();
        registrationPane.setPadding(new Insets(10));
        registrationPane.setVgap(10);
        registrationPane.setHgap(10);


        registrationPane.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        // Campos de registro
        Label userLabel = new Label("Nome de Usuário:");
        TextField userField = new TextField();
        Label passLabel = new Label("Senha:");
        PasswordField passField = new PasswordField();
        Label dietTypeLabel = new Label("Tipo de Dieta:");
        TextField dietTypeField = new TextField();

        // Botão de Registrar
        Button registerButton = new Button("Registrar");
        registerButton.setOnAction(event -> {
            String username = userField.getText();
            String password = passField.getText();
            String dietType = dietTypeField.getText();
            try {
                db.createUser(username, password, dietType);
                showAlert("Sucesso!", "Usuário registrado com sucesso.");
                showMainScreen(primaryStage, username);
                primaryStage.setScene(new Scene(createLoginPane(primaryStage), 600, 600));
            } catch (SQLException e) {
                showAlert("Erro", "Erro ao registrar usuário: " + e.getMessage());
            }
        });

        // Adiciona componentes ao layout de registro
        registrationPane.add(userLabel, 0, 0);
        registrationPane.add(userField, 1, 0);
        registrationPane.add(passLabel, 0, 1);
        registrationPane.add(passField, 1, 1);
        registrationPane.add(dietTypeLabel, 0, 2);
        registrationPane.add(dietTypeField, 1, 2);
        registrationPane.add(registerButton, 1, 3);

        return registrationPane;
    }

    private GridPane createFoodPane(Stage primaryStage) {
        GridPane foodPane = new GridPane();
        foodPane.setPadding(new Insets(10));
        foodPane.setVgap(10);
        foodPane.setHgap(10);

        // Campos de entrada
        TextField foodNameField = new TextField();
        foodNameField.setPromptText("Nome do Alimento");
        TextField quantityField = new TextField();
        quantityField.setPromptText("Quantidade");
        TextField expiryField = new TextField();
        expiryField.setPromptText("Data de Validade (YYYY-MM-DD)");

        // Botão para adicionar alimento
        Button addFoodButton = new Button("Adicionar Alimento");
        addFoodButton.setOnAction(event -> {
            String name = foodNameField.getText();
            int quantity = Integer.parseInt(quantityField.getText());
            String expiryDate = expiryField.getText();
            try {
                db.createFood(name, quantity, expiryDate, userId);
                showAlert("Sucesso", "Alimento adicionado com sucesso.");
            } catch (SQLException e) {
                showAlert("Erro ao Adicionar Alimento", e.getMessage());
            }
        });

        // Botão para voltar
        Button backButton = new Button("Voltar");
        backButton.setOnAction(event -> {
            showMainScreen(primaryStage, username); // Redireciona para a tela principal
        });

        // Adiciona componentes ao layout de alimentos
        foodPane.add(new Label("Nome do Alimento:"), 0, 0);
        foodPane.add(foodNameField, 1, 0);
        foodPane.add(new Label("Quantidade:"), 0, 1);
        foodPane.add(quantityField, 1, 1);
        foodPane.add(new Label("Data de Validade:"), 0, 2);
        foodPane.add(expiryField, 1, 2);
        foodPane.add(addFoodButton, 1, 3);
        foodPane.add(backButton, 1, 4); // Adiciona o botão de voltar abaixo do botão de adicionar

        return foodPane;
    }

    public void showFoodsScreen(Stage primaryStage, int userId) {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Label titleLabel = new Label("Lista de Alimentos");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        ListView<String> itemsListView = new ListView<>();

        try {
            // Chama o mtodo para buscar itens do banco de dados
            List<String> items = db.listaComidas(userId); // Aqui deve ser db.listaComidas
            itemsListView.getItems().addAll(items);
        } catch (SQLException e) {
            e.printStackTrace();
            itemsListView.getItems().add("Erro ao carregar itens: " + e.getMessage());
        }

        Button backButton = new Button("Voltar");
        backButton.setOnAction(e -> showMainScreen(primaryStage, username)); // Mudar username_placeholder para o username real

        layout.getChildren().addAll(titleLabel, itemsListView, backButton);

        Scene sceneFood = new Scene(layout, 400, 300);
        sceneFood.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        primaryStage.setScene(sceneFood);
        primaryStage.setTitle("Ver Itens");
        primaryStage.show();
    }

    private void showRecipesScreen(Stage primaryStage, int userId) {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Label titleLabel = new Label("Lista de Receitas");
        //System.out.println("userId atual: " + userId); debug
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        ListView<String> recipesListView = new ListView<>();

        try {

            List<String> recipes = db.listaReceitas(userId);
            recipesListView.getItems().addAll(recipes);
        } catch (SQLException e) {
            e.printStackTrace();
            recipesListView.getItems().add("Erro ao carregar receitas: " + e.getMessage());
        }

        Button backButton = new Button("Voltar");
        backButton.setOnAction(e -> showMainScreen(primaryStage,username));

        layout.getChildren().addAll(titleLabel, recipesListView, backButton);

        Scene sceneRecipe = new Scene(layout, 600, 600);
        sceneRecipe.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        primaryStage.setScene(sceneRecipe);
        primaryStage.setTitle("Ver Receitas");
        primaryStage.show();
    }

    private void createRecipePane(Stage primaryStage) {
        GridPane recipePane = new GridPane();
        recipePane.setPadding(new Insets(10));
        recipePane.setVgap(10);
        recipePane.setHgap(10);


        TextField recipeNameField = new TextField();
        recipeNameField.setPromptText("Nome da Receita");
        TextArea ingredientsField = new TextArea();
        ingredientsField.setPromptText("Ingredientes");
        TextArea instructionsField = new TextArea();
        instructionsField.setPromptText("Instruções");

        // Botão para adicionar receita
        Button addRecipeButton = new Button("Adicionar Receita");
        addRecipeButton.setOnAction(event -> {
            String name = recipeNameField.getText();
            String ingredients = ingredientsField.getText();
            String instructions = instructionsField.getText();
            try {
                db.createRecipe(name, ingredients, instructions, userId);
                showAlert("Sucesso", "Receita adicionada com sucesso.");
                // Limpa os campos após adicionar
                recipeNameField.clear();
                ingredientsField.clear();
                instructionsField.clear();
            } catch (SQLException e) {
                showAlert("Erro ao Adicionar Receita", e.getMessage());
            }
        });

        // Adiciona componentes ao layout de receitas
        recipePane.add(new Label("Nome da Receita:"), 0, 0);
        recipePane.add(recipeNameField, 1, 0);
        recipePane.add(new Label("Ingredientes:"), 0, 1);
        recipePane.add(ingredientsField, 1, 1);
        recipePane.add(new Label("Instruções:"), 0, 2);
        recipePane.add(instructionsField, 1, 2);
        recipePane.add(addRecipeButton, 1, 3);

        // Define a cena para o painel de receitas
        Scene recipeScene = new Scene(recipePane, 600, 600);
        recipeScene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        primaryStage.setScene(recipeScene);
        primaryStage.setTitle("Criar Receita");

        Button backToMainButton = new Button("Voltar para o Menu Principal");
        backToMainButton.setOnAction(event -> showMainScreen(primaryStage,username));
        recipePane.add(backToMainButton, 1, 4);
    }
}
