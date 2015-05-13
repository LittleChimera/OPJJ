package hr.fer.zemris.java.tecaj_1;

public class Demo {

	public static void main(String[] args) {
		
		int loopTimes = 10;
		
		if(args.length == 1) {
			loopTimes = Integer.parseInt(args[0]);
		}
		
		for(int i=0; i < loopTimes; i++) {
			System.out.println("Pozdrav!");
		}
	}

}
