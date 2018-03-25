/* 
 * Copyright (c) 2018 Lorenzo Baldassarri
 * All rights reserved.
 * Redistributions in any form must reproduce the above copyright notice.
 */
package com.lb.neo.view;

import com.lb.neo.model.IntelCPU;
import com.lb.neo.model.NeoCompiler;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Dialog;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCombination;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author Lorenzo Baldassarri
 */
public class MainWindowController implements Initializable {

    @FXML
    private Button runButton;
    @FXML
    private Button debugButton;
    @FXML
    private Button resetButton;
    @FXML
    private TextArea inputArea;
    @FXML
    private TextArea outputArea;
    @FXML
    private ChoiceBox<String> chooseCPUBox;
    @FXML
    private ChoiceBox<String> inputModeBox;
    @FXML
    private MenuBar neoMenuBar;

    private int numOfRun = 1;
    
    /**
     * Sets parameters for the stage and its elements.
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // Create MenuBar menus and their items
        Menu fileMenu = new Menu("File");
        Menu editorMenu = new Menu("Editor");
        Menu helpMenu = new Menu("Help"); 
        Menu advancedMenu = new Menu("Advanced");
        MenuItem load = new MenuItem("Load Program from File");
        MenuItem save = new MenuItem("Save Program to File");
        MenuItem quit = new MenuItem("Quit Program");
        MenuItem changeTheme = new MenuItem("Change Editor Theme");
        MenuItem showHelp = new MenuItem("Show Help");
        MenuItem showExamples = new MenuItem("Show Examples");
        MenuItem converter = new MenuItem("Floating Point Converter");
        MenuItem about = new MenuItem("About NEO");
        MenuItem devFlags = new MenuItem("Developer Optiones");
        
        // Add all items to their menus
        fileMenu.getItems().addAll(load, save, quit);
        editorMenu.getItems().addAll(changeTheme);
        helpMenu.getItems().addAll(showHelp, showExamples, converter, about);
        advancedMenu.getItems().addAll(devFlags);
        
        // Set up shortcuts for items
        quit.setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));
        load.setAccelerator(KeyCombination.keyCombination("Ctrl+L"));
        save.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
        showHelp.setAccelerator(KeyCombination.keyCombination("Ctrl+H"));
        devFlags.setAccelerator(KeyCombination.keyCombination("Ctrl+D"));
        
        // Set up ActionEvents for the MenuItems
        load.setOnAction((ActionEvent event) -> {
            loadFromFile();
        });
        save.setOnAction((ActionEvent event) -> {
            saveToFile();
        });
        quit.setOnAction((ActionEvent event) -> {
            Platform.exit();
        });
        changeTheme.setOnAction((ActionEvent event) -> {
            changeTheme();
        });
        showHelp.setOnAction((ActionEvent event) -> {
            showHelp();
        });
        showExamples.setOnAction((ActionEvent event) -> {
            showExamples();
        });
        converter.setOnAction((ActionEvent event) -> {
            showConverter();
        });
        about.setOnAction((ActionEvent event) -> {
            showAbout();
        });
        devFlags.setOnAction((ActionEvent event) -> {
            showDevMenu();
        });
        
        // Put all Items and Menus into the MenuBar
        neoMenuBar.getMenus().addAll(fileMenu, editorMenu, helpMenu, advancedMenu);
        
        // Disable editing on the output area
        outputArea.setEditable(false);
        //outputArea.setDisable(true);
        
        // Set the focus on the input area
        inputArea.requestFocus();
        
        // Fill the choice boxes when scene is initialized and set default value
        inputModeBox.getItems().add("Raw Hex (Default)");
        inputModeBox.getItems().add("NeoAssembly (Check Help)");
        inputModeBox.getItems().add("NeoLang (Check Help)");
        inputModeBox.getSelectionModel().select(0);
        chooseCPUBox.getItems().add("CPU Intel (Default)");
        chooseCPUBox.getItems().add("CPU AMD (Check Help)");
        chooseCPUBox.getSelectionModel().select(0);
    }    

    @FXML
    private void handleRunAction(ActionEvent event) {
        
        String output = "ERROR";
        
        int cpu = chooseCPUBox.getSelectionModel().getSelectedIndex();
        int inputMode = inputModeBox.getSelectionModel().getSelectedIndex();
        NeoCompiler c = new NeoCompiler();
        
        if (cpu == 0) {
            IntelCPU intel = new IntelCPU();
            output = c.complileProgram(inputArea.getText(), inputMode, intel);
        } else {
            // TODO: handle amd cpu
        }
        
        if (numOfRun == 1) {
            outputArea.setText(outputArea.getText() + "Run #" + numOfRun);
            outputArea.setText(outputArea.getText() + "\n" + output);
        }else{
            outputArea.setText(outputArea.getText() + "\n\n" + "Run #" + numOfRun);
            outputArea.setText(outputArea.getText() + "\n" + output);
        }
        
        this.numOfRun++;
        
        

    }

    @FXML
    private void handleDebugAction(ActionEvent event) {
        // TODO
    }
    
    /**
     * Clears all the areas
     * @param event 
     */
    @FXML
    private void handleResetAction(ActionEvent event) {
        inputArea.clear();
        outputArea.clear();
        this.numOfRun = 1;
    }
    
