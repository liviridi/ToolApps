package com.liviridi.tools.desptn.strategy;

import com.liviridi.tools.desptn.strategy.duck.Duck;
import com.liviridi.tools.desptn.strategy.duck.VarietyDucks.NiceDuck;
import com.liviridi.tools.desptn.strategy.duck.VarietyDucks.SBDuck;

public class DuckBorn {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Duck wq;
		wq = new SBDuck();
		wq.display();
		wq.performFly();
		wq.performQuack();
		wq = new NiceDuck();
		wq.display();
		wq.performFly();
		wq.performQuack();
		
		

		wq.swim();
	}

}
