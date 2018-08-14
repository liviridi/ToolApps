package com.liviridi.tools.desptn.template;

import java.util.Iterator;

/**
 * 模板方法模式 java api 的 arrays.sort() java api 中还有 java.io的InputStream的read()类似
 *
 * @author liyun
 *
 */
public class Sort implements Iterator<Object> {
	private static void mergeSort(Object dest[]) {
		for (int i = 0; i < dest.length; i++) {
		    try {
			for (int j = i; j > 0
					&& ((Comparable<Object>) dest[j - 1])
							.compareTo((Object) dest[j]) > 0; j--) {
				swap(dest, j, j - 1);
			}
		    } catch (ClassCastException ccex) {
		        ccex.printStackTrace();
		    }

		}
	}

	private static void swap(Object src[], int i, int j) {
		Object temp = src[i];
		src[i] = src[j];
		src[j] = temp;
	}

	public static void main(String[] args) {
		Duck[] ducks = {
			new Duck("duck1", 8),
			new Duck("duck2", 2),
			new Duck("duck3", 18),
			new Duck("duck4", 9),
			new Duck("duck5", 8)
		};

		// Arrays.sort(ducks);
		mergeSort(ducks);
		for (Duck duck : ducks) {
			System.out.println(duck);
		}
	}

	@Override
	public boolean hasNext() {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override
	public Object next() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public void remove() {
		// TODO 自動生成されたメソッド・スタブ

	}
}