    /**
     * Loads the contents of a .txt File into the input Area
     * Handles errors and shows them to user, -> IOException and Exception
     * .rtf is not working only txt
     */
    private void loadFromFile() {
        
        final FileChooser fc = new FileChooser();
        String fileType;
        List<String> fileContent;
        
        try {
            File choosenFile = fc.showOpenDialog(null);
            
            fileType = choosenFile.getPath().substring(choosenFile.getPath().length()-4);
           
            if (!".txt".equals(fileType) /*| !".rtf".equals(fileType)*/) {
                throw new Exception("Bad FileType \n" + fileType + " Is not supported");
            }
            
            fileContent = Files.readAllLines(choosenFile.toPath());
            
            fileContent.forEach((line) -> {
                inputArea.setText(inputArea.getText() + line + "\n");
            });
            
        } catch(IOException ioe) {
            System.err.println(ioe);

            String header = "An error occured!";
            String text = ioe.toString();
            exceptionHandler(ioe, 0 , header, text);
            
        } catch (Exception e) {
            System.err.print(e);
            
            String header = "An error occured!";
            String text = e.toString() + "\n" + "HINT: Only .txt " /*and .rtf*/ + "files are supported!";
            exceptionHandler(e, 0, header, text);
            
        }
            
    };
    
    /**
     * Saves the contents of inputArea as a .txt file 
     * .rtf can't be opened with textedit on Mac
     */
    private void saveToFile() {
        
        final FileChooser fc = new FileChooser();
        fc.setInitialFileName("Program");
        File file = fc.showSaveDialog(null);
        String absolutePath = file.getAbsolutePath();

        try {
            Files.write(Paths.get(absolutePath), inputArea.getText().getBytes());
        } catch (IOException e) {
            System.out.println(e);            
        }
        
    };
    
    /**
     * Allows u to change colors of editor 
     * TODO: pretty much everything
     */
    private void changeTheme() {};
    
    /**
     * Switches to Help window
     */
    private void showHelp() {
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("HelpWindow.fxml"));
            Parent root = (Parent)loader.load();
            
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
            
            stage.setTitle("Help");
            
            //Prevent resizing of main window
            stage.setResizable(false);
            
        } catch (IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    };
    
    /**
     * Shows and loads examples into editor
     */
    private void showExamples() {
    
        //TODO: assembler and lang example
        String hex = "02 00 000F; Sets register 00 to F (16)\n";
        hex += "03 00 0001; Adds 1 to register 00 "; 
        
        String assembly = "_start:\n" +
        "	set, 00, FF;	stores ff in register 00\n" +
        "	set, 01, FF;\n" +
        "_end";
        
        List<String> examples = new ArrayList();
        examples.add("Hex");
        examples.add("Assembly");
        examples.add("Lang");
        
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Hex", examples);
        dialog.setTitle("Examples");
        dialog.setHeaderText("Diffrent Examples are listed below");
        dialog.setContentText("Choose one:");
        
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            
            switch (result.get()) {
                case "Hex":
                    this.inputArea.setText(hex);
                    break;
                case "Assembly":
                    this.inputArea.setText(assembly);
                    break;
                default:
                    throw new AssertionError();
            }

        }
    };
    
    /**
     * Shows the Floating Point number converter
     */
    private void showConverter() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ConverterWindow.fxml"));
            Parent root = (Parent)loader.load();
            
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
            
            stage.setTitle("Help");
            
            //Prevent resizing of main window
            stage.setResizable(false);
            
        } catch (IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    };
    
    /**
     * Shows About Panel 
     * TODO: Add proper text and links
     */
    private void showAbout() {
        Dialog d = new Dialog();
        d.getDialogPane().getButtonTypes().add(ButtonType.OK);
        d.setTitle("About");
        d.setHeaderText("About NEO");
        d.setContentText("Created by Lorenzo Baldassarri (c) 2018" + "\n" + "Version: 0.1 alpha");
        d.showAndWait();
    };
    
    /**
     * Allows advanced users to change certain options of program
     */
    private void showDevMenu() {
        
    };
    
    /**
     * Handles possible exceptions from all classes
     * @param e
     * @param type
     * @param header
     * @param text 
     */
    public static void exceptionHandler(Exception e, int type, String header, String text) {
        
        Alert alert;
        
        switch (type) {
            case 0:
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("ERROR");
                break;
            case 1:
                alert = new Alert(AlertType.WARNING);
                alert.setTitle("WARNING");
                break;
            case 2:
                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("INFO");
                break;
            case 3:
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirm");
                break;
            default:
                throw new AssertionError();
        }
        
        alert.setHeaderText(header);
        alert.setContentText(text);
        alert.show();
    };
}
