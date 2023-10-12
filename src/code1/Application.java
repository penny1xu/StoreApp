package code1;

import code1.dao.DataAdapter;
import code1.model.User;
import code1.view.LoginScreen;
import code1.view.MainScreen;
import code1.view.OrderView;
import code1.view.ProductView;

import java.sql.*;

public class Application {

    private static Application instance;   // Singleton pattern

    public static Application getInstance() {
        if (instance == null) {
            instance = new Application();
        }
        return instance;
    }
    // Main components of this application

    private Connection connection;

    public Connection getDBConnection() {
        return connection;
    }

    private DataAdapter dataAdapter;

    private User currentUser = null;

    public User getCurrentUser() { return currentUser; }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    private ProductView productView;

    private OrderView orderView;

    private MainScreen mainScreen = new MainScreen();

    public MainScreen getMainScreen() {
        mainScreen.loadUserInfo();
        return mainScreen;
    }

    public ProductView getProductView() {
        return productView;
    }

    public OrderView getOrderView() {
        return orderView;
    }

    public LoginScreen loginScreen;

    public LoginScreen getLoginScreen() {
        return loginScreen;
    }

    //public LoginController loginController;


    public DataAdapter getDataAdapter() {
        return dataAdapter;
    }


    private Application() {
        // create SQLite database connection here!
        try {
            Class.forName("org.sqlite.JDBC");

            String url = "jdbc:sqlite:store.db";

            connection = DriverManager.getConnection(url);
            dataAdapter = new DataAdapter(connection);

        }
        catch (ClassNotFoundException ex) {
            System.out.println("SQLite is not installed. System exits with error!");
            ex.printStackTrace();
            System.exit(1);
        }

        catch (SQLException ex) {
            System.out.println("SQLite database is not ready. System exits with error!" + ex.getMessage());

            System.exit(2);
        }

        //loginController = new LoginController(loginScreen);
        loginScreen = new LoginScreen();
        orderView = new OrderView();
        productView = new ProductView();
    }


    public static void main(String[] args) {
        Application.getInstance().getLoginScreen().setVisible(true);
    }
}
