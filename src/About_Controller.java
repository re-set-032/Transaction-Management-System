package sample;

import com.jfoenix.controls.JFXTextArea;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;


public class About_Controller implements Initializable {

    @FXML
    private JFXTextArea Body;

    @FXML
    private Label AboutLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        // Nothing currently
    }

}
