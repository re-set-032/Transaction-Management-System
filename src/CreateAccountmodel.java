package sample;

import java.sql.*;

public class CreateAccountmodel {

    private Connection connection;
    public CreateAccountmodel()
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

    //<-----------------For Customers------------------->

    public void createAccount(String first_name,String middle_name,String last_name,String username,String password,String DOB_day,String DOB_month,String DOB_year,String Gender,String email,String contact_no,String security_question,String security_answer) throws SQLException,NullPointerException
    {

        String query = "insert into customer_account(first_name,middle_name,last_name,username,password,DOB_day,DOB_month,DOB_year,Gender,email,contact_no,security_question,security_answer) values ('"+first_name+"','"+middle_name+"','"+last_name+"','"+username+"','"+Cryptography.generateHash(password.getBytes(),"SHA-512").toLowerCase()+"',"+DOB_day+","+DOB_month+","+DOB_year+",'"+Gender+"','"+email+"',"+contact_no+",'"+security_question+"','"+Cryptography.generateHash(security_answer.getBytes(),"SHA-512").toLowerCase()+"')";
        try
        {
            SQLdatabase.executeQuery(connection,query);
        }
        catch (Exception e)
        {
            System.out.println("Error in create Account");
            e.printStackTrace();
        }

    }


    //<----------------For Employees------------->

    public void createAccount(String EmployeeID,String first_name,String middle_name,String last_name,String username,String password,String DOB_day,String DOB_month,String DOB_year,String Gender,String email,String contact_no,String security_question,String security_answer) throws SQLException,NullPointerException
    {

        String query = "insert into employee_account(Employee_id,first_name,middle_name,last_name,username,password,DOB_day,DOB_month,DOB_year,Gender,email,contact_no,security_question,security_answer) values ("+EmployeeID+",'"+first_name+"','"+middle_name+"','"+last_name+"','"+username+"','"+Cryptography.generateHash(password.getBytes(),"SHA-512").toLowerCase()+"',"+DOB_day+","+DOB_month+","+DOB_year+",'"+Gender+"','"+email+"',"+contact_no+",'"+security_question+"','"+Cryptography.generateHash(security_answer.getBytes(),"SHA-512").toLowerCase()+"')";
        try
        {
            SQLdatabase.executeQuery(connection,query);
        }
        catch (Exception e)
        {
            System.out.println("Error in create Account");
            e.printStackTrace();
        }

    }

}
