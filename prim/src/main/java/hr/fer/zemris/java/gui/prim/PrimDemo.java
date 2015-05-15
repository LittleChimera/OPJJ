package hr.fer.zemris.java.gui.prim;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class PrimDemo extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PrimDemo() {
		setLocation(200, 100);
		setSize(500, 300);
		setTitle("PrimDemo");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		initGUI();
	}

	private void initGUI() {
		getContentPane().setLayout(new BorderLayout());
		
		PrimListModel primModel = new PrimListModel();
		JList<Integer> primListLeft = new JList<Integer>(primModel);
		JList<Integer> primListRight = new JList<Integer>(primModel);
		
		JPanel centerPanel = new JPanel(new GridLayout(1, 0));
		centerPanel.add(primListLeft);
		centerPanel.add(primListRight);
		JScrollPane scrollableCenter = new JScrollPane(centerPanel);
		
		JButton nextPrim = new JButton("Generate next prim");
		nextPrim.addActionListener(e -> {
			SwingUtilities.invokeLater(() -> {
				primModel.next();
				primListLeft.revalidate();
				primListLeft.repaint();
				primListRight.revalidate();
				primListRight.repaint();
			});
		});
		getContentPane().add(nextPrim, BorderLayout.PAGE_END);
		
		getContentPane().add(scrollableCenter, BorderLayout.CENTER);
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new PrimDemo();
			// frame.pack();
			frame.setVisible(true);
		});
	}

}
