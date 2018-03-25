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
public class RAM {
    
    private static HashMap varRegisters = new HashMap();
    //private static int[] registers = new int[0x200];
    private static long[] registers = new long[0x200];
    private static RAM ramInstance = new RAM();
    
    private RAM() {};
    
    public static RAM getInstance() {
        return ramInstance;
    }
    
    public static /*int[]*/ long[] getProgram(int startReg, int endReg) {

        //int[] instructions = new int[0x100];
        long[] instructions = new long[0x100];
        //System.out.println(registers[0]);
        
        for (int i = 0; i <= endReg; i++) {
            //System.out.println(registers[i]);
            //instructions[i] = registers[i];
            instructions[i] = registers[i];
        }
        /*
        for (;startReg <= endReg; startReg++) {
            System.out.println(registers.get(startReg));
            instructions[startReg] = registers.get(startReg);
        }*/
        return instructions;
    }
    
    public static /*int*/ long getValue(int reg) {
        //return registers.get(reg);
        return registers[reg];
    }
    
    public static void setValue(int reg, /*int*/ long value) {
        //registers.put(reg, value);
        registers[reg] = value;
    }
    
    public static int getVariable(int name) {
        //TODO:
        return 0;
    }
    
    public static void setVariable(int name, int value) {
        //TODO: 
    }
}
