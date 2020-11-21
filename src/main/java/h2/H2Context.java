package h2;

import lombok.Getter;
import lombok.Setter;
import org.h2.tools.Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class H2Context {

    // Singleton
    private static H2Context h2Instance;

    public static final String JDBC_DRIVER = "org.h2.Driver";
    public static final String DB_URL = "jdbc:h2:tcp://localhost/~/test";

    private static final String USERNAME = "sa";
    private static final String PASS = "";

    @Getter
    @Setter
    private Connection connection;

    @Getter
    @Setter
    private Statement statement;

    private H2Context() throws SQLException, ClassNotFoundException {
        Server h2Server = Server.createTcpServer("-tcpAllowOthers").start();
        Class.forName(JDBC_DRIVER);

        connection = DriverManager.getConnection(DB_URL, PASS, PASS);
        statement = connection.createStatement();
    }

    public void closeConnection() {
        try {
            connection.close();
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static H2Context getInstance() throws SQLException, ClassNotFoundException {
        if(h2Instance == null) {
            h2Instance = new H2Context();
        }

        return h2Instance;
    }

}
