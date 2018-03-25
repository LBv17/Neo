/*
 * Copyright (c) 2018 Lorenzo Baldassarri
 * All rights reserved.
 * Redistributions in any form must reproduce the above copyright notice.
 */
package com.lb.neo.model;

import java.util.HashMap;

/**
 *
 * @author LB
 */
public class Neo implements CPU {

    @Override
    public void fetch(int startRegRAM, int endRegRAM) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String dumpRegisters() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public HashMap getInstructionSet() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resetAllRegisters() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String outputReadable() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
