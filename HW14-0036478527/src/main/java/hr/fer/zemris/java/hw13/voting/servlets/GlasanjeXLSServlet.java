package hr.fer.zemris.java.hw13.voting.servlets;

import hr.fer.zemris.java.hw13.dao.DAOProvider;
import hr.fer.zemris.java.hw13.model.PollEntry;
import hr.fer.zemris.java.hw13.voting.VotingHelper;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * GlasanjeXLS generates a XLS file of the voting results.
 * 
 * @author Luka Skugor
 *
 */
@WebServlet(name = "GlasanjeXLS", urlPatterns = { "/glasanje-xls" })
public class GlasanjeXLSServlet extends HttpServlet {

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

		Long pollId = VotingHelper.verifyId("pollId", req, resp);
		Collection<PollEntry> results = DAOProvider.getDao().getPollEntries(
				pollId);
		req.setAttribute("results", results);

		HSSFWorkbook workbook = createWorkbook(results);

		resp.setContentType("application/vnd.ms-excel");
		OutputStream os = resp.getOutputStream();
		workbook.write(os);
		os.flush();

		workbook.close();
	}

	/**
	 * Creates a HSSFWorkbook of the voting results.
	 * 
	 * @param definition
	 *            voting database definition
	 * @param results
	 *            voting results
	 * @return creates workbook
	 */
	private HSSFWorkbook createWorkbook(Collection<PollEntry> results) {

		HSSFWorkbook workbook = new HSSFWorkbook();

		HSSFSheet sheet = workbook.createSheet("Band Voting results");
		HSSFRow head = sheet.createRow(0);
		head.createCell(0).setCellValue("Band name");
		head.createCell(1).setCellValue("Votes");

		int rowCounter = 1;
		for (PollEntry resultEntry : results) {
			HSSFRow row = sheet.createRow(rowCounter);
			rowCounter++;

			row.createCell(0).setCellValue(
					resultEntry.getOptionTitle());
			row.createCell(1).setCellValue(resultEntry.getVotesCount());
		}

		return workbook;
	}

}