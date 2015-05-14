package hr.fer.zemris.java.tecaj_8.p09;


import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class OtvaranjeProzora extends JFrame {
	
	public OtvaranjeProzora() {
		setLocation(20, 50);
		setSize(500, 300);
		setTitle("Program tvog kompjutera");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		initGUI();
	}

	private void initGUI() {
		getContentPane().setLayout(null);
		
		JLabel labela = new JLabel("Ovo je tekst labele");
		JButton gumb = new JButton("Stisni me");
		labela.setOpaque(true);
		labela.setBackground(Color.YELLOW);
		labela.setHorizontalAlignment(SwingConstants.CENTER);
		
		labela.setBounds(10, 10, 250, 20);
		gumb.setBounds(10, 40, 250, 25);
		
		getContentPane().add(labela);
		getContentPane().add(gumb);
	}


	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new OtvaranjeProzora();
			frame.setVisible(true);			
		});
		
	}
	
}
