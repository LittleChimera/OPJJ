package hr.fer.zemris.java.tecaj_8.p06;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
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
		
		getContentPane().setBackground(Color.ORANGE);
		MojaKomponenta komponenta = new MojaKomponenta();
		komponenta.setLocation(20, 50);
		komponenta.setSize(200, 100);
		komponenta.setBorder(BorderFactory.createLineBorder(Color.RED, 4));
		komponenta.setOpaque(true);
		komponenta.setBackground(Color.WHITE);
		komponenta.setForeground(Color.BLACK);
		
		getContentPane().add(komponenta);
	}
	
	private static class MojaKomponenta extends JComponent {
		
		private String vrijeme;

		public MojaKomponenta() {
			Thread thread = new Thread(() -> {
				while (true) {
					SwingUtilities.invokeLater(() -> {
						vrijeme = new Date().toString();
						repaint();
					}); 
					try {
						Thread.sleep(500);
					} catch (Exception ignorable) {
					}
				}
			});
			thread.setDaemon(true);
			thread.start();
			
			vrijeme = new Date().toString();
		}
		
		@Override
		protected void paintComponent(Graphics g) {
			Graphics2D g2d = (Graphics2D)g.create();
			
			Dimension size = getSize();
			Insets ins = getInsets();
			
			Rectangle rect = new Rectangle(
				ins.left,
				ins.top,
				size.width - ins.left - ins.right,
				size.height - ins.top - ins.bottom
			);
			
			if (isOpaque()) {
				g2d.setColor(getBackground());
				g2d.fillRect(rect.x, rect.y, rect.width, rect.height);
			}
			
			g2d.setColor(getForeground());
			FontMetrics fm = g2d.getFontMetrics();
			int sirina = fm.stringWidth(vrijeme);
			g2d.drawString(
					vrijeme,
					rect.x + (rect.width-sirina)/2, 
					rect.y + rect.height - (rect.height-fm.getAscent())/2);
			
			g2d.dispose();
		}
	}

	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new OtvaranjeProzora();
			frame.setVisible(true);			
		});
		
	}
	
}
