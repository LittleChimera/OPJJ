package hr.fer.zemris.java.tecaj6;

import java.io.File;

public class ListerUtility {
	
	public static void branchTree(File root, IPosao posao) {
		
		posao.priUlaskuUDirektorij(root);
		
		for (File file : root.listFiles()) {
			if (file.isFile()) {
				posao.priNailaskuNaFile(file);
			} else if (file.isDirectory()) {
				branchTree(file, posao);
			}
		}
		
		posao.priIzlaskuIzDirektorija(root);
	}
	
	
	
	
}
