package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class Cinterface_Controller implements Initializable {
    private Loginmodel loginmodel = new Loginmodel();
    private Cinterfacemodel customerInterfaceModel = new Cinterfacemodel();
    List<accountTableElement> rowList;
    List<transactionElement> transactionElementList;

    private final String[] bankAccountTypes = {"none","Saving","Checking","Loan"};

    // Dashboard --------------------------------------------------
    @FXML
    private JFXTextField emailField;

    @FXML
    private JFXTextField contactField;

    @FXML
    private Label genderLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Label DOBLabel;

    @FXML
    private Label customerIDLabel;

    @FXML
    private JFXButton editDetails;

    @FXML
    private JFXButton saveDetails;

    // Transaction History ------------------------------------------
    @FXML
    private TableView<transactionElement> transactionsTable;

    @FXML
    private TableColumn<Object,Object> S_no;

    @FXML
    private TableColumn<Object,Object> from_Acc;

    @FXML
    private TableColumn<Object,Object> Ttype;

    @FXML
    private TableColumn<Object,Object> to_Acc;

    @FXML
    private TableColumn<Object,Object> tAmount;

    @FXML
    private TableColumn<Object,Object> Tdate;

    @FXML
    private TableColumn<Object,Object> Ttime;

    // Linked Accounts ----------------------------------------------
    @FXML
    private TableView<accountTableElement> accountTable;

    @FXML
    private TableColumn<Object, Object> s_No;

    @FXML
    private TableColumn<Object, Object> acc_no;

    @FXML
    private TableColumn<Object, Object> type;

    @FXML
    private TableColumn<Object, Object> balance;

    @FXML
    private TableColumn<Object, Object> doc;

    @FXML
    private TableColumn<Object, Object> toc;

    @FXML
    private TableColumn<Object, Object> loanOutstanding;

    // Banking Operations -----------------------------------------------
    // Loan Deposit
    @FXML
    private JFXTextField loanRepayAccountNumber;

    @FXML
    private JFXTextField loanRepayAmountField;

    @FXML
    private JFXButton loanPayButton;

    // Balance Enquiry
    @FXML
    private Label balanceLabel;

    @FXML
    private JFXTextField BaccountNumber;

    @FXML
    private JFXButton checkBalance;

    // Transfer funds
    @FXML
    private JFXTextField TfromAccountNumber;

    @FXML
    private JFXTextField TtoAccountNumber;

    @FXML
    private JFXTextField Tamount;

    @FXML
    private JFXButton makeTransfer;

    @FXML
    private JFXTextField tPin;

    //Deposit funds
    @FXML
    private JFXTextField DaccountNumber;

    @FXML
    private JFXTextField Damount;

    @FXML
    private JFXButton depositFunds;

    //Withdraw Funds
    @FXML
    private JFXTextField WaccountNumber;

    @FXML
    private JFXTextField Wamount;

    @FXML
    private JFXButton withdrawMoney;

    @FXML
    private JFXTextField wPin;

    //Create Bank Account
    @FXML
    private JFXTextField CstartingBalance;

    @FXML
    private Label CstartingBalanceLabel;

    @FXML
    private ComboBox<String> accountType;

    @FXML
    private Label loanAmountLabel;

    @FXML
    private Label loanDurationLabel;

    @FXML
    private JFXTextField loanAmountField;

    @FXML
    private JFXTextField loanDurationField;

    @FXML
    private JFXButton createBankAccount;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        emailField.setEditable(false);
        emailField.setCursor(Cursor.DEFAULT);
        contactField.setEditable(false);
        contactField.setCursor(Cursor.DEFAULT);

        // Account table
        s_No.setCellValueFactory(new PropertyValueFactory<>("s_no"));
        acc_no.setCellValueFactory(new PropertyValueFactory<>("acc_no"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        balance.setCellValueFactory(new PropertyValueFactory<>("balance"));
        doc.setCellValueFactory(new PropertyValueFactory<>("DOC"));
        toc.setCellValueFactory(new PropertyValueFactory<>("TOC"));
        loanOutstanding.setCellValueFactory(new PropertyValueFactory<>("loanOutstanding"));

        // Transaction table
        S_no.setCellValueFactory(new PropertyValueFactory<>("s_no"));
        from_Acc.setCellValueFactory(new PropertyValueFactory<>("from_acc"));
        Ttype.setCellValueFactory(new PropertyValueFactory<>("type"));
        to_Acc.setCellValueFactory(new PropertyValueFactory<>("to_acc"));
        tAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        Tdate.setCellValueFactory(new PropertyValueFactory<>("date"));
        Ttime.setCellValueFactory(new PropertyValueFactory<>("time"));

        accountType.getItems().addAll(bankAccountTypes);
        accountType.setValue("none");
        loanAmountField.setVisible(false);
        loanAmountLabel.setVisible(false);
        loanDurationField.setVisible(false);
        loanDurationLabel.setVisible(false);

        createBankAccount.setDisable(true);
        createBankAccount.setOnAction(this::createBankAccount);
        editDetails.setOnAction(this::editOnAction);
        saveDetails.setOnAction(this::saveOnAction);
        accountType.setOnAction(this::comboBoxOnAction);
        withdrawMoney.setOnAction(this::withdrawFunds);
        depositFunds.setOnAction(this::depositFunds);
        checkBalance.setOnAction(this::balanceEnquiry);
        makeTransfer.setOnAction(this::transferFunds);
        loanPayButton.setOnAction(this::loanDeposit);
    }

    public void setInitials(int customerId,String Name)
    {
        this.customerIDLabel.setText(String.valueOf(customerId));
        this.nameLabel.setText(Name);
        setDashboard();
        updateAccountTable();
        updateTransactiontable();
    }

    public void setDashboard()
    {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            String query = "select * from customer_account where customer_id = ?";
            preparedStatement = loginmodel.getConnection().prepareStatement(query);
            preparedStatement.setString(1,customerIDLabel.getText());
            resultSet = preparedStatement.executeQuery();
            if(resultSet == null)
                throw new Exception("Result null");


            resultSet.next();
            genderLabel.setText(resultSet.getString(10));
            DOBLabel.setText(resultSet.getInt(7)+"/"+resultSet.getInt(8)+"/"+resultSet.getInt(9));
            emailField.setText(resultSet.getString(11));
            contactField.setText(String.valueOf(resultSet.getInt(12)));

            resultSet.close();
            preparedStatement.close();
        }
        catch (Exception e)
        {
            System.out.println("Dashboard error");
            System.out.println(e.getMessage());
        }
    }

    public void editOnAction(ActionEvent e)
    {
        emailField.setEditable(true);
        emailField.setCursor(Cursor.TEXT);
        contactField.setEditable(true);
        contactField.setCursor(Cursor.TEXT);
    }

    public void saveOnAction(ActionEvent e)
    {
        emailField.setEditable(false);
        contactField.setEditable(false);
        emailField.setCursor(Cursor.DEFAULT);
        contactField.setCursor(Cursor.DEFAULT);

        try {
            String query = "update customer_account set contact_no = ?,email = ? where Customer_id = ?";
            PreparedStatement preparedStatement = loginmodel.getConnection().prepareStatement(query);
            preparedStatement.setString(2,emailField.getText());
            preparedStatement.setString(1,contactField.getText());
            preparedStatement.setString(3,customerIDLabel.getText());

            int rowAffected = preparedStatement.executeUpdate();
            System.out.println("Row Affected = "+rowAffected);

            preparedStatement.close();
        }
        catch (SQLException E)
        {
            System.out.println("SQL error in employee saveValues");
            E.printStackTrace();
        }
    }

    public void createBankAccount(ActionEvent e)
    {
        if(accountType.getValue().equalsIgnoreCase("saving"))
        {
            customerInterfaceModel.createSavingAccount(Integer.parseInt(customerIDLabel.getText()),Double.parseDouble(CstartingBalance.getText()));
            System.out.println("Saving account created");
        }
        else if(accountType.getValue().equalsIgnoreCase("checking"))
        {
            customerInterfaceModel.createCheckingAccount(Integer.parseInt(customerIDLabel.getText()),Double.parseDouble(CstartingBalance.getText()));
            System.out.println("Checking account created");
        }
        else if(accountType.getValue().equalsIgnoreCase("loan"))
        {
            // In development
            customerInterfaceModel.createLoanAccount(Integer.parseInt(customerIDLabel.getText()),Double.parseDouble(loanAmountField.getText()),Double.parseDouble(loanDurationField.getText()));
            System.out.println("Loan Account created");
        }
        updateTransactiontable();
        updateAccountTable();
    }

    private void comboBoxOnAction(ActionEvent e)
    {
        if(accountType.getValue().equals("Loan"))
        {
            loanDurationLabel.setVisible(true);
            loanDurationField.setVisible(true);
            loanAmountLabel.setVisible(true);
            loanAmountField.setVisible(true);
            CstartingBalance.setVisible(false);
            CstartingBalanceLabel.setVisible(false);
        }
        else
        {
            CstartingBalance.setVisible(true);
            CstartingBalanceLabel.setVisible(true);
            loanDurationLabel.setVisible(false);
            loanDurationField.setVisible(false);
            loanAmountLabel.setVisible(false);
            loanAmountField.setVisible(false);
        }

        if(accountType.getValue().equals("none"))
            createBankAccount.setDisable(true);
        else
            createBankAccount.setDisable(false);
    }

    private void transferFunds(ActionEvent e)
    {
        boolean pinValidation = false;
        for(Accounts saving : customerInterfaceModel.getSavingAccountList())
        {
            if(saving.getBankAccountNo().equals(TfromAccountNumber.getText().trim()) && saving.getPinNumber().equals(tPin.getText().trim()))
                pinValidation = true;
        }
        for(Accounts checking : customerInterfaceModel.getCheckingAccountList())
        {
            if(checking.getBankAccountNo().equals(TfromAccountNumber.getText().trim()) && checking.getPinNumber().equals(tPin.getText().trim()))
                pinValidation = true;
        }

        if(!pinValidation)
        {
            Alert.AlertType alertAlertType = AlertType.WARNING;
            Alert alert = new Alert(alertAlertType,"Invalid PIN, try again",ButtonType.OK);
            alert.setTitle("PIN VALIDATION..");
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.showAndWait();
            return;
        }

        if(customerInterfaceModel.transfer(TfromAccountNumber.getText().trim(),TtoAccountNumber.getText().trim(),Double.parseDouble(Tamount.getText())))
        {
            try
            {
                 String[] currentDate = new Date().toString().trim().split(" ");
                 String date = currentDate[2]+"-"+currentDate[1]+"-"+currentDate[5];
                 String time = currentDate[3];
                 String query = "insert into transactions(Customer_id,from_account,ttype,to_account,amount,tdate,ttime) values(?,?,?,?,?,?,?)";
                 PreparedStatement preparedStatement = customerInterfaceModel.getConnection().prepareStatement(query);
                 preparedStatement.setString(1,customerIDLabel.getText());
                 preparedStatement.setString(2,TfromAccountNumber.getText().trim());
                 preparedStatement.setString(3,"Transfer");
                 preparedStatement.setString(4,TtoAccountNumber.getText().trim());
                 preparedStatement.setString(5,Tamount.getText());
                 preparedStatement.setString(6,date);
                 preparedStatement.setString(7,time);

                 int rowAffected = preparedStatement.executeUpdate();
                 System.out.println("Row Affected = "+rowAffected);
                 System.out.println("Transfer successful");
            }
            catch (SQLException E)
            {
                System.out.println("SQL Error in transferFunds");
                E.printStackTrace();
            }
        }
        else
        {
            Alert.AlertType alertAlertType = Alert.AlertType.WARNING;
            Alert alert = new Alert(alertAlertType,"Account unavailable",ButtonType.OK);
            alert.setTitle("WARNING !!");
            alert.setHeaderText("Account warning..");
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.showAndWait();
        }
        updateTransactiontable();
        updateAccountTable();
    }

    private void withdrawFunds(ActionEvent e)
    {
        boolean pinValidation = false;
        for(Accounts saving : customerInterfaceModel.getSavingAccountList())
        {
            if(saving.getBankAccountNo().equals(WaccountNumber.getText().trim()) && saving.getPinNumber().equals(wPin.getText().trim()))
                pinValidation = true;
        }
        for(Accounts checking : customerInterfaceModel.getCheckingAccountList())
        {
            if(checking.getBankAccountNo().equals(WaccountNumber.getText().trim()) && checking.getPinNumber().equals(wPin.getText().trim()))
                pinValidation = true;
        }

        if(!pinValidation)
        {
            Alert.AlertType alertAlertType = AlertType.WARNING;
            Alert alert = new Alert(alertAlertType,"Invalid PIN, try again",ButtonType.OK);
            alert.setTitle("PIN VALIDATION..");
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.showAndWait();
            return;
        }

        if(customerInterfaceModel.withdraw(WaccountNumber.getText(),Double.parseDouble(Wamount.getText())))
        {
            try
            {
                String[] currentDate = new Date().toString().trim().split(" ");
                String date = currentDate[2]+"-"+currentDate[1]+"-"+currentDate[5];
                String time = currentDate[3];
                String query = "insert into transactions(Customer_id,from_account,ttype,to_account,amount,tdate,ttime) values(?,?,?,?,?,?,?)";
                PreparedStatement preparedStatement = customerInterfaceModel.getConnection().prepareStatement(query);
                preparedStatement.setString(1,customerIDLabel.getText());
                preparedStatement.setString(2,WaccountNumber.getText().trim());
                preparedStatement.setString(3,"Withdraw");
                preparedStatement.setString(4,"null");
                preparedStatement.setString(5,Wamount.getText());
                preparedStatement.setString(6,date);
                preparedStatement.setString(7,time);

                int rowAffected = preparedStatement.executeUpdate();
                System.out.println("Row Affected = "+rowAffected);

                System.out.println("Withdraw successful");
                Alert alert = new Alert(AlertType.INFORMATION,"Withdraw successfully", ButtonType.OK);
                alert.setTitle("INFORMATION !!");
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.showAndWait();
            }
            catch (SQLException E)
            {
                System.out.println("SQL error in withdrawFunds");
                E.printStackTrace();
            }

        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION,"Withdraw failed", ButtonType.OK);
            alert.setTitle("INFORMATION !!");
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.showAndWait();
        }
        updateTransactiontable();
        updateAccountTable();
    }

    private void depositFunds(ActionEvent e)
    {
        if(customerInterfaceModel.deposit(DaccountNumber.getText(),Double.parseDouble(Damount.getText())))
        {
            try
            {
                String[] currentDate = new Date().toString().trim().split(" ");
                String date = currentDate[2]+"-"+currentDate[1]+"-"+currentDate[5];
                String time = currentDate[3];
                String query = "insert into transactions(Customer_id,from_account,ttype,to_account,amount,tdate,ttime) values(?,?,?,?,?,?,?)";
                PreparedStatement preparedStatement = customerInterfaceModel.getConnection().prepareStatement(query);
                preparedStatement.setString(1,customerIDLabel.getText());
                preparedStatement.setString(2,DaccountNumber.getText().trim());
                preparedStatement.setString(3,"Deposit");
                preparedStatement.setString(4,"null");
                preparedStatement.setString(5,Damount.getText());
                preparedStatement.setString(6,date);
                preparedStatement.setString(7,time);

                int rowAffected = preparedStatement.executeUpdate();
                System.out.println("Row Affected = "+rowAffected);

                System.out.println("Deposit successful");
                Alert alert = new Alert(Alert.AlertType.INFORMATION,"Deposit sucessfully", ButtonType.OK);
                alert.setTitle("INFORMATION !!");
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.showAndWait();
            }
            catch (SQLException E)
            {
                System.out.println("SQL Error in depositFunds");
                E.printStackTrace();
            }
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.WARNING,"Deposit failed", ButtonType.OK);
            alert.setTitle("WARNING !!");
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.showAndWait();
        }
        updateTransactiontable();
        updateAccountTable();
    }

    private void balanceEnquiry(ActionEvent e)
    {
        if(customerInterfaceModel.balanceEnquiry(BaccountNumber.getText()))
        {
            // Checking output
            System.out.println("Balance Enquiry done");
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR,"Can't find the account",ButtonType.OK);
            alert.setTitle("Balance Check Error !!");
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.showAndWait();
        }
    }

    private void loanDeposit(ActionEvent e)
    {
        if(customerInterfaceModel.loanDeposit(loanRepayAccountNumber.getText(),Double.parseDouble(loanRepayAmountField.getText())))
        {
            try {
                String[] currentDate = new Date().toString().trim().split(" ");
                String date = currentDate[2]+"-"+currentDate[1]+"-"+currentDate[5];
                String time = currentDate[3];
                String query = "insert into transactions(Customer_id,from_account,ttype,to_account,amount,tdate,ttime) values(?,?,?,?,?,?,?)";
                PreparedStatement preparedStatement = customerInterfaceModel.getConnection().prepareStatement(query);
                preparedStatement.setString(1,customerIDLabel.getText());
                preparedStatement.setString(2,loanRepayAccountNumber.getText().trim());
                preparedStatement.setString(3,"Loan Deposit");
                preparedStatement.setString(4,"null");
                preparedStatement.setString(5,loanRepayAmountField.getText());
                preparedStatement.setString(6,date);
                preparedStatement.setString(7,time);

                int rowAffected = preparedStatement.executeUpdate();
                System.out.println("Row Affected = "+rowAffected);

                System.out.println("Loan deposit successful");
            }
            catch (SQLException E)
            {
                System.out.println("SQL error in loanDeposit");
                E.printStackTrace();
            }
        }
        else
        {
            Alert.AlertType alertAlertType = AlertType.WARNING;
            Alert alert = new Alert(alertAlertType,"Account unavailable", ButtonType.OK);
            alert.setTitle("WARNING !!");
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.showAndWait();
        }
        updateAccountTable();
        updateTransactiontable();
    }

    private void updateTransactiontable()
    {
        transactionElementList = new ArrayList<>();
        try {
            int T_count = 0;
            String query3 = "select * from transactions";
            PreparedStatement statement3 = customerInterfaceModel.getConnection().prepareStatement(query3);
            ResultSet resultSet3 = statement3.executeQuery();
            while(resultSet3.next())
            {
                if(resultSet3.getInt(1) == Integer.parseInt(customerIDLabel.getText().trim())) {
                    T_count++;
                    transactionElement T = new transactionElement(String.valueOf(T_count), resultSet3.getString(2), resultSet3.getString(3), resultSet3.getString(4), String.valueOf(resultSet3.getFloat(5)), resultSet3.getString(6), resultSet3.getString(7));
                    transactionElementList.add(T);
                }
            }
            transactionsTable.getItems().addAll(transactionElementList);
        }
        catch (SQLException e)
        {
            System.out.println("SQL error in updateTransactions");
            e.printStackTrace();
        }
    }

    private void updateAccountTable()
    {
        List<Accounts> savingAccounts = customerInterfaceModel.getSavingAccountList();
        List<Accounts> checkingAccounts = customerInterfaceModel.getCheckingAccountList();
        List<Accounts> loanAccounts = customerInterfaceModel.getLoanAccountList();

        rowList = new ArrayList<>();

        int elements = 0;
        for(Accounts saving : savingAccounts)
        {
            if(saving.getCustomer_id() == Integer.parseInt(customerIDLabel.getText().trim()))
            {
                elements++;
                rowList.add(new accountTableElement(String.valueOf(elements),saving.getBankAccountNo(),"Saving",String.valueOf(saving.balanceEnquiry()),saving.getDOC(),saving.getTOC(),"null"));
            }
        }

        for(Accounts checking : checkingAccounts)
        {
            if(checking.getCustomer_id() == Integer.parseInt(customerIDLabel.getText().trim()))
            {
                elements++;
                rowList.add(new accountTableElement(String.valueOf(elements),checking.getBankAccountNo(),"Checking",String.valueOf(checking.balanceEnquiry()),checking.getDOC(),checking.getTOC(),"null"));
            }
        }

        for(Accounts loan: loanAccounts)
        {
            if(loan.getCustomer_id() == Integer.parseInt(customerIDLabel.getText().trim()))
            {
                elements++;
                rowList.add(new accountTableElement(String.valueOf(elements),loan.getBankAccountNo(),"Loan","null",loan.getDOC(),loan.getTOC(),String.valueOf(((loanAccount)loan).getPrincipalAmount())));
            }
        }

        accountTable.getItems().clear();
        accountTable.getItems().addAll(rowList);

    }

}
