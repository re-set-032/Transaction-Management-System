package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Modality;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;

public class Forgot_psswd_Controller implements Initializable {

    ForgotPasswordModel forgotPasswordModel = new ForgotPasswordModel();

    private final String[] securityQuestions = {"none",
            "Enter your best friend name ?",
            "Enter your favourite color ?",
            "What primary school did you attend ?",
            "In what town or city was your first full time job?",
            "What was the house number and street name you lived in as a child ?",
            "What are the last five digits of your driver's licence number ?"
    };
    private final String[] accountTypes = {"none","Employee","Customer"};

    @FXML private Label accountTypeLabel;
    @FXML private Label userLabel;
    @FXML private Label securityLabel;
    @FXML private Label securityAnswerLabel;
    @FXML private Label passwordLabel;
    @FXML private Label cpasswordLabel;
    @FXML private JFXTextField usernameField;
    @FXML private JFXTextField securityAnswerField;
    @FXML private JFXTextField passwordField;
    @FXML private JFXTextField cpasswordField;
    @FXML private ComboBox<String> securityQuestionList;
    @FXML private ComboBox<String> accountTypeList;
    @FXML private JFXButton proceedButton;
    @FXML private JFXButton changePasswordButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        passwordLabel.setVisible(false);
        cpasswordLabel.setVisible(false);
        passwordField.setVisible(false);
        cpasswordField.setVisible(false);
        changePasswordButton.setVisible(false);
        securityAnswerField.setDisable(true);

        usernameField.setDisable(true);
        securityQuestionList.setDisable(true);
        securityAnswerField.setDisable(true);
        proceedButton.setDisable(true);

        securityQuestionList.getItems().addAll(securityQuestions);
        securityQuestionList.setValue("none");
        accountTypeList.getItems().addAll(accountTypes);
        accountTypeList.setValue("none");

        proceedButton.setOnAction(this::proceedFurtherOnAction);
        changePasswordButton.setOnAction(this::changePasswordOnAction);
        securityQuestionList.setOnAction(this::comboBoxListener);
        accountTypeList.setOnAction(this::comboBoxListener);
    }

    public void proceedFurtherOnAction(ActionEvent e)
    {
        int mod = accountTypeList.getValue().equalsIgnoreCase("customer") ? 0 : 1;
        if(forgotPasswordModel.searchAccount(usernameField.getText(),securityQuestionList.getValue(),securityAnswerField.getText(),mod))
        {
            passwordLabel.setVisible(true);
            cpasswordLabel.setVisible(true);
            passwordField.setVisible(true);
            cpasswordField.setVisible(true);
            changePasswordButton.setVisible(true);

            userLabel.setVisible(false);
            usernameField.setVisible(false);
            securityQuestionList.setVisible(false);
            securityAnswerField.setVisible(false);
            securityAnswerLabel.setVisible(false);
            securityLabel.setVisible(false);
            proceedButton.setVisible(false);
            accountTypeList.setVisible(false);
            accountTypeLabel.setVisible(false);
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.WARNING,"Invalid input, try again", ButtonType.OK);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setTitle("Warning");
            alert.showAndWait();
        }
    }

    public void changePasswordOnAction(ActionEvent e)
    {
        int mod = accountTypeList.getValue().equalsIgnoreCase("customer") ? 0 : 1;
        if(forgotPasswordModel.updatePassword(usernameField.getText(),passwordField.getText(),mod))
            System.out.println("updated password");
        else
            System.out.println("update password failed");
    }

    public void comboBoxListener(ActionEvent e)
    {
        if(!accountTypeList.getValue().equals("none"))
        {
            usernameField.setDisable(false);
            securityQuestionList.setDisable(false);
            proceedButton.setDisable(false);
        }
        else
        {
            usernameField.setDisable(true);
            securityQuestionList.setDisable(true);
            proceedButton.setDisable(true);
        }

        if(!securityQuestionList.getValue().equals("none") && !accountTypeList.getValue().equals("none")) { securityAnswerField.setDisable(false); }
        else {securityAnswerField.setDisable(true);}

    }
}
