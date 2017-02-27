package imagethread;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
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
        new Thread(new Listener()).start();
        
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
    
    class Listener implements Runnable {
        
        public List<String> command;
        
        public Listener() {
            
            this.command = new ArrayList<>(
                    Arrays.asList(
                           "C:/Program Files (x86)/Graphviz2.38/bin/dot",
                            "-Tpng",
                            "C:/Files/avl.dot",
                            "-o",
                            "C:/Files/avl.png" 
                    )
            );
            
        }

        @Override
        public void run() {
            
            Path path = new File("C:\\Files").toPath();
            
            try {
                
                WatchService watchService = FileSystems.getDefault().newWatchService();
                
                path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
                
                while (true) {
                    
                    WatchKey watchKey = watchService.take();
                    
                    for (WatchEvent<?> event : watchKey.pollEvents()) {
                        
                        Path changed = (Path) event.context();
                        
                        if (changed.getFileName().toString().equals("avl.dot")) {
                            
                            this.executeCommand(this.command);
                            
                        }
                        
                        watchKey.reset();
                        
                    }
                    
                }
            } catch (IOException | InterruptedException ex) {
            }
            
        }
        
        private String executeCommand(List<String> command) {
        
            String output = "";

            try {

                ProcessBuilder processBuilder = new ProcessBuilder(command);

                processBuilder.redirectErrorStream(true);

                Process process = processBuilder.start();

                process.waitFor();

                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

                for (Object object : reader.lines().toArray()) {
                    output += object.toString() + "\n";
                }

            } catch (IOException | InterruptedException ex) {

                ex.printStackTrace();

            }

            return output;

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
