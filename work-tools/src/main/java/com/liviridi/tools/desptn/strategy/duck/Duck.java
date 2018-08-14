package com.liviridi.tools.desptn.strategy.duck;

import com.liviridi.tools.desptn.strategy.behavior.FlyBehavior;
import com.liviridi.tools.desptn.strategy.behavior.QuackBehavior;

public abstract class Duck {
	protected FlyBehavior flyBehavior;
	protected QuackBehavior quackBehavior;

	public Duck() {

	}

	public abstract void display();

	public void performFly() {
		flyBehavior.fly();
	}

	public void performQuack() {
		quackBehavior.quack();
	}

	public void swim() {
		System.out.println("哥会蛙式！！");
	}
}