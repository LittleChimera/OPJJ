package hr.fer.zemris.java.hw10.jnotepadpp;

import java.awt.Component;
import java.awt.FlowLayout;
import java.nio.file.Path;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * JTabbedEditor is a JTabbedPane which holds {@link JFileEditor}s as tabs.
 * 
 * @author Luka Skugor
 *
 */
public class JTabbedEditor extends JTabbedPane {

	/**
	 * Tab closing action.
	 */
	private Action tabClosingAction;

	/**
	 * Creates a new JTabEditor with given action called on tab closing.
	 * 
	 * @param tabClosingAction
	 *            tab closing action
	 */
	public JTabbedEditor(Action tabClosingAction) {
		this.tabClosingAction = tabClosingAction;
	}

	/**
	 * Adds a new tab.
	 * 
	 * @param editor
	 *            editor which is instanced in the new tab
	 * @return added tab
	 */
	public Component addTab(JFileEditor editor) {
		JScrollPane scrollableEditor = new JScrollPane(editor);
		Component tab = add(scrollableEditor);
		setSelectedComponent(tab);

		JTabComponent tabComponent = new JTabComponent(editor);
		setTabComponentAt(getSelectedIndex(), tabComponent);

		return tab;
	}

	/**
	 * JTabComponent is a tab header component for every tab in JTabbedEditor.
	 * It displays a closing button, name of the editor and an icon which
	 * indicates whether the tab is saved or not.
	 * 
	 * @author Luka Skugor
	 *
	 */
	private class JTabComponent extends JPanel {

		/**
		 * Editor which information is displayed.
		 */
		private JFileEditor editor;
		/**
		 * Editor's name (i.e. name of the file it's editing)
		 */
		private JLabel fileName;
		/**
		 * Indicates if the editor has been modified.
		 */
		private boolean modified;

		/**
		 * Creates a new JTabComponent for the given editor.
		 * @param editor
		 */
		public JTabComponent(JFileEditor editor) {
			this.editor = editor;
			this.modified = false;
			updateTooltipForSelected(editor);
			editor.getDocument().addDocumentListener(new DocumentListener() {

				@Override
				public void removeUpdate(DocumentEvent e) {
					docChanged();
				}

				@Override
				public void insertUpdate(DocumentEvent e) {
					docChanged();
				}

				@Override
				public void changedUpdate(DocumentEvent e) {
					docChanged();
				}

				private void docChanged() {
					setIcon(notSaved);
					modified = true;
				}
			});
			setLayout(new FlowLayout());
			initGUI();
		}

		/**
		 * Initializes GUI.
		 */
		private void initGUI() {
			setOpaque(false);

			JButton closeButton = new JButton("x");
			closeButton.addActionListener(e -> {
				if (modified) {
					setSelectedIndex(indexOfTabComponent(this));
					tabClosingAction.actionPerformed(null);
				} else {
					JTabbedEditor.this.remove(indexOfTabComponent(this));
				}
			});
			final int buttonBorder = 3;
			closeButton.setBorder(new EmptyBorder(buttonBorder, buttonBorder,
					buttonBorder, buttonBorder));
			add(closeButton);

			fileName = new JLabel(editor.getName());
			// sets icon to right
			fileName.setHorizontalTextPosition(JLabel.LEFT);
			add(fileName);
		}

		/**
		 * Updates tab display name.
		 */
		public void updateDisplayName() {
			fileName.setText(editor.getName());
		}

		/**
		 * Sets tab icon.
		 * @param icon set icon
		 */
		private void setIcon(Icon icon) {
			fileName.setIcon(icon);
		}

	}

	/**
	 * Modifies components to indicate that active tab contents are saved.
	 */
	public void saveActiveTabContents() {
		JTabComponent selectedTabComponent = (JTabComponent) getTabComponentAt(getSelectedIndex());

		updateTooltipForSelected(selectedTabComponent.editor);

		selectedTabComponent.updateDisplayName();
		selectedTabComponent.setIcon(saved);
		selectedTabComponent.modified = false;
	}

	/**
	 * Checks if selected tab is saved.
	 * @return true if saved, false otherwise
	 */
	public boolean isSelectedSaved() {
		return !((JTabComponent) getTabComponentAt(getSelectedIndex())).modified;
	}

	/**
	 * Updates tooltip for selected tab.
	 * @param editor
	 */
	private void updateTooltipForSelected(JFileEditor editor) {
		Path tooltip = editor.getFilePath();
		if (tooltip != null) {
			setToolTipTextAt(getSelectedIndex(), tooltip.toString());
		}
	}

	/**
	 * Icon for indicating saved file.
	 */
	private static ImageIcon saved = new ImageIcon("icons/saved.png",
			"File saved");
	/**
	 * Icon for indicating unsaved file.
	 */
	private static ImageIcon notSaved = new ImageIcon("icons/not_saved.png",
			"File not saved");

}
