package hr.fer.zemris.java.hw10.jnotepadpp;

import hr.fer.zemris.java.hw10.jnotepadpp.localization.LocalizationProvider;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.Collator;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

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
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Utilities;

public class JNotepadPP extends JFrame {

	private boolean closingFlag = false;

	private static final String APP_NAME = "JNotepad++";

	private JTabbedEditor editorTabs;
	private String clipboard = "";
	private boolean clipOneUsage = false;
	private FileEditorFactory fileEditorFactory = new FileEditorFactory();

	private StatusBar statusBar;

	public JNotepadPP() {
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(onWindowClosing);
		setSize(500, 300);
		initGUI();
	}

	private void initGUI() {
		getContentPane().setLayout(new BorderLayout());
		
		LocalizationProvider.getInstance().addLocalizationListener(() -> {
			LocalizationProvider lProvider = LocalizationProvider.getInstance();
			saveDocumentAction.putValue(Action.NAME, lProvider.getString("save"));
		});
		
		LocalizationProvider.getInstance().fire();

		editorTabs = new JTabbedEditor(tabCloseAction);
		editorTabs
				.addChangeListener(e -> {
					updateTitle();
					// will be true if at least one tab is opened
					boolean editorActive = editorTabs.getTabCount() != 0;
					calculateStatisticsAction.setEnabled(editorActive);
					saveDocumentAction.setEnabled(editorActive);
					copyTextAction.setEnabled(editorActive);
					cutTextAction.setEnabled(editorActive);
					saveDocumentAsAction.setEnabled(editorActive);
					pasteTextAction.setEnabled(editorActive && clipboard != "");

					JFileEditor activeEditor = getActiveEditor();
					if (activeEditor == null) {
						statusBar.updateCaret(0, 0, 0);
						statusBar.updateLength(0);
					} else {
						activeEditor.fireEditorUpdate();
						statusBar.updateLength(activeEditor.getDocument()
								.getLength());
					}
					
					boolean textSelected = (activeEditor != null) ? activeEditor
							.getCaret().getDot() != activeEditor.getCaret()
							.getMark() : false;
					enableSelectionActions(textSelected);

				});
		getContentPane().add(editorTabs, BorderLayout.CENTER);
		
		createActions();
		createMenus();
		createToolbars();

		newDocumentAction.actionPerformed(null);
	}

	private void createActions() {
		LocalizationProvider lProvider = LocalizationProvider.getInstance();
		
		openDocumentAction.putValue(Action.NAME, "Open");
		openDocumentAction.putValue(Action.SHORT_DESCRIPTION,
				"Used to open existing document");
		openDocumentAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control O"));
		openDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_O);

