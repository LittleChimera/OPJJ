package hr.fer.zemris.java.tecaj_8.p10;


import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
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
		getContentPane().setLayout(new BorderLayout());
		
		JLabel labela = new JLabel("Ovo je tekst labele");
		JButton gumb = new JButton("Stisni me");
		labela.setOpaque(true);
		labela.setBackground(Color.YELLOW);
		labela.setHorizontalAlignment(SwingConstants.CENTER);
		
		getContentPane().add(labela, BorderLayout.CENTER);
		getContentPane().add(gumb, BorderLayout.PAGE_END);
	}


	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new OtvaranjeProzora();
			frame.setVisible(true);			
		});
		
	}
	
}
