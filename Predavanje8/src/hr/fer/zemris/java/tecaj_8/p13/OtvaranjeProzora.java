package hr.fer.zemris.java.tecaj_8.p13;


import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
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
		labela.setOpaque(true);
		labela.setBackground(Color.YELLOW);
		labela.setHorizontalAlignment(SwingConstants.CENTER);
		
		JPanel donjiPanel = new JPanel(new GridLayout(1, 0));
		
		donjiPanel.add(new JLabel("Unesi broj: "));
		JTextField unosBroja = new JTextField();
		donjiPanel.add(unosBroja);
		JButton izvrsi = new JButton("Izvrsi");
		donjiPanel.add(izvrsi);
		
		izvrsi.addActionListener((e) -> {
			obrada(unosBroja.getText(), labela);
		});
		
		
		getContentPane().add(labela, BorderLayout.CENTER);
		getContentPane().add(donjiPanel, BorderLayout.PAGE_END);
	}


	private void obrada(String text, JLabel labela) {
		try {
			double broj = Double.parseDouble(text);
			double rezultat = broj * broj;
			labela.setText(Double.toString(rezultat));
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "Tekst " + text + " nije moguce pretvoriti u broj.", "Pogreska!", JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new OtvaranjeProzora();
			// frame.pack();
			frame.setVisible(true);			
		});
		
	}
	
}
