package sample;

import com.jfoenix.controls.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Modality;

import java.net.URL;
import java.util.ResourceBundle;

public class Account_Creation_Controller implements Initializable {

    private CreateAccountmodel createAccountmodel = new CreateAccountmodel();
    private final String[] securityQuestions = {"none",
            "Enter your best friend name ?",
            "Enter your favourite color ?",
            "What primary school did you attend ?",
            "In what town or city was your first full time job?",
            "What was the house number and street name you lived in as a child ?",
            "What are the last five digits of your driver's licence number ?"
    };

    //Customer Account fields------------------------------------------------------


    @FXML
    private JFXTextField CsecurityAnswer;

    @FXML
    private ComboBox<String> CsecurityQuestionList;

    @FXML
    private JFXCheckBox CvalidityAgreement;

    @FXML
    private JFXTextField C_first_name_TF;

    @FXML
    private JFXTextField C_middle_name_TF;

    @FXML
    private JFXTextField C_last_name_TF;

    @FXML
    private JFXTextField C_username_TF;

    @FXML
    private JFXPasswordField C_password_TF;

    @FXML
    private JFXPasswordField C_confirmpassword_TF;

    @FXML
    private JFXTextField C_email_TF;

    @FXML
    private JFXTextField C_contact_no_TF;

    @FXML
    private JFXRadioButton C_gender_male;

    @FXML
    private JFXRadioButton C_gender_female;

    @FXML
    private JFXRadioButton C_gender_others;

    @FXML
    private ToggleGroup C_gender_group ;

    @FXML
    private DatePicker C_DOB_DP;

    @FXML
    private Label ResultBox;

    @FXML
    private JFXButton C_createButton;

    public void Create_Customer(ActionEvent e)
    {
        // Exceptions

        if(C_first_name_TF.getText().trim().isEmpty() || C_last_name_TF.getText().trim().isEmpty() || C_username_TF.getText().trim().isEmpty() || C_password_TF.getText().trim().isEmpty() || C_confirmpassword_TF.getText().trim().isEmpty() || C_email_TF.getText().trim().isEmpty() || C_contact_no_TF.getText().trim().isEmpty() || CsecurityAnswer.getText().trim().isEmpty()) {
            showAlert("Fill all details properly", Alert.AlertType.WARNING);
            return;
        }

        if(!C_confirmpassword_TF.getText().equals(C_password_TF.getText())) {
            showAlert("Enter password correctly", Alert.AlertType.WARNING);
            return;
        }

        if(!CvalidityAgreement.isSelected()) {
            showAlert("Check customer agreement !!", Alert.AlertType.WARNING);
            return;
        }

        // Splitting date
        String[] C_DOB =C_DOB_DP.getValue().toString().split("-");

        //Gender
        String gender = null;
        if(C_gender_male.isSelected())
            gender = "Male";
        else if(C_gender_female.isSelected())
            gender = "Female";
        else if(C_gender_others.isSelected())
            gender = "Others";

        try{
            createAccountmodel.createAccount(C_first_name_TF.getText(),C_middle_name_TF.getText(),C_last_name_TF.getText(),C_username_TF.getText(),C_password_TF.getText(),C_DOB[2],C_DOB[1],C_DOB[0],gender,C_email_TF.getText(),C_contact_no_TF.getText(),CsecurityQuestionList.getValue(),CsecurityAnswer.getText());
            ResultBox.setText("Account created");
        }
        catch(Exception E)
        {
            System.out.println("Customer Account Creation error");
            ResultBox.setText("Failed");
            E.printStackTrace();
        }
    }

//    //Employee Account Fields------------------------------------------------------

    @FXML
    private JFXTextField EsecurityAnswer;

    @FXML
    private ComboBox<String> EsecurityQuestionList;

    @FXML
    private JFXCheckBox EvalidityAgreement;

    @FXML
    private JFXTextField E_employeeID_TF;

    @FXML
    private JFXTextField E_first_name_TF;

    @FXML
    private JFXTextField E_middle_name_TF;

    @FXML
    private JFXTextField E_last_name_TF;

    @FXML
    private JFXTextField E_username_TF;

    @FXML
    private JFXPasswordField E_password_TF;

