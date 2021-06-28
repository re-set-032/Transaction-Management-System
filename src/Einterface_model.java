package sample;

import java.sql.Connection;

public class Einterface_model {
    private Connection connection;

    Einterface_model()
    {
        connection = SQLdatabase.connector("jdbc:mysql://localhost/accounts_database");
        if(connection == null)
            System.exit(1);
    }

    public Connection getConnection() {
        return connection;
    }

}
