package com.liviridi.tools.desptn.template;

public class Duck implements Comparable<Duck> {
	private String name;
	private int weight;

	public Duck(String name, int weight) {
		this.name = name;
		this.weight = weight;
	}

	public String toString() {
		return name;
	}
	@Override
	public int compareTo(Duck arg0) {
		return weight - arg0.weight;
		//return this.name.compareTo(arg0.name);
	}

}
