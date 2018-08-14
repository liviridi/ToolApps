package com.liviridi.tools.desptn.adapter;

import com.liviridi.tools.desptn.adapter.turkey.WildTurkey;
import com.liviridi.tools.desptn.strategy.duck.Duck;

public class TurkeyAdapter extends Duck {
	WildTurkey turkey;

	public TurkeyAdapter(WildTurkey turkey) {
		this.turkey = turkey;
	}

	public void display() {
		System.out.println("am i a duck?");
	}

	public void performFly() {
		turkey.Fly();
	}

	public void performQuack() {
		turkey.Gobble();
	}

	public void swim() {
		turkey.swim();
	}
}