    @FXML
    private JFXPasswordField E_confirmpassword_TF;

    @FXML
    private JFXTextField E_email_TF;

    @FXML
    private JFXTextField E_contact_no_TF;

    @FXML
    private JFXRadioButton E_gender_male;

    @FXML
    private JFXRadioButton E_gender_female;

    @FXML
    private JFXRadioButton E_gender_others;

    @FXML
    private ToggleGroup E_gender_group ;

    @FXML
    private DatePicker E_DOB_DP;

    @FXML
    private  Label ResultLabel;

    @FXML
    private JFXButton E_createButton;

    public void Create_Employee(ActionEvent e)
    {
        // Exceptions

        if(E_first_name_TF.getText().trim().isEmpty() || E_last_name_TF.getText().trim().isEmpty() || E_username_TF.getText().trim().isEmpty() || E_password_TF.getText().trim().isEmpty() || E_confirmpassword_TF.getText().trim().isEmpty() || E_email_TF.getText().trim().isEmpty() || E_contact_no_TF.getText().trim().isEmpty() || EsecurityAnswer.getText().trim().isEmpty()) {
            showAlert("Fill all details properly", Alert.AlertType.WARNING);
            return;
        }

        if(!E_confirmpassword_TF.getText().equals(E_password_TF.getText())) {
            showAlert("Enter password correctly", Alert.AlertType.WARNING);
            return;
        }

        if(!EvalidityAgreement.isSelected()) {
            showAlert("Check employee agreement !!", Alert.AlertType.WARNING);
            return;
        }

        //Splitting day,month and year
        String[] E_DOB =E_DOB_DP.getValue().toString().split("-");

        //Gender
        String gender = null;
        if(E_gender_male.isSelected())
            gender = "Male";
        else if(E_gender_female.isSelected())
            gender = "Female";
        else if(E_gender_others.isSelected())
            gender = "Others";


        try{
            createAccountmodel.createAccount(E_employeeID_TF.getText(),E_first_name_TF.getText(),E_middle_name_TF.getText(),E_last_name_TF.getText(),E_username_TF.getText(),E_password_TF.getText(),E_DOB[2],E_DOB[1],E_DOB[0],gender,E_email_TF.getText(),E_contact_no_TF.getText(),EsecurityQuestionList.getValue(),EsecurityAnswer.getText());
            ResultLabel.setText("Account created");
        }
        catch(Exception E)
        {
            System.out.println("Employee Account Creation error");
            ResultLabel.setText("Failed");
            E.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        EsecurityAnswer.setDisable(true);
        CsecurityAnswer.setDisable(true);

        EsecurityQuestionList.getItems().addAll(securityQuestions);
        CsecurityQuestionList.getItems().addAll(securityQuestions);
        EsecurityQuestionList.setValue("none");
        CsecurityQuestionList.setValue("none");

        // Toggle-grouping of radio buttons
        C_gender_group = new ToggleGroup();
        C_gender_male.setToggleGroup(C_gender_group);
        C_gender_female.setToggleGroup(C_gender_group);
        C_gender_others.setToggleGroup(C_gender_group);

        E_gender_group = new ToggleGroup();
        E_gender_male.setToggleGroup(E_gender_group);
        E_gender_female.setToggleGroup(E_gender_group);
        E_gender_others.setToggleGroup(E_gender_group);

        if(createAccountmodel.isDbConnected()) {
            ResultBox.setText("Driver connected");
            ResultLabel.setText("Driver connected");
        }
        else
        {
            ResultBox.setText("Driver connection failed");
            ResultLabel.setText("Driver connection failed");
        }
    }

    public void comboListener(ActionEvent e)
    {
        if(!EsecurityQuestionList.getValue().equals("none")) { EsecurityAnswer.setDisable(false);}
        else{ EsecurityAnswer.setDisable(true);}
        if(!CsecurityQuestionList.getValue().equals("none")){ CsecurityAnswer.setDisable(false);}
        else{CsecurityAnswer.setDisable(true);}
    }

    public void showAlert(String msg, Alert.AlertType alertType)
    {
        Alert alert = new Alert(alertType,msg,ButtonType.OK);
        alert.setTitle("WARNING !!");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
    }

}
