package hr.fer.zemris.java.tecaj_3.demo;

import java.util.Date;

import hr.fer.zemris.java.tecaj_3.LLStack;
import hr.fer.zemris.java.tecaj_3.SizeProvider;

public class Primjer4 {
	
	
	public static void main(String[] args) {
		LLStack stack = new LLStack();
		
		stack.push("Zagreb");
		stack.push("Slika");
		stack.push("Tramvaj");
		stack.push("Semafor");
		
		LLStack stack2 = new LLStack();
		
		stack2.push("Zemlja");
		stack2.push("Vjeverica");
		
		SizeProvider provider2 = stack2.new SizeProviderV2();
		
		SizeProvider provider1 = stack.getSizeProvider2();
		
		lijpiIspis(provider1);
		
		stack.pop();
		
		lijpiIspis(provider1);
	}
	
	
	private static void lijpiIspis(SizeProvider provider) {
		System.out.println(
				"Dana Gospodnjeg " + new Date().toString() +
				" velicina objekta je bila " + provider.getSize()
		);
	}
}
