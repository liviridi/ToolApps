package com.liviridi.tools.desptn.adapter;

import com.liviridi.tools.desptn.adapter.turkey.WildTurkey;
import com.liviridi.tools.desptn.strategy.duck.Duck;
import com.liviridi.tools.desptn.strategy.duck.VarietyDucks.NiceDuck;

/**
 * 适配器模式
 * @author liyun
 *
 */
public class AdaptDrive {
	//private const  m = 1;
	//private static final int aaa = 1;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		WildTurkey turkey = new WildTurkey();
		turkey.display();
		turkey.Fly();
		turkey.Gobble();
		turkey.swim();

		Duck wq;
		wq = new NiceDuck();
		wq.display();
		wq.performFly();
		wq.performQuack();
		wq.swim();

		wq = new TurkeyAdapter(turkey);
		wq.display();
		wq.performFly();
		wq.performQuack();
		wq.swim();
	}

}
