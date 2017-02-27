package imagethread;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author VakSF
 */
public class ImageFXMLController implements Initializable {
    
    @FXML
    private ImageView imageView;
    
    private static volatile String path = "C:\\Files\\avl.png";
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        new Thread(new Updater()).start();
        
    }       
    
    @FXML
    private void setOption(ActionEvent event) {
        
        String option = ((MenuItem) event.getSource()).getText();
        
        if (option.equalsIgnoreCase("binarysearchtree")) {
            
            ImageFXMLController.path = "C:\\Files\\search.png";
            
        } else {
            
            if (option.equalsIgnoreCase("binarytreeavl")) {
                
                ImageFXMLController.path = "C:\\Files\\avl.png";
                
            }
            
        }
        
    }
    
    class Updater implements Runnable {
        
        private volatile boolean isRunning = true;

        @Override
        public void run() {
            
            while (isRunning) {
                
                try {
                    
                    File file = new File(ImageFXMLController.path);

                    if (file.exists()) {
                        
                        Thread.sleep(100);
                        
                        Platform.runLater(() -> {
                            
                            try {
                                imageView.setImage(
                                        new Image(file.toURI().toURL().toExternalForm())
                                );
                            } catch (MalformedURLException ex) {
                            }
                            
                        });
                        
                    }
                    
                } catch (InterruptedException ex) {
                    
                }
                
            }
            
        }
        
        public void kill() {
            this.isRunning = false;
        }
        
    }
    
}
