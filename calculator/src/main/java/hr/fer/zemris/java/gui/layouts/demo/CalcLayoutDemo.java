package hr.fer.zemris.java.gui.layouts.demo;

import java.awt.BorderLayout;
import java.awt.Color;

import hr.fer.zemris.java.gui.layouts.CalcLayout;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class CalcLayoutDemo extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CalcLayoutDemo() {
		setLocation(20, 50);
		setSize(500, 300);
		setTitle("Calc layout");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout());

		initGUI();
	}

	private void initGUI() {
		JPanel p = new JPanel(new CalcLayout(3));
		p.add(new JButton("x"), "1,1");
		p.add(new JButton("y"), "2,3");
		p.add(new JButton("z"), "2,7");
		p.add(new JButton("w"), "4,2");
		p.add(new JButton("a"), "4,5");
		p.add(new JButton("b"), "4,7");
		
		p.setBackground(Color.ORANGE);
		add(p, BorderLayout.CENTER);
	}

	public static void main(String[] args) {

		SwingUtilities.invokeLater(() -> {
			JFrame frame = new CalcLayoutDemo();
			frame.pack();
			frame.setVisible(true);
		});

	}

}
