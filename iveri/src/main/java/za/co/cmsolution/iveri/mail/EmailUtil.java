/*
 za.co.cmsolution.iveri.mail.EmailUtil<br>

 Copyright (c) 2018 - Kumbirai 'Coach' Mundangepfupfu (www.kumbirai.com)

 All rights reserved.
 */
package za.co.cmsolution.iveri.mail;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * <p><b>Purpose:</b><br>
 * <br>
 *
 * <p><b>Title:</b> EmailUtil<br>
 * <b>Description:</b> </p>
 *
 * @author Kumbirai 'Coach' Mundangepfupfu<br>
 * @date 01 Feb 2018<br>
 * @version 1.0<br>
 *
 * <b>Revision:</b>
 *					
 */
public class EmailUtil
{
	private static final Logger LOGGER = LogManager.getLogger(EmailUtil.class.getName());
	private static final String TEXT_HTML = "text/html";
	private static final String ADDRESS_EXCEPTION_HAS_BEEN_CAUGHT = "[AddressException] has been caught.";
	private static final String UNSUPPORTED_ENCODING_EXCEPTION_HAS_BEEN_CAUGHT = "[UnsupportedEncodingException] has been caught.";
	private static final String MESSAGING_EXCEPTION_HAS_BEEN_CAUGHT = "[MessagingException] has been caught.";

	/**
	 * Constructor:
	 */
	private EmailUtil()
	{
		super();
	}

	/**
	 * Purpose:
	 * <br>
	 * sendEmail<br>
	 * <br>
	 * @param session
	 * @param toEmail
	 * @param subject
	 * @param body<br>
	 */
	public static void sendEmail(Session session, List<String> toEmail, String subject, String body)
	{
		try
		{
			MimeMessage msg = createMimeMessage(session, toEmail, subject);

			msg.setContent(body, TEXT_HTML);

			LOGGER.debug("Message is ready");
			Transport.send(msg);
			LOGGER.debug("EMail Sent Successfully!!");
		}
		catch (AddressException ex)
		{
			LOGGER.error(ADDRESS_EXCEPTION_HAS_BEEN_CAUGHT, ex);
		}
		catch (UnsupportedEncodingException ex)
		{
			LOGGER.error(UNSUPPORTED_ENCODING_EXCEPTION_HAS_BEEN_CAUGHT, ex);
		}
		catch (MessagingException ex)
		{
			LOGGER.error(MESSAGING_EXCEPTION_HAS_BEEN_CAUGHT, ex);
		}
	}

	/**
	 * Purpose:
	 * <br>
	 * sendAttachmentEmail<br>
	 * <br>
	 * @param session
	 * @param toEmail
	 * @param subject
	 * @param body
	 * @param attachment
	 * @param filename<br>
	 */
	public static void sendAttachmentEmail(Session session, List<String> toEmail, String subject, String body, String attachment, String filename)
	{
		try
		{
			MimeMessage msg = createMimeMessage(session, toEmail, subject);

			// Create the message body part
			BodyPart messageBodyPart = new MimeBodyPart();

			// Fill the message
			messageBodyPart.setContent(body, TEXT_HTML);

			// Create a multipart message for attachment
			Multipart multipart = new MimeMultipart();

			// Set text message part
			multipart.addBodyPart(messageBodyPart);

			// Second part is attachment
			messageBodyPart = new MimeBodyPart();
			DataSource source = new FileDataSource(attachment);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(filename);
			multipart.addBodyPart(messageBodyPart);

			// Send the complete message parts
			msg.setContent(multipart);

			// Send message
			Transport.send(msg);
			LOGGER.debug("EMail Sent Successfully with attachment!!");
			LOGGER.debug(String.format("EMail Sent to %s Successfully with attachment!!", toEmail));
		}
		catch (AddressException ex)
		{
			LOGGER.error(ADDRESS_EXCEPTION_HAS_BEEN_CAUGHT, ex);
		}
		catch (UnsupportedEncodingException ex)
		{
			LOGGER.error(UNSUPPORTED_ENCODING_EXCEPTION_HAS_BEEN_CAUGHT, ex);
		}
		catch (MessagingException ex)
		{
			LOGGER.error(MESSAGING_EXCEPTION_HAS_BEEN_CAUGHT, ex);
		}
	}

	/**
	 * Purpose:
	 * <br>
	 * createMimeMessage<br>
	 * <br>
	 * @param session
	 * @param toEmail
	 * @param subject
	 * @return
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException<br>
	 */
	private static MimeMessage createMimeMessage(Session session, List<String> toEmail, String subject) throws MessagingException, UnsupportedEncodingException
	{
		MimeMessage msg = new MimeMessage(session);
		msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
		msg.addHeader("format", "flowed");
		msg.addHeader("Content-Transfer-Encoding", "8bit");

		msg.setFrom(new InternetAddress("powerbi@iveri.com", "PowerBI Reports"));

		msg.setReplyTo(new InternetAddress[]
		{ new InternetAddress("powerbi@iveri.com", "iVeri PowerBI") });

		msg.setSubject(subject, "UTF-8");

		msg.setSentDate(new Date());

		InternetAddress[] addresses = new InternetAddress[toEmail.size()];
		int idx = 0;
		for (String add : toEmail)
		{
			addresses[idx] = new InternetAddress(add.trim(), false);
			idx++;
		}

		msg.setRecipients(Message.RecipientType.TO, addresses);
		return msg;
	}
}