package hr.fer.zemris.java.tecaj_8.p02;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class OtvaranjeProzora {

	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame();
			frame.setLocation(20, 50);
			frame.setSize(500, 300);
			frame.setTitle("Program tvog kompjutera");
			frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			frame.dispose();
			frame.setVisible(true);			
		});
		
	}
	
}
