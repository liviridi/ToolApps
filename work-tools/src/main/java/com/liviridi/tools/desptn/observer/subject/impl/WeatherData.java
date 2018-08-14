package com.liviridi.tools.desptn.observer.subject.impl;

import java.util.ArrayList;

import com.liviridi.tools.desptn.observer.observerdisp.Observer;
import com.liviridi.tools.desptn.observer.subject.Subject;

public class WeatherData implements Subject {
	private ArrayList<Object> observers;
	private float temperature;
	private float humidity;
	private float pressure;

	public WeatherData() {
		observers = new ArrayList<Object>();
	}
	public void registerObserver(Observer o) {
		observers.add(o);
	}

	public void removeObserver(Observer o) {
		int i = observers.indexOf(o);
		if (i >= 0) {
			observers.remove(o);
		}
	}

	public void notifyObservers() {
		for (Object ob : observers) {
			Observer observer = (Observer) ob;
			observer.update(temperature, humidity, pressure);
		}
	}

	public void measurementsChanged() {
		notifyObservers();
	}

	public void setMeasurements(float temperature, float humidity, float pressure) {
		this.temperature = temperature;
		this.humidity = humidity;
		this.pressure = pressure;
		measurementsChanged();
	}
}
