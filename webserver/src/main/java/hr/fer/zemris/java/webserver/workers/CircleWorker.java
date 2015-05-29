package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 * CircleWorker creates a PNG image of a circle with dimensions of 200x200 and
 * sends it as a reponse.
 *
 * @see IWebWorker
 *
 * @author Luka Skugor
 *
 */
public class CircleWorker implements IWebWorker {

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * hr.fer.zemris.java.webserver.IWebWorker#processRequest(hr.fer.zemris.
	 * java.webserver.RequestContext)
	 */
	@Override
	public void processRequest(RequestContext context) {
		context.setMimeType("image/png");

		int width = 200, height = 200;
		BufferedImage bim = new BufferedImage(width, height,
				BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g2d = bim.createGraphics();

		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, width, height);

		Random random = new Random();
		int radiusDecrement = (Math.abs(random.nextInt()) % 15 + 1) * 2;
		int iRadiusDecrement = 0;
		while (height > 0 && width > 0) {
			g2d.setColor(new Color((float) Math.random(),
					(float) Math.random(), (float) Math.random()));
			iRadiusDecrement += radiusDecrement / 2;

			width -= radiusDecrement;
			height -= radiusDecrement;
			g2d.fillOval(iRadiusDecrement, iRadiusDecrement, width, height);

			radiusDecrement = (Math.abs(random.nextInt()) % 15 + 1) * 2;
		}
		g2d.dispose();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ImageIO.write(bim, "png", bos);
			context.write(bos.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
