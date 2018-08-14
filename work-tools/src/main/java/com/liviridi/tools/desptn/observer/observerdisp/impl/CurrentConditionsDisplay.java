package com.liviridi.tools.desptn.observer.observerdisp.impl;

import com.liviridi.tools.desptn.observer.observerdisp.DisplayElement;
import com.liviridi.tools.desptn.observer.observerdisp.Observer;

public class CurrentConditionsDisplay implements Observer, DisplayElement {
	private float temperature;
	private float humidity;

	public CurrentConditionsDisplay() {
	}

	public void update(float temperature, float humidity, float pressure){
		this.temperature = temperature;
		this.humidity = humidity;
		display();
	}

	public void display(){
		System.out.println("今温度：" + temperature + "℃");
		System.out.println("hehe湿度：" + humidity + "％");
		System.out.println("以上。");
		System.out.println();
	}
}
