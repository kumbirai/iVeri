/*
 za.co.cmsolution.iveri.file.FileUtils<br>

 Copyright (c) 2013 - Kumbirai 'Coach' Mundangepfupfu (www.kumbirai.com)

 All rights reserved.
 */
package za.co.cmsolution.iveri.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import za.co.cmsolution.iveri.utils.StringUtils;

/**
 * <p>
 * <b>Purpose:</b><br>
 * <br>
 *
 * <p>
 * <b>Title:</b> FileUtils<br>
 * <b>Description:</b>
 * </p>
 *
 * @author Kumbirai 'Coach' Mundangepfupfu<br>
 * @date 22 Nov 2013<br>
 * @version 1.0<br>
 *
 *          <b>Revision:</b>
 *
 */
public class FileUtils
{
	private static final Logger LOGGER = LogManager.getLogger(FileUtils.class.getName());
	private static final String IO_EXCEPTION_HAS_BEEN_CAUGHT = "[IOException] has been caught.";
	private static final String FILE_DID_NOT_EXIST = "File \"%s\" did not exist. Created new one... [%s]";

	/**
	 * Constructor:
	 */
	private FileUtils()
	{
		super();
	}

	/**
	 * PURPOSE: <br>
	 * addToZipFile<br>
	 * <br>
	 *
	 * @param parent
	 * @param entry
	 * @param zos
	 * @throws FileNotFoundException
	 * @throws IOException
	 * <br>
	 */
	public static void addToZipFile(File parent, File entry, ZipOutputStream zos) throws FileNotFoundException, IOException // NOSONAR
	{
		try (FileInputStream fis = new FileInputStream(entry))
		{
			ZipEntry zipEntry = new ZipEntry(generateZipEntry(parent, entry));
			LOGGER.debug("Zip Entry: " + zipEntry.getName());
			zos.putNextEntry(zipEntry);
			byte[] bytes = new byte[1024];
			int length;
			while ((length = fis.read(bytes)) >= 0)
			{
				zos.write(bytes, 0, length);
			}
			zos.closeEntry();
		}
	}

	/**
	 * INTERNAL: <br>
	 * createFolder<br>
	 * <br>
	 *
	 * @param parentFolder
	 * @param path
	 * @return<br>
	 * @throws IOException
	 */
	public static File createFolder(File parentFolder, String path) throws IOException
	{
		if (path == null || "".equalsIgnoreCase(path))
			return parentFolder;
		File file = new File(parentFolder.getAbsolutePath());
		for (String folder : path.split("/"))
		{
			file = new File(file, folder);
			if (!(file.exists() || file.isDirectory()))
			{
				file.mkdir();
			}
		}
		return file;
	}

	/**
	 * PURPOSE: <br>
	 * delete<br>
	 * <br>
	 *
	 * @param file
	 * @throws IOException
	 * <br>
	 */
	public static void delete(File file)
	{
		if (!file.exists())
			return;
		if (file.isDirectory())
		{
			// directory is empty, then delete it
			if (file.list().length == 0)
			{
				fileDelete(file);
			}
			else
			{
				// list all the directory contents
				for (File temp : file.listFiles())
				{
					// recursive delete
					delete(temp);
				}
				// check the directory again, if empty then delete it
				if (file.list().length == 0)
				{
					fileDelete(file);
				}
			}
		}
		else
		{
			fileDelete(file);
		}
	}

	/**
	 * PURPOSE: <br>
	 * deleteDirectory<br>
	 * <br>
	 *
	 * @param directory
	 * @throws IOException
	 * <br>
	 * @deprecated
	 */
	@Deprecated
	public static void deleteDirectory(File directory) // NOSONAR
	{
		delete(directory);
	}

	/**
	 * PURPOSE: <br>
	 * getBasicFileAttributes<br>
	 * <br>
	 *
	 * @param file
	 * @return
	 * @throws IOException
	 * <br>
	 */
	public static BasicFileAttributes getBasicFileAttributes(File file) throws IOException
	{
		Path path = Paths.get(file.getAbsolutePath());
		return Files.readAttributes(path, BasicFileAttributes.class);
	}

