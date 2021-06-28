package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Einterface_Controller implements Initializable {
    Loginmodel loginmodel = new Loginmodel();
    Einterface_model Einterface_model = new Einterface_model();
    List<searchTableEntry> searchTableEntryList;

//    Table view
    @FXML
    private TableView<searchTableEntry> tableView;

    @FXML
    private TableColumn<Object,Object> S_no;

    @FXML
    private TableColumn<Object,Object> cid;

    @FXML
    private TableColumn<Object,Object> name;

    @FXML
    private TableColumn<Object,Object> accountNum;

    @FXML
    private TableColumn<Object,Object> accountType;

    @FXML
    private TableColumn<Object,Object> balance;

    @FXML
    private TableColumn<Object,Object> email;

    @FXML
    private TableColumn<Object,Object> contact;

    //---------------------------------------
    // Searching Area

    @FXML
    private JFXTextField customerIDField;

    @FXML
    private JFXButton searchButton;

    @FXML
    private JFXButton showAllButton;

    @FXML
    private JFXButton clearButton;

    // Profile
    @FXML
    private JFXButton editButton;

    @FXML
    private JFXButton saveButton;

    @FXML
    private Label eName;

    @FXML
    private Label eGender;

    @FXML
    private Label eDOB;

    @FXML
    private Label eID;

    @FXML
    private TextField idEmail;

    @FXML
    private TextField idContact;

//    for editing values
    @FXML
    public void editValues(ActionEvent e)
    {
        idEmail.setEditable(true);
        idEmail.setCursor(Cursor.TEXT);
        idContact.setEditable(true);
        idContact.setCursor(Cursor.TEXT);
    }

    @FXML
    public void saveValues(ActionEvent e)
    {
        idContact.setCursor(Cursor.DEFAULT);
        idEmail.setCursor(Cursor.DEFAULT);
        idEmail.setEditable(false);
        idContact.setEditable(false);

        try {
            String query = "update employee_account set contact_no = ?,email = ? where Employee_id = ?";
            PreparedStatement preparedStatement = loginmodel.getConnection().prepareStatement(query);
            preparedStatement.setString(2,idEmail.getText());
            preparedStatement.setString(1,idContact.getText());
            preparedStatement.setString(3,eID.getText());

            int rowAffected = preparedStatement.executeUpdate();
            System.out.println("Row Affected = "+rowAffected);

            preparedStatement.close();
        }
        catch (SQLException E) {
            System.out.println("SQL error in employee saveValues");
            E.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        S_no.setCellValueFactory(new PropertyValueFactory<>("s_no"));
        cid.setCellValueFactory(new PropertyValueFactory<>("cid"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        accountNum.setCellValueFactory(new PropertyValueFactory<>("accountNum"));
        accountType.setCellValueFactory(new PropertyValueFactory<>("accountType"));
        balance.setCellValueFactory(new PropertyValueFactory<>("balance"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        contact.setCellValueFactory(new PropertyValueFactory<>("contact"));

        searchButton.setOnAction(this::searchCustomer);
        clearButton.setOnAction(this::clearTable);
        showAllButton.setOnAction(this::showAllAccounts);
        editButton.setOnAction(this::editValues);
        saveButton.setOnAction(this::saveValues);
    }

    public void setInitials(String employeeID,String name)
    {
        eID.setText(employeeID);
        eName.setText(name);
        setDashboard();
    }

    public void setDashboard()
    {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            String query = "select * from employee_account where Employee_id = ?";
            preparedStatement = loginmodel.getConnection().prepareStatement(query);
            preparedStatement.setString(1,eID.getText());
            resultSet = preparedStatement.executeQuery();
            if(resultSet == null)
                throw new Exception("Result null");

            resultSet.next();
            eGender.setText(resultSet.getString(10));
            eDOB.setText(resultSet.getInt(7)+"/"+resultSet.getInt(8)+"/"+resultSet.getInt(9));
            idEmail.setText(resultSet.getString(11));
            idContact.setText(String.valueOf(resultSet.getInt(12)));

            resultSet.close();
            preparedStatement.close();
        }
        catch (Exception e)
        {
            System.out.println("Dashboard error");
            System.out.println(e.getMessage());
        }
    }

    public void searchCustomer(ActionEvent e)
    {
        searchTableEntryList = new ArrayList<>();
        int search_count = 0;
        try {
            String query1 = "select * from customer_account where Customer_id = ?";
            PreparedStatement preparedStatement1 = loginmodel.getConnection().prepareStatement(query1);
            preparedStatement1.setString(1,customerIDField.getText().trim());
            ResultSet resultSet1 = preparedStatement1.executeQuery();
            resultSet1.next();

            String name = resultSet1.getString(2)+" "+resultSet1.getString(3)+" "+resultSet1.getString(4);
            String email = resultSet1.getString(11);
            String contact_no = resultSet1.getString(12);

            String query2 = "select * from saving_account where Customer_id = ?";
            PreparedStatement preparedStatement2 = Einterface_model.getConnection().prepareStatement(query2);
            preparedStatement2.setString(1,customerIDField.getText().trim());
            ResultSet resultSet2 = preparedStatement2.executeQuery();
            while(resultSet2.next())
            {
                search_count++;
                searchTableEntry S = new searchTableEntry(String.valueOf(search_count),customerIDField.getText().trim(),name,resultSet2.getString(2),"Saving",String.valueOf(resultSet2.getFloat(4)),email,contact_no);
                searchTableEntryList.add(S);
            }

            String query3 = "select * from checking_account where Customer_id = ?";
            PreparedStatement preparedStatement3 = Einterface_model.getConnection().prepareStatement(query3);
            preparedStatement3.setString(1,customerIDField.getText().trim());
            ResultSet resultSet3 = preparedStatement3.executeQuery();
            while(resultSet3.next())
            {
                search_count++;
                searchTableEntry C = new searchTableEntry(String.valueOf(search_count),customerIDField.getText().trim(),name,resultSet3.getString(2),"Checking",String.valueOf(resultSet3.getFloat(4)),email,contact_no);
                searchTableEntryList.add(C);
            }

            tableView.getItems().clear();
            tableView.getItems().addAll(searchTableEntryList);

            resultSet1.close();
            resultSet2.close();
            resultSet3.close();
            preparedStatement1.close();
            preparedStatement2.close();
            preparedStatement3.close();

        }
        catch (SQLException E)
        {
            System.out.println("SQL error in searchCustomer");
            E.printStackTrace();
        }

    }

    public void clearTable(ActionEvent e)
    {
        tableView.getItems().clear();
    }

    public void showAllAccounts(ActionEvent e)
    {
        searchTableEntryList = new ArrayList<>();
        int search_count = 0;
        try {
            PreparedStatement preparedStatement1 = null;
            ResultSet resultSet1 = null;

            String query2 = "select * from saving_account";
            PreparedStatement preparedStatement2 = Einterface_model.getConnection().prepareStatement(query2);
            ResultSet resultSet2 = preparedStatement2.executeQuery();
            while(resultSet2.next())
            {
                // Add---------------------------------------------------------------------
                String CID = String.valueOf(resultSet2.getInt(1));
                String query1 = "select * from customer_account where Customer_id = ?";
                preparedStatement1 = loginmodel.getConnection().prepareStatement(query1);
                preparedStatement1.setString(1,CID.trim());
                resultSet1 = preparedStatement1.executeQuery();
                resultSet1.next();

                String name = resultSet1.getString(2)+" "+resultSet1.getString(3)+" "+resultSet1.getString(4);
                String email = resultSet1.getString(11);
                String contact_no = resultSet1.getString(12);

                //--------------------------------------------------------------------------

                search_count++;
                searchTableEntry S = new searchTableEntry(String.valueOf(search_count),CID.trim(),name,resultSet2.getString(2),"Saving",String.valueOf(resultSet2.getFloat(4)),email,contact_no);
                searchTableEntryList.add(S);
            }

            String query3 = "select * from checking_account";
            PreparedStatement preparedStatement3 = Einterface_model.getConnection().prepareStatement(query3);
            ResultSet resultSet3 = preparedStatement3.executeQuery();
            while(resultSet3.next())
            {
                // Add-------------------------------------------------------------------
                String CID = String.valueOf(resultSet3.getInt(1));
                String query1 = "select * from customer_account where Customer_id = ?";
                preparedStatement1 = loginmodel.getConnection().prepareStatement(query1);
                preparedStatement1.setString(1,CID.trim());
                resultSet1 = preparedStatement1.executeQuery();
                resultSet1.next();

                String name = resultSet1.getString(2)+" "+resultSet1.getString(3)+" "+resultSet1.getString(4);
                String email = resultSet1.getString(11);
                String contact_no = resultSet1.getString(12);

                //--------------------------------------------------------------------------


                search_count++;
                searchTableEntry C = new searchTableEntry(String.valueOf(search_count),CID.trim(),name,resultSet3.getString(2),"Checking",String.valueOf(resultSet3.getFloat(4)),email,contact_no);
                searchTableEntryList.add(C);
            }

            tableView.getItems().clear();
            tableView.getItems().addAll(searchTableEntryList);

            if(resultSet1 !=null)
                resultSet1.close();

            resultSet2.close();
            resultSet3.close();

            if(preparedStatement1 !=null)
                preparedStatement1.close();

            preparedStatement2.close();
            preparedStatement3.close();

        }
        catch (SQLException E)
        {
            System.out.println("SQL error in searchCustomer");
            E.printStackTrace();
        }
    }
}
