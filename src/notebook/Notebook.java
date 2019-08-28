/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package notebook;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author rewil
 */
public class Notebook extends Application {
    
    private static Scene scene = null;
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        
        scene = new Scene(root);
        
        stage.setTitle("Notebook by Yawrf - View Screen");
        stage.setScene(scene);
        stage.setOnHidden(event -> {
            FXMLDocumentController.editScreen.close();
            for(Stage s : FXMLDocumentController.popoutArray) {
                s.close();
            }
        });
        stage.show();
    }

    public static Scene getScene() {
        return scene;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
