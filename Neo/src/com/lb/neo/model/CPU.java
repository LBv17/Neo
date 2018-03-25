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
public interface CPU {
    
    /**
     * Starts CPU instruction cycle fetch, decode and execute
     * 
     * @param startRegRAM
     * @param endRegRAM 
     */
    public void fetch(int startRegRAM, int endRegRAM);
    
    /**
     * Prints out special CPU registers and ram registered that were filled
     * and returns String containing the registers
     * @return 
     */
    public String dumpRegisters();  

    /**
     * Gets the CPU instruction set which is different for every CPU
     * @return 
     */
    public HashMap getInstructionSet();
    
    /**
     * Sets all CPU registers to 0x0
     */
    public void resetAllRegisters();
    
    /**
     * Actual program output, basically print, returns the readable output 
     * @return 
     */
    public String outputReadable();
    
}
