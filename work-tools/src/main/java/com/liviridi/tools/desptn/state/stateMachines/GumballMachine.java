package com.liviridi.tools.desptn.state.stateMachines;

public class GumballMachine {
    private final static int SOLD_OUT = 0;
    private final static int NO_QUARTER = 1;
    private final static int HAS_QUARTER = 2;
    private final static int SOLD = 3;

    private static states state = states.SOLD_OUT_STATE;
    private static int gumballCnt = 0;

    public GumballMachine(int Cnt) {
        gumballCnt = Cnt;
        if (gumballCnt > 0) {
            state = states.NO_QUARTER_STATE;
        }
    }

    public void insertQuarter() {
        state.insertQuarter();
    }

    public void ejectQuarter() {
        state.ejectQuarter();
    }

    public void turnCrank() {
        state.turnCrank();
        state.dispense();
    }

    private enum states {

        SOLD_OUT_STATE(SOLD_OUT) {
            @Override
            protected void insertQuarter() {
                System.out.println("the machine has been sold out");
            }

            @Override
            protected void ejectQuarter() {
                System.out.println("you can't eject, you haven't inserted a quarter yet");
            }

            @Override
            protected void turnCrank() {
                System.out.println("you can't turn the crank, you haven't inserted a quarter yet");
            }

            @Override
            protected void dispense() {
                System.out.println("no gumball dispensed");
            }

        },
        NO_QUARTER_STATE(NO_QUARTER) {

            @Override
            protected void insertQuarter() {
                state = HAS_QUARTER_STATE;
                System.out.println("You inserted a quarter");

            }

            @Override
            protected void ejectQuarter() {
                System.out.println("you can't eject, you haven't inserted a quarter yet");
            }

            @Override
            protected void turnCrank() {
                System.out.println("you must insert a quarter before you turn");
            }

            @Override
            protected void dispense() {
                System.out.println("you need to pay first");
            }

        },
        HAS_QUARTER_STATE(HAS_QUARTER) {

            @Override
            protected void insertQuarter() {
                System.out.println("You had already inserted a quarter");
            }

            @Override
            protected void ejectQuarter() {
                state = NO_QUARTER_STATE;
                System.out.println("quarter returned");
            }

            @Override
            protected void turnCrank() {
                System.out.println("wait pls...");
                state = SOLD_STATE;
            }

            @Override
            protected void dispense() {
                System.out.println("turn the crank, pls");
            }

        },
        SOLD_STATE(SOLD) {

            @Override
            protected void insertQuarter() {
                System.out.println("wait pls");
            }

            @Override
            protected void ejectQuarter() {
                System.out.println("you already turned the crank");
            }

            @Override
            protected void turnCrank() {
                System.out.println("turning twice doesn't get you another gumball");
            }

            @Override
            protected void dispense() {
                System.out.println("A gumball comes out the slot");
                gumballCnt--;
                if(gumballCnt > 0) {
                    state = NO_QUARTER_STATE;
                } else {
                    System.out.println("Oops, out of gumballs");
                    state = SOLD_OUT_STATE;
                }
            }

        };

        states(int code) {

        }

        protected abstract void insertQuarter();

        protected abstract void ejectQuarter();

        protected abstract void turnCrank();

        protected abstract void dispense();

    }

}
