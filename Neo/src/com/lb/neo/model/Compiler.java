/*
 * Copyright (c) 2018 Lorenzo Baldassarri
 * All rights reserved.
 * Redistributions in any form must reproduce the above copyright notice.
 */
package com.lb.neo.model;

import com.lb.neo.view.MainWindowController;
import java.util.HashMap;

/**
 *
 * @author Lorenzo Baldassarri
 */
public abstract class Compiler {
    
    // private String compilerMode;
    // private String outputMode;
    
    public String complileProgram(String program, int inputMode, CPU CPUType) {
        
        String result;
        
        switch (inputMode) {
            case 0: //HEX
                CPUType.fetch(0, hexStringToExecutable(program));
                result = CPUType.dumpRegisters();
                CPUType.resetAllRegisters();
                break;
            case 1: //Assembly
                String hex = assemblyToHexString(program, CPUType);
                CPUType.fetch(0, hexStringToExecutable(hex));
                result = CPUType.dumpRegisters();
                CPUType.resetAllRegisters(); 
                break;
            default:
                throw new AssertionError();
        } 
        
        return result;
    };
    
    /**
     * Takes program in hex mode as string and saves numeric instructions into RAM
     * @param program
     * @return position of last instruction in ram
     */
    private int /*int[]*/ hexStringToExecutable(String programString) {
        
        int programEndInRam = 0;
        String program = removeComments(programString);
        
        try {
            String[] lines = program.split(";");
            
            for (int i = 0; i < lines.length; i++) {
               
                String line = lines[i].replace(" ", "").trim();
                
                if (line == null | "".equals(line)) {
                    //EmpyLine dont throw exception
                } else {       
                    
                    if (line.length() != 8) {
                        throw new Exception("Invalid Syntax! Line: " + i+1);
                    }
                    //System.out.println(line);
                    
                    
                    RAM.setValue(i, Long.parseLong(line, 16));
                    //RAM.setValue(i, Integer.parseInt(line, 16));
                    programEndInRam = i;
                }
            }
            
        } catch (NumberFormatException ne) {
            //System.out.println("ERROR: " + ne.toString());
            String header = "An error occured!";
            String text = ne.toString() + "\n" + "Something went wrong!";
            MainWindowController.exceptionHandler(ne, 0, header, text);
            
        } catch (Exception e) {
            //System.out.println("ERROR: " + e.toString());
            String header = "An error occured!";
            String text = e.toString() + "\n\n"; 
            text += "Hex should be 8 digits long and no 0x prefix is needed.";
            text += "\n";
            text += "Don't forget the ; after every line!";
            text += "\n";
            text += "Escape characters such as \\n are not allowed!";
            text += "\n";
            text += "Proper syntax example: 02 00 FFFF;";
            MainWindowController.exceptionHandler(e, 0, header, text);
        }

        return programEndInRam;
    };
    
    /**
     * Takes program in assembly mode and translates it to hex string
     * @param programString
     * @param CPUType
     * @return 
     */
    private String assemblyToHexString(String programString, CPU CPUType) {
        
        HashMap instructionSet = CPUType.getInstructionSet();
        
        String command;
        String address;
        String value;
        int instructionId = 0;
        String programInHexString = "";
        int indexOfStart = programString.indexOf("_start:") + 7;  
        // TODO: variables
        // int indexOfData = programString.indexOf("section .data");
        int indexOfData = programString.indexOf("_end");
        
        String assemblyInstructions = programString.substring(indexOfStart, indexOfData);    
        assemblyInstructions = removeComments(assemblyInstructions);
        
        try {
            String[] lines = assemblyInstructions.split(";");
            
            for (String lineRaw : lines) {
                String line = lineRaw.replace(" ", "").trim();
                if (!"".equals(line)) {
            
                    String[] components = line.split(",");
                
                    command = components[0].toUpperCase();
                    address = components[1];
                    value = components[2];
                                        
                    if (instructionSet.containsValue(command) == true) {
                        
                        for (int j = 0; j < instructionSet.size(); j++) {
                            String cmd = instructionSet.get(j).toString();                           
                            if (cmd.equals(command)) {
                                instructionId = j;
                            }
                        }
                        
                        // CHECK If value is mov command behaves wrong change to 
                        // correct hex string
                        // CC RR VVVV
                        if (instructionId == 0x01) {
                            for (; value.length() < 4;) {
                                value = value + "0";
                            }
                        } else {
                            for (; value.length() < 4;) {
                                value = "0" + value;
                            }
                        }

                        String cmdHex = String.format("%02x", instructionId);
                        
                        String instruction = cmdHex + address + value;
                        programInHexString += instruction + ";\n";
                        
                    } else {
                        throw new Exception("Command not found!");                        
                    }
                }
            }        
        } catch (Exception e) {
            //TODO: exception handler
            System.out.println("EXCEPTION" + e);
        }
        
        //TODO: handle data sector with variables
        return programInHexString;
    };
    
    private String langToAssembly(String programString) {
    
        String programWithoutWhiteSpace = programString.trim().replace(" ", "");
        
        // Get everything inside the main function
        int entryPoint = programWithoutWhiteSpace.indexOf("main(){")+7;
        int end = programWithoutWhiteSpace.length()-1;
        String instructions = programWithoutWhiteSpace.substring(entryPoint, end);
        
        try {
            String[] lines = instructions.split(";");
            /*
            Check Datatypes -> int string float
            Check loops and alteration
            later: array, Objects
            Translate
            Return Assembly
            */
        } catch (Exception e) {
        }
        
        return "";
    };
    
    private String removeComments(String programString) {
        String program = "";
        char[] programInChar = programString.toCharArray();
        boolean semicolon = false;
        // Loops through char array and disrgeards everything between ; and \n
        for (int i = 0; i < programString.length(); i++) {
            char c = programInChar[i];
            if (semicolon) {
                //DontAdd
            } else {
                program += c;
            }
            if (c == ';') {
                semicolon = true;  
            } else if (c == '\n') {
                semicolon = false;
            }
        }
        return program;
    }
}
