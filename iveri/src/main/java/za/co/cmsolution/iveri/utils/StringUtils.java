/*
 za.co.cmsolution.iveri.utils.StringUtils<br>

 Copyright (c) 2013 - Kumbirai 'Coach' Mundangepfupfu (www.kumbirai.com)

 All rights reserved.
 */
package za.co.cmsolution.iveri.utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * <p>
 * <b>Purpose:</b><br>
 * <br>
 *
 * <p>
 * <b>Title:</b> StringUtils<br>
 * <b>Description:</b>
 * </p>
 *
 * @author Kumbirai 'Coach' Mundangepfupfu<br>
 * @date 28 Nov 2013<br>
 * @version 1.0<br>
 *
 *          <b>Revision:</b>
 *
 */
public class StringUtils
{
	private static final Logger LOGGER = LogManager.getLogger(StringUtils.class.getName());

	/**
	 * Constructor:
	 */
	private StringUtils()
	{
		super();
	}

	/**
	 * PURPOSE: <br>
	 * stringTime<br>
	 * <br>
	 *
	 * @param time
	 * @return<br>
	 */
	public static String stringTime(long time)
	{
		DecimalFormat twoDigitIntFormat = new DecimalFormat("00");
		DecimalFormat threeDigitIntFormat = new DecimalFormat("000");
		int hrs = (int) (time / (1000 * 60 * 60));
		int mins = (int) ((time % (1000 * 60 * 60)) / (1000 * 60));
		int sec = (int) (((time % (1000 * 60 * 60)) % (1000 * 60)) / 1000);
		int millis = (int) (time % 1000);
		return String.format("%s:%s:%s.%s", twoDigitIntFormat.format(hrs), twoDigitIntFormat.format(mins), twoDigitIntFormat.format(sec),
				threeDigitIntFormat.format(millis));
	}

	/**
	 * PURPOSE: <br>
	 * formatDate<br>
	 * <br>
	 *
	 * @param date
	 * @param format
	 * @return<br>
	 */
	public static String formatDate(Date date, String format)
	{
		String formattedDate = "";
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			formattedDate = sdf.format(date);
		}
		catch (Exception ex)
		{
			LOGGER.error("StringUtils#formatDate caught a Exception", ex);
			if (date != null)
			{
				formattedDate = date.toString();
			}
		}
		return formattedDate;
	}

	/**
	 * PURPOSE: <br>
	 * getStringOfLength<br>
	 * <br>
	 *
	 * @param string
	 * @param len
	 * @return<br>
	 */
	public static String getStringOfLength(String string, int len)
	{
		String newString = string;
		if (string.length() > len)
		{
			newString = string.substring(0, len);
		}
		if (string.length() < len)
		{
			StringBuilder whiteSpace = new StringBuilder(string);
			int padding = len - string.length();
			for (int i = 0; i < padding; i++)
				whiteSpace.append(" ");
			newString = new String(whiteSpace);
		}
		return newString;
	}

	/**
	 * Purpose: <br>
	 * getStringOfMinimumLength<br>
	 * <br>
	 *
	 * @param string
	 * @param len
	 * @return<br>
	 */
	public static String getStringOfMinimumLength(String string, int len)
	{
		String newString = string;
		if (string.length() < len)
		{
			StringBuilder whiteSpace = new StringBuilder(string);
			int padding = len - string.length();
			for (int i = 0; i < padding; i++)
				whiteSpace.append(" ");
			newString = new String(whiteSpace);
		}
		return newString;
	}

	/**
	 * PURPOSE: <br>
	 * humanReadableByteCount<br>
	 * <br>
	 *
	 * @param bytes
	 * @param si
	 * @return<br>
	 */
	public static String humanReadableByteCount(long bytes, boolean si)
	{
		int unit = si ? 1000 : 1024;
		if (bytes < unit)
			return bytes + " B";
		int exp = (int) (Math.log(bytes) / Math.log(unit));
		String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
		return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}

	/**
	 * Purpose: <br>
	 * toDisplayCase<br>
	 * <br>
	 *
	 * @param s
	 * @return<br>
	 */
	public static String toDisplayCase(String s)
	{
		// these cause the character following to be capitalized
		final String actionableDelimiters = " '-/";
		StringBuilder sb = new StringBuilder();
		boolean capNext = true;
		for (char c : s.toCharArray())
		{
			c = capNext ? Character.toUpperCase(c) : Character.toLowerCase(c);
			sb.append(c);
			capNext = actionableDelimiters.indexOf(c) >= 0; // explicit cast not needed
		}
		return new String(sb);
	}

	/**
	 * Purpose: <br>
	 * stringsEqual<br>
	 * <br>
	 *
	 * @param one
	 * @param two
	 * @return<br>
	 */
	public static boolean stringsEqual(String one, String two)
	{
		if (one == null || two == null)
			return false;
		return one.equals(two);
	}

	/**
	 * Purpose:
	 * <br>
	 * rTrim<br>
	 * <br>
	 * @param s
	 * @return<br>
	 */
	public static String rTrim(String s)
	{
		int i = s.length() - 1;
		while (i >= 0 && Character.isWhitespace(s.charAt(i)))
		{
			i--;
		}
		return s.substring(0, i + 1);
	}

	/**
	 * Purpose:
	 * <br>
	 * lTrim<br>
	 * <br>
	 * @param s
	 * @return<br>
	 */
	public static String lTrim(String s)
	{
		int i = 0;
		while (i < s.length() && Character.isWhitespace(s.charAt(i)))
		{
			i++;
		}
		return s.substring(i);
	}

	/**
	 * Purpose: Determine if the given String is a numeric
	 * <br>
	 * isNumeric<br>
	 * <br>
	 * @param str
	 * @return<br>
	 */
	public static boolean isNumeric(String str)
	{
		return str.matches("-?\\d+(\\.\\d+)?"); // match a number with optional '-' and decimal.
	}
}