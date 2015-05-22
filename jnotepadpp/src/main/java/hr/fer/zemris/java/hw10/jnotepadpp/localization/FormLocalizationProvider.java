package hr.fer.zemris.java.hw10.jnotepadpp.localization;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

/**
 * This class is used to provide localization for a frame. Each frame should
 * have it's own FormLocalizationProvider which is constructed from either
 * parent's localization provider or from {@link LocalizationProvider}.
 * 
 * @author Luka Skugor
 *
 */
public class FormLocalizationProvider extends LocalizationProviderBridge {

	/**
	 * Creates a new FormLocalizationProvider for a frame.
	 * @param localizationProvider parent's localization provider
	 * @param frame frame for which FormLocalizationProvider is created
	 */
	public FormLocalizationProvider(ILocalizationProvider localizationProvider,
			JFrame frame) {
		super(localizationProvider);

		connect();

		frame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				disconnect();
			}

		});
	}

}