		saveDocumentAction.putValue(Action.SHORT_DESCRIPTION,
				"Used to save currently editing document");
		saveDocumentAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control S"));
		saveDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);

		saveDocumentAsAction.putValue(Action.NAME, "Save As");
		saveDocumentAsAction.putValue(Action.SHORT_DESCRIPTION,
				"Used to save currently editing document as a different file");
		saveDocumentAsAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control shift S"));
		saveDocumentAsAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_A);

		exitAction.putValue(Action.NAME, "Exit");
		exitAction.putValue(Action.SHORT_DESCRIPTION, "Exits " + APP_NAME);
		exitAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control Q"));
		exitAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_Q);

		// deleteSelectedPartAction.putValue(Action.NAME, "Delete selection");

		invertCaseAction.putValue(Action.NAME, "Invert case");
		invertCaseAction.putValue(Action.SHORT_DESCRIPTION,
				"Used to invert case of selected text");
		invertCaseAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_I);

		toLowerCaseAction.putValue(Action.NAME, "To lower case");
		toLowerCaseAction.putValue(Action.SHORT_DESCRIPTION,
				"Transforms text in lower case");
		toLowerCaseAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_L);

		toUpperCaseAction.putValue(Action.NAME, "To upper case");
		toUpperCaseAction.putValue(Action.SHORT_DESCRIPTION,
				"Transforms text in upper case");
		toUpperCaseAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_U);

		newDocumentAction.putValue(Action.NAME, "New");
		newDocumentAction.putValue(Action.SHORT_DESCRIPTION,
				"Used to create new document");
		newDocumentAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control N"));
		newDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_N);

		calculateStatisticsAction.putValue(Action.NAME, "Statistics");
		openDocumentAction.putValue(Action.SHORT_DESCRIPTION,
				"Generates statistics for the document");
		openDocumentAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control T"));
		openDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_T);

		copyTextAction.putValue(Action.NAME, "Copy");
		copyTextAction.putValue(Action.SHORT_DESCRIPTION,
				"Used to copy selection");
		copyTextAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control C"));
		copyTextAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_C);

		cutTextAction.putValue(Action.NAME, "Cut");
		cutTextAction.putValue(Action.SHORT_DESCRIPTION,
				"Used to cut selection");
		cutTextAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control X"));
		cutTextAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_X);

		pasteTextAction.putValue(Action.NAME, "Paste");
		pasteTextAction.putValue(Action.SHORT_DESCRIPTION,
				"Used to paste clipboards content");
		pasteTextAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control V"));
		pasteTextAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_V);
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
		editMenu.add(new JMenuItem(copyTextAction));
		editMenu.add(new JMenuItem(cutTextAction));
		editMenu.add(new JMenuItem(pasteTextAction));

		JMenu toolsMenu = new JMenu("Tools");
		menuBar.add(toolsMenu);

		JMenu transforMenu = new JMenu("Transform");
		transforMenu.add(new JMenuItem(invertCaseAction));
		transforMenu.add(new JMenuItem(toLowerCaseAction));
		transforMenu.add(new JMenuItem(toUpperCaseAction));
		
		JMenu sortMenu = new JMenu("Sort");
		sortMenu.add(new JMenuItem(sortAscendingAction));
		sortMenu.add(new JMenuItem(sortDescendingAction));

		toolsMenu.add(transforMenu);
		toolsMenu.add(sortMenu);
		toolsMenu.addSeparator();
		toolsMenu.add(new JMenuItem(calculateStatisticsAction));
		
		JMenu helpMenu = new JMenu("Help");
		JMenu languageMenu = new JMenu("Language");
		
		class LanguageSetter extends JMenuItem {
			
			public LanguageSetter(String language) {
				setAction(new AbstractAction() {
					
					{
						putValue(Action.NAME, language);
					}
					
					@Override
					public void actionPerformed(ActionEvent e) {
						LocalizationProvider.getInstance().setLanguage(language);
					}
				});
			}
		}
		languageMenu.add(new LanguageSetter("hr"));
		languageMenu.add(new LanguageSetter("en"));
		languageMenu.add(new LanguageSetter("de"));
		helpMenu.add(languageMenu);
		
		menuBar.add(helpMenu);

		setJMenuBar(menuBar);
	}

	private void createToolbars() {
		JToolBar toolBar = new JToolBar("Tools");
		toolBar.setFloatable(true);

		toolBar.add(new JButton(newDocumentAction));
		toolBar.add(new JButton(openDocumentAction));
		toolBar.add(new JButton(saveDocumentAction));
		toolBar.add(new JButton(saveDocumentAsAction));
		toolBar.addSeparator();
		// toolBar.add(new JButton(toggleCaseAction));

		statusBar = new StatusBar();
		statusBar.setFloatable(true);
		Thread clockUpdater = new Thread(() -> {
			while (!closingFlag) {
				SwingUtilities.invokeLater(() -> {
					statusBar.updateClock();
				});
				try {
					Thread.sleep(500);
				} catch (Exception ignorable) {
				}
			}
		});
		clockUpdater.setDaemon(true);
		clockUpdater.start();

		getContentPane().add(toolBar, BorderLayout.PAGE_START);
		getContentPane().add(statusBar, BorderLayout.PAGE_END);
	}

	private javax.swing.Action openDocumentAction = new AbstractAction() {

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fc = new JFileChooser();
			fc.setDialogTitle("Open file");
			if (fc.showOpenDialog(JNotepadPP.this) != JFileChooser.APPROVE_OPTION) {
				return;
			}

			Path file = fc.getSelectedFile().toPath();
			if (!Files.isReadable(file)) {
				JOptionPane.showMessageDialog(JNotepadPP.this, "Selected file "
						+ file + " isn't readable", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			try {
				byte[] octets = Files.readAllBytes(file);
				JFileEditor editor = fileEditorFactory.createEditor(new String(
						octets, StandardCharsets.UTF_8), file);
				editorTabs.addTab(editor);
				updateTitle();
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(
						JNotepadPP.this,
						"Unable to open the file " + file + ": "
								+ e1.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	};

	private Action saveDocumentAction = new AbstractAction() {

		@Override
		public void actionPerformed(ActionEvent e) {
			saveDocument();
		}
	};

	private Action saveDocumentAsAction = new AbstractAction() {

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileEditor activeEditor = getActiveEditor();
			Path oldPath = activeEditor.getFilePath();
			activeEditor.setFilePath(null);

			if (!saveDocument()) {
				activeEditor.setFilePath(oldPath);
			}
		}
	};

	private boolean saveDocument() {
		if (getActiveEditor().getFilePath() == null) {
			JFileChooser fc = new JFileChooser();
			fc.setDialogTitle("Save document");
			if (fc.showSaveDialog(JNotepadPP.this) != JFileChooser.APPROVE_OPTION) {
				JOptionPane.showMessageDialog(JNotepadPP.this,
						"Nista nije pogranjeno.", "Poruka",
						JOptionPane.INFORMATION_MESSAGE);
				return false;
			}

			Path file = fc.getSelectedFile().toPath();
			if (Files.exists(file)) {
				int rez = JOptionPane
						.showConfirmDialog(
								JNotepadPP.this,
								"Odabrana datoteka "
										+ file
										+ " vec postoji. Jeste li sigurni da je zelite pregaziti?",
								"Upozoronje", JOptionPane.YES_NO_OPTION,
								JOptionPane.WARNING_MESSAGE);

				if (rez == JOptionPane.CANCEL_OPTION) {
					return false;
				} else if (rez != JOptionPane.YES_OPTION) {
					return true;
				}
			}
			getActiveEditor().setFilePath(file);
		}

		try {
			Files.write(getActiveEditor().getFilePath(), getActiveEditor()
					.getText().getBytes(StandardCharsets.UTF_8));
			editorTabs.saveActiveTabContents();
			updateTitle();
			return true;
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(
					JNotepadPP.this,
					"Pogreska pri pisanju datoteke "
							+ getActiveEditor().getFilePath() + ": "
							+ e1.getMessage(), "Pogreska",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}

	private Action exitAction = new AbstractAction() {

		@Override
		public void actionPerformed(ActionEvent e) {
			onWindowClosing.windowClosing(null);
		}
	};

	private Action deleteSelectedPartAction = new AbstractAction() {

		@Override
		public void actionPerformed(ActionEvent e) {
			Document doc = getActiveEditor().getDocument();
			int pocetak = Math.min(getActiveEditor().getCaret().getDot(),
					getActiveEditor().getCaret().getMark());
			int duljina = Math.max(getActiveEditor().getCaret().getDot(),
					getActiveEditor().getCaret().getMark()) - pocetak;
			try {
				doc.remove(pocetak, duljina);
			} catch (BadLocationException ignorable) {
			}
		}
	};

	private Action invertCaseAction = new AbstractAction() {

		@Override
		public void actionPerformed(ActionEvent e) {
			Document doc = getActiveEditor().getDocument();
			int pocetak = Math.min(getActiveEditor().getCaret().getDot(),
					getActiveEditor().getCaret().getMark());
			int duljina = Math.max(getActiveEditor().getCaret().getDot(),
					getActiveEditor().getCaret().getMark()) - pocetak;

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

	private Action toLowerCaseAction = new AbstractAction() {

		@Override
		public void actionPerformed(ActionEvent e) {
			Document doc = getActiveEditor().getDocument();
			int pocetak = Math.min(getActiveEditor().getCaret().getDot(),
					getActiveEditor().getCaret().getMark());
			int duljina = Math.max(getActiveEditor().getCaret().getDot(),
					getActiveEditor().getCaret().getMark()) - pocetak;

			try {
				String text = doc.getText(pocetak, duljina);
				text = text.toLowerCase();
				doc.remove(pocetak, duljina);
				doc.insertString(pocetak, text, null);
			} catch (BadLocationException ignorable) {
			}
		}
	};

	private Action toUpperCaseAction = new AbstractAction() {

		@Override
		public void actionPerformed(ActionEvent e) {
			Document doc = getActiveEditor().getDocument();
			int pocetak = Math.min(getActiveEditor().getCaret().getDot(),
					getActiveEditor().getCaret().getMark());
			int duljina = Math.max(getActiveEditor().getCaret().getDot(),
					getActiveEditor().getCaret().getMark()) - pocetak;

			try {
				String text = doc.getText(pocetak, duljina);
				text = text.toUpperCase();
				doc.remove(pocetak, duljina);
				doc.insertString(pocetak, text, null);
			} catch (BadLocationException ignorable) {
			}
		}
	};

	private Action newDocumentAction = new AbstractAction() {

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileEditor editor = fileEditorFactory.createEditor("", null);
			editor.setText("");
			editorTabs.addTab(editor);
		}
	};

	private Action copyTextAction = new AbstractAction() {

		@Override
		public void actionPerformed(ActionEvent e) {
			Document doc = getActiveEditor().getDocument();
			int selectionStart = Math.min(
					getActiveEditor().getCaret().getDot(), getActiveEditor()
							.getCaret().getMark());
			int selectionLength = Math.max(getActiveEditor().getCaret()
					.getDot(), getActiveEditor().getCaret().getMark())
					- selectionStart;
			try {
				updateClipboard(doc.getText(selectionStart, selectionLength),
						false);
				pasteTextAction.setEnabled(true);
			} catch (Exception ignorable) {
			}
		}
	};

	private Action cutTextAction = new AbstractAction() {

		@Override
		public void actionPerformed(ActionEvent e) {
			Document doc = getActiveEditor().getDocument();
			int selectionStart = Math.min(
					getActiveEditor().getCaret().getDot(), getActiveEditor()
							.getCaret().getMark());
			int selectionLength = Math.max(getActiveEditor().getCaret()
					.getDot(), getActiveEditor().getCaret().getMark())
					- selectionStart;
			try {
				updateClipboard(doc.getText(selectionStart, selectionLength),
						true);
				doc.remove(selectionStart, selectionLength);
				pasteTextAction.setEnabled(true);
			} catch (Exception ignorable) {
			}
		}
	};

	private Action pasteTextAction = new AbstractAction() {

		{
			setEnabled(false);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			Document doc = getActiveEditor().getDocument();
			int selectionStart = Math.min(
					getActiveEditor().getCaret().getDot(), getActiveEditor()
							.getCaret().getMark());
			int selectionLength = Math.max(getActiveEditor().getCaret()
					.getDot(), getActiveEditor().getCaret().getMark())
					- selectionStart;
			try {
				doc.remove(selectionStart, selectionLength);
				doc.insertString(selectionStart, clipboard, null);
				if (clipOneUsage) {
					pasteTextAction.setEnabled(false);
					updateClipboard("", false);
				}
			} catch (Exception ignorable) {
			}
		}
	};

	private Action calculateStatisticsAction = new AbstractAction() {

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileEditor editor = getActiveEditor();

			String fileContents = editor.getText();
			int characterCount = fileContents.length();
			int nonBlankCharacterCount = fileContents.replaceAll("\\s", "")
					.length();
			int linesCount = fileContents.length()
					- fileContents.replaceAll("\\n", "").length();

			String statistics = String.format("%n" + "Characters: %d%n"
					+ "Non-blank characters: %d%n" + "Lines: %d%n",
					characterCount, nonBlankCharacterCount, linesCount);

			JOptionPane.showMessageDialog(JNotepadPP.this, "File statistics:"
					+ statistics, "File statistics: " + editor.getName(),
					JOptionPane.INFORMATION_MESSAGE);
		}
	};
	
	private Action sortAscendingAction = new AbstractAction() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			sortSelected(true);
		}
	};
	
	private Action sortDescendingAction = new AbstractAction() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			sortSelected(false);
		}
	};
	
	private void sortSelected(boolean ascending) {
		JFileEditor activeEditor = getActiveEditor();
		int dot = activeEditor.getCaret().getDot();
		int mark = activeEditor.getCaret().getMark();
		
		int offsetStart = 0, offsetEnd = 0;
		try {
			offsetStart = Utilities.getRowStart(activeEditor, Math.min(mark, dot));
			offsetEnd = Utilities.getRowEnd(activeEditor, Math.max(mark, dot));
		} catch (BadLocationException ignorable) {
		}
		
		String selectedLines = null;
		try {
			selectedLines = activeEditor.getDocument().getText(offsetStart, offsetEnd - offsetStart);
		} catch (BadLocationException ignorable) {
			System.out.println("terrible");
		}
		
		String[] lines = selectedLines.split("\\n");
		Locale hrLocale = new Locale("hr");
		Collator hrCollator = Collator.getInstance(hrLocale);
		Comparator<Object> comparator = (ascending)?hrCollator:hrCollator.reversed();
		Collections.sort(Arrays.asList(lines), comparator);
		
		StringBuilder sortedSelectionBuilder = new StringBuilder(offsetEnd - offsetStart + 2);
		for (String string : lines) {
			sortedSelectionBuilder.append(string).append(String.format("%n"));
		}
		
		String sortedSelection = sortedSelectionBuilder.toString().trim();
		
		try {
			activeEditor.getDocument().remove(offsetStart, offsetEnd - offsetStart);
			activeEditor.getDocument().insertString(offsetStart, sortedSelection, null);
		} catch (BadLocationException ignorable) {
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			LocalizationProvider.getInstance().setLanguage("en");
			try {
				UIManager.setLookAndFeel(UIManager
						.getSystemLookAndFeelClassName());
			} catch (Exception ignorable) {
			}
			new JNotepadPP().setVisible(true);
		});
	}

	private JFileEditor getActiveEditor() {
		Component selectedTab = editorTabs.getSelectedComponent();
		if (selectedTab == null) {
			return null;
		}
		return (JFileEditor) ((JScrollPane) selectedTab).getViewport()
				.getView();
	}

	private void updateTitle() {
		JFileEditor activEditor = getActiveEditor();
		if (activEditor == null) {
			setTitle(APP_NAME);
		} else {
			setTitle(getActiveEditor().getName() + " - " + APP_NAME);
		}
	}

	private WindowListener onWindowClosing = new WindowAdapter() {

		@Override
		public void windowClosing(WindowEvent e) {
			while (editorTabs.getSelectedIndex() != -1) {
				if (!closeActiveTab()) {
					return;
				}
			}
			closingFlag = true;
			dispose();
		};
	};

	private Action tabCloseAction = new AbstractAction() {

		@Override
		public void actionPerformed(ActionEvent e) {
			closeActiveTab();
		}
	};

	private boolean closeActiveTab() {
		if (!editorTabs.isSelectedSaved()) {

			int result = JOptionPane.showConfirmDialog(JNotepadPP.this, "File "
					+ getActiveEditor().getName()
					+ " has unsaved changes. Do you want to save changes?",
					"Unsaved changes", JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.WARNING_MESSAGE);

			if (result == JOptionPane.YES_OPTION) {
				saveDocument();
			} else if (result == JOptionPane.CANCEL_OPTION) {
				return false;
			}

		}
		editorTabs.remove(editorTabs.getSelectedIndex());
		return true;
	}

	private void updateClipboard(String text, boolean oneUse) {
		clipboard = text;
		clipOneUsage = oneUse;
	}

	private class FileEditorFactory {

		public JFileEditor createEditor(String text, Path filePath) {
			JFileEditor editor = new JFileEditor(filePath);
			editor.setText(text);
			editor.addCaretListener(editorCaretListener);
			editor.getDocument().addDocumentListener(editorDocumentListener);

			return editor;
		}

		private CaretListener editorCaretListener = new CaretListener() {

			@Override
			public void caretUpdate(CaretEvent e) {
				JTextArea editor = (JTextArea) e.getSource();
				Document doc = editor.getDocument();
				int dot = e.getDot();
				int mark = e.getMark();
				int selectionStart = Math.min(dot, mark);
				int selectionLength = Math.max(dot, mark) - selectionStart;
				
				enableSelectionActions(selectionLength != 0); 

				int offset = 0;
				try {
					offset = Utilities.getRowStart(editor, dot);
				} catch (BadLocationException ignorable) {
				}

				int row = 0;
				try {
					for (int currMark = Math.max(1, offset); currMark > 0; currMark = Utilities
							.getRowStart(editor, currMark) - 1) {
						row++;
					}
				} catch (BadLocationException ignorable) {
				}

				int col = dot - offset + 1;

				statusBar.updateCaret(row, col, selectionLength);
			}
		};

		private DocumentListener editorDocumentListener = new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				changedUpdate(e);
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				changedUpdate(e);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				statusBar.updateLength(e.getDocument().getLength());
			}
		};
	}
	
	private void enableSelectionActions(boolean enable) {
		copyTextAction.setEnabled(enable);
		cutTextAction.setEnabled(enable);
		invertCaseAction.setEnabled(enable);
		toLowerCaseAction.setEnabled(enable);
		toUpperCaseAction.setEnabled(enable);
	}

}