	/**
	 * Purpose: <br>
	 * getBasicFileAttributesString<br>
	 * <br>
	 *
	 * @param file
	 * @return<br>
	 */
	public static String getBasicFileAttributesString(File file)
	{
		StringBuilder sb = new StringBuilder();
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			BasicFileAttributes bfa = getBasicFileAttributes(file);
			sb.append(String.format("%s- %s\t::", bfa.isDirectory() ? "Dir " : "File", StringUtils.getStringOfMinimumLength(file.getAbsolutePath(), 75)));
			String humanReadableByteCount = StringUtils.humanReadableByteCount(bfa.size(), true);
			sb.append("Size: ").append(StringUtils.getStringOfLength(humanReadableByteCount, 9)).append(", ");
			sb.append("Last Access Time: ").append(sdf.format(new Date(bfa.lastAccessTime().toMillis()))).append(", ");
			sb.append("Last Modified Time: ").append(sdf.format(new Date(bfa.lastModifiedTime().toMillis())));
		}
		catch (IOException ex)
		{
			LOGGER.error(ex);
			sb.append(String.format("%s- %s\t::", file.isDirectory() ? "Dir " : "File", StringUtils.getStringOfMinimumLength(file.getAbsolutePath(), 75)));
			String humanReadableByteCount = StringUtils.humanReadableByteCount(file.length(), true);
			sb.append("Size: ").append(StringUtils.getStringOfLength(humanReadableByteCount, 9));
		}
		return new String(sb);
	}

	/**
	 * PURPOSE: <br>
	 * getFile<br>
	 * <br>
	 *
	 * @param directory
	 * @param filename
	 * @param createFile
	 * @return<br>
	 */
	public static File getFile(String directory, String filename, boolean createFile)
	{
		File file = null;
		try
		{
			file = new File(directory);
			// if the directory doesnt exixts/
			if (!(file.exists() || file.isDirectory()))
			{
				file.mkdirs();
			}
			if (filename != null && !"".equalsIgnoreCase(filename))
			{
				file = new File(file, filename);
				// if there is no file
				if (!file.exists() && createFile)
				{
					boolean fileCreated = file.createNewFile();
					LOGGER.debug(String.format(FILE_DID_NOT_EXIST, file.getAbsolutePath(), fileCreated));
				}
			}
		}
		catch (IOException ex)
		{
			LOGGER.error(IO_EXCEPTION_HAS_BEEN_CAUGHT, ex);
		}
		return file;
	}

	/**
	 * PURPOSE: <br>
	 * listFileTree<br>
	 * <br>
	 *
	 * @param file
	 * @return<br>
	 */
	public static List<File> listFileTree(File file)
	{
		List<File> fileTree = new ArrayList<>();
		if (file.isDirectory())
		{
			for (File entry : file.listFiles())
			{
				if (entry.isFile())
					fileTree.add(entry);
				else
					fileTree.addAll(listFileTree(entry));
			}
		}
		else
		{
			fileTree.add(file);
		}
		return fileTree;
	}

	/**
	 * PURPOSE: <br>
	 * readFile<br>
	 * <br>
	 *
	 * @param file
	 * @return<br>
	 */
	public static String readFile(File file)
	{
		StringBuilder sb = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new FileReader(file)))
		{
			String line;
			while ((line = br.readLine()) != null)
			{
				sb.append(line).append(System.lineSeparator());
			}
		}
		catch (FileNotFoundException ex)
		{
			LOGGER.error("[FileNotFoundException] has been caught.", ex);
		}
		catch (IOException ex)
		{
			LOGGER.error(IO_EXCEPTION_HAS_BEEN_CAUGHT, ex);
		}
		return new String(sb);
	}

	/**
	 * PURPOSE: <br>
	 * readFileLinesAsList<br>
	 * <br>
	 *
	 * @param file
	 * @return<br>
	 */
	public static List<String> readFileLinesAsList(File file)
	{
		List<String> lines = new LinkedList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(file)))
		{
			String line;
			while ((line = br.readLine()) != null)
			{
				lines.add(line);
			}
		}
		catch (FileNotFoundException ex)
		{
			LOGGER.error("[FileNotFoundException] has been caught.", ex);
		}
		catch (IOException ex)
		{
			LOGGER.error(IO_EXCEPTION_HAS_BEEN_CAUGHT, ex);
		}
		return lines;
	}

	/**
	 * Purpose: <br>
	 * readFileLinesAsList<br>
	 * <br>
	 *
	 * @param in
	 * @return<br>
	 */
	public static List<String> readFileLinesAsList(InputStream in)
	{
		List<String> lines = new LinkedList<>();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(in)))
		{
			String line;
			while ((line = br.readLine()) != null)
			{
				lines.add(line);
			}
		}
		catch (IOException ex)
		{
			LOGGER.error(IO_EXCEPTION_HAS_BEEN_CAUGHT, ex);
		}
		return lines;
	}

	/**
	 * INTERNAL: <br>
	 * writeToFile<br>
	 * <br>
	 *
	 * @param file
	 * @param text
	 * @param encoding
	 * @param append
	 * <br>
	 */
	public static void writeToFile(File file, String text, String encoding, boolean append)
	{
		String newFileEncode = encoding == null || "".equalsIgnoreCase(encoding) ? "US-ASCII" : encoding;
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, append), newFileEncode)))
		{
			if (!file.exists())
			{
				boolean fileCreated = file.createNewFile();
				LOGGER.debug(String.format(FILE_DID_NOT_EXIST, file.getAbsolutePath(), fileCreated));
			}
			if (text != null && text.trim().length() > 0)
			{
				writer.write(text + System.lineSeparator());
				writer.flush();
			}
		}
		catch (Exception ex)
		{
			LOGGER.error("An error occured writing to file", ex);
		}
	}

	/**
	 * PURPOSE: <br>
	 * writeToFile<br>
	 * <br>
	 *
	 * @param directory
	 * @param filename
	 * @param text
	 * @param encoding
	 * @param append
	 * <br>
	 */
	public static void writeToFile(String directory, String filename, String text, String encoding, boolean append)
	{
		try
		{
			File file = getFile(directory, filename, true);
			// if we have any write issues
			if (!file.canWrite())
			{
				LOGGER.debug(String.format("The file \"%s\" is not writable, the system will attempt to delete the file and create a new one",
						file.getAbsolutePath()));
				boolean isDeleted = file.delete();
				if (LOGGER.isInfoEnabled())
				{
					LOGGER.debug(String.format("File deleted : %s", isDeleted));
				}
				boolean fileCreated = file.createNewFile();
				LOGGER.debug(String.format(FILE_DID_NOT_EXIST, file.getAbsolutePath(), fileCreated));
			}
			writeToFile(file, text, encoding, append);
		}
		catch (Exception ex)
		{
			LOGGER.error("An error occured writing to file", ex);
		}
	}

	/**
	 * PURPOSE: Adds the given file to the given zip file. If the file to be zipped is a folder, this
	 * method will recursively add this folder and all files to the zip file. <br>
	 * zip<br>
	 * <br>
	 *
	 * @param zip
	 * @param file
	 * @throws IOException
	 * <br>
	 */
	public static void zip(File zip, File file) throws IOException
	{
		List<File> fileTree = listFileTree(file);
		try (FileOutputStream fileOutputStream = new FileOutputStream(zip); ZipOutputStream zos = new ZipOutputStream(fileOutputStream))
		{
			for (File entry : fileTree)
			{
				addToZipFile(file, entry, zos);
			}
			zos.flush();
		}
	}

	/**
	 * Purpose: <br>
	 * fileDelete<br>
	 * <br>
	 *
	 * @param file
	 * @return<br>
	 */
	private static boolean fileDelete(File file)
	{
		boolean deleted;
		String fileAttributes;
		fileAttributes = getBasicFileAttributesString(file);
		deleted = file.delete();
		LOGGER.debug(String.format("%s%s", deleted ? "[X]" : "[O]", fileAttributes));
		return deleted;
	}

	/**
	 * PURPOSE: <br>
	 * generateZipEntry<br>
	 * <br>
	 *
	 * @param parent
	 * @param entry
	 * @return<br>
	 */
	private static String generateZipEntry(File parent, File entry)
	{
		String fileName = entry.getAbsolutePath();
		String parentName = parent.getAbsolutePath();
		String sourceFolder = parentName.substring(0, parentName.lastIndexOf(File.separator));
		return fileName.substring(sourceFolder.length() + 1, fileName.length());
	}
}