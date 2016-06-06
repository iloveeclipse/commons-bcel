package org.apache.bcel.generic;

import org.apache.bcel.generic.ClassGenException;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.InstructionConst;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.NOP;
import org.apache.bcel.generic.TABLESWITCH;
import org.junit.Assert;
import org.junit.Test;

public class InstructionHandleTestCase {

    // Test that setInstruction only allows Instructions that are not BranchInstructions

    @Test(expected=ClassGenException.class)
    public void testsetInstructionNull() {
        InstructionHandle ih = InstructionHandle.getInstructionHandle(new NOP());// have to start with a valid non BI
        Assert.assertNotNull(ih);
        ih.setInstruction(null);
        Assert.assertNotNull(ih);
    }

    @Test
    public void testsetInstructionI() {
        InstructionHandle ih = InstructionHandle.getInstructionHandle(new NOP());// have to start with a valid non BI
        Assert.assertNotNull(ih);
        ih.setInstruction(new NOP());        
        Assert.assertNotNull(ih);
    }

    @Test(expected=ClassGenException.class)
    public void testsetInstructionnotI() {
        InstructionHandle ih = InstructionHandle.getInstructionHandle(new NOP());// have to start with a valid non BI
        Assert.assertNotNull(ih);
        ih.setInstruction(new GOTO(null));        
        Assert.assertNotNull(ih);
    }

    @Test(expected=ClassGenException.class)
    public void testGetIHnull() {
        InstructionHandle.getInstructionHandle(null); 
    }

    @Test
    public void testBCEL195() {
        InstructionList il = new InstructionList();
        InstructionHandle ih = il.append(InstructionConst.NOP);
        new TABLESWITCH(new int[0], new InstructionHandle[0], ih);
        new TABLESWITCH(new int[0], new InstructionHandle[0], ih);
    }
}
