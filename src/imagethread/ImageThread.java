package imagethread;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author VakSF
 */
public class ImageThread extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("ImageFXML.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setTitle("Tree Visualizer");
        stage.setOnCloseRequest((event)-> {System.exit(0);});
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
