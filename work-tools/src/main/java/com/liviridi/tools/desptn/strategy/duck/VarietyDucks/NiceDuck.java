package com.liviridi.tools.desptn.strategy.duck.VarietyDucks;

import com.liviridi.tools.desptn.strategy.behavior.impl.BigQuack;
import com.liviridi.tools.desptn.strategy.behavior.impl.FlyHigh;
import com.liviridi.tools.desptn.strategy.duck.Duck;

public class NiceDuck extends Duck {
	public NiceDuck() {
		flyBehavior = new FlyHigh();
		quackBehavior = new BigQuack();
	}

	public void display() {
		System.out.println("帥氣");
	}
}
