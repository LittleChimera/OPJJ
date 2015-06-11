package hr.fer.zemris.java.hw13.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

@WebServlet(name = "powers", urlPatterns = { "/powers" })
public class PowersServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {


		int startFrom = 0, endAt = 0, maxPower = 0;
		try {
			startFrom = parseParameter(req.getParameter("a"), -100, 100);
			endAt = parseParameter(req.getParameter("b"), -100, 100);
			maxPower = parseParameter(req.getParameter("n"), 1, 5);
		} catch (IllegalArgumentException e) {
			String message = e.getMessage();
			req.setAttribute("error", message);
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
			return;
		}

		HSSFWorkbook workbook = new HSSFWorkbook();
		
		for (int i = 1; i <= maxPower; i++) {
			HSSFSheet sheet = workbook.createSheet(i + "-th power");
			HSSFRow head = sheet.createRow(0);
			head.createCell(0).setCellValue("x");
			head.createCell(1).setCellValue("x^" + i);

			for (int j = startFrom; j <= endAt; j++) {
				HSSFRow row = sheet.createRow(j - startFrom + 1);
				row.createCell(0).setCellValue(j);
				row.createCell(1).setCellValue(Math.pow(j, i));
			}
		}

		resp.setContentType("application/vnd.ms-excel");
		workbook.write(resp.getOutputStream());

		workbook.close();
	}

	private int parseParameter(String param, int minContraint, int maxConstraint) {
		Integer value = null;
		try {
			value = Integer.valueOf(param);
		} catch (Exception e) {
			value = 0;
		}
		if (value < minContraint || value > maxConstraint) {
			throw new IllegalArgumentException(String.format(
					"Parameter %s doesn't satisfies constraints: [%d, %d].",
					param, minContraint, maxConstraint));
		}

		return value;
	}

}
