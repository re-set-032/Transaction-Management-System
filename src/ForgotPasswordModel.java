package sample;

import java.sql.*;

public class ForgotPasswordModel {

    private Connection connection;
    public ForgotPasswordModel()
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

    public boolean searchAccount(String username,String securityQuestion,String securityAnswer,int mod)
    {
        try
        {
            String query = null;
            if(mod == 0)
                query = "select * from customer_account where username = ? and security_question = ? and security_answer = ?";
            else if(mod == 1)
                query = "select * from employee_account where username = ? and security_question = ? and security_answer = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,username);
            statement.setString(2,securityQuestion);
            statement.setString(3,Cryptography.generateHash(securityAnswer.getBytes(),"SHA-512").toLowerCase());
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        }
        catch (SQLException e)
        {
            System.out.println("SQL error in searchAccount");
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePassword(String username, String newPassword,int mod)
    {
        try
        {
            String query = null;
            if(mod == 0)
                query = "update customer_account set password = ? where username = ?";
            else if(mod == 1)
                query = "update employee_account set password = ? where username = ?";
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             preparedStatement.setString(2,username);
             preparedStatement.setString(1,Cryptography.generateHash(newPassword.getBytes(),"SHA-512").toLowerCase());
             int rowAffected = preparedStatement.executeUpdate();
             System.out.println("row = "+rowAffected);
             return true;
        }
        catch (SQLException e)
        {
            System.out.println("update password error");
            e.printStackTrace();
            return false;
        }
    }

    public Connection getConnection() { return connection; }


}
