/*
 za.co.cmsolution.iveri.App<br>

 Copyright (c) 2018 - Kumbirai 'Coach' Mundangepfupfu (www.kumbirai.com)

 All rights reserved.
 */
package za.co.cmsolution.iveri;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Session;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import za.co.cmsolution.iveri.file.EmailerFileVisitor;
import za.co.cmsolution.iveri.file.ExcelRefreshFileVisitor;
import za.co.cmsolution.iveri.file.FileUtils;
import za.co.cmsolution.iveri.mail.EmailUtil;
import za.co.cmsolution.iveri.mail.IveriMailSessionFactory;
import za.co.cmsolution.iveri.utils.StringUtils;

/**
 * <p><b>Purpose:</b><br>
 * <br>
 *
 * <p><b>Title:</b> App<br>
 * <b>Description:</b> </p>
 *
 * @author Kumbirai 'Coach' Mundangepfupfu<br>
 * @date 01 Feb 2018<br>
 * @version 1.0<br>
 *
 * <b>Revision:</b>
 *					
 */
public class App
{
	private static final Logger LOGGER = LogManager.getLogger(App.class.getName());
	private static final String APPLICATION_PROPERTIES = "application.properties";
	private static final String IO_EXCEPTION = "[IOException] has been caught.";
	private static final String FILE_NOT_FOUND_EXCEPTION = "[FileNotFoundException] has been caught.";
	private static final String LOG_SEPARATOR = "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~";
	private static Properties props;
	static
	{
		props = new Properties();
		try
		{
			props.putAll(System.getProperties());
			props.load(new FileInputStream(APPLICATION_PROPERTIES));
		}
		catch (FileNotFoundException ex)
		{
			LOGGER.error(FILE_NOT_FOUND_EXCEPTION, ex);
		}
		catch (IOException ex)
		{
			LOGGER.error(IO_EXCEPTION, ex);
		}
	}
	// private String[] emailAry = new String[] { "java@kumbirai.co.za", "olga@iveri.com" };
	private String[] emailAry = new String[]
	{ "java@kumbirai.co.za" };

	/**
	 * Constructor:
	 */
	public App()
	{
		super();
	}

	/**
	 * Purpose:
	 * <br>
	 * main<br>
	 * <br>
	 * @param args<br>
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException
	{
		long startTime = System.currentTimeMillis();
		App app = new App();
		LOGGER.info(String.format("Starting application %s", new Date(startTime)));
		app.process();
		long endTime = System.currentTimeMillis();
		LOGGER.info(String.format("Processing complete. It took %s", StringUtils.stringTime(endTime - startTime)));
	}

	/**
	 * Purpose:
	 * <br>
	 * process<br>
	 * <br><br>
	 * @throws InterruptedException
	 */
	private void process() throws InterruptedException
	{
		refreshExcelFiles();

		String reportDirectory = props.getProperty("reportDirectory");
		LOGGER.info(LOG_SEPARATOR);
		LOGGER.info(String.format("Emailing reports in %s", reportDirectory));
		LOGGER.info(LOG_SEPARATOR);
		walkFileTree(reportDirectory, new EmailerFileVisitor(props));
		emailLogFile();
	}

	/**
	 * Purpose:
	 * <br>
	 * refreshExcelFiles<br>
	 * <br>
	 * @throws InterruptedException<br>
	 */
	private void refreshExcelFiles() throws InterruptedException
	{
		String runRefresh = props.getProperty("runRefresh");
		boolean refresh = Boolean.parseBoolean(runRefresh);
		if (refresh)
		{
			String refreshVBScript = props.getProperty("refreshVBScript");
			String excelDirectory = props.getProperty("excelDirectory");
			String refreshRunTime = props.getProperty("refreshRunTime");
			LOGGER.info(LOG_SEPARATOR);
			LOGGER.info("Refreshing Excel Data");
			LOGGER.info(LOG_SEPARATOR);
			try
			{
				String[] parms =
				{ "wscript", refreshVBScript, excelDirectory };
				Runtime.getRuntime().exec(parms);
			}
			catch (IOException ex)
			{
				LOGGER.error(IO_EXCEPTION, ex);
				System.exit(-1);
			}

			Long runTime = Long.valueOf(refreshRunTime);
			Thread.sleep(runTime * 60 * 1000);

			LOGGER.info(LOG_SEPARATOR);
			LOGGER.info(String.format("Refreshing excel files in %s", excelDirectory));
			LOGGER.info(LOG_SEPARATOR);
			walkFileTree(excelDirectory, new ExcelRefreshFileVisitor(props));
		}
	}

	/**
	 * Purpose:
	 * <br>
	 * walkFileTree<br>
	 * <br>
	 * @param filePath
	 * @param visitor<br>
	 */
	private void walkFileTree(String filePath, FileVisitor<Path> visitor)
	{
		FileSystem fileSystem = FileSystems.getDefault();
		Path rootPath = fileSystem.getPath(filePath);
		try
		{
			Files.walkFileTree(rootPath, visitor);
		}
		catch (IOException ex)
		{
			LOGGER.error(IO_EXCEPTION, ex);
		}
	}

	/**
	 * Purpose:
	 * <br>
	 * emailLogFile<br>
	 * <br><br>
	 * @throws InterruptedException
	 */
	private void emailLogFile()
	{
		String filename = "application.log";
		File logFile = FileUtils.getFile("./logs", filename, true);
		String attachment = logFile.getAbsolutePath();
		List<String> toEmail = Arrays.asList(emailAry);
		Session session = IveriMailSessionFactory.getMailSession(props);
		String subject = String.format("BI Report: %s", filename);
		String body = constructLogEmailBody(filename);
		EmailUtil.sendAttachmentEmail(session, toEmail, subject, body, attachment, filename);
	}

	/**
	 * Purpose:
	 * <br>
	 * constructLogEmailBody<br>
	 * <br>
	 * @param filename
	 * @return<br>
	 */
	private String constructLogEmailBody(String filename)
	{
		DateFormat dateFormat = new SimpleDateFormat("EEEE, d MMMM yyyy");
		return String.format(
				"<h1 style=\"background-color:whitesmoke;\">BI Report</h1>\r\n" + "<p style=\"color:Night;\">Hi</p>\r\n"
						+ "<p style=\"color:Night;\">Please find BI Report attached (%s).</p>\r\n"
						+ "<div style=\"color:darkslateblue;\"><b>Filename:</b> %s</div>\r\n" + "<p style=\"color:Night;\">Powered by CM Solutions</p>",
				dateFormat.format(new Date()), filename);
	}
}