package com.liviridi.tools.desptn.state;

import com.liviridi.tools.desptn.state.stateMachines.GumballMachine;

public class MachineRunner {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO 自動生成されたメソッド・スタブ
        GumballMachine gbm = new GumballMachine(3);
        gbm.insertQuarter();
        gbm.turnCrank();
        
        //gbm.dispense();
    }

}
