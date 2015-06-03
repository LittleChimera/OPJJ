package hr.fer.zemris.java.hw12.jvdraw;

import hr.fer.zemris.java.hw12.jvdraw.colors.JColorArea;
import hr.fer.zemris.java.hw12.jvdraw.drawing.DrawingModel;
import hr.fer.zemris.java.hw12.jvdraw.drawing.JDrawingCanvas;
import hr.fer.zemris.java.hw12.jvdraw.objects.FullCircleDrawing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class JVDraw extends JFrame {

	/**
	 * Serial
	 */
	private static final long serialVersionUID = 1L;
	
	private JDrawingCanvas drawingCanvas;
	private DrawingModel drawingModel;
	
	public JVDraw() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(600, 400);
		initGUI();
	}

	private void initGUI() {
		getContentPane().setLayout(new BorderLayout());

		drawingModel = new DrawingModel();
		drawingCanvas = new JDrawingCanvas(drawingModel);
		getContentPane().add(drawingCanvas, BorderLayout.CENTER);

		JPanel topPanel = new JPanel(new FlowLayout());

		JColorArea background = new JColorArea(Color.WHITE);
		initColorArea(background, "background");

		JColorArea foreground = new JColorArea(Color.BLACK);
		initColorArea(foreground, "foreground");

		topPanel.add(foreground);
		topPanel.add(background);
		topPanel.add(new JButton(new AbstractAction() {

			{
				setTitle("Generate circle");
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				Random random = new Random();
				drawingModel.addObject(new FullCircleDrawing(300 + random
						.nextInt() % 100, 200 + random.nextInt() % 100, 20,
						background.getCurrentColor(), foreground
								.getCurrentColor()));
			}
		}));

		getContentPane().add(topPanel, BorderLayout.PAGE_START);
	}
	
	private void initColorArea(JColorArea colorArea, String name) {
		colorArea.addColorChangeListener((src, oldC, newC) -> {
			if (!oldC.equals(newC)) {
				drawingCanvas.setBackground(newC);
			}
		});
		
		colorArea.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Color choosen = JColorChooser.showDialog(JVDraw.this,
						"Choose " + name + " color", colorArea.getCurrentColor());
				if (choosen != colorArea.getCurrentColor()) {
					colorArea.setColor(choosen);
					colorArea.repaint();
				}
			}
		});
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JVDraw();
			frame.pack();
			frame.setVisible(true);
		});
	}

}
