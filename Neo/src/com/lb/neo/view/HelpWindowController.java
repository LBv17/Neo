/*
 * Copyright (c) 2018 Lorenzo Baldassarri
 * All rights reserved.
 * Redistributions in any form must reproduce the above copyright notice.
 */
package com.lb.neo.view;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * FXML Controller class
 *
 * @author LB16
 */
public class HelpWindowController implements Initializable {

    @FXML
    private TabPane HelpPane;

    String defaultFont;
    double defaultFontSize;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
                
       
        defaultFont = Font.getDefault().getName();
        defaultFontSize = Font.getDefault().getSize();
        
        // Create Tabs
        Tab start = new Tab();
        Tab guide = new Tab();
        Tab features = new Tab();
        Tab cpus = new Tab();
        
        // Set Title of Tabs
        start.setText("Quick Start");
        guide.setText("Coding Guide");
        features.setText("Feature Overview");
        cpus.setText("CPUs");
        
        // Create and style text inside Tabs
        TextFlow startFlow = new TextFlow(getStartText());
        TextFlow guideFlow = new TextFlow(getGuideText());
        TextFlow featuresFlow = new TextFlow(getFeatureText());
        TextFlow cpusFlow = new TextFlow(getCPUsText());
        
        startFlow.setStyle("-fx-padding: 10 10 10 10;");
        guideFlow.setStyle("-fx-padding: 10 10 10 10;");
        featuresFlow.setStyle("-fx-padding: 10 10 10 10;");
        cpusFlow.setStyle("-fx-padding: 10 10 10 10;");
        
        // Add text to tabs
        start.setContent(startFlow);
        guide.setContent(guideFlow);
        features.setContent(featuresFlow);
        cpus.setContent(cpusFlow);
        
        // Add Tabs to the Pane
        HelpPane.getTabs().addAll(start, guide, features, cpus);

    }    
    
    private Group getStartText() {
        Text title = new Text();
        title.setFont(Font.font(defaultFont, FontWeight.SEMI_BOLD, FontPosture.REGULAR, 20));
        title.setText("Quick Start");
         
        Group textGroup = new Group(title);
        
        return textGroup;
    }
    private Group getGuideText() {
        Text title = new Text();
        title.setFont(Font.font(defaultFont, FontWeight.SEMI_BOLD, FontPosture.REGULAR, 20));
        title.setText("Coding Guide");
         
        Group textGroup = new Group(title);
        
        return textGroup;
    }
    private Group getFeatureText() {
        Text title = new Text();
        title.setFont(Font.font(defaultFont, FontWeight.SEMI_BOLD, FontPosture.REGULAR, 20));
        title.setText("Features");
         
        Group textGroup = new Group(title);
        
        return textGroup;
    }
    private Group getCPUsText() {
        Text title = new Text();
        title.setFont(Font.font(defaultFont, FontWeight.SEMI_BOLD, FontPosture.REGULAR, 20));
        title.setText("CPUs");
         
        Group textGroup = new Group(title);
        
        return textGroup;
    }
    
}
