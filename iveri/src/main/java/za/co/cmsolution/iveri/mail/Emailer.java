/*
 za.co.cmsolution.iveri.mail.Emailer<br>

 Copyright (c) 2018 - Kumbirai 'Coach' Mundangepfupfu (www.kumbirai.com)

 All rights reserved.
 */
package za.co.cmsolution.iveri.mail;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Session;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * <p><b>Purpose:</b><br>
 * <br>
 *
 * <p><b>Title:</b> Emailer<br>
 * <b>Description:</b> </p>
 *
 * @author Kumbirai 'Coach' Mundangepfupfu<br>
 * @date 01 Feb 2018<br>
 * @version 1.0<br>
 *
 * <b>Revision:</b>
 *					
 */
public class Emailer
{
	private static final Logger LOGGER = LogManager.getLogger(Emailer.class.getName());
	private Properties props;
	private String filename;
	private String attachment;

	/**
	 * Constructor: @param props
	 * Constructor: @param filename
	 * Constructor: @param attachment
	 */
	public Emailer(Properties props, String filename, String attachment)
	{
		super();
		this.props = props;
		this.filename = filename;
		this.attachment = attachment;
	}

	/**
	 * Purpose:
	 * <br>
	 * send<br>
	 * <br><br>
	 */
	public void send()
	{
		String emails = props.getProperty(filename);
		if (!"".equals(emails) && emails != null)
		{
			List<String> toEmail = Arrays.asList(emails.split(","));
			Session session = MailSessionFactory.getMailSession(props);
			String subject = String.format("BI Report: %s", filename);
			String body = constructBody();
			EmailUtil.sendAttachmentEmail(session, toEmail, subject, body, attachment, filename);
		}
		else
		{
			LOGGER.error(String.format("%s not sent. No email address defined.", filename));
		}
	}

	/**
	 * Purpose:
	 * <br>
	 * constructBody<br>
	 * <br>
	 * @return<br>
	 */
	private String constructBody()
	{
		DateFormat dateFormat = new SimpleDateFormat("EEEE, d MMMM yyyy");
		return String.format("<h1 style=\"background-color:whitesmoke;\">BI Report</h1>\r\n"
				+ "<p style=\"color:Night;\">Please find BI Report attached (%s).</p>\r\n" + "<div style=\"color:darkslateblue;\"><b>Filename:</b> %s</div>\r\n"
				+ "<hr>\r\n" + "<p style=\"color:Night;\">Powered by CM Solutions</p>", dateFormat.format(new Date()), this.filename);
	}
}