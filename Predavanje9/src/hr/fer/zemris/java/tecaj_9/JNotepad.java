package hr.fer.zemris.java.tecaj_9;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class JNotepad extends JFrame {

	private JTextArea editor;
	private Path openedFilePath;

	public JNotepad() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(500, 300);
		initGUI();
	}

	private void initGUI() {
		getContentPane().setLayout(new BorderLayout());

		editor = new JTextArea();
		getContentPane().add(new JScrollPane(editor), BorderLayout.CENTER);

		createActions();
		createMenus();
		createToolbars();
	}

	private void createActions() {
		openDocumentAction.putValue(Action.NAME, "Open");
		openDocumentAction.putValue(Action.SHORT_DESCRIPTION, "Used to open existing document");
		openDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control O"));
		openDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_O);
		
		saveDocumentAction.putValue(Action.NAME, "Save");
		
		exitAction.putValue(Action.NAME, "Exit");
		
		deleteSelectedPartAction.putValue(Action.NAME, "Delete selection");
		
		toggleCaseAction.putValue(Action.NAME, "Toggle case in selection");
	}

	private void createMenus() {		
		JMenuBar menuBar = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		
		fileMenu.add(new JMenuItem(openDocumentAction));
		fileMenu.add(new JMenuItem(saveDocumentAction));
		fileMenu.addSeparator();
		fileMenu.add(new JMenuItem(exitAction));
		
		JMenu editMenu = new JMenu("Edit");
		menuBar.add(editMenu);
		
		editMenu.add(new JMenuItem(deleteSelectedPartAction));
		editMenu.add(new JMenuItem(toggleCaseAction));
		
		setJMenuBar(menuBar);
	}

	private void createToolbars() {
		JToolBar toolBar = new JToolBar("Alati");
		toolBar.setFloatable(true);
		
		toolBar.add(new JButton(openDocumentAction));
		toolBar.add(new JButton(saveDocumentAction));
		toolBar.addSeparator();
		toolBar.add(new JButton(toggleCaseAction));
		
		getContentPane().add(toolBar, BorderLayout.PAGE_START);
	}

	private javax.swing.Action openDocumentAction = new AbstractAction() {

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fc = new JFileChooser();
			fc.setDialogTitle("Open file");
			if (fc.showOpenDialog(JNotepad.this) != JFileChooser.APPROVE_OPTION) {
				return;
			}

			Path file = fc.getSelectedFile().toPath();
			if (!Files.isReadable(file)) {
				JOptionPane.showMessageDialog(
						JNotepad.this,
						"Odabrana datoteka " + file + " nije citljiva",
						"Pogreska",
						JOptionPane.ERROR_MESSAGE);
			}
			
			try {
				byte[] okteti = Files.readAllBytes(file);
				editor.setText(new String(okteti, StandardCharsets.UTF_8));
				openedFilePath = file;
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(
						JNotepad.this,
						"Pogreska pri citanju datoteke " + file + ": " + e1.getMessage(),
						"Pogreska",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	};
	
	private Action saveDocumentAction = new AbstractAction() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if (openedFilePath == null) {
				JFileChooser fc = new JFileChooser();
				fc.setDialogTitle("Save document");
				if (fc.showSaveDialog(JNotepad.this) != JFileChooser.APPROVE_OPTION) {
					JOptionPane.showMessageDialog(
							JNotepad.this, "Nista nije pogranjeno.", 
							"Poruka", 
							JOptionPane.INFORMATION_MESSAGE);
				}
				
				Path file = fc.getSelectedFile().toPath();
				if (Files.exists(file)) {
					int rez = JOptionPane.showConfirmDialog(JNotepad.this,
							"Odabrana datoteka " + file + " vec postoji. Jeste li sigurni da je zelite pregaziti?", 
							"Upozoronje", 
							JOptionPane.YES_NO_OPTION,
							JOptionPane.WARNING_MESSAGE);
					
					if (rez != JOptionPane.YES_OPTION) {
						return;
					}
				}
				openedFilePath = file;
			}
			
			try {
				Files.write(openedFilePath, editor.getText().getBytes(StandardCharsets.UTF_8));
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(
						JNotepad.this,
						"Pogreska pri pisanju datoteke " + openedFilePath + ": " + e1.getMessage(),
						"Pogreska",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	};
	
	private Action exitAction = new AbstractAction() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			dispose();			
		}
	};
	
	private Action deleteSelectedPartAction = new AbstractAction() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			Document doc = editor.getDocument();
			int pocetak = Math.min(editor.getCaret().getDot(), editor.getCaret().getMark());
			int duljina = Math.max(editor.getCaret().getDot(), editor.getCaret().getMark()) - pocetak;
			try {
				doc.remove(pocetak, duljina);
			} catch (BadLocationException ignorable) {
			}
		}
	};
	
	private Action toggleCaseAction = new AbstractAction() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			Document doc = editor.getDocument();
			int pocetak = Math.min(editor.getCaret().getDot(), editor.getCaret().getMark());
			int duljina = Math.max(editor.getCaret().getDot(), editor.getCaret().getMark() - pocetak);
			
			try {
				String text = doc.getText(pocetak, duljina);
				text = toggleCase(text);
				doc.remove(pocetak, duljina);
				doc.insertString(pocetak, text, null);
			} catch (BadLocationException ignorable) {
			}
		}

		private String toggleCase(String text) {
			char[] znakovi = text.toCharArray();
			for (int i = 0; i < znakovi.length; i++) {
				char c = znakovi[i];
				if (Character.isLowerCase(c)) {
					znakovi[i] = Character.toUpperCase(c);
				} else if (Character.isUpperCase(c)) {
					znakovi[i] = Character.toLowerCase(c);
				}
			}
			
			return new String(znakovi);
		}
	};

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new JNotepad().setVisible(true);
		});
	}

}
