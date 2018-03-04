/*
 za.co.cmsolution.iveri.RunVB<br>

 Copyright (c) 2018 - Kumbirai 'Coach' Mundangepfupfu (www.kumbirai.com)

 All rights reserved.
 */
package za.co.cmsolution.iveri;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * <p><b>Purpose:</b><br>
 * <br>
 *
 * <p><b>Title:</b> RunVB<br>
 * <b>Description:</b> </p>
 *
 * @author Kumbirai 'Coach' Mundangepfupfu<br>
 * @date 07 Feb 2018<br>
 * @version 1.0<br>
 *
 * <b>Revision:</b>
 *					
 */
public class RunVB
{
	private static final Logger LOGGER = LogManager.getLogger(RunVB.class.getName());

	/**
	 * Constructor:
	 */
	private RunVB()
	{
		super();
	}

	/**
	 * Purpose:
	 * <br>
	 * main<br>
	 * <br>
	 * @param args<br>
	 */
	public static void main(String... args)
	{
		try
		{
			String refreshVBScript = "C:\\Code\\iVeri\\iveri\\refresh.vbs";
			String excelDirectory = "C:\\temp";
			String[] parms =
			{ "wscript", refreshVBScript, excelDirectory };

			Runtime.getRuntime().exec(parms);
		}
		catch (IOException ex)
		{
			LOGGER.error("[IOException] has been caught.", ex);
		}
	}
}