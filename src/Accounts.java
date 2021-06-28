package sample;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.Date;

abstract public class Accounts {
    private Connection connection;
    private String DOC;
    private String TOC;
    private String DOM;
    private int Customer_id;
    private double balance;
    private String bankAccountNo;
    private String pinNumber;

    Accounts(double bal) { this.balance = bal; }
    Accounts(int customer_id,String bankAccountNo,String pinNumber,double bal,String DOC,String TOC,String DOM)
    {
        connection = SQLdatabase.connector("jdbc:mysql://localhost/accounts_database");
        this.DOM = DOM;
        this.balance = bal;
        this.Customer_id = customer_id;
        this.pinNumber = pinNumber;
        this.bankAccountNo = bankAccountNo;
        this.DOC = DOC;
        this.TOC = TOC;
    }

    public boolean deposit(double amountDeposited) { balance += amountDeposited; return true; }

    public boolean withdraw(double amountWithdrawn) {
        if(balance >= amountWithdrawn) {
            balance -= amountWithdrawn;
        }
        else
        {
            // Alert throw
            return false;
        }
        return true;
    }

    public String getDOM() { return DOM; }
    public String getDOC() { return DOC; }
    public String getTOC() { return TOC; }
    public int getCustomer_id() { return Customer_id; }
    public double balanceEnquiry(){return balance;}
    public void setBalance(double balance){this.balance = balance;}
    public String getBankAccountNo() { return bankAccountNo; }
    public String getPinNumber() { return pinNumber; }
    public Connection getConnection() { return connection; }

}

class checkingAccount extends Accounts{
    private double creditLimit = 500d;
    checkingAccount(int customer_id,String acc_no,String pin,double balance,String DOC,String TOC,String DOM) { super(customer_id,acc_no,pin,balance,DOC,TOC,DOM); }

    @Override
    public boolean withdraw(double amountWithdrawn)
    {
        if(balanceEnquiry() + creditLimit >= amountWithdrawn) {
            setBalance(balanceEnquiry() - amountWithdrawn);
            // update database --------------------------------------------------------------------
            updateDatabase();
        }
        else
        {
            return false;
            // Alert throw
        }
        return true;
    }

    @Override
    public boolean deposit(double amountDeposited)
    {
        boolean res = super.deposit(amountDeposited);
        if(res)
        {
            updateDatabase();
            return true;
        }
        return false;
    }

    public void updateDatabase()
    {
        try {
            String query = "update checking_account set balance = ? where account_number = ?";
            PreparedStatement preparedStatement = this.getConnection().prepareStatement(query);
            preparedStatement.setString(1,String.valueOf(balanceEnquiry()));
            preparedStatement.setString(2,this.getBankAccountNo());

            int rowAffected = preparedStatement.executeUpdate();
            System.out.println("Row affected = "+rowAffected);
        }
        catch (SQLException e)
        {
            System.out.println("SQL error in checking account");
            e.printStackTrace();
        }
    }
}

class savingAccount extends Accounts{
    private double interestRate = 0.05;
    savingAccount(int customer_id,String acc_no,String pin,double balance,String DOC,String TOC,String DOM)
    {
        super(customer_id,acc_no,pin,balance,DOC,TOC,DOM);
        checkForInterestUpdate();
    }

    @Override
    public boolean withdraw(double amount)
    {
        boolean res = super.withdraw(amount);
        if(res)
        {
            // update database --------------------------------------------------------------------
            updateDatabase();
            return true;
        }
        return false;
    }

    @Override
    public boolean deposit(double amountDeposited)
    {
        boolean res = super.deposit(amountDeposited);
        if(res)
        {
            updateDatabase();
            return true;
        }
        return false;
    }

    public void updateDatabase()
    {
        try {
            String query = "update saving_account set balance = ? where account_number = ?";
            PreparedStatement preparedStatement = this.getConnection().prepareStatement(query);
            preparedStatement.setString(1,String.valueOf(balanceEnquiry()));
            preparedStatement.setString(2,this.getBankAccountNo());

            int rowAffected = preparedStatement.executeUpdate();
            System.out.println("Row affected = "+rowAffected);
        }
        catch (SQLException e)
        {
            System.out.println("SQL error in saving account (default)");
            e.printStackTrace();
        }
    }

    public void updateDOM()
    {
        try {
            Date date = new Date();
            String[] currentDate = date.toString().trim().split(" ");
            String query = "update saving_account set DOM = ? where account_number = ?";

            PreparedStatement preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setString(1,currentDate[2]+"-"+currentDate[1]+"-"+currentDate[5]);
            preparedStatement.setString(2,this.getBankAccountNo());
            int rowAffected = preparedStatement.executeUpdate();
            System.out.println("Row affected = "+rowAffected);
        }
        catch (SQLException e)
        {
            System.out.println("SQL Error in updateDOM");
            e.printStackTrace();
        }
    }

