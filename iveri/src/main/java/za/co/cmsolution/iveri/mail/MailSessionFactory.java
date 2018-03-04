/*
 za.co.cmsolution.iveri.mail.MailSessionFactory<br>

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
 * <p><b>Title:</b> MailSessionFactory<br>
 * <b>Description:</b> </p>
 *
 * @author Kumbirai 'Coach' Mundangepfupfu<br>
 * @date 01 Feb 2018<br>
 * @version 1.0<br>
 *
 * <b>Revision:</b>
 *					
 */
public class MailSessionFactory
{
	private static final Logger LOGGER = LogManager.getLogger(MailSessionFactory.class.getName());
	private static Session session;

	/**
	 * Constructor:
	 */
	private MailSessionFactory()
	{
		super();
	}

	/**
	 * Purpose:
	 * <br>
	 * getMailSession<br>
	 * <br>
	 * @param props
	 * @return<br>
	 */
	public static Session getMailSession(Properties props)
	{
		String sessionType = props.getProperty("sessionType");
		if ("SIMPLE".equalsIgnoreCase(sessionType))
		{
			if (session == null)
			{
				session = Session.getInstance(props, null);
				LOGGER.info(String.format("SIMPLE Session - %s", session));
			}
			return session;
		}
		if ("TLS".equalsIgnoreCase(sessionType))
		{
			if (session == null)
			{
				Authenticator auth = getAuthenticator(props);
				session = Session.getInstance(props, auth);
				LOGGER.info(String.format("TLS Session - %s", session));
			}
			return session;
		}
		if ("SSL".equalsIgnoreCase(sessionType))
		{
			if (session == null)
			{
				Authenticator auth = getAuthenticator(props);
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
		String user = props.getProperty("user");
		String pWord = props.getProperty("pWord");

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