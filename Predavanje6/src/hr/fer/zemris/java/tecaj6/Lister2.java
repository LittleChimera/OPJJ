package hr.fer.zemris.java.tecaj6;

import java.io.File;

public class Lister2 {
	
	private static class IndentiraniIspis implements IPosao {
		
		private int indentLevel;
		

		@Override
		public void priUlaskuUDirektorij(File dir) {
			if (indentLevel == 0) {
				System.out.println(dir.getName());
			} else {
				System.out.format("%" + indentLevel + "s%s%n", "", dir.getName());
			}
			
			indentLevel += 2;
		}

		@Override
		public void priIzlaskuIzDirektorija(File dir) {
			indentLevel -= 2;
		}

		@Override
		public void priNailaskuNaFile(File dat) {
			System.out.format("%" + indentLevel + "s%s%n", "", dat.getName());
		}
		
	}
	
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Expected one argument!");
			System.exit(1);
		}
		String path = args[0];
		
		File root = new File(path);
		if (!root.isDirectory()) {
			System.out.println("Argument should be a directory.");
			System.exit(2);
		}
		if (!root.exists()) {
			System.out.println("Given file doesn't exist");
			System.exit(3);
		}
		System.out.println(root.getAbsolutePath());
		ListerUtility.branchTree(root, new IndentiraniIspis());
		
	}
}
