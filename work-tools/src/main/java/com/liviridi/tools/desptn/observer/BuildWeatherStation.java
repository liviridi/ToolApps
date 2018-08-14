package com.liviridi.tools.desptn.observer;

import com.liviridi.tools.desptn.observer.observerdisp.impl.CurrentConditionsDisplay;
import com.liviridi.tools.desptn.observer.subject.impl.WeatherData;

/**
 * 观察者模式
 * @author liyun
 *
 */
public class BuildWeatherStation {

	public static void main(String[] args) {
		WeatherData weatherStation = new WeatherData();

		CurrentConditionsDisplay stationInfoGetter = new CurrentConditionsDisplay();
		weatherStation.registerObserver(stationInfoGetter);

		weatherStation.setMeasurements(80, 65, 30.4f);
		weatherStation.setMeasurements(82, 70, 30.4f);
		stationInfoGetter.display();
	}

}
