/*
 za.co.cmsolution.iveri.file.ExcelRefreshFileVisitor<br>

 Copyright (c) 2018 - Kumbirai 'Coach' Mundangepfupfu (www.kumbirai.com)

 All rights reserved.
 */
package za.co.cmsolution.iveri.file;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import za.co.cmsolution.iveri.excel.ExcelRefresher;

/**
 * <p><b>Purpose:</b><br>
 * <br>
 *
 * <p><b>Title:</b> ExcelRefreshFileVisitor<br>
 * <b>Description:</b> </p>
 *
 * @author Kumbirai 'Coach' Mundangepfupfu<br>
 * @date 06 Feb 2018<br>
 * @version 1.0<br>
 *
 * <b>Revision:</b>
 *					
 */
public class ExcelRefreshFileVisitor implements FileVisitor<Path>
{
	private static final Logger LOGGER = LogManager.getLogger(ExcelRefreshFileVisitor.class.getName());
	private Properties props;

	/**
	 * Constructor:
	 */
	public ExcelRefreshFileVisitor(Properties props)
	{
		super();
		this.props = props;
	}

	/** (non-Javadoc)
	 * @see java.nio.file.FileVisitor#preVisitDirectory(java.lang.Object, java.nio.file.attribute.BasicFileAttributes)
	 */
	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException
	{
		LOGGER.info("-------------------------------------");
		LOGGER.info("DIRECTORY NAME: " + dir.getFileName() + " - LOCATION: " + dir.toFile().getPath());
		LOGGER.info("-------------------------------------");
		return FileVisitResult.CONTINUE;
	}

	/** (non-Javadoc)
	 * @see java.nio.file.FileVisitor#visitFile(java.lang.Object, java.nio.file.attribute.BasicFileAttributes)
	 */
	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
	{
		Path filePath = file.getFileName();
		String absoluteFileName = file.toFile().getPath();
		LOGGER.info(String.format("FILE NAME: %s - PATH: %S", filePath, absoluteFileName));
		ExcelRefresher refresher = new ExcelRefresher(props, file);
		try
		{
			refresher.refresh();
		}
		catch (Exception ex)
		{
			LOGGER.error("[Exception] has been caught.", ex);
		}
		return FileVisitResult.CONTINUE;
	}

	/** (non-Javadoc)
	 * @see java.nio.file.FileVisitor#visitFileFailed(java.lang.Object, java.io.IOException)
	 */
	@Override
	public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException
	{
		LOGGER.error(String.format("[IOException] has been caught reading file: %s", file), exc);
		return FileVisitResult.CONTINUE;
	}

	/** (non-Javadoc)
	 * @see java.nio.file.FileVisitor#postVisitDirectory(java.lang.Object, java.io.IOException)
	 */
	@Override
	public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException
	{
		return FileVisitResult.CONTINUE;
	}
}