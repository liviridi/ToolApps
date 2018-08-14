package com.liviridi.tools.desptn.main;

import java.io.FileNotFoundException;
import java.util.Random;
import java.util.regex.Pattern;

import com.liviridi.tools.desptn.observer.observerdisp.impl.CurrentConditionsDisplay;
import com.liviridi.tools.desptn.observer.subject.impl.WeatherData;
import com.liviridi.tools.desptn.strategy.duck.Duck;
import com.liviridi.tools.desptn.strategy.duck.VarietyDucks.SBDuck;
import com.liviridi.tools.xmlwriter.XmlWriter;


public class Main {

	/**
	 * @param args
	 * @throws FileNotFoundException
	 * @throws ClassNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException {
        String mString[] = "12.55".split("0-9|.");
        System.out.println("**" + "12.55".indexOf("."));
//		strategyTest();
//		observerTest();
//		double x = 13052.0;
//		x /= 10;
//		System.out.println(x);
//      System.out.println(x[0]);
//      System.out.println(isStrNumber("00000"));
//        System.out.println('B'-'s');
//		String xx = "888ddds.";
//		String yy = "nimei";
//		List<String> list = new ArrayList<String>();
//		list.add(xx);
//		list.add(yy);
//		String[] x =  {};x = list.toArray(x);
//        int[] f = {11111,12};
//        x = xxxx(xx);
	    String s = new String("sssk");
	    String m = new String("sssk");
        String k = "sssk";

        Random rand =new Random();
        //System.out.println(6 & -6);
        System.out.println(rand.nextInt(100));
        System.out.println(10L<<32);
        System.out.println(1L<<32);
        System.out.println("m:" + m);
        System.out.println("k:" + m);
        System.out.println("==**::" + (k == m));
        System.out.println("eq**::" + (k.equals(m)));
        XmlWriter.WriteXml();

        String ssss = "shdsdfsdfsdfsdf";
        String[] xStrings = ssss.split(",");
        System.out.println("hehe");
	}

	public static String[] xxxx(String ...xBytes) {
	    for(String xB : xBytes) {
	        System.out.println(xB);
	    }

	    return xBytes;
	}

	public static String ffff(Integer ...xInteger) {
	    return xInteger.toString();
	}

	public static boolean isStrNumber(String str) {

    	if ("".equals(str)) {
    		return false;
    	}
        Pattern pattern = Pattern.compile("^[0-9]*$");
        return pattern.matcher(str).matches();
    }

	private static void strategyTest() {
		Duck wq = new SBDuck();
		wq.display();
		wq.performFly();
		wq.performQuack();
	}

	private static void observerTest() {
		WeatherData weatherStation = new WeatherData();

		weatherStation.registerObserver(new CurrentConditionsDisplay());

		weatherStation.setMeasurements(80, 65, 30.4f);
		weatherStation.setMeasurements(82, 70, 30.4f);
	}

}

