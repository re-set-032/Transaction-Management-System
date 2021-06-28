package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class Controller implements Initializable  {
    private Loginmodel loginmodel = new Loginmodel();

    @FXML private JFXTextField UserField;
    @FXML private JFXPasswordField PassField;
    @FXML private Label Login_status;
    @FXML private Label Driver_status;
    @FXML private JFXButton AboutUs;
    @FXML private JFXButton LoginButton;
    @FXML private JFXButton CreateButton;
    @FXML private JFXButton ForgotButton;
    @FXML private ToggleGroup loginGroup;
    @FXML private JFXRadioButton customerRadio;
    @FXML private JFXRadioButton managerRadio;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginGroup = new ToggleGroup();
        customerRadio.setToggleGroup(loginGroup);
        managerRadio.setToggleGroup(loginGroup);

        LoginButton.setOnAction(this::Login);
        CreateButton.setOnAction(this::CreateAccount);
        AboutUs.setOnAction(this::AboutUS);
        ForgotButton.setOnAction(this::ForgotPassword);

        if(loginmodel.isDbConnected())
            Driver_status.setText("Driver Status : Online");
        else
            Driver_status.setText("Driver Status : Unknown");
    }

    public void Login(ActionEvent E)   // Addition here with customer or manager interface
    {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try
        {
            if(customerRadio.isSelected())
            {
                if(loginmodel.islogin(UserField.getText(),PassField.getText(),0))
                {
                    Login_status.setText("Login Successful");
                    if(customerRadio.isSelected())
                    {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("Customer_Interface.fxml"));
                        Parent root = loader.load();

                        Cinterface_Controller controller = loader.getController();

                        String query = "select * from customer_account where username = ? and password = ?";
                        preparedStatement = loginmodel.getConnection().prepareStatement(query);
                        preparedStatement.setString(1,UserField.getText());
                        preparedStatement.setString(2,Cryptography.generateHash(PassField.getText().getBytes(),"SHA-512").toLowerCase());
                        resultSet = preparedStatement.executeQuery();
                        resultSet.next();
                        controller.setInitials(resultSet.getInt(1),resultSet.getString(2).trim()+" "+resultSet.getString(3).trim()+" "+resultSet.getString(4).trim());

                        Stage customerWindow = new Stage();
                        customerWindow.setTitle("Customer Interface");
                        customerWindow.setScene(new Scene(root));
                        customerWindow.show();

                        customerWindow.setOnCloseRequest(e -> {
                            e.consume();

                            Alert closeAlert = new Alert(Alert.AlertType.CONFIRMATION, "Sure you want to close this window ?", ButtonType.YES, ButtonType.NO);
                            closeAlert.showAndWait();

                            if(closeAlert.getResult() == ButtonType.YES)
                                customerWindow.close();
                        });

                        preparedStatement.close();
                        resultSet.close();
                    }
                }
                else{
                    Login_status.setText("Login Failed");

                    Alert alert = new Alert(Alert.AlertType.ERROR,"Login failed. Try Again",ButtonType.OK);
                    alert.showAndWait();
                }
            }

            if(managerRadio.isSelected())
            {
                if(loginmodel.islogin(UserField.getText(),PassField.getText(),1))
                {
                    Login_status.setText("Login Successful");
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("Employee_Interface.fxml"));
                    Parent root = loader.load();

                    Einterface_Controller controller = loader.getController();

                    String query = "select * from employee_account where username = ? and password = ?";
                    preparedStatement = loginmodel.getConnection().prepareStatement(query);
                    preparedStatement.setString(1,UserField.getText());
                    preparedStatement.setString(2,Cryptography.generateHash(PassField.getText().getBytes(),"SHA-512").toLowerCase());
                    resultSet = preparedStatement.executeQuery();
                    resultSet.next();
                    controller.setInitials(String.valueOf(resultSet.getInt(1)),resultSet.getString(2).trim()+" "+resultSet.getString(3).trim()+" "+resultSet.getString(4).trim());

                    Stage employeeWindow = new Stage();
                    employeeWindow.setTitle("Customer Interface");
                    employeeWindow.setScene(new Scene(root));
                    employeeWindow.show();

                    employeeWindow.setOnCloseRequest(e -> {
                        e.consume();

                        Alert closeAlert = new Alert(Alert.AlertType.CONFIRMATION, "Sure you want to close this window ?", ButtonType.YES, ButtonType.NO);
                        closeAlert.showAndWait();

                        if(closeAlert.getResult() == ButtonType.YES)
                            employeeWindow.close();
                    });

                    preparedStatement.close();
                    resultSet.close();
                }
                else{
                    Login_status.setText("Login Failed");

                    Alert alert = new Alert(Alert.AlertType.ERROR,"Login failed. Try Again",ButtonType.OK);
                    alert.showAndWait();
                }
            }

        }
        catch(Exception e)
        {
            System.out.println("SQL error");
            e.printStackTrace();
        }

    }

    public void CreateAccount(ActionEvent E)
    {
        try
        {
            Parent createPage = FXMLLoader.load(getClass().getResource("Acreation.fxml"));
            Stage createStage = new Stage();

            createStage.initModality(Modality.APPLICATION_MODAL);
            createStage.setScene(new Scene(createPage));
            createStage.setTitle("Account Creation");
            createStage.showAndWait();

        }
        catch(IOException e)
        {
            System.out.println("Failed in creating Account");
            e.printStackTrace();
        }

    }

    public void ForgotPassword(ActionEvent E)
    {
        try
        {
            Parent createPage = FXMLLoader.load(getClass().getResource("Forgot_Password.fxml"));
            Stage createStage = new Stage();

            createStage.initModality(Modality.APPLICATION_MODAL);
            createStage.setScene(new Scene(createPage));
            createStage.setTitle("Forgot Password");
            createStage.showAndWait();

        }
        catch(IOException e)
        {
            System.out.println("Failed in Forgot password area");
        }
    }

    public void AboutUS(ActionEvent E)
    {
        try
        {
            System.out.println(getClass().getResource("AboutUs.fxml"));
            Parent createPage = FXMLLoader.load(getClass().getResource("AboutUs.fxml"));
            Stage createStage = new Stage();

            createStage.initModality(Modality.APPLICATION_MODAL);
            createStage.setScene(new Scene(createPage));
            createStage.setTitle("About Us");
            createStage.showAndWait();

        }
        catch(IOException e)
        {
            System.out.println("Failed in AboutUs area");
            e.printStackTrace();
        }
    }


}
