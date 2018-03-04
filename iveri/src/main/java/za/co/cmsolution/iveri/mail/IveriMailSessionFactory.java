/*
 za.co.cmsolution.iveri.mail.IveriMailSessionFactory<br>

 Copyright (c) 2018 - Kumbirai 'Coach' Mundangepfupfu (www.kumbirai.com)

 All rights reserved.
 */
package za.co.cmsolution.iveri.mail;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * <p><b>Purpose:</b><br>
 * <br>
 *
 * <p><b>Title:</b> IveriMailSessionFactory<br>
 * <b>Description:</b> </p>
 *
 * @author Kumbirai 'Coach' Mundangepfupfu<br>
 * @date 27 Feb 2018<br>
 * @version 1.0<br>
 *
 * <b>Revision:</b>
 *					
 */
public class IveriMailSessionFactory
{
	private static final Logger LOGGER = LogManager.getLogger(IveriMailSessionFactory.class.getName());
	//
	private static final String USER = "user";
	private static final String P_WORD = "pWord";
	private static final String MAIL_SMTP_HOST = "mail.smtp.host";
	private static final String MAIL_SMTP_PORT = "mail.smtp.port";
	private static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
	private static final String MAIL_SMTP_STARTTLS_ENABLE = "mail.smtp.starttls.enable";
	private static final String MAIL_SMTP_SSL_ENABLE = "mail.smtp.ssl.enable";
	private static final String MAIL_SMTP_SOCKET_FACTORY_PORT = "mail.smtp.socketFactory.port";
	private static final String MAIL_SMTP_SOCKET_FACTORY_CLASS = "mail.smtp.socketFactory.class";
	private static final String MAIL_SMTP_SOCKET_FACTORY_FALLBACK = "mail.smtp.socketFactory.fallback";
	//
	private static Session session;

	/**
	 * Constructor:
	 */
	private IveriMailSessionFactory()
	{
		super();
	}

	public static Session getMailSession(Properties props)
	{
		String sessionType = props.getProperty("sessionType");
		if ("SIMPLE".equalsIgnoreCase(sessionType))
		{
			if (session == null)
			{
				props.put(MAIL_SMTP_HOST, "localhost"); // SMTP Host
				session = Session.getInstance(props, null);
				LOGGER.info(String.format("SIMPLE Session - %s", session));
			}
			return session;
		}
		if ("TLS".equalsIgnoreCase(sessionType))
		{
			if (session == null)
			{
				props.put(USER, "kumbirai@cm-solution.co.za");
				props.put(P_WORD, "Mukund!15");
				Authenticator auth = getAuthenticator(props);
				props.put(MAIL_SMTP_HOST, "smtp.mweb.co.za"); // SMTP Host
				props.put(MAIL_SMTP_PORT, 587); // TLS Port
				props.put(MAIL_SMTP_AUTH, true); // enable authentication
				props.put(MAIL_SMTP_STARTTLS_ENABLE, false); // enable STARTTLS
				session = Session.getInstance(props, auth);
				LOGGER.info(String.format("TLS Session - %s", session));
			}
			return session;
		}
		if ("SSL".equalsIgnoreCase(sessionType))
		{
			if (session == null)
			{
				props.put(USER, "powerbi@iveri.com");
				props.put(P_WORD, "Qw3rty!@#");
				Authenticator auth = getAuthenticator(props);
				props.put(MAIL_SMTP_HOST, "mail.iveri.com"); // SMTP Host
				props.put(MAIL_SMTP_SOCKET_FACTORY_PORT, 465); // SSL Port
				props.put(MAIL_SMTP_SOCKET_FACTORY_CLASS, "javax.net.ssl.SSLSocketFactory"); // SSL Factory Class
				props.put(MAIL_SMTP_SOCKET_FACTORY_FALLBACK, false); // SSL Socket Factory fallback
				props.put(MAIL_SMTP_SSL_ENABLE, true); // Enable SSL
				props.put(MAIL_SMTP_AUTH, true); // Enabling SMTP Authentication
				props.put(MAIL_SMTP_PORT, 465); // SMTP Port
				session = Session.getInstance(props, auth);
				LOGGER.info(String.format("SSL Session - %s", session));
			}
			return session;
		}
		throw new MailException("MailSession cannot be determined");
	}

	/**
	 * Purpose:
	 * <br>
	 * getAuthenticator<br>
	 * <br>
	 * @param props
	 * @return<br>
	 */
	private static Authenticator getAuthenticator(Properties props)
	{
		String user = props.getProperty(USER);
		String pWord = props.getProperty(P_WORD);

		// create Authenticator object to pass in Session.getInstance argument
		return new Authenticator()
		{
			/** (non-Javadoc)
			 * @see javax.mail.Authenticator#getPasswordAuthentication()
			 */
			@Override
			protected PasswordAuthentication getPasswordAuthentication()
			{
				return new PasswordAuthentication(user, pWord);
			}
		};
	}
}