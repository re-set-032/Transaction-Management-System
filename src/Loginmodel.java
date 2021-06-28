package sample;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Loginmodel {
    private Connection connection;

    public Loginmodel()
    {
        connection = SQLdatabase.connector("jdbc:mysql://localhost/login_database");
        if(connection == null)
        {
            System.exit(1);
        }
    }

    public boolean isDbConnected()
    {
        try
        {
            return !connection.isClosed();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    // Link login to employee database later ---

    public boolean islogin(String user,String pass,int Mod) throws SQLException,NullPointerException
    {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        boolean valid = false;
        try
        {
            if(Mod == 0)
            {
                String query = "select * from customer_account where username = ? and password = ?";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1,user);
                preparedStatement.setString(2,Cryptography.generateHash(pass.getBytes(),"SHA-512").toLowerCase());

                resultSet = preparedStatement.executeQuery();
                if(resultSet.next())
                    valid =  true;
            }
            else if(Mod == 1)
            {
                String query = "select * from employee_account where username = ? and password = ?";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1,user);
                preparedStatement.setString(2,Cryptography.generateHash(pass.getBytes(),"SHA-512").toLowerCase());

                resultSet = preparedStatement.executeQuery();
                if(resultSet.next())
                    valid = true;
            }
        }
        catch (Exception e)
        {
            System.out.println("Error in fetching data");
            e.printStackTrace();
            return false;
        }
        finally {
            preparedStatement.close();
            resultSet.close();
            return valid;
        }
    }

    public Connection getConnection() {
        return connection;
    }



}
