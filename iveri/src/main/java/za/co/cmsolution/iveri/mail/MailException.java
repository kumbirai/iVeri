/*
 za.co.cmsolution.iveri.mail.MailException<br>

 Copyright (c) 2018 - Kumbirai 'Coach' Mundangepfupfu (www.kumbirai.com)

 All rights reserved.
 */
package za.co.cmsolution.iveri.mail;

/**
 * <p><b>Purpose:</b><br>
 * <br>
 *
 * <p><b>Title:</b> MailException<br>
 * <b>Description:</b> </p>
 *
 * @author Kumbirai 'Coach' Mundangepfupfu<br>
 * @date 01 Feb 2018<br>
 * @version 1.0<br>
 *
 * <b>Revision:</b>
 *					
 */
public class MailException extends RuntimeException
{
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor:
	 */
	public MailException()
	{
		super();
	}

	/**
	 * Constructor: @param message
	 */
	public MailException(String message)
	{
		super(message);
	}

	/**
	 * Constructor: @param cause
	 */
	public MailException(Throwable cause)
	{
		super(cause);
	}

	/**
	 * Constructor: @param message
	 * Constructor: @param cause
	 */
	public MailException(String message, Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * Constructor: @param message
	 * Constructor: @param cause
	 * Constructor: @param enableSuppression
	 * Constructor: @param writableStackTrace
	 */
	public MailException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}
}