    public void increaseBalance() { setBalance(balanceEnquiry() + interestRate*balanceEnquiry()); updateDatabase();}
    public void checkForInterestUpdate()
    {
        // update check
        System.out.println("Checking Interest Update");
        String[] date = getDOM().trim().split("-");
        Month month = Month.JANUARY;
        switch (date[1])
        {
            case "Jan":
                month = Month.JANUARY;
                break;
            case "Feb":
                month = Month.FEBRUARY;
                break;
            case "Mar":
                month = Month.MARCH;
                break;
            case "Apr":
                month = Month.APRIL;
                break;
            case "Jun":
                month = Month.JUNE;
                break;
            case "Jul":
                month = Month.JULY;
                break;
            case "Aug":
                month = Month.AUGUST;
                break;
            case "Sep":
                month = Month.SEPTEMBER;
                break;
            case "Oct":
                month = Month.OCTOBER;
                break;
            case "Nov":
                month = Month.NOVEMBER;
                break;
            case "Dec":
                month = Month.DECEMBER;
                break;
        }

        LocalDate lastModificationDate = LocalDate.of(Integer.parseInt(date[2]),month,Integer.parseInt(date[0]));
        LocalDate currentDate = LocalDate.now();

        long days = ChronoUnit.DAYS.between(lastModificationDate,currentDate);
        System.out.println("sdays = "+days);
        if(days >= 30)
        {
            long count = days/30;
            while(count!=0)
            {
                increaseBalance();
                count--;
            }
            this.updateDatabase();
            this.updateDOM();
        }

    }
}

class loanAccount extends Accounts{

    private double principalAmount;
    private double interestRate = 0.08;
    private double loanDuration;

    loanAccount(int customer_id,String acc_no,String pin,double principalAmount,int duration,String DOC,String TOC,String DOM)
    {
        super(customer_id,acc_no,pin,0,DOC,TOC,DOM);
        this.principalAmount = principalAmount;
        this.loanDuration = duration;
        checkForOutstndingIncrement();
    }

    public void loanDeposit(double amountDeposited)
    {
        principalAmount-=amountDeposited;
        // update database
        updateDatabase();
    }

    public void updateDatabase()
    {
        try {
            String query = "update loan_account set loanOutstanding = ? where account_number = ?";
            PreparedStatement preparedStatement = this.getConnection().prepareStatement(query);
            preparedStatement.setString(1,String.valueOf(this.getPrincipalAmount()));
            preparedStatement.setString(2,this.getBankAccountNo());

            int rowAffected = preparedStatement.executeUpdate();
            System.out.println("Row affected = "+rowAffected);
        }
        catch (SQLException e)
        {
            System.out.println("SQL error in loan account");
            e.printStackTrace();
        }
    }

    public void updateDOM()
    {
        try {
            Date date = new Date();
            String[] currentDate = date.toString().trim().split(" ");
            String query = "update loan_account set DOM = ? where account_number = ?";

            PreparedStatement preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setString(1,currentDate[2]+"-"+currentDate[1]+"-"+currentDate[5]);
            preparedStatement.setString(2,this.getBankAccountNo());
            int rowAffected = preparedStatement.executeUpdate();
            System.out.println("Row affected = "+rowAffected);
        }
        catch (SQLException e)
        {
            System.out.println("SQL Error in updateDOM");
            e.printStackTrace();
        }
    }

    public void checkForOutstndingIncrement()
    {
        System.out.println("Checking Outstanding Update");
        String[] date = getDOM().trim().split("-");
        System.out.println(getDOM());
        Month month = Month.JANUARY;

        switch (date[1])
        {
            case "Jan":
                month = Month.JANUARY;
                break;
            case "Feb":
                month = Month.FEBRUARY;
                break;
            case "Mar":
                month = Month.MARCH;
                break;
            case "Apr":
                month = Month.APRIL;
                break;
            case "Jun":
                month = Month.JUNE;
                break;
            case "Jul":
                month = Month.JULY;
                break;
            case "Aug":
                month = Month.AUGUST;
                break;
            case "Sep":
                month = Month.SEPTEMBER;
                break;
            case "Oct":
                month = Month.OCTOBER;
                break;
            case "Nov":
                month = Month.NOVEMBER;
                break;
            case "Dec":
                month = Month.DECEMBER;
                break;
        }

        LocalDate lastModificationDate = LocalDate.of(Integer.parseInt(date[2]),month,Integer.parseInt(date[0]));
        LocalDate currentDate = LocalDate.now();

        long days = ChronoUnit.DAYS.between(lastModificationDate,currentDate);
        System.out.println("ldays = "+days);

        if(days >= 30)
        {
            long count = days/30;
            while(count!=0)
            {
                increaseOutstanding();
                count--;
            }
            this.updateDatabase();
            this.updateDOM();
        }
    }

    public void increaseOutstanding() { setPrincipalAmount(getPrincipalAmount()+interestRate*getPrincipalAmount()); }
    public void setPrincipalAmount(double principalAmount) { this.principalAmount = principalAmount; }
    public double getPrincipalAmount() { return principalAmount; }
}

