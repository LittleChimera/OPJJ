package hr.fer.zemris.java.hw13.servlets;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;

/**
 * ReportImageServlet generates a pie chart of OSes using this application. Data
 * is hard-coded and doesn't have any statistical significance.
 * 
 * @author Luka Skugor
 *
 */
@WebServlet(name = "OSreportImage", urlPatterns = { "/reportImage" })
public class ReportImageServlet extends HttpServlet {

	/**
	 * Serial
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		JFreeChart chart = createChart(createDataset(), "OS Usage");

		final int width = 600, height = 400;
		BufferedImage image = chart.createBufferedImage(width, height);

		final String imageExtension = "png";
		resp.setContentType("image/" + imageExtension);

		OutputStream os = resp.getOutputStream();
		ImageIO.write(image, imageExtension, os);
		os.flush();
	}

	/**
	 * Generates a hard-coded data set of OS usage.
	 * @return created data set
	 */
	private PieDataset createDataset() {
		DefaultPieDataset result = new DefaultPieDataset();
		result.setValue("Linux", 41);
		result.setValue("Mac", 19);
		result.setValue("Windows", 40);
		return result;

	}

	/**
	 * Creates a pie chart for given data set and chart title.
	 * @param dataset data of the pie chart
	 * @param title title of the chart
	 * @return created pie chart
	 */
	private JFreeChart createChart(PieDataset dataset, String title) {

		JFreeChart chart = ChartFactory.createPieChart3D(title, dataset, true,
				true, false);

		PiePlot3D plot = (PiePlot3D) chart.getPlot();
		plot.setStartAngle(290);
		plot.setDirection(Rotation.CLOCKWISE);
		plot.setForegroundAlpha(0.5f);
		return chart;

	}

}
