/*
 * Copyright (c) 2018 Lorenzo Baldassarri
 * All rights reserved.
 * Redistributions in any form must reproduce the above copyright notice.
 */
package com.lb.neo.view;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author LB
 */
public class ConverterWindowController implements Initializable {

    @FXML
    private TextField inputField;
    @FXML
    private Button convertButton;
    @FXML
    private TextArea outputField;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }   
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        outputField.setText(convert(inputField.getText()));
    }
    
    private String convert(String input) {
        
        String output = "";
        
        try {
            float inputAsFloat = Float.parseFloat(input);
            
            int floatToIntBits = Float.floatToIntBits(inputAsFloat);
            
            String temp = Integer.toBinaryString(floatToIntBits);
            while (temp.length() < 32) {
                temp = "0" + temp;
            }
            
            String binary = "Binary: " + temp;
            String hex = "Hex: " + Integer.toHexString(floatToIntBits);
            String octal = "Octal: " + Integer.toOctalString(floatToIntBits);
            
            output += binary + "\n" + hex + "\n" + octal + "\n";
        } catch (Exception e) {
            outputField.setText("ERROR: Format Error");
            MainWindowController.exceptionHandler(e, 0,
                "ERROR: Format Exception", "Please check your input!" +
                "\nFormat should be Number.Number");
        }
        
        return output;
    }    
    
}
