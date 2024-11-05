package foodwise;

import java.sql.SQLException;
import java.util.Objects;
import java.util.Scanner;


public class AppFoodWise{
    public static void main(String[] args) throws SQLException {
        //instancia de conexão com banco de dados
        MySQLConnection db = new MySQLConnection();

        Scanner scan = new Scanner(System.in);
        //cria novo usuario
        try{
            //tela principal


            //cadastro de novo usuario. Esse cadastro só acontece se o usuario não existir.
            System.out.println("Olá, bem vindo ao FoodWise! \n------\nDigite seu nome de usuário:");
            String username = scan.nextLine();


            System.out.print("Digite a senha: ");
            String password = scan.nextLine();

            System.out.print("Digite o tipo de dieta (Ex: Vegano, Vegetariano, etc.): ");
            String dietType = scan.nextLine();

            db.createUser(username, password, dietType);
        }
        catch (SQLException e){
            System.out.println("Erro ao criar novo usuario " + e.getMessage());
        }
        finally{
            //db.closeConnection();
        }
        //verifica autenticação do usuario
        System.out.println("Digite seu nome de usuário:");
        String username = scan.nextLine();

        System.out.print("Digite a senha: ");
        String password = scan.nextLine();

        // Verificar se o usuário é autenticado
        try {
            boolean isAuthenticated = db.authenticateUser(username, password);

            if (!isAuthenticated) {
                System.out.println("Credenciais inválidas!");
            } else {
                // Recuperar o userId do usuário autenticado
                int userId = db.getUserId(username, password);
                System.out.println("Usuário autenticado com userId: " + userId);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao autenticar o usuário: " + e.getMessage());
        }


        //verificação de itens vencidos
        try {
            db.checkExpiringItems();
        } catch (SQLException e){
            System.out.println("Erro ao verificar itens: " + e.getMessage());
        }
        finally {
            //db.closeConnection();
        }

        //criar comidas e adicionar ou exibir lista de comida
        int userId = db.getUserId(username, password);
        if(userId !=-1){
            try{
                System.out.println("Pressione 1 para -adicionar alimento a despensa. \nPressione 2 - para ver lista de aliemntos:");
                String opcao = scan.nextLine();
                if(Objects.equals(opcao, "1")){
                    System.out.println("Qual comida/ ingrediente deseja adicionar à sua despensa? Insira o nome: ");
                    String nomecomida= scan.nextLine();

                    System.out.println("Qual a quantidade?");
                    String quantidade= scan.nextLine();

                    System.out.println("Quando vai vencer? Insira a data no formato ano-mes-dia");
                    String datavalidade= scan.nextLine();

                    db.createFood(nomecomida, Integer.parseInt(quantidade), datavalidade, userId);}
                else{
                    db.listaComidas(userId);
                }
            }
            catch(SQLException e) {
                System.out.println("Erro ao criar comida: " + e.getMessage());
            }
            finally {
                //db.closeConnection();
            }


        //criar receitas e adicionar ao banco ou exibir receitas
        try{
            System.out.println("Pressione 1 para -adicionar RECEITA a despensa. \nPressione 2 - para ver lista de RECEITAS:");
            String opcaoESCOLHIDA = scan.nextLine();
            if(Objects.equals(opcaoESCOLHIDA, "1")){
                System.out.println("Qual RECEITA deseja adicionar? Insira o nome: ");
                String nomeReceita= scan.nextLine();

                System.out.println("Quais os ingredientes?");
                String ingredientes= scan.nextLine();

                System.out.println("Insira as instruções:");
                String instrucoes = scan.nextLine();

                db.createRecipe(nomeReceita, ingredientes, instrucoes, userId);}
            else{
                //db.listaReceitas();
            }
        }
        catch(SQLException e) {
            System.out.println("Erro ao criar comida: " + e.getMessage());
        }
        finally {
            //db.closeConnection();
        }
    }
}}