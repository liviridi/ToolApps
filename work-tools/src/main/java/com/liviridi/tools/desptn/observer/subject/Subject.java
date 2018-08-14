package com.liviridi.tools.desptn.observer.subject;

import com.liviridi.tools.desptn.observer.observerdisp.Observer;

public interface Subject {
	public void registerObserver(Observer o);
	public void removeObserver(Observer o);
	public void notifyObservers();
}
