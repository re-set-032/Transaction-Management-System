package sample;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Cinterfacemodel
{
    private Connection connection;

    List<Accounts> checkingAccountList;
    List<Accounts> savingAccountList;
    List<Accounts> loanAccountList;

    Cinterfacemodel()
    {
        connection = SQLdatabase.connector("jdbc:mysql://localhost/accounts_database");
        if(connection == null)
            System.exit(1);
        bringDatabase();
    }

    public List<Accounts> getCheckingAccountList() { return checkingAccountList; }
    public List<Accounts> getSavingAccountList() { return savingAccountList; }
    public List<Accounts> getLoanAccountList() { return loanAccountList; }

    private void bringDatabase()
    {
        checkingAccountList = new ArrayList<>();
        savingAccountList = new ArrayList<>();
        loanAccountList = new ArrayList<>();

        try
        {
            String query1 = "select * from checking_account";
            PreparedStatement statement1 = connection.prepareStatement(query1);
            ResultSet resultSet1 = statement1.executeQuery();
            while(resultSet1.next())
            {
                // under dev
                System.out.println("inside c");
                Accounts Caccount= new checkingAccount(resultSet1.getInt(1),resultSet1.getString(2),resultSet1.getString(3),resultSet1.getFloat(4),resultSet1.getString(5),resultSet1.getString(6),resultSet1.getString(7));
                checkingAccountList.add(Caccount);
            }

            String query2 = "select * from saving_account";
            PreparedStatement statement2 = connection.prepareStatement(query2);
            ResultSet resultSet2 = statement2.executeQuery();
            while(resultSet2.next())
            {
                // under dev
                System.out.println("inside s");
                Accounts Saccount= new savingAccount(resultSet2.getInt(1),resultSet2.getString(2),resultSet2.getString(3),resultSet2.getFloat(4),resultSet2.getString(5),resultSet2.getString(6),resultSet2.getString(7));
                savingAccountList.add(Saccount);
            }

            String query3 = "select * from loan_account";
            PreparedStatement statement3 = connection.prepareStatement(query3);
            ResultSet resultSet3 = statement3.executeQuery();
            while(resultSet3.next())
            {
                // under dev
                Accounts Laccount= new loanAccount(resultSet3.getInt(1),resultSet3.getString(2),resultSet3.getString(3),resultSet3.getFloat(4),resultSet3.getInt(5),resultSet3.getString(6),resultSet3.getString(7),resultSet3.getString(8));
                loanAccountList.add(Laccount);
            }

            resultSet1.close();
            statement1.close();
            resultSet2.close();
            statement2.close();
            resultSet3.close();
            statement3.close();
        }
        catch (SQLException e)
        {
            System.out.println("SQL error in bringDatabase");
            e.printStackTrace();
        }
    }

    public void createLoanAccount(int customerID,double loanAmount,double loanDuration)
    {
         try
        {
            Random r = new Random();
            String acc_no = "12345678"+String.valueOf(r.nextInt(89999998)+10000000);
            String pin = String.valueOf(r.nextInt(8998)+1000);
            String[] currentDate = new Date().toString().trim().split(" ");
            String DOC = currentDate[2]+"-"+currentDate[1]+"-"+currentDate[5];
            String TOC = currentDate[3];

            String query = "insert into loan_account(Customer_id,account_number,pin,loanOutstanding,duration,DOC,TOC,DOM) values(?,?,?,?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,String.valueOf(customerID));
            preparedStatement.setString(2,acc_no);
            preparedStatement.setString(3,pin);
            preparedStatement.setString(4,String.valueOf(loanAmount));
            preparedStatement.setString(5,String.valueOf(loanDuration));
            preparedStatement.setString(6,DOC);
            preparedStatement.setString(7,TOC);
            preparedStatement.setString(8,DOC);

            int rowAffected = preparedStatement.executeUpdate();
            System.out.println("Affected row = "+rowAffected);

            Alert alert = new Alert(Alert.AlertType.INFORMATION,"Account_no = "+acc_no+" , pin = "+pin,ButtonType.OK);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setHeaderText("Loan Account Details");
            alert.setTitle("Account Created");
            alert.showAndWait();

            this.bringDatabase();
        }
        catch (SQLException e)
        {
            System.out.println("error in creating loan account");
            e.printStackTrace();
        }
    }

    public void createSavingAccount(int customerID,double balance)
    {
        try
        {
            Random r = new Random();
            String acc_no = "12345678"+String.valueOf(r.nextInt(89999998)+10000000);
            String pin = String.valueOf(r.nextInt(8998)+1000);
            String[] currentDate = new Date().toString().trim().split(" ");
            String DOC = currentDate[2]+"-"+currentDate[1]+"-"+currentDate[5];
            String TOC = currentDate[3];

            String query = "insert into saving_account(Customer_id,account_number,pin,balance,DOC,TOC,DOM) values(?,?,?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,String.valueOf(customerID));
            preparedStatement.setString(2,acc_no);
            preparedStatement.setString(3,pin);
            preparedStatement.setString(4,String.valueOf(balance));
            preparedStatement.setString(5,DOC);
            preparedStatement.setString(6,TOC);
            preparedStatement.setString(7,DOC);

            int rowAffected = preparedStatement.executeUpdate();
            System.out.println("Affected row = "+rowAffected);

            Alert alert = new Alert(Alert.AlertType.INFORMATION,"Account_no = "+acc_no+" , pin = "+pin,ButtonType.OK);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setHeaderText("Saving Account Details");
            alert.setTitle("Account Created");
            alert.showAndWait();

            this.bringDatabase();

        }
        catch (SQLException e)
        {
            System.out.println("error in creating saving account");
            e.printStackTrace();
        }
    }

    public void createCheckingAccount(int customerID,double balance)
    {
        try
        {
            Random r = new Random();
            String acc_no = "12345678"+ (r.nextInt(89999998) + 10000000);
            String pin = String.valueOf(r.nextInt(8998)+1000);
            String[] currentDate = new Date().toString().trim().split(" ");
            String DOC = currentDate[2]+"-"+currentDate[1]+"-"+currentDate[5];
            String TOC = currentDate[3];

            String query = "insert into checking_account(Customer_id,account_number,pin,balance,DOC,TOC,DOM) values(?,?,?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,String.valueOf(customerID));
            preparedStatement.setString(2,acc_no);
            preparedStatement.setString(3,pin);
            preparedStatement.setString(4,String.valueOf(balance));
            preparedStatement.setString(5,DOC);
            preparedStatement.setString(6,TOC);
            preparedStatement.setString(7,DOC);

            int rowAffected = preparedStatement.executeUpdate();
            System.out.println("Affected row = "+rowAffected);

            Alert alert = new Alert(Alert.AlertType.INFORMATION,"Account_no = "+acc_no+" , pin = "+pin,ButtonType.OK);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setHeaderText("Checking Account Details");
            alert.setTitle("Account Created");
            alert.showAndWait();

            this.bringDatabase();
        }
        catch (SQLException e)
        {
            System.out.println("error in creating checking account");
            e.printStackTrace();
        }
    }

    public boolean withdraw(String acc_no,double amountToWithdraw)
    {
        for(Accounts saving: savingAccountList)
        {
            if(saving.getBankAccountNo().equals(acc_no))
                return saving.withdraw(amountToWithdraw);
        }

        for(Accounts checking: checkingAccountList)
        {
            if(checking.getBankAccountNo().equals(acc_no))
                return checking.withdraw(amountToWithdraw);
        }

        return false;
    }

    public boolean deposit(String acc_no, double depositAmount)
    {
        for(Accounts saving: savingAccountList)
        {
            if(saving.getBankAccountNo().equals(acc_no))
                return saving.deposit(depositAmount);
        }

        for(Accounts checking: checkingAccountList)
        {
            if(checking.getBankAccountNo().equals(acc_no))
                return checking.deposit(depositAmount);
        }

        return false;
    }

    public boolean transfer(String from_acc_no,String to_acc_no,double amount)
    {
        Accounts from = null;
        Accounts to = null;

        for(Accounts saving: savingAccountList)
        {
            if(saving.getBankAccountNo().equals(from_acc_no))
                from = saving;
            if(saving.getBankAccountNo().equals(to_acc_no))
                to = saving;
        }

        for(Accounts checking: checkingAccountList)
        {
            if(checking.getBankAccountNo().equals(from_acc_no))
                from = checking;
            if(checking.getBankAccountNo().equals(to_acc_no))
                to = checking;
        }

        if(from == null || to == null)
            return false;

        if(from.balanceEnquiry() < amount)
        {
            Alert.AlertType alertAlertType = AlertType.WARNING;
            Alert alert = new Alert(alertAlertType,"Insufficient funds available",ButtonType.OK);
            alert.setTitle("WARNING !!");
            alert.setHeaderText("Account warning..");
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.showAndWait();
            return true;
        }

        from.withdraw(amount);
        to.deposit(amount);

        Alert alert = new Alert(Alert.AlertType.INFORMATION,"Transfer successful",ButtonType.OK);
        alert.setTitle("INFORMATION !!");
        alert.setHeaderText(" Funds Transfer Information ");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();

        return true;
    }

    public boolean balanceEnquiry(String acc_no)
    {
        for(Accounts saving: savingAccountList)
        {
            if(saving.getBankAccountNo().equals(acc_no)) {
                Alert alert = new Alert(AlertType.INFORMATION, "Balance = " + saving.balanceEnquiry(), ButtonType.OK);
                alert.setTitle("INFORMATION !!");
                alert.setHeaderText("Balance Report..");
                alert.showAndWait();
                return true;
            }
        }
        for(Accounts checking: checkingAccountList)
        {
            if(checking.getBankAccountNo().equals(acc_no)) {
                Alert alert = new Alert(AlertType.INFORMATION,"Balance = "+checking.balanceEnquiry(),ButtonType.OK);
                alert.setTitle("INFORMATION !!");
                alert.setHeaderText("Balance Report..");
                alert.showAndWait();
                return true;
            }
        }
        return false;
    }

    public boolean loanDeposit(String acc_no,double amountDeposited)
    {
        for(Accounts loan: loanAccountList)
        {
            if(loan.getBankAccountNo().equals(acc_no.trim()) && loan instanceof loanAccount) {
                ((loanAccount) loan).loanDeposit(amountDeposited);
                Alert alert = new Alert(Alert.AlertType.INFORMATION,"Loan Deposit successful",ButtonType.OK);
                alert.setTitle("INFORMATION !!");
                alert.setHeaderText(" Loan Deposit !! ");
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.showAndWait();
                return true;
            }
        }
        return false;
    }

    public Connection getConnection() { return connection; }
}
