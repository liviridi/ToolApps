package com.liviridi.tools.desptn.strategy.duck.VarietyDucks;

import com.liviridi.tools.desptn.strategy.behavior.impl.BigQuack;
import com.liviridi.tools.desptn.strategy.behavior.impl.FlyNoWay;
import com.liviridi.tools.desptn.strategy.duck.Duck;

public class SBDuck extends Duck {
	public SBDuck() {
		flyBehavior = new FlyNoWay();
		quackBehavior = new BigQuack();
	}

	public void display() {
		System.out.println("我像人一樣");
	}

}
