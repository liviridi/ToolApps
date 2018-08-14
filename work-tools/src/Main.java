import pattern1.duck.Duck;


public class Main {
	public static void main(String[] args) {
        String mString[] = "12.55".split(".");
        System.out.println("**" + mString.length);
		//strategyTest();
	}

	private void strategyTest() {
		Duck wq = new SBDuck();
		wq.display();
		wq.performFly();
		wq.performQuack();
	}


}