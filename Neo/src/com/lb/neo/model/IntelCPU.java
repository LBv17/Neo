/*
 * Copyright (c) 2018 Lorenzo Baldassarri
 * All rights reserved.
 * Redistributions in any form must reproduce the above copyright notice.
 */
package com.lb.neo.model;

import java.util.HashMap;

/**
 *
 * @author Lorenzo Baldassarri
 */
public class IntelCPU implements CPU {

    private int programCounter = 0x0;
    
    private long instructionRegister = 0x0;
    //private int accumulator = 0x0;
    private int programSize = 0x0;
    
    private int opCode = 0x0;
   
    //New method
    private int operand = 0x0;
   
    /* @legacy
    private int operandOne = 0x0;
    private int operandTwo = 0x0;
    */
    
    private int registerDest = 0x0;
    private int registerSrc = 0x0;
   
    private HashMap instructionSet = new HashMap();
    private final String[] instructionsAssembly = {"NOP", "MOV", "SET", "ADD", "SUB", "MUL", "DIV", "AND", "OR", "XOR"};
    
    /*
    //New method
    private int[] instructionSetInt = new int[0xFF];
    private HashMap instr = new HashMap();
    private void initArr() {
        for (int i = 0; i < instructionSetInt.length; i++) {
            instructionSetInt[i] = i;
        }
    };
    */
    
    public IntelCPU() {
        intitializeMap();
    };
    
    /**
     * Fills map with commands and they're numeric values
     */
    private void intitializeMap() {
        for (int i = 0; i < this.instructionsAssembly.length; i++) {
            this.instructionSet.put(i, instructionsAssembly[i]);
        }
    };
    
    @Override
    public void fetch(int startRegRAM, int endRegRAM) {

        // Get iprogram nstructions from RAM
        //int[] instructions = RAM.getProgram(startRegRAM, endRegRAM);
        long[] instructions = RAM.getProgram(startRegRAM, endRegRAM);
        
        // Save the program size for later operations
        this.programSize = instructions.length;
        
        // TODO: check program size max = 0x200
        for (int i = 0; i < instructions.length; i++) {
            if (instructions[i] < 1) {
                //System.out.println("NOP OR EMPTY");
            } else {
                this.programCounter = i;
                this.instructionRegister = instructions[this.programCounter];
                decodeAndExecute();
            }
        }

    };

    @Override
    public String dumpRegisters() {
        String pc = "Program Counter: " + "\t\t" + String.format("0x%08x", this.programCounter);
        String ir = "Instruction Register: " + "\t" + String.format("0x%08x", this.instructionRegister);
        String separator = "--------------------------------------";
        String content = "";

        content += separator + "\n";
        content += "CPU Registers: \n";
        content += pc + "\n" + ir + "\n";
        content += separator + "\n";
        content += "RAM Registers: \n";
        
        for (int i = 0x0+this.programSize;i < 0x200;i++) {
            long ramValue = RAM.getValue(i);
            if (ramValue != 0) {
                System.out.println(ramValue);
                content += "Register: " + String.format("0x%03x",(i)); 
                content += " Value: " + String.format("0x%08x", ramValue) + "\n";
                //System.out.println("Register: 0x" + String.format("0x%04x",(i-this.programSize)) + " Value: 0x" + String.format("%08X", ramValue));
            }
        }
        
        content+=separator;
        /*
        System.out.println("=================================");
        System.out.println("CPU Registers:");        
        System.out.println("Program Counter: " + pc);
        System.out.println("Instruction Register: " + ir);
        System.out.println("---------------------------------");
        System.out.println("RAM Registers:");
        
        for (int i = 0x0+this.programSize;i < 0x400;i++) {
            int ramValue = RAM.getValue(i);
            if (ramValue != 0) {
                System.out.println("Register: 0x" + String.format("0x%04x",(i-this.programSize)) + " Value: 0x" + String.format("%08X", ramValue));
            }
        }*/
        
        return content;
    };

    @Override
    public HashMap getInstructionSet() {
        return this.instructionSet;
    };
    
