package sample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLdatabase {

    public static Connection connector(String location)
    {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(location,"root","Wildcat_117");
            return connection;
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("Class Not found");
            return null;
        }
        catch (SQLException e)
        {
            System.out.println("SQL error");
            e.printStackTrace();
            return null;
        }
    }

    public static void executeQuery(Connection connection, String query)
    {
        Statement stmt = null;
        try
        {
            stmt = connection.createStatement();
            stmt.executeUpdate(query);

            if(stmt!=null)
                stmt.close();

        }
        catch (SQLException e)
        {
            System.out.println("SQLError in query execution");
            e.printStackTrace();
        }
        catch (Exception e)
        {
            System.out.println("Other Error in query execution");
            e.printStackTrace();
        }
    }


}
