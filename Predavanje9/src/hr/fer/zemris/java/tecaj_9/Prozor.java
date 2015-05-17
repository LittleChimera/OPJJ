package hr.fer.zemris.java.tecaj_9;

import java.awt.FlowLayout;


import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.plaf.ButtonUI;
import javax.swing.plaf.metal.MetalButtonUI;

public class Prozor extends JFrame {
	
	public Prozor() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(500, 200);
		getContentPane().setLayout(new FlowLayout());
		getContentPane().add(new JLabel("lalal"));
		getContentPane().add(new JButton("stiskaj"));
		getContentPane().add(new JCheckBox("oznaciii"));
		getContentPane().add(new JRadioButton("lalfasdf"));
		
		JButton gumb2 = new JButton("Stiiisni UI");
		ButtonUI delegat = (ButtonUI) MetalButtonUI.createUI(gumb2);
		gumb2.setUI(delegat);
		getContentPane().add(gumb2);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			setupApp();
		});
	}

	private static void setupApp() {
		for (LookAndFeelInfo lafi : UIManager.getInstalledLookAndFeels()) {
			System.out.println(lafi.getClassName());
		}
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		new Prozor().setVisible(true);
	}
	
}