    private void decodeAndExecute() {
        
        // Bitshift so only first 2 hex digits are saved
        this.opCode = (int) (this.instructionRegister >> 24 & 0xff);
        
        
        if (this.opCode > 0x02 && this.opCode < 0x0a) {
            
            // gets the 3rd and 4th digit 
            this.registerDest = (int) (this.instructionRegister >> 16 & 0xff);
            
            // gets last 4 digits
            this.operand = (int) (this.instructionRegister & 0xffff); 
           
            /* @legacy
            this.operandOne = this.instructionRegister >> 8 & 0xff;
            this.operandTwo = this.instructionRegister & 0xff;
            */
            
            ALU();
        } else {
            
            switch (this.opCode) {
                case 0x00: // NOP
                    System.out.println("NOP");
                    break;
                case 0x01: // MOV
                    /*this.registerDest = this.instructionRegister >> 16 & 0xff;*/
                    this.registerSrc = (int) (this.instructionRegister >> 8 & 0xff);
                    RAM.setValue(this.registerDest+this.programSize, RAM.getValue(this.registerSrc+this.programSize));
                    break;
                case 0x02: // SET
                    this.registerDest = (int) (this.instructionRegister >> 16 & 0xff);
                    RAM.setValue(this.registerDest+this.programSize, (this.instructionRegister & 0xffff));
                    break;
                    /*
                case 0xEE: // in
                    // write inpput to register EE XX VALUE;
                    break;
                    */
                case 0xFF: // out
                    // output register from x - n FF FR TO 00;
                    // 00 aa bb OFFSET
                    int from = this.registerDest;
                    int to = (int) (this.instructionRegister >> 8 & 0xff);
                    /*
                    int[] toprint = null;
                    int counter = 0;
                    */
                    for (; from < to; from++) {
                        System.out.println(RAM.getValue(from));
                    }
                    
                    break;
                    /*
                case 0x1D: // DB literal var
                    break;
                case 0x2D: // DW numeric var
                    break;
                    */
                default:
                    throw new AssertionError();
            }
        }
    };
    
    private void ALU() {
        switch (this.opCode) {
            case 0x03:
                long valueToAddTo = RAM.getValue(this.registerDest+this.programSize);
                RAM.setValue(this.registerDest+this.programSize, valueToAddTo+this.operand);
                /*
                RAM.setValue((this.registerDest+this.programSize), (this.operandOne+this.operandTwo));
                */
                break;
            case 0x04:
                long valueToSubTo = RAM.getValue(this.registerDest+this.programSize);
                RAM.setValue(this.registerDest+this.programSize, valueToSubTo-this.operand);
                /* @legacy
                RAM.setValue((this.registerDest+this.programSize), (this.operandOne-this.operandTwo));
                */
                break;
            case 0x05:
                long valueToMulWith = RAM.getValue(this.registerDest+this.programSize);
                RAM.setValue(this.registerDest+this.programSize, valueToMulWith+this.operand);
                /* @legacy
                RAM.setValue((this.registerDest+this.programSize), (this.operandOne*this.operandTwo));
                */
                break;
            case 0x06:
                long valueToDivWith = RAM.getValue(this.registerDest+this.programSize);
                RAM.setValue(this.registerDest+this.programSize, valueToDivWith+this.operand);
                /* @legacy
                RAM.setValue((this.registerDest+this.programSize), (this.operandOne/this.operandTwo));
                */
                break;
            case 0x07:
                long valueToAND = RAM.getValue(this.registerDest+this.programSize);
                RAM.setValue(this.registerDest+this.programSize, valueToAND & this.operand);
                break;
            case 0x08:
                long valueToOR = RAM.getValue(this.registerDest+this.programSize);
                RAM.setValue(this.registerDest+this.programSize, valueToOR | this.operand);
                break;
            case 0x09:
                long valueToXOR = RAM.getValue(this.registerDest+this.programSize);
                RAM.setValue(this.registerDest+this.programSize, valueToXOR ^ this.operand);
                break;
            default:
                throw new AssertionError();
        }
    };
    
    @Override
    public void resetAllRegisters() {
        this.programCounter = 0x0;
        this.instructionRegister = 0x0; 
        //this.accumulator = 0x0;
        this.programSize = 0x0;
        this.opCode = 0x0;
        this.operand = 0x0;
        /* @legacy
        this.operandOne = 0x0;
        this.operandTwo = 0x0;
        */
        this.registerSrc = 0x0;
        this.registerDest = 0x0;
    };

    //TODO
    @Override
    public String outputReadable() {
        
        
        return "";
    }
}
