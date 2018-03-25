/* 
 * Copyright (c) 2018 Lorenzo Baldassarri
 * All rights reserved.
 * Redistributions in any form must reproduce the above copyright notice.
 */
package com.lb.neo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Lorenzo Baldassarri
 */
public class Main extends Application {
        
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("view/MainWindow.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
       
        stage.setTitle("Neo");
        
        //Prevent resizing of main window
        stage.setResizable(false);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
