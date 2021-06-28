package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.util.Date;

public class Main extends Application {

    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage primaryStage) {
        try
        {
            System.out.println(new Date().toString());
            System.out.println(getClass().getResource("Main.fxml"));
            Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));

            primaryStage.setTitle("Bank is here");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();

            primaryStage.setOnCloseRequest(e -> {
                e.consume();

                Alert closeAlert = new Alert(Alert.AlertType.CONFIRMATION, "Sure you want to close the program ?", ButtonType.YES, ButtonType.NO);
                closeAlert.showAndWait();

                if(closeAlert.getResult() == ButtonType.YES)
                    primaryStage.close();
            });
        }
        catch (Exception e)
        {
            System.out.println("Error in main");
            e.printStackTrace();
        }

    }
}
