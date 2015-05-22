package hr.fer.zemris.java.hw10.jnotepadpp;

import hr.fer.zemris.java.hw10.jnotepadpp.localization.DefaultLocalizableAction;
import hr.fer.zemris.java.hw10.jnotepadpp.localization.FormLocalizationProvider;
import hr.fer.zemris.java.hw10.jnotepadpp.localization.LocalizableAction;
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
import java.util.LinkedHashSet;
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

/**
 * JNotepadPP is a simple text editing application. It supports tabs and some
 * other text manipulation functions.
 * 
 * @author Luka Skugor
 *
 */
public class JNotepadPP extends JFrame {

	/**
	 * Frame's localization provider.
	 */
	private FormLocalizationProvider flp = new FormLocalizationProvider(
			LocalizationProvider.getInstance(), this);

	/**
	 * Indicates if application is closing.
	 */
	private boolean closingFlag = false;

	/**
	 * App's name.
	 */
	private static final String APP_NAME = "JNotepad++";

	/**
	 * Tab panel containing editors.
	 */
	private JTabbedEditor editorTabs;
	/**
	 * Clipboard's content.
	 */
	private String clipboard = "";
	/**
	 * Indicates whether clipboard's content should be used once only (i.e. if
	 * cut is called)
	 */
	private boolean clipOneUsage = false;
	/**
	 * JFileEditor factory.
	 */
	private FileEditorFactory fileEditorFactory = new FileEditorFactory();

	/**
	 * Status bar.
	 */
	private StatusBar statusBar;

	/**
	 * Creates a new JNotepad++
	 */
	public JNotepadPP() {
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(onWindowClosing);
		setSize(500, 300);
		initGUI();
	}

	/**
	 * Initializes GUI
	 */
	private void initGUI() {
		getContentPane().setLayout(new BorderLayout());

		editorTabs = new JTabbedEditor(tabCloseAction);
		editorTabs.addChangeListener(e -> {
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

		LocalizationProvider.getInstance().fire();

		newDocumentAction.actionPerformed(null);
	}

	/**
	 * Initializes actions.
	 */
	private void createActions() {

		openDocumentAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control O"));
		openDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_O);

		saveDocumentAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control S"));
		saveDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);

		saveDocumentAsAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control shift S"));
		saveDocumentAsAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_A);

		exitAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control Q"));
		exitAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_Q);

		tabCloseAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control W"));
		tabCloseAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_C);

		calculateStatisticsAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control I"));
		calculateStatisticsAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);

		invertCaseAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_I);

		toLowerCaseAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_L);

		toUpperCaseAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_U);

		newDocumentAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control N"));
		newDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_N);

		openDocumentAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control T"));
		openDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_T);

		copyTextAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control C"));
		copyTextAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_C);

		cutTextAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control X"));
		cutTextAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_X);

		pasteTextAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control V"));
		pasteTextAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_V);

		sortAscendingAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_A);

		sortDescendingAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_D);

		uniqueLines.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_U);

	}

	/**
	 * Creates frame's menus.
	 */
	private void createMenus() {
		LocalizationProvider lProvider = LocalizationProvider.getInstance();

		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu(new DefaultLocalizableAction("file",
				lProvider));
		menuBar.add(fileMenu);

		fileMenu.add(new JMenuItem(openDocumentAction));
		fileMenu.add(new JMenuItem(saveDocumentAction));
		fileMenu.addSeparator();
		fileMenu.add(new JMenuItem(exitAction));

		JMenu editMenu = new JMenu(new DefaultLocalizableAction("edit",
				lProvider));
		menuBar.add(editMenu);

		editMenu.add(new JMenuItem(tabCloseAction));
		editMenu.addSeparator();
		editMenu.add(new JMenuItem(copyTextAction));
		editMenu.add(new JMenuItem(cutTextAction));
		editMenu.add(new JMenuItem(pasteTextAction));

		JMenu toolsMenu = new JMenu(new DefaultLocalizableAction("tools",
				lProvider));
		menuBar.add(toolsMenu);

		JMenu transforMenu = new JMenu(new DefaultLocalizableAction(
				"transform", lProvider));
		transforMenu.add(new JMenuItem(invertCaseAction));
		transforMenu.add(new JMenuItem(toLowerCaseAction));
		transforMenu.add(new JMenuItem(toUpperCaseAction));

		JMenu sortMenu = new JMenu(new DefaultLocalizableAction("sort",
				lProvider));
		sortMenu.add(new JMenuItem(sortAscendingAction));
		sortMenu.add(new JMenuItem(sortDescendingAction));

		toolsMenu.add(transforMenu);
		toolsMenu.add(sortMenu);
		toolsMenu.add(new JMenuItem(uniqueLines));
		toolsMenu.addSeparator();
		toolsMenu.add(new JMenuItem(calculateStatisticsAction));

		JMenu helpMenu = new JMenu(new DefaultLocalizableAction("help",
				lProvider));
		JMenu languageMenu = new JMenu(new DefaultLocalizableAction("language",
				lProvider));

		/**
		 * LanguageSetter is a JMenuItem which changes localization of the
		 * application.
		 * 
		 * @author Luka Skugor
		 *
		 */
		class LanguageSetter extends JMenuItem {

			/**
			 * Creates a new LanguageSetter which changes application language
			 * to the given one.
			 * 
			 * @param language
			 *            language which will be set when LanguageSetter is
			 *            clicked
			 */
			public LanguageSetter(String language) {
				setAction(new AbstractAction() {

					{
						putValue(Action.NAME, language);
					}

					/*
					 * (non-Javadoc)
					 * 
					 * @see
					 * java.awt.event.ActionListener#actionPerformed(java.awt
					 * .event.ActionEvent)
					 */
					@Override
					public void actionPerformed(ActionEvent e) {
						LocalizationProvider.getInstance()
								.setLanguage(language);
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

	/**
	 * Creates toolbars.
	 */
	private void createToolbars() {
		LocalizationProvider lProvider = LocalizationProvider.getInstance();

		JToolBar toolBar = new JToolBar(lProvider.getString("tools"));
		toolBar.setFloatable(true);

		toolBar.add(new JButton(newDocumentAction));
		toolBar.add(new JButton(openDocumentAction));
		toolBar.add(new JButton(saveDocumentAction));
		toolBar.add(new JButton(saveDocumentAsAction));
		toolBar.add(new JButton(tabCloseAction));
		toolBar.addSeparator();
		toolBar.add(new JButton(copyTextAction));
		toolBar.add(new JButton(cutTextAction));
		toolBar.add(new JButton(pasteTextAction));
		toolBar.addSeparator();
		toolBar.add(new JButton(calculateStatisticsAction));

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

	/**
	 * Action which opens a document.
	 */
	private LocalizableAction openDocumentAction = new LocalizableAction(
			"open", flp) {

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fc = new JFileChooser();
			fc.setDialogTitle("Open file");
			if (fc.showOpenDialog(JNotepadPP.this) != JFileChooser.APPROVE_OPTION) {
				return;
			}

			Path file = fc.getSelectedFile().toPath();
			if (!Files.isReadable(file)) {
				JOptionPane.showMessageDialog(JNotepadPP.this,
						flp.getString("selected_not_readable"),
						flp.getString("error"), JOptionPane.ERROR_MESSAGE);
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
						flp.getString("unable_to_open_file") + file + ": "
								+ e1.getMessage(), flp.getString("error"),
						JOptionPane.ERROR_MESSAGE);
			}
		}
	};

	/**
	 * Action which saves a document.
	 */
	private LocalizableAction saveDocumentAction = new LocalizableAction(
			"save", flp) {

		@Override
		public void actionPerformed(ActionEvent e) {
			saveDocument();
		}
	};

	/**
	 * Action which saves document as a different file.
	 */
	private LocalizableAction saveDocumentAsAction = new LocalizableAction(
			"save_as", flp) {

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

	/**
	 * Saves document of active JFileEditor. Can be interrupted by the user.
	 * 
	 * @return true if saved, false otherwise
	 */
	private boolean saveDocument() {
		if (getActiveEditor().getFilePath() == null) {
			JFileChooser fc = new JFileChooser();
			fc.setDialogTitle("Save document");
			if (fc.showSaveDialog(JNotepadPP.this) != JFileChooser.APPROVE_OPTION) {
				JOptionPane.showMessageDialog(JNotepadPP.this,
						flp.getString("nothing_saved"),
						flp.getString("message"),
						JOptionPane.INFORMATION_MESSAGE);
				return false;
			}

			Path file = fc.getSelectedFile().toPath();
			if (Files.exists(file)) {
				int rez = JOptionPane.showConfirmDialog(
						JNotepadPP.this,
						flp.getString("selected_file") + " " + file + " "
								+ flp.getString("already_exists") + " "
								+ flp.getString("confirm_overwrite"),
						flp.getString("warning"), JOptionPane.YES_NO_OPTION,
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
					flp.getString("writing_error") + " "
							+ getActiveEditor().getFilePath() + ": "
							+ e1.getMessage(), flp.getString("error"),
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}

	/**
	 * Action which exits the application.
	 */
	private LocalizableAction exitAction = new LocalizableAction("exit", flp) {

		@Override
		public void actionPerformed(ActionEvent e) {
			onWindowClosing.windowClosing(null);
		}
	};

	/**
	 * Action which inverts case of selected text.
	 */
	private LocalizableAction invertCaseAction = new LocalizableAction(
			"invert_case", flp) {

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

	/**
	 * Action which transforms selected text to lower case.
	 */
	private LocalizableAction toLowerCaseAction = new LocalizableAction(
			"to_lower_case", flp) {

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

	/**
	 * Action which transforms selected text to upper case.
	 */
	private LocalizableAction toUpperCaseAction = new LocalizableAction(
			"to_upper_case", flp) {

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

	/**
	 * Action which creates a new document.
	 */
	private LocalizableAction newDocumentAction = new LocalizableAction(
			"new_document", flp) {

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileEditor editor = fileEditorFactory.createEditor("", null);
			editor.setText("");
			editorTabs.addTab(editor);
		}
	};

	/**
	 * Action which copies selected text.
	 */
	private LocalizableAction copyTextAction = new LocalizableAction("copy",
			flp) {

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

	/**
	 * Action which cuts selected text.
	 */
	private LocalizableAction cutTextAction = new LocalizableAction("cut", flp) {

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

	/**
	 * Action which pastes text from clipboard at dot index.
	 */
	private LocalizableAction pasteTextAction = new LocalizableAction("paste",
			flp) {

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

	/**
	 * Action which calculates statistics of the active document.
	 */
	private LocalizableAction calculateStatisticsAction = new LocalizableAction(
			"statistics", flp) {

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileEditor editor = getActiveEditor();

			String fileContents = editor.getText();
			int characterCount = fileContents.length();
			int nonBlankCharacterCount = fileContents.replaceAll("\\s", "")
					.length();
			int linesCount = fileContents.length()
					- fileContents.replaceAll("\\n", "").length();

			String statistics = String.format("%n" + "%s: %d%n" + "%s: %d%n"
					+ "%s: %d%n", flp.getString("statistics_characters"),
					characterCount, flp.getString("statistics_non_blanks"),
					nonBlankCharacterCount, flp.getString("statistics_lines"),
					linesCount);

			JOptionPane.showMessageDialog(JNotepadPP.this,
					flp.getString("file_statistics") + ":" + statistics,
					flp.getString("file_statistics") + ": " + editor.getName(),
					JOptionPane.INFORMATION_MESSAGE);
		}
	};

	/**
	 * Action which sorts selected lines ascending. If part of the line is
	 * selected, whole line will be affected.
	 */
	private LocalizableAction sortAscendingAction = new LocalizableAction(
			"sort_asc", flp) {

		@Override
		public void actionPerformed(ActionEvent e) {
			sortSelected(true);
		}
	};

	/**
	 * Action which sorts selected lines descending. If part of the line is
	 * selected, whole line will be affected.
	 */
	private LocalizableAction sortDescendingAction = new LocalizableAction(
			"sort_desc", flp) {

		@Override
		public void actionPerformed(ActionEvent e) {
			sortSelected(false);
		}
	};

	/**
	 * Sorts selected lines. If part of the line is selected, whole line will be
	 * affected.
	 * 
	 * @param ascending
	 *            if true result will be in ascending order and if false in
	 *            descending order
	 */
	private void sortSelected(boolean ascending) {
		JFileEditor activeEditor = getActiveEditor();
		int dot = activeEditor.getCaret().getDot();
		int mark = activeEditor.getCaret().getMark();

		int offsetStart = 0, offsetEnd = 0;
		try {
			offsetStart = Utilities.getRowStart(activeEditor,
					Math.min(mark, dot));
			offsetEnd = Utilities.getRowEnd(activeEditor, Math.max(mark, dot));
		} catch (BadLocationException ignorable) {
		}

		String selectedLines = null;
		try {
			selectedLines = activeEditor.getDocument().getText(offsetStart,
					offsetEnd - offsetStart);
		} catch (BadLocationException ignorable) {
		}

		String[] lines = selectedLines.split("\\n");
		Locale locale = Locale.forLanguageTag(LocalizationProvider
				.getInstance().getLanguage());
		Collator collator = Collator.getInstance(locale);
		Comparator<Object> comparator = (ascending) ? collator : collator
				.reversed();
		Collections.sort(Arrays.asList(lines), comparator);

		StringBuilder sortedSelectionBuilder = new StringBuilder(offsetEnd
				- offsetStart + 2);
		for (String string : lines) {
			sortedSelectionBuilder.append(string).append(String.format("%n"));
		}

		String sortedSelection = sortedSelectionBuilder.toString().trim();

		try {
			activeEditor.getDocument().remove(offsetStart,
					offsetEnd - offsetStart);
			activeEditor.getDocument().insertString(offsetStart,
					sortedSelection, null);
		} catch (BadLocationException ignorable) {
		}
	}

	/**
	 * Action which removes duplicates from selected lines. If part of the line
	 * is selected, whole line will be affected.
	 */
	private LocalizableAction uniqueLines = new LocalizableAction("unique", flp) {

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileEditor activeEditor = getActiveEditor();
			int dot = activeEditor.getCaret().getDot();
			int mark = activeEditor.getCaret().getMark();

			int offsetStart = 0, offsetEnd = 0;
			try {
				offsetStart = Utilities.getRowStart(activeEditor,
						Math.min(mark, dot));
				offsetEnd = Utilities.getRowEnd(activeEditor,
						Math.max(mark, dot));
			} catch (BadLocationException ignorable) {
			}

			String selectedLines = null;
			try {
				selectedLines = activeEditor.getDocument().getText(offsetStart,
						offsetEnd - offsetStart);
			} catch (BadLocationException ignorable) {
			}

			String[] lines = selectedLines.split("\\n");
			LinkedHashSet<String> lineSet = new LinkedHashSet<String>();
			lineSet.addAll(Arrays.asList(lines));

			StringBuilder uniqueSelectionBuilder = new StringBuilder(offsetEnd
					- offsetStart + 2);
			for (String line : lineSet) {
				uniqueSelectionBuilder.append(line).append(String.format("%n"));
			}

			String uniqueSelection = uniqueSelectionBuilder.toString().trim();

			try {
				activeEditor.getDocument().remove(offsetStart,
						offsetEnd - offsetStart);
				activeEditor.getDocument().insertString(offsetStart,
						uniqueSelection, null);
			} catch (BadLocationException ignorable) {
			}
		}
	};

	/**
	 * Called on program start.
	 * 
	 * @param args
	 *            command line arguments
	 */
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

	/**
	 * Gets active editor.
	 * 
	 * @return active editor
	 */
	private JFileEditor getActiveEditor() {
		Component selectedTab = editorTabs.getSelectedComponent();
		if (selectedTab == null) {
			return null;
		}
		return (JFileEditor) ((JScrollPane) selectedTab).getViewport()
				.getView();
	}

	/**
	 * Updates frame's title based on currently active editor.
	 */
	private void updateTitle() {
		JFileEditor activEditor = getActiveEditor();
		if (activEditor == null) {
			setTitle(APP_NAME);
		} else {
			setTitle(getActiveEditor().getName() + " - " + APP_NAME);
		}
	}

	/**
	 * Defines closing action for the frame.
	 */
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

	/**
	 * Action which closes active tab.
	 */
	private LocalizableAction tabCloseAction = new LocalizableAction(
			"tab_close", flp) {

		@Override
		public void actionPerformed(ActionEvent e) {
			closeActiveTab();
		}
	};

	/**
	 * Closes active tab. Can be interrupted by the user.
	 * 
	 * @return true if close, false otherwise
	 */
	private boolean closeActiveTab() {
		if (!editorTabs.isSelectedSaved()) {

			int result = JOptionPane.showConfirmDialog(JNotepadPP.this,
					flp.getString("file") + " " + getActiveEditor().getName()
							+ " " + flp.getString("has_unsaved_changes") + ". "
							+ flp.getString("save_prompt"),
					flp.getString("unsaved_title"),
					JOptionPane.YES_NO_CANCEL_OPTION,
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

	/**
	 * Updates clipboard's contents.
	 * 
	 * @param text
	 *            clipboards's new text
	 * @param oneUse
	 *            indicates whether clipoboard's contents should be used only
	 *            once (i.e. if cut is called)
	 */
	private void updateClipboard(String text, boolean oneUse) {
		clipboard = text;
		clipOneUsage = oneUse;
	}

	/**
	 * Factory class for JFileEditor.
	 * 
	 * @author Luka Skugor
	 *
	 */
	private class FileEditorFactory {

		/**
		 * Creates a new JFileEditor and adds appropriate Listeners to it.
		 * 
		 * @param text
		 *            editor's text
		 * @param filePath
		 *            editor's source or destination path
		 * @return created editor
		 */
		public JFileEditor createEditor(String text, Path filePath) {
			JFileEditor editor = new JFileEditor(filePath);
			editor.setText(text);
			editor.addCaretListener(editorCaretListener);
			editor.getDocument().addDocumentListener(editorDocumentListener);

			return editor;
		}

		/**
		 * Caret listener which updates status bar of JNotepad++
		 */
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

		/**
		 * Document listener which updates JNotepad++ status bar.
		 */
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

	/**
	 * Updates enable status of actions which are performed on selected text.
	 * 
	 * @param enable
	 *            if no text is selected this parameter should be false, true
	 *            otherwise
	 */
	private void enableSelectionActions(boolean enable) {
		copyTextAction.setEnabled(enable);
		cutTextAction.setEnabled(enable);
		invertCaseAction.setEnabled(enable);
		toLowerCaseAction.setEnabled(enable);
		toUpperCaseAction.setEnabled(enable);
		sortAscendingAction.setEnabled(enable);
		sortDescendingAction.setEnabled(enable);
		uniqueLines.setEnabled(enable);
	}

}
