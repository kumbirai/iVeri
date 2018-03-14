/*
 za.co.cmsolution.iveri.excel.ExcelRefresher<br>

 Copyright (c) 2018 - Kumbirai 'Coach' Mundangepfupfu (www.kumbirai.com)

 All rights reserved.
 */
package za.co.cmsolution.iveri.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;

import za.co.cmsolution.iveri.file.FileUtils;

/**
 * <p><b>Purpose:</b><br>
 * <br>
 *
 * <p><b>Title:</b> ExcelRefresher<br>
 * <b>Description:</b> </p>
 *
 * @author Kumbirai 'Coach' Mundangepfupfu<br>
 * @date 06 Feb 2018<br>
 * @version 1.0<br>
 *
 * <b>Revision:</b>
 *					
 */
public class ExcelRefresher
{
	private static final Logger LOGGER = LogManager.getLogger(ExcelRefresher.class.getName());
	private Properties props;
	private Path path;

	/**
	 * Constructor: @param props
	 * Constructor: @param path
	 */
	public ExcelRefresher(Properties props, Path path)
	{
		super();
		this.props = props;
		this.path = path;
	}

	/**
	 * Purpose:
	 * <br>
	 * refresh<br>
	 * <br>
	 * @throws IOException<br>
	 */
	public void refresh() throws IOException
	{
		try (Workbook workbook = WorkbookFactory.create(path.toFile()))
		{
			workbook.getCreationHelper().createFormulaEvaluator().clearAllCachedResultValues();
			workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();

			XSSFFormulaEvaluator.evaluateAllFormulaCells(workbook);

			writeWorkbook(workbook);
		}
		catch (Exception ex)
		{
			throw new IOException(ex);
		}
	}

	/**
	 * Purpose:
	 * <br>
	 * writeWorkbook<br>
	 * <br>
	 * @param workbook
	 * @throws IOException<br>
	 */
	private void writeWorkbook(Workbook workbook) throws IOException
	{
		String reportDirectory = props.getProperty("reportDirectory");
		File outFile = FileUtils.getFile(reportDirectory, path.getFileName().toString(), true);
		try (OutputStream os = new FileOutputStream(outFile))
		{
			LOGGER.info(String.format("Writing path : %s", outFile.getAbsolutePath()));
			workbook.write(os);
		}
		catch (Exception ex)
		{
			throw new IOException(ex);
		}
	}
